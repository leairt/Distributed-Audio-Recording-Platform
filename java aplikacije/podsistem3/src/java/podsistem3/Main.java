/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package podsistem3;

import entiteti.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

    @Resource(lookup = "jms/Podsistem3Requests")
    static Queue queue;
    
    public static void main(String[] args) {
        
        JMSContext context = connectionFactory.createContext();
        JMSConsumer consumer = context.createConsumer(queue);
        
        while(true){
            System.out.println("[podsistem3] Čekam zahteve...");
            EntityManagerFactory emf = null;
            EntityManager em = null;

            try {
                ObjectMessage message = (ObjectMessage) consumer.receive(); // primljena poruka
                String operation = message.getStringProperty("operation"); //  dohvatanje operacije
                System.out.println("Operacija: " + operation);
                
                emf = Persistence.createEntityManagerFactory("podsistem3PU");
                em = emf.createEntityManager();
                
                switch(operation) {
                    case "createPackage":{
                        Paket paket = message.getBody(Paket.class);
                        em.getTransaction().begin();                        
                        em.persist(paket);
                        em.getTransaction().commit();
                        reply(context, message, Boolean.TRUE);
                        break;
                    }
                    case "updatePackagePrice":{
                        Integer paketId = Integer.parseInt(message.getStringProperty("paketId"));
                        Cenapaketa nova = message.getBody(Cenapaketa.class);
                        Cenapaketa stara = null;
                        List<Cenapaketa> ceneZaMesec = em.createNamedQuery("Cenapaketa.findByMesec").setParameter("mesec", nova.getMesec()).getResultList();
                        boolean result = false;
                        for(Cenapaketa cenapaketa : ceneZaMesec){
                            if(cenapaketa.getIdPaket().getId()==paketId){
                                stara = cenapaketa;
                                result = true;
                                break;
                            }
                        }
                        if(stara != null) {
                            em.getTransaction().begin();                        
                            stara.setCena(nova.getCena());
                            em.persist(stara);
                            em.getTransaction().commit();
                            result = true;
                        }
                        reply(context, message, result);
                        break;
                    }
                    /*
                    Audiosnimak audioNew = message.getBody(Audiosnimak.class);
                        Audiosnimak audioOld = (Audiosnimak) em.createNamedQuery("Audiosnimak.findById")
                                .setParameter("id", audioNew.getId())
                                .getSingleResult();
                        boolean result = false;
                        if(audioOld != null) {
                            em.getTransaction().begin();                        
                            audioOld.setNaziv(audioNew.getNaziv());
                            em.persist(audioOld);
                            em.getTransaction().commit();
                            result = true;
                        }
                        reply(context, message, result);
                        break;*/
                    
                    case "listPackages": {                    
                        List<Paket> response = em.createNamedQuery("Paket.findAll").getResultList();
                        for(Paket paket: response)
                            System.out.println(paket);
                        reply(context, message, response.toArray(new Paket[0]));
                        break;
                    }
                    case "listSubscriptions": {      
                        Integer userId = message.getBody(Integer.class);
                        System.out.println("userId=" + userId);

                        Korisnik korisnik = (Korisnik) em.createNamedQuery("Korisnik.findById")
                                .setParameter("id", userId)
                                .getSingleResult();
                        List<Pretplata> response = em.createNamedQuery("Pretplata.findByKorisnik")
                                .setParameter("Korisnik", korisnik)
                                .getResultList();
                        reply(context, message, response.toArray(new Pretplata[0]));
                        break;
                    }
                    case "addAudioListening": {
                        Slusanje slusanje = message.getBody(Slusanje.class);
                        em.getTransaction().begin();                        
                        em.persist(slusanje);
                        em.getTransaction().commit();
                        reply(context, message, Boolean.TRUE);
                        break;
                    }
                    case "listAudioListenings": {      
                        Integer audioId = Integer.parseInt(message.getStringProperty("audioId"));
                        System.out.println("audioId=" + audioId);
                        
                        Audiosnimak audio = (Audiosnimak) em.createNamedQuery("Audiosnimak.findById")
                           .setParameter("id", audioId)
                           .getSingleResult();

                        // List<Slusanje> response = em.createNamedQuery("Slusanje.findAll").getResultList();
                        // response.removeIf(e -> e.getIdAudioSnimak().getId() != audioId);
                        List<Slusanje> response = em.createNamedQuery("Slusanje.findByAudio")
                            .setParameter("Audiosnimak", audio)
                            .getResultList();
                        
                        reply(context, message, response.toArray(new Slusanje[0]));
                        break;
                    }
                    case "listAudioReviews": {      
                        Integer audioId = message.getBody(Integer.class);
                        System.out.println("audioId=" + audioId);
                        
                        Audiosnimak audio = (Audiosnimak) em.createNamedQuery("Audiosnimak.findById")
                            .setParameter("id", audioId)
                            .getSingleResult();
                     
                        List<Ocena> response = em.createNamedQuery("Ocena.findByAudio")
                            .setParameter("Audiosnimak", audio)
                            .getResultList();
                        
                        reply(context, message, response.toArray(new Ocena[0]));
                        break;
                    }
                    case "createAudioReview": { 
                        Ocena ocena = message.getBody(Ocena.class);
                        ocena.setDatumVreme(LocalDateTime.now());
                        em.getTransaction().begin();                        
                        em.persist(ocena);
                        em.getTransaction().commit();
                        reply(context, message, Boolean.TRUE);
                        break;
                    }     
                    case "updateAudioReview": { 
                        Ocena ocenaNew = message.getBody(Ocena.class);
                        Audiosnimak audio = (Audiosnimak) em.createNamedQuery("Audiosnimak.findById")
                                .setParameter("id", ocenaNew.getIdAudioSnimak().getId())
                                .getSingleResult();
                        Korisnik korisnik = (Korisnik) em.createNamedQuery("Korisnik.findById")
                                .setParameter("id", ocenaNew.getIdKorisnik().getId())
                                .getSingleResult();
                        Ocena ocenaOld = (Ocena) em.createNamedQuery("Ocena.findByKorisnik")
                                .setParameter("Audiosnimak", audio)
                                .setParameter("Korisnik", korisnik)
                                .getSingleResult();
                        ocenaOld.setOcena(ocenaNew.getOcena());
                        ocenaOld.setDatumVreme(LocalDateTime.now());
                        em.getTransaction().begin();                        
                        em.persist(ocenaOld);
                        em.getTransaction().commit();
                        reply(context, message, Boolean.TRUE);
                        break;
                    }     
                    case "deleteAudioReview": { 
                        Ocena ocenaNew = message.getBody(Ocena.class);
                        Audiosnimak audio = (Audiosnimak) em.createNamedQuery("Audiosnimak.findById")
                                .setParameter("id", ocenaNew.getIdAudioSnimak().getId())
                                .getSingleResult();
                        Korisnik korisnik = (Korisnik) em.createNamedQuery("Korisnik.findById")
                                .setParameter("id", ocenaNew.getIdKorisnik().getId())
                                .getSingleResult();
                        Ocena ocenaOld = (Ocena) em.createNamedQuery("Ocena.findByKorisnik")
                                .setParameter("Audiosnimak", audio)
                                .setParameter("Korisnik", korisnik)
                                .getSingleResult();
                        em.getTransaction().begin();                        
                        em.remove(ocenaOld);
                        em.getTransaction().commit();
                        reply(context, message, Boolean.TRUE);
                        break;
                    }     
                    case "createSubscription": {
                        Pretplata pretplata = message.getBody(Pretplata.class);
                        pretplata.setPocetak(LocalDateTime.now());
                        
                        String mesec = pretplata.getPocetak().format(DateTimeFormatter.ofPattern("yyyy-MM"));
                        List<Cenapaketa> cenepaketa = em.createNamedQuery("Cenapaketa.findByMesec")
                                .setParameter("mesec", mesec)
                                .getResultList();
                        
                        boolean found = false;
                        for(Cenapaketa item: cenepaketa) {
                            System.out.println(item);
                            if(!item.getIdPaket().equals(pretplata.getIdPaket()))
                                continue;

                            pretplata.setCena(item.getCena());
                            System.out.println("cena=" + item.getCena());
                            
                            em.getTransaction().begin(); 
                            em.persist(pretplata);
                            em.getTransaction().commit();                        
                            
                            found = true;
                        }
                        
                        if(!found)
                            System.err.println("Nedostaje cena paketa br " + pretplata.getIdPaket().getId() + " za mesec " + mesec);
                        
                        reply(context, message, found);
                        break;
                    }
                    case "addToFavourites": {
                        Audiosnimak audioNew = message.getBody(Audiosnimak.class);
                        System.out.println("addToFavourites: " + audioNew);
                        Audiosnimak audio = (Audiosnimak) em.createNamedQuery("Audiosnimak.findById")
                                .setParameter("id", audioNew.getId())
                                .getSingleResult();
                        Integer userId = Integer.parseInt(message.getStringProperty("userId"));
                        Korisnik user = (Korisnik) em.createNamedQuery("Korisnik.findById").setParameter("id", userId).getSingleResult();
                        em.getTransaction().begin(); 
                        user.getAudiosnimakList().add(audio);
                        em.getTransaction().commit();                        
                        reply(context, message, Boolean.TRUE);
                        break;
                    }
                    case "listFavouriteAudios": {      
                        Integer userId = message.getBody(Integer.class);
                        Korisnik user = (Korisnik) em.createNamedQuery("Korisnik.findById").setParameter("id", userId).getSingleResult();
                        List<Audiosnimak> response = user.getAudiosnimakList(); // omiljeni audio snimci
                        reply(context, message, response.toArray(new Audiosnimak[0]));
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
