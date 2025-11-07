package rs.etf.is1.centralni_server.resources;

import entiteti.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Properties;
import javax.annotation.Resource;
import javax.inject.Singleton;
import javax.jms.*;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.UUID;

@Path("server")
@Singleton
@Produces(MediaType.APPLICATION_JSON)
public class CentralniServerResource {

    @Resource(mappedName = "jms/audio_cf")
    private ConnectionFactory cf;

    @Resource(mappedName = "jms/Podsistem1Requests")
    private Queue p1;
    
    @Resource(mappedName = "jms/Podsistem2Requests")
    private Queue p2;
    
    @Resource(mappedName = "jms/Podsistem3Requests")
    private Queue p3;
        
    // PODSISTEM 1
    
    @POST
    @Path("/city/create")
    public Response createCity(Mesto mesto) {
    	try {
	    Boolean response = send("createCity", mesto, p1, Boolean.class);
	    return Response.ok(response).build();
        }
    	catch(Exception e) {
            return Response.serverError().build();
    	}
    }
    
    @GET
    @Path("/city/list")
    public Response listCities() {
    	try {
	    Mesto[] response = send("listCities", null, p1, Mesto[].class);
	    return Response.ok(response).build();
        }
    	catch(Exception e) {
            e.printStackTrace();
            return Response.serverError().build();
    	}
    }
    
    
    @POST
    @Path("/user/create")
    public Response createUser(Korisnik korisnik) {
    	try {
	    Integer korisnikId = send("createUser", korisnik, p1, Integer.class);
	    return Response.ok(korisnikId).build();
        }
    	catch(Exception e) {
            e.printStackTrace();
            return Response.serverError().build();
    	}
    }

    @POST
    @Path("/user/update/email")
    public Response updateUserEmail(@Context HttpHeaders headers, Korisnik korisnik) {
    	try {
            System.out.println("[server] updateUserEmail");
            Integer userId = getUserId(headers);
            korisnik.setId(userId);
            Boolean response = send("updateUserEmail", korisnik, p1, Boolean.class);
	    return Response.ok(response).build();
        }
    	catch(Exception e) {
            e.printStackTrace();
            return Response.serverError().build();
    	}
    }

    @POST
    @Path("/user/update/city")
    public Response updateUserCity(@Context HttpHeaders headers, Korisnik korisnik) {
    	try {
            System.out.println("[server] updateUserCity");
            Integer userId = getUserId(headers);
            korisnik.setId(userId);
            Boolean response = send("updateUserCity", korisnik, p1, Boolean.class);
	    return Response.ok(response).build();
        }
    	catch(Exception e) {
            e.printStackTrace();
            return Response.serverError().build();
    	}
    }

    @GET
    @Path("/user/list")
    public Response listUsers() {
    	try {
            Korisnik[] response = send("listUsers", null, p1, Korisnik[].class);
	    return Response.ok(response).build();
        }
    	catch(Exception e) {
    		e.printStackTrace();
    		return Response.serverError().build();
    	}
    }
    
    // PODSISTEM 2
    
    @POST
    @Path("/category/create")
    public Response createCategory(Kategorija kategorija) {
    	try {
	    Boolean response = send("createCategory", kategorija, p2, Boolean.class);
	    return Response.ok(response).build();
        }
    	catch(Exception e) {
            return Response.serverError().build();
    	}
    }
    
    @POST
    @Path("/audio/create")
    public Response createAudio(@Context HttpHeaders headers, Audiosnimak audiosnimak) {
    	try {
            audiosnimak.setIdVlasnik(new Korisnik());
            // id vlasnik = ulogovani korisnik
            Integer userId = getUserId(headers);            
            audiosnimak.getIdVlasnik().setId(userId);
            audiosnimak.setPostavljeno(LocalDateTime.now());
	    Boolean response = send("createAudio", audiosnimak, p2, Boolean.class);
	    return Response.ok(response).build();
        }
    	catch(Exception e) {
            return Response.serverError().build();
    	}
    }
    
    @POST
    @Path("/audio/update/{audioId}/name")
    public Response updateAudioName(@PathParam("audioId") Integer audioId, Audiosnimak audiosnimak) {
    	try {
            System.out.println("[server] updateAudioName");
            audiosnimak.setId(audioId);
            Boolean response = send("updateAudioName", audiosnimak, p2, Boolean.class);
	    return Response.ok(response).build();
        }
    	catch(Exception e) {
            e.printStackTrace();
            return Response.serverError().build();
    	}
    }

    @DELETE
    @Path("/audio/delete/{audioId}")
    public Response deleteAudio(@PathParam("audioId") Integer audioId, @Context HttpHeaders headers) {
    	try {
            System.out.println("[server] deleteAudio");
            Audiosnimak audiosnimak = new Audiosnimak();
            audiosnimak.setId(audioId);
            Properties props = new Properties();
            Integer userId = getUserId(headers);            
            props.setProperty("userId", userId.toString());
            Boolean response = send("deleteAudio", audiosnimak, props, p2, Boolean.class);
	    return Response.ok(response).build();
        }
    	catch(Exception e) {
            e.printStackTrace();
            return Response.serverError().build();
    	}
    }

