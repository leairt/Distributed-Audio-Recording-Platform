/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package podsistem2;

import entiteti.*;
import java.io.Serializable;
import java.time.LocalDateTime;
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

    @Resource(lookup = "jms/Podsistem2Requests")
    static Queue queue;
    
    public static void main(String[] args) {
        
        JMSContext context = connectionFactory.createContext();
        JMSConsumer consumer = context.createConsumer(queue);
        
        while(true){
            System.out.println("[podsistem2] Čekam zahteve...");
            EntityManagerFactory emf = null;
            EntityManager em = null;

            try {
                ObjectMessage message = (ObjectMessage) consumer.receive(); // primljena poruka
                String operation = message.getStringProperty("operation"); //  dohvatanje operacije
                System.out.println("Operacija: " + operation);
                
                emf = Persistence.createEntityManagerFactory("podsistem2PU");
                em = emf.createEntityManager();
                
                switch(operation) {
                    case "createAudio":{
                        Audiosnimak audiosnimak = message.getBody(Audiosnimak.class);
                        em.getTransaction().begin();                        
                        em.persist(audiosnimak);
                        em.getTransaction().commit();
                        reply(context, message, Boolean.TRUE);
                        break;
                    }
                    case "deleteAudio": {
                        Integer userId = Integer.parseInt(message.getStringProperty("userId"));
                        Audiosnimak audioNew = message.getBody(Audiosnimak.class);
                        System.out.println("audioId=" + audioNew.getId());
                        System.out.println("userId=" + userId);
                        
                        Audiosnimak audioOld = (Audiosnimak) em.createNamedQuery("Audiosnimak.findById")
                                .setParameter("id", audioNew.getId())
                                .getSingleResult();
                        
                        boolean response;
                        if(audioOld != null && audioOld.getIdVlasnik().getId().equals(userId)) {
                            em.getTransaction().begin();                        
                            em.remove(audioOld);
                            em.getTransaction().commit();
                            response = true;
                        }
                        else {
                            System.out.println("Snimak ne postoji ili korisnik nije vlasnik tog audio snimka.");
                            response = false;
                        }
                        reply(context, message, response);
                    }
                    case "createCategory":{
                        Kategorija kategorija = message.getBody(Kategorija.class);
                        em.getTransaction().begin();                        
                        em.persist(kategorija);
                        em.getTransaction().commit();
                        reply(context, message, Boolean.TRUE);
                        break;
                    }
                    case "updateAudioName":{
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
                        break;
                    }
                    case "addAudioCategory":{
                        Kategorija kategorija = message.getBody(Kategorija.class);
                        Integer audioId = Integer.parseInt(message.getStringProperty("audioId"));
                        Audiosnimak audiosnimak = (Audiosnimak) em.createNamedQuery("Audiosnimak.findById")
                                .setParameter("id", audioId)
                                .getSingleResult();
                        audiosnimak.getKategorijaList().add(kategorija);
                        em.getTransaction().begin();                        
                        em.persist(audiosnimak);
                        em.getTransaction().commit();
                        reply(context, message, Boolean.TRUE);
                        break;
                    }
                    case "listCategories": {                    
                        List<Kategorija> response = em.createNamedQuery("Kategorija.findAll").getResultList();
                        for(Kategorija kategorija: response)
                            System.out.println(kategorija);
                        reply(context, message, response.toArray(new Kategorija[0]));
                        break;
                    }
                    case "listAudios": {                    
                        List<Audiosnimak> response = em.createNamedQuery("Audiosnimak.findAll").getResultList();
                        for(Audiosnimak audiosnimak: response)
                            System.out.println(audiosnimak);
                        reply(context, message, response.toArray(new Audiosnimak[0]));
                        break;
                    }
                    case "listAudioCategories": {               
                        Integer audioId = Integer.parseInt(message.getStringProperty("audioId"));
                        Audiosnimak audiosnimak = (Audiosnimak) em.createNamedQuery("Audiosnimak.findById").setParameter("id", audioId).getSingleResult();
                        List<Kategorija> response = audiosnimak.getKategorijaList();
                        for(Kategorija kategorija: response)
                            System.out.println(kategorija);
                        reply(context, message, response.toArray(new Kategorija[0]));
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
