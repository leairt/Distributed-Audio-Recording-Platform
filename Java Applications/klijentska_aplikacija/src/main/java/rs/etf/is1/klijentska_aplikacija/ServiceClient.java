/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rs.etf.is1.klijentska_aplikacija;

import entiteti.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import javax.ws.rs.core.HttpHeaders;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;

public class ServiceClient {
    private static final String BASE_URL = "http://localhost:8080/centralni_server/api/server";

    private static String username;
    private static String password;
    
    public static void setUsername(String username) {
        ServiceClient.username = username;
    }

    public static void setPassword(String password) {
        ServiceClient.password = password;
    }
    
    public static Boolean createCity(Mesto mesto) throws Exception {
        return post("/city/create", mesto, Boolean.class);  // povratna vrednost: da li je uspešno izvršeno
    }

    public static List<Mesto> listCities() throws Exception {
        return Arrays.asList(get("/city/list", Mesto[].class));  // povratna vrednost: lista mesta
    }

    public static Long createUser(Korisnik korisnik) throws Exception {
        return post("/user/create", korisnik, Long.class); // povratna vrednost: id korisnika
    }

    public static Boolean updateUserEmail(String email) throws Exception {
        Korisnik korisnik = new Korisnik();
        korisnik.setEmail(email);
        return post("/user/update/email", korisnik, Boolean.class);  // povratna vrednost: da li je uspešno izvršeno
    }

    public static Boolean updateUserCity(String naziv) throws Exception {
        Korisnik korisnik = new Korisnik();
        Mesto mesto = new Mesto();
        mesto.setNaziv(naziv);
        korisnik.setMesto(mesto);
        return post("/user/update/city", korisnik, Boolean.class);  // povratna vrednost: da li je uspešno izvršeno
    }

    public static List<Korisnik> listUsers() throws Exception {
        return Arrays.asList(get("/user/list", Korisnik[].class));  // povratna vrednost: lista korisnika
    }
    
    public static Boolean createCategory(Kategorija kategorija) throws Exception {
        return post("/category/create", kategorija, Boolean.class);
    }
    
    public static Boolean createAudio(Audiosnimak audiosnimak) throws Exception {
        return post("/audio/create", audiosnimak, Boolean.class); // povratna vrednost: da li je uspešno izvršeno
    }
    
    public static Boolean updateAudioName(Integer audioId, String name) throws Exception {
        Audiosnimak audiosnimak = new Audiosnimak();
        audiosnimak.setNaziv(name);
        return post("/audio/update/" + audioId + "/name", audiosnimak, Boolean.class);  // povratna vrednost: da li je uspešno izvršeno
    }
    
    public static Boolean addAudioCategory(Integer audioId, Kategorija kategorija) throws Exception {
        return post("/audio/update/" + audioId + "/category/add", kategorija, Boolean.class);  // povratna vrednost: da li je uspešno izvršeno
    }
    
    public static List<Kategorija> listCategories() throws Exception {
        return Arrays.asList(get("/category/list", Kategorija[].class)); // povratna vrednost: lista kategorija
    }

    public static List<Audiosnimak> listAudios() throws Exception {
        return Arrays.asList(get("/audio/list", Audiosnimak[].class)); // povratna vrednost: lista audiosnimaka
    }
    
    public static List<Kategorija> listAudioCategories(Integer audioId) throws Exception {
        return Arrays.asList(get("/audio/" + audioId + "/category/list", Kategorija[].class)); // povratna vrednost: lista kategorija
    }
    
    public static Boolean createPackage(Paket paket) throws Exception {
        return post("/package/create", paket, Boolean.class);   // povratna vrednost: da li je uspešno izvršeno
    }
    
    public static Boolean updatePackagePrice(Integer paketId, String mesec, BigDecimal cena) throws Exception{
        Cenapaketa cenapaketa = new Cenapaketa();
        cenapaketa.setMesec(mesec);
        cenapaketa.setCena(cena);
        return post("/package/update/" + paketId + "/price", cenapaketa, Boolean.class);  // povratna vrednost: da li je uspešno izvršeno
    }
    
    public static List<Paket> listPackages() throws Exception {
        return Arrays.asList(get("/package/list", Paket[].class)); // povratna vrednost: lista paketa
    }
    
    public static List<Pretplata> listSubscriptions() throws Exception {
        return Arrays.asList(get("/subscription/list", Pretplata[].class)); // povratna vrednost: lista pretplata
    }
    
