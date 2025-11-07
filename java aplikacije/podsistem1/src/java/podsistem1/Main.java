/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package podsistem1;

import entiteti.Korisnik;
import entiteti.Mesto;
import java.io.Serializable;
import java.util.List;
import javax.annotation.Resource;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/*
 * javaee-api-8.0.jar
 * javax.persistence-2.1.0.jar
 * mysql-connector-j-9.4.0.jar
 * org.eclipse.persistence.antlr-2.5.2.jar
 * org.eclipse.persistence.asm-2.5.2.jar
 * org.eclipse.persistence.core-2.5.2.jar
 * org.eclipse.persistence.jpa.jpql-2.5.2.jar
 * org.eclipse.persistence.jpa.modelgen.processor-2.5.2.jar
 * org.eclipse.persistence.jpa-2.5.2.jar
 */

public class Main {
    
    @Resource(lookup = "jms/audio_cf")
    static ConnectionFactory connectionFactory;

    @Resource(lookup = "jms/Podsistem1Requests")
    static Queue queue;
    
    public static void main(String[] args) {
        
        JMSContext context = connectionFactory.createContext();
        JMSConsumer consumer = context.createConsumer(queue);
        
        while(true){
            System.out.println("[podsistem1] Čekam zahteve...");
            EntityManagerFactory emf = null;
            EntityManager em = null;

            try {
                ObjectMessage message = (ObjectMessage) consumer.receive(); // primljena poruka
                String operation = message.getStringProperty("operation"); //  dohvatanje operacije
                System.out.println("Operacija: " + operation);
                
                emf = Persistence.createEntityManagerFactory("podsistem1PU");
                em = emf.createEntityManager();
                
                switch(operation) {
                    case "createCity": {
                        Mesto mesto = message.getBody(Mesto.class);
                        em.getTransaction().begin();                        
                        em.persist(mesto);
                        em.getTransaction().commit();
                        reply(context, message, Boolean.TRUE);
                        break;
                    }
                    case "listCities": {                    
                        List<Mesto> response = em.createNamedQuery("Mesto.findAll").getResultList();
                        for(Mesto mesto: response)
                            System.out.println(mesto);
                        reply(context, message, response.toArray(new Mesto[0]));
                        break;
                    }
                    case "createUser": {
                        Korisnik korisnik = message.getBody(Korisnik.class);
                        em.getTransaction().begin();                        
                        em.persist(korisnik);
                        em.getTransaction().commit();
                        reply(context, message, korisnik.getId());
                        break;
                    }
                    case "listUsers": {                    
                        List<Korisnik> response = em.createNamedQuery("Korisnik.findAll").getResultList();
                        for(Korisnik korisnik: response)
                            System.out.println(korisnik);
                        reply(context, message, response.toArray(new Korisnik[0]));
                        break;
                    }
                    case "updateUserEmail": {
                        Korisnik korisnikNew = message.getBody(Korisnik.class);
                        Korisnik korisnikOld = (Korisnik) em.createNamedQuery("Korisnik.findById")
                                .setParameter("id", korisnikNew.getId())
                                .getSingleResult();
                        boolean result = false;
                        if(korisnikOld != null) {
                            em.getTransaction().begin();                        
                            korisnikOld.setEmail(korisnikNew.getEmail());
                            em.persist(korisnikOld);
                            em.getTransaction().commit();
                            result = true;
                        }
                        reply(context, message, result);
                        break;
                    }
                    case "updateUserCity": {
                        Korisnik korisnikNew = message.getBody(Korisnik.class);
                        Korisnik korisnikOld = (Korisnik) em.createNamedQuery("Korisnik.findById")
                                .setParameter("id", korisnikNew.getId())
                                .getSingleResult();
                        boolean result = false;
                        if(korisnikOld != null) {
                            em.getTransaction().begin();                        
                            korisnikOld.setMesto(korisnikNew.getMesto());
                            em.persist(korisnikOld);
                            em.getTransaction().commit();
                            result = true;
                        }
                        reply(context, message, result);
                        break;
                    }

                    default:
                        System.out.println("Nepoznata operacija: " + operation);
                        break;                        
                }
                
            } catch (JMSException ex) {
            }
            finally {
                if(em != null) {
                    if (em.getTransaction().isActive())
                        em.getTransaction().rollback();
                    em.close();
                    em = null;
                }
                if(emf != null) {
                    emf.close();
                    emf = null;
                }
            }            
        }
    }
    
    // pomoćna funkcija za odgovaranje na request - contekst, poruka na koju se odgovara, odgovor
    protected static void reply(JMSContext ctx, Message message, Serializable response) throws JMSException {
	Destination replyTo = message.getJMSReplyTo(); // čitamo gde da pošaljemo odgovor
	if (replyTo != null) { // ako je potrebno poslati odgovor, pripremamo ga
            ObjectMessage reply = ctx.createObjectMessage(response);
	    reply.setJMSCorrelationID(message.getJMSCorrelationID()); // preko CorrelationID-a se uparuju zahtev i odgovor
	    ctx.createProducer().send(replyTo, reply); // šaljemo odgovor na replyTo destinaciju
        }
    }
}