    @POST
    @Path("/audio/update/{audioId}/category/add")
    public Response addAudioCategory(@PathParam("audioId") Integer audioId, Kategorija kategorija){
        try {
            System.out.println("[server] addAudioCategory");
            Properties props = new Properties();
            props.put("audioId", audioId.toString());
            Boolean response = send("addAudioCategory", kategorija, props, p2, Boolean.class);
	    return Response.ok(response).build();
        }
    	catch(Exception e) {
            e.printStackTrace();
            return Response.serverError().build();
    	}
    }
    
    @GET
    @Path("/category/list")
    public Response listCategories() {
    	try {
            Kategorija[] response = send("listCategories", null, p2, Kategorija[].class);
	    return Response.ok(response).build();
        }
    	catch(Exception e) {
    		e.printStackTrace();
    		return Response.serverError().build();
    	}
    }
    
    @GET
    @Path("/audio/list")
    public Response listAudios() {
    	try {
            Audiosnimak[] response = send("listAudios", null, p2, Audiosnimak[].class);
	    return Response.ok(response).build();
        }
    	catch(Exception e) {
    		e.printStackTrace();
    		return Response.serverError().build();
    	}
    }
    
    @GET
    @Path("/audio/{audioId}/category/list")
    public Response listAudioCategories(@PathParam("audioId") Integer audioId) {
    	try {
            System.out.println("[server] listAudioCategories");
            Properties props = new Properties();
            props.put("audioId", audioId.toString());
            Kategorija[] response = send("listAudioCategories", null, props, p2, Kategorija[].class);
	    return Response.ok(response).build();
        }
    	catch(Exception e) {
    		e.printStackTrace();
    		return Response.serverError().build();
    	}
    }
    
    // PODSISTEM 3
    
    @POST
    @Path("/package/create")
    public Response createPackage(Paket paket) {
    	try {
	    Boolean response = send("createPackage", paket, p3, Boolean.class);
	    return Response.ok(response).build();
        }
    	catch(Exception e) {
            return Response.serverError().build();
    	}
    }
    
    @POST
    @Path("/package/update/{paketId}/price")
    public Response updatePackagePrice(@PathParam("paketId") Integer paketId, Cenapaketa cenapaketa){
        try {
            System.out.println("[server] updatePackagePrice");
            Properties props = new Properties();
            props.put("paketId", paketId.toString());
            Boolean response = send("updatePackagePrice", cenapaketa, props, p3, Boolean.class);
	    return Response.ok(response).build();
        }
    	catch(Exception e) {
            e.printStackTrace();
            return Response.serverError().build();
    	}
    }
    
    @GET
    @Path("/package/list")
    public Response listPackages() {
    	try {
            Paket[] response = send("listPackages", null, p3, Paket[].class);
	    return Response.ok(response).build();
        }
    	catch(Exception e) {
    		e.printStackTrace();
    		return Response.serverError().build();
    	}
    }
    
    @GET
    @Path("/subscription/list")
    public Response listSubscriptions(@Context HttpHeaders headers) {
    	try {
            Integer userId = getUserId(headers);
            Pretplata[] response = send("listSubscriptions", userId, p3, Pretplata[].class);
	    return Response.ok(response).build();
        }
    	catch(Exception e) {
    		e.printStackTrace();
    		return Response.serverError().build();
    	}
    }
    
    @GET
    @Path("/audio/{audioId}/listening/list")
    public Response listAudioListenings(@PathParam("audioId") Integer audioId) {
    	try {
            System.out.println("[server] listAudioListenings");
            Properties props = new Properties();
            props.put("audioId", audioId.toString());
            Slusanje[] response = send("listAudioListenings", null, props, p3, Slusanje[].class);
	    return Response.ok(response).build();
        }
    	catch(Exception e) {
    		e.printStackTrace();
    		return Response.serverError().build();
    	}
    }

    @POST
    @Path("/audio/{audioId}/listening/add")
    public Response addAudioListening(@PathParam("audioId") Integer audioId, @Context HttpHeaders headers, Slusanje slusanje) {
    	try {
            System.out.println("[server] addAudioListening");
            Properties props = new Properties();
            slusanje.setIdAudioSnimak(new Audiosnimak());
            slusanje.getIdAudioSnimak().setId(audioId);
            slusanje.setIdKorisnik(new Korisnik());
            Integer userId = getUserId(headers);            
            slusanje.getIdKorisnik().setId(userId);
            Boolean response = send("addAudioListening", slusanje, p3, Boolean.class);
	    return Response.ok(response).build();
        }
    	catch(Exception e) {
    		e.printStackTrace();
    		return Response.serverError().build();
    	}
    }
    