    public static Boolean createSubscription(Integer paketId) throws Exception {
        Pretplata pretplata = new Pretplata();
        // pretplata.setIdKorisnik(new Korisnik());
        // pretplata.getIdKorisnik().setId(korisnikId);
        pretplata.setIdPaket(new Paket());
        pretplata.getIdPaket().setId(paketId);
        return post("/subscription/create", pretplata, Boolean.class);
    }
    
    public static Boolean addAudioListening(Integer audioId, Slusanje slusanje) throws Exception {
        return post("/audio/" + audioId + "/listening/add", slusanje, Boolean.class);
    }

    public static List<Slusanje> listAudioListenings(Integer audioId) throws Exception {
        return Arrays.asList(get("/audio/" + audioId + "/listening/list", Slusanje[].class)); // povratna vrednost: lista slusanja
    }
    
    public static List<Ocena> listAudioReviews(Integer audioId) throws Exception {
        return Arrays.asList(get("/review/list/" + audioId, Ocena[].class)); // povratna vrednost: lista ocena
    }

    public static Boolean addToFavourites(Integer audioId) throws Exception {
        Audiosnimak audio = new Audiosnimak();
        audio.setId(audioId);
        return post("/favourite/add", audio, Boolean.class); 
    }
    
    public static List<Audiosnimak> listFavouriteAudios() throws Exception {
        return Arrays.asList(get("/favourite/list", Audiosnimak[].class)); // povratna vrednost: lista audiosnimaka
    }
    
    public static Boolean deleteAudio(Integer audioId) throws Exception {
        return delete("/audio/delete/" + audioId, null, Boolean.class); // povratna vrednost: da li je uspešno izvršeno
    }
    
    public static Boolean createReview(Integer audioId, Ocena ocena) throws Exception  {
        return post("/review/create/" + audioId, ocena, Boolean.class); // povratna vrednost: da li je uspešno izvršeno
    }

    public static Boolean updateReview(Integer audioId, Ocena ocena) throws Exception  {
        return post("/review/update/" + audioId, ocena, Boolean.class); // povratna vrednost: da li je uspešno izvršeno
    }

    public static Boolean deleteReview(Integer audioId) throws Exception  {
        return delete("/review/delete/" + audioId, null, Boolean.class); // povratna vrednost: da li je uspešno izvršeno
    }

    // GET - path putanja zahteva i tip povratne vrednosti 
    private static <T> T get(String path, Class<T> clazz) throws Exception {
        String token = Base64.getEncoder().encodeToString((username + ":" + password).getBytes(StandardCharsets.UTF_8));

        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(BASE_URL + path);
        Response response = target.request(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Basic " + token)
                .get();
        if (response.getStatus() != 200) {
            throw new Exception("[get] " + response.getStatusInfo());
        }
        T resp = response.readEntity(clazz);
        response.close();
        return resp;
    }
    
    // POST - path putanja zahteva, objekat koji treba umetnuti u bazu i tip povratne vrednosti
    private static <T> T post(String path, Serializable object, Class<T> clazz) throws Exception {
        String token = Base64.getEncoder().encodeToString((username + ":" + password).getBytes(StandardCharsets.UTF_8));

        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(BASE_URL  + path);
        Response response = target
                .request(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Basic " + token)
                .post(Entity.json(object));
        if (response.getStatus() != 200) {
            throw new Exception("[post] " + response.getStatusInfo());
        }
        T resp = response.readEntity(clazz);
        response.close();
        return resp;
    }

    private static <T> T delete(String path, Serializable object, Class<T> clazz) throws Exception {
        String token = Base64.getEncoder().encodeToString((username + ":" + password).getBytes(StandardCharsets.UTF_8));

        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(BASE_URL  + path);
        Response response = target.request(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Basic " + token)
                .delete();
        if (response.getStatus() != 200) {
            throw new Exception("[delete] " + response.getStatusInfo());
        }
        T resp = response.readEntity(clazz);
        response.close();
        return resp;
    }
    
    // TODO: proveriti
    // PUT - path putanja zahteva, objekat koji treba izmeniti u bazu i tip povratne vrednosti
    /*private static <T> T put(String path, Serializable object, Class<T> clazz) throws Exception {
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(BASE_URL  + path);
        Response response = target.request(MediaType.APPLICATION_JSON)
                .method("PUT", Entity.json(object));
        
        if (response.getStatus() != 200) {
            throw new Exception("[put] " + response.getStatusInfo());
        }
        T resp = response.readEntity(clazz);
        response.close();
        return resp;
    }*/    
}
