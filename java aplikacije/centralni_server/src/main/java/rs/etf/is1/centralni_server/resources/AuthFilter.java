/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rs.etf.is1.centralni_server.resources;

import entiteti.Korisnik;
import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.Priority;
import javax.annotation.Resource;
import javax.jms.ConnectionFactory;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.TemporaryQueue;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

/**
 *
 * @author sergej
 */

@Provider
@Priority(Priorities.AUTHENTICATION) // izvrsi pre operacije
public class AuthFilter implements ContainerRequestFilter{
    
    // thread safe
    private static Map<String,Integer> userMap = new ConcurrentHashMap<>();
    private static boolean initialized = false;
    
    @Resource(mappedName = "jms/audio_cf")
    private ConnectionFactory cf;

    @Resource(mappedName = "jms/Podsistem1Requests")
    private Queue p1;

    public static boolean isInitialized() {
        return initialized;
    }

    public void init() throws Exception {
        Korisnik[] users = send("listUsers", null, p1, Korisnik[].class);                
        for(Korisnik user: users)
            AuthFilter.addUser(user.getIme(), user.getId());

        initialized = true;
    }
    
    public static void addUser(String name, Integer userId) {
        userMap.put(name.toLowerCase(), userId);
    }
        
    @Override
    public void filter(ContainerRequestContext ctx) throws IOException {        
        try {
            //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            MultivaluedMap<String, String> headers = ctx.getHeaders();

            // 1. Da li postoji Auth header
            String auth = headers.getFirst("Authorization");
            if(auth == null || auth.trim().isEmpty()){
                abort(ctx, Response.Status.UNAUTHORIZED, "Korisničko ime i lozinka nisu prosleđeni.");
                return;
            }

            System.out.println("Auth=" + auth);

            // 2. Ocekujemo format base64(true, 0, basic, 0, 6)
            // if(!auth.regionMatches(true, 0, "Basic ", 0, 6)){
            if(!auth.startsWith("Basic ")){
                abort(ctx, Response.Status.BAD_REQUEST, "Pogrešan tip Authorization zaglavlja (očekuje se Basic).");
                return;
            }

            // odsečemo "Basic "
            auth = auth.substring("Basic ".length()).trim();

            // 3. Dekodiranje kredencijala i parsanje na user:pass
            String user_pass = new String(Base64.getDecoder().decode(auth), StandardCharsets.UTF_8);        
            System.out.println("UserPass=" + user_pass);

            int p = user_pass.indexOf(":");
            if(p < 0){
                abort(ctx, Response.Status.BAD_REQUEST, "Pogrešno prosleđeno korisničko ime ili lozinka.");
            }

            String username = user_pass.substring(0, p).toLowerCase();
            String password = user_pass.substring(p + 1);

            System.out.println("Username=" + username);
            System.out.println("Password=" + password);

            // napunimo listu korisnika - ako nismo
            if(!initialized)
                init();
        
            // 4. pronalazenje korisnika ...
            Integer userId = userMap.get(username);

            // ... i provera lozinke
            if(userId == null || !password.equals("changeit")){ // TODO: nemamo podatka o pasvordima nigde u sistemu
                abort(ctx, Response.Status.BAD_REQUEST, "Pogrešno korisničko ime ili lozinka.");
                return;
            }

            // 6. Dodaja internog zaglavlja sa ID-jem
            ctx.getHeaders().putSingle("X-User-ID", userId.toString());
        }
        catch(Exception e) {
            throw new IOException(e);
        }
    }

    private void abort(ContainerRequestContext ctx, Response.Status status, String msg) {
        ctx.abortWith(Response.status(status).entity(msg).build());
    }
    
        // funkcija za slanje zahteva podsistemu - operacija, podaci, kom sistemu se šalje, tip rezultata
    private <T> T send(String operation, Serializable data, Queue queue, Class<T> clazz) throws Exception {		
        return send(operation, data, null, queue, clazz);
    }

    private <T> T send(String operation, Serializable data, Properties props, Queue queue, Class<T> clazz) throws Exception {		
	try (JMSContext ctx = cf.createContext(JMSContext.AUTO_ACKNOWLEDGE)) {
            // Kreiramo temporary queue za ovaj zahtev
            TemporaryQueue replyQueue = ctx.createTemporaryQueue();

            // postavljamo mu correlation id, ubacujemo operaciju i sadržaj
            String correlationId = UUID.randomUUID().toString();
            ObjectMessage request = ctx.createObjectMessage(data);
            request.setStringProperty("operation", operation);
            request.setJMSCorrelationID(correlationId);
            request.setJMSReplyTo(replyQueue);

            if(props != null) {
                // dodatni parametri
                for (Object key : props.keySet()) {
                   String name = (String) key;
                   String value = props.getProperty(name);
                   request.setStringProperty(name, value);
                }
            }
            // slanje zahteva podsistemu
            System.out.println("Slanje " + operation + " zahteva " + queue.getQueueName());
            ctx.createProducer().send(queue, request);

            // čekanje odgovora na temp queue
            System.out.println("Čekam odgovor...");
            
            String selector = "JMSCorrelationID = '" + correlationId + "'";
            try (JMSConsumer consumer = ctx.createConsumer(replyQueue, selector)) {
                Message reply = consumer.receive(60*1000); // timeout
                if (reply == null) {
                    throw new RuntimeException("Timedout: Čekanje JMS odgovora.");
                }
                if (reply.isBodyAssignableTo(clazz)) {
                    return reply.getBody(clazz);
                } else {
                    throw new RuntimeException("Neočekivani tip odgovora: " + reply.getClass());
                }
            }
        }		
    }	
}