    @GET
    @Path("/review/list/{audioId}")
    public Response listAudioReviews(@PathParam("audioId") Integer audioId) {
    	try {
            System.out.println("[server] listAudioReviews");
            Ocena[] response = send("listAudioReviews", audioId, p3, Ocena[].class);
	    return Response.ok(response).build();
        }
    	catch(Exception e) {
            e.printStackTrace();
            return Response.serverError().build();
    	}
    }
    
    @POST
    @Path("/review/create/{audioId}")
    public Response createAudioReview(@Context HttpHeaders headers, @PathParam("audioId") Integer audioId, Ocena ocena) {
    	try {
            System.out.println("[server] createAudioReview");
            ocena.setIdAudioSnimak(new Audiosnimak());
            ocena.getIdAudioSnimak().setId(audioId);
            ocena.setIdKorisnik(new Korisnik());
            Integer userId = getUserId(headers);
            ocena.getIdKorisnik().setId(userId);
            Boolean response = send("createAudioReview", ocena, p3, Boolean.class);
	    return Response.ok(response).build();
        }
    	catch(Exception e) {
            e.printStackTrace();
            return Response.serverError().build();
    	}
    }

    @POST
    @Path("/review/update/{audioId}")
    public Response updateAudioReview(@Context HttpHeaders headers, @PathParam("audioId") Integer audioId, Ocena ocena) {
    	try {
            System.out.println("[server] updateAudioReview");
            ocena.setIdAudioSnimak(new Audiosnimak());
            ocena.getIdAudioSnimak().setId(audioId);
            ocena.setIdKorisnik(new Korisnik());
            Integer userId = getUserId(headers);
            ocena.getIdKorisnik().setId(userId);
            Boolean response = send("updateAudioReview", ocena, p3, Boolean.class);
	    return Response.ok(response).build();
        }
    	catch(Exception e) {
            e.printStackTrace();
            return Response.serverError().build();
    	}
    }

    @DELETE
    @Path("/review/delete/{audioId}")
    public Response deleteAudioReview(@Context HttpHeaders headers, @PathParam("audioId") Integer audioId) {
    	try {
            System.out.println("[server] deleteAudioReview");
            Ocena ocena = new Ocena();
            ocena.setIdAudioSnimak(new Audiosnimak());
            ocena.getIdAudioSnimak().setId(audioId);
            ocena.setIdKorisnik(new Korisnik());
            Integer userId = getUserId(headers);
            ocena.getIdKorisnik().setId(userId);
            Boolean response = send("deleteAudioReview", ocena, p3, Boolean.class);
	    return Response.ok(response).build();
        }
    	catch(Exception e) {
            e.printStackTrace();
            return Response.serverError().build();
    	}
    }
   
    @POST
    @Path("/subscription/create")
    public Response createSubscription(@Context HttpHeaders headers, Pretplata pretplata) {
    	try {
            Integer userId = getUserId(headers);
            pretplata.setIdKorisnik(new Korisnik());
            pretplata.getIdKorisnik().setId(userId);
            Boolean response = send("createSubscription", pretplata, p3, Boolean.class);
	    return Response.ok(response).build();
        }
    	catch(Exception e) {
            e.printStackTrace();
            return Response.serverError().build();
    	}
    }

    @POST
    @Path("/favourite/add")
    public Response addToFavourites(@Context HttpHeaders headers, Audiosnimak audio) {
    	try {
            Properties props = new Properties();
            Integer userId = getUserId(headers);            
            props.put("userId", userId.toString());
            Boolean response = send("addToFavourites", audio, props, p3, Boolean.class);
	    return Response.ok(response).build();
        }
    	catch(Exception e) {
            e.printStackTrace();
            return Response.serverError().build();
    	}
    }

    @GET
    @Path("/favourite/list")
    public Response listFavouriteAudios(@Context HttpHeaders headers) {
    	try {
            Integer userId = getUserId(headers);            
            Audiosnimak[] response = send("listFavouriteAudios", userId, p3, Audiosnimak[].class);
	    return Response.ok(response).build();
        }
    	catch(Exception e) {
            e.printStackTrace();
            return Response.serverError().build();
    	}
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
    
    private void addUserId(Korisnik user) throws Exception {        
        AuthFilter.addUser(user.getIme(), user.getId());
    }

    private Integer getUserId(HttpHeaders headers) throws Exception {
        try {
            List<String> idHeaders = headers.getRequestHeaders().get("X-User-ID");
                    
            if(idHeaders == null)
                throw new Exception("Nedostaje zaglavlje X-User-ID");

            if(idHeaders.size() > 1)
                throw new Exception("Zaglavlje X-User-ID se ne sme slati u zahtevima.");

            return Integer.parseInt(idHeaders.get(0));           
        }
        catch(Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
}
