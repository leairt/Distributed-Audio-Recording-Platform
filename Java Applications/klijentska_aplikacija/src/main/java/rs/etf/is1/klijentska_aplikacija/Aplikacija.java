package rs.etf.is1.klijentska_aplikacija;

import entiteti.*;
import java.util.List;
import java.util.Scanner;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Aplikacija {
    public static void main(String[] args) throws Exception {
        // potrebno zbog LocalDateTime
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        
        try (Scanner scanner = new Scanner(System.in)) {
            
            login(scanner);
            
            while (true) {
                try {
                    System.out.println("\n--- Klijentska aplikacija ---");
                    System.out.println("[01] Kreiranje grada");
                    System.out.println("[02] Kreiranje korisnika");
                    System.out.println("[03] Promena email adrese za korisnika");
                    System.out.println("[04] Promena mesta za korisnika");
                    System.out.println("[05] Kreiranje kategorije");
                    System.out.println("[06] Kreiranje audio snimka");
                    System.out.println("[07] Promena naziva audio snimka");
                    System.out.println("[08] Dodavanje kategorije audio snimku");
                    System.out.println("[09] Kreiranje paketa");
                    System.out.println("[10] Promena mesečne cene za paket");
                    System.out.println("[11] Kreiranje pretplate korisnika na paket");
                    System.out.println("[12] Kreiranje slušanja audio snimka od strane korisnika");
                    System.out.println("[13] Dodavanje audio snimka u omiljene od strane korisnika");
                    System.out.println("[14] Kreiranje ocene korisnika za audio snimak");
                    System.out.println("[15] Menjanje ocene korisnika za audio snimak");
                    System.out.println("[16] Brisanje ocene korisnika za audio snimak");
                    System.out.println("[17] Brisanje audio snimka od strane korisnika koji ga je kreirao");
                    System.out.println("[18] Dohvatanje svih gradova");
                    System.out.println("[19] Dohvatanje svih korisnika");
                    System.out.println("[20] Dohvatanje svih kategorija");
                    System.out.println("[21] Dohvatanje svih audio snimaka");
                    System.out.println("[22] Dohvatanje kategorija za određeni audio snimak");
                    System.out.println("[23] Dohvatanje svih paketa");
                    System.out.println("[24] Dohvatanje svih pretplata za korisnika");
                    System.out.println("[25] Dohvatanje svih slušanja za audio snimak");
                    System.out.println("[26] Dohvatanje svih ocena za audio snimak");
                    System.out.println("[27] Dohvatanje liste omiljenih audio snimaka za korisnika");
                    System.out.println("[X]  Izlaz");

                    String opcija = readMenuChoice(scanner).toUpperCase();

                    switch (opcija) {
                        case "01": { // Kreiranje grada
                            Mesto mesto = new Mesto();
                            mesto.setNaziv(readNonEmptyLine(scanner, "Naziv grada: "));
                            ServiceClient.createCity(mesto);
                            break;
                        }
                        case "02": { // Kreiranje korisnika
                            Korisnik korisnik = new Korisnik();
                            korisnik.setIme(readNonEmptyLine(scanner, "Ime korisnika: "));
                            korisnik.setEmail(readNonEmptyLine(scanner, "E-mail: "));
                            korisnik.setGodiste(Integer.parseInt(readNonEmptyLine(scanner, "Godiste: ")));
                            korisnik.setPol(readNonEmptyLine(scanner, "Pol[M/Z]: ").charAt(0));
                            Mesto mesto = new Mesto();
                            mesto.setNaziv(readNonEmptyLine(scanner, "Mesto: "));
                            korisnik.setMesto(mesto);
                            ServiceClient.createUser(korisnik);
                            break;
                        }
                        case "03": { // Promena email adrese za korisnika
                            // Integer korisnikId = Integer.parseInt(readNonEmptyLine(scanner, "Id korisnika: "));
                            String email = readNonEmptyLine(scanner, "Nova email adresa: ");
                            ServiceClient.updateUserEmail(email);
                            break;
                        }
                        case "04": { // Promena mesta za korisnika
                            // Integer korisnikId = Integer.parseInt(readNonEmptyLine(scanner, "Id korisnika: "));
                            String mesto = readNonEmptyLine(scanner, "Naziv novog mesta: ");
                            ServiceClient.updateUserCity(mesto);
                            break;
                        }
                        case "05": { // Kreiranje kategorije
                            Kategorija kategorija = new Kategorija();
                            kategorija.setNaziv(readNonEmptyLine(scanner, "Naziv kategorije: "));
                            ServiceClient.createCategory(kategorija);
                            break;
                        }
                        case "06": { // Kreiranje audio snimka
                            Audiosnimak audiosnimak = new Audiosnimak();
                            audiosnimak.setNaziv(readNonEmptyLine(scanner, "Naziv audiosnimka: "));
                            audiosnimak.setTrajanje(readIntLine(scanner, "Trajanje(u sekundama): "));
                            // TODO: datum postavljanja
                            // TODO: idvlasnik
                            ServiceClient.createAudio(audiosnimak);
                            break;
                        }
                        case "07": { // Promena naziva audio snimka
                            Integer audiosnimakId = Integer.parseInt(readNonEmptyLine(scanner, "Id audiosnimka: "));
                            String naziv = readNonEmptyLine(scanner, "Novi naziv audiosnimka: ");
                            ServiceClient.updateAudioName(audiosnimakId, naziv);
                            break;
                        }

                        case "08": { // Dodavanje kategorije audio snimku
                            Integer audiosnimakId = Integer.parseInt(readNonEmptyLine(scanner, "Id audiosnimka: "));
                            Kategorija kategorija = new Kategorija();
                            kategorija.setNaziv(readNonEmptyLine(scanner, "Naziv kategorije: "));
                            ServiceClient.addAudioCategory(audiosnimakId, kategorija);
                            break;
                        }
                        case "09": { // Kreiranje paketa
                            Paket paket = new Paket();
                            paket.setNaziv(readNonEmptyLine(scanner, "Naziv paketa: "));
                            ServiceClient.createPackage(paket);
                            break;
                        }
                        case "10": { // Promena mesečne cene za paket
                            Integer paketId = Integer.parseInt(readNonEmptyLine(scanner, "Id paketa: "));
                            String mesec = readNonEmptyLine(scanner, "Mesec (GGGG-MM): ");
                            BigDecimal cena = BigDecimal.valueOf(readIntLine(scanner, "Nova cena: "));
                            ServiceClient.updatePackagePrice(paketId, mesec, cena);
                            break;
                        }
                        case "11": { // Kreiranje pretplate korisnika na paket
                            // Integer korisnikId = Integer.parseInt(readNonEmptyLine(scanner, "Id korisnika: "));
                            // Integer paketId = Integer.parseInt(readNonEmptyLine(scanner, "Id paketa: "));
                            // ServiceClient.createSubscription(korisnikId, paketId);
                            Integer paketId = Integer.parseInt(readNonEmptyLine(scanner, "Id paketa: "));
                            ServiceClient.createSubscription(paketId);
                            break;
                        }
                        case "12": { // Kreiranje slušanja audio snimka od strane korisnika
                            Integer audioId = Integer.parseInt(readNonEmptyLine(scanner, "Id audiosnimka: "));
                            Slusanje slusanje = new Slusanje();
                            String datumVremePocetka = readNonEmptyLine(scanner, "Početak slušanja (GGGG-MM-DD HH:MM:SS): ");                            
                            DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                            slusanje.setDatumVremePocetka(LocalDateTime.parse(datumVremePocetka, format));

                            slusanje.setSekundaPocetka(Integer.parseInt(readNonEmptyLine(scanner, "Sekunda početka: ")));
                            slusanje.setTrajanjeOdslusanihSekundi(Integer.parseInt(readNonEmptyLine(scanner, "Trajanje odslušanih sekundi: ")));
                            ServiceClient.addAudioListening(audioId, slusanje);
                            break;
                        }
                        case "13": { // Dodavanje audio snimka u omiljene od strane korisnika
                            Ocena ocena = new Ocena();                        
                            Integer audioId = Integer.parseInt(readNonEmptyLine(scanner, "Id audiosnimka: "));
                            ServiceClient.addToFavourites(audioId);
                            break;
                        }
                        case "14": { // Kreiranje ocene korisnika za audio snimak
                            Ocena ocena = new Ocena();                        
                            Integer audioId = Integer.parseInt(readNonEmptyLine(scanner, "Id audiosnimka: "));
                            ocena.setOcena(Integer.parseInt(readNonEmptyLine(scanner, "Ocena: ")));
                            ServiceClient.createReview(audioId, ocena);
                            break;
                        }
                        case "15": { // Menjanje ocene korisnika za audio snimak
                            Ocena ocena = new Ocena();                        
                            Integer audioId = Integer.parseInt(readNonEmptyLine(scanner, "Id audiosnimka: "));
                            ocena.setOcena(Integer.parseInt(readNonEmptyLine(scanner, "Nova ocena: ")));
                            ServiceClient.updateReview(audioId, ocena);
                            break;
                        }
                        case "16": { // Brisanje ocene korisnika za audio snimak
                            Ocena ocena = new Ocena();                        
                            Integer audioId = Integer.parseInt(readNonEmptyLine(scanner, "Id audiosnimka: "));
                            ServiceClient.deleteReview(audioId);
                            break;
                        }
                        case "17": { // Brisanje audio snimka od strane korisnika koji ga je kreirao
                            Integer audiosnimakId = Integer.parseInt(readNonEmptyLine(scanner, "Id audiosnimka: "));
                            ServiceClient.deleteAudio(audiosnimakId);                        
                            break;
                        }
                        case "18": { // Dohvatanje svih gradova
                            List<Mesto> list = ServiceClient.listCities();
                            for(Mesto mesto: list)
                                System.out.println(mesto);
                            break;
                        }
                        case "19": { // Dohvatanje svih korisnika
                            List<Korisnik> list = ServiceClient.listUsers();
                            for(Korisnik korisnik: list)
                                System.out.println(korisnik);
                            break;
                        }
                        case "20": { // Dohvatanje svih kategorija
                            List<Kategorija> list = ServiceClient.listCategories();
                            for(Kategorija kategorija: list)
                                System.out.println(kategorija);
                            break;
                        }
                        case "21": { // Dohvatanje svih audio snimaka
                            List<Audiosnimak> list = ServiceClient.listAudios();
                            for(Audiosnimak audiosnimak: list)
                                System.out.println(audiosnimak);
                            break;
                        }
                        case "22": { // Dohvatanje kategorija za određeni audio snimak
                            Integer audiosnimakId = Integer.parseInt(readNonEmptyLine(scanner, "Id audiosnimka: "));
                            List<Kategorija> list = ServiceClient.listAudioCategories(audiosnimakId);
                            for(Kategorija kategorija: list)
                                System.out.println(kategorija);
                            break;
                        }
                        case "23": { // Dohvatanje svih paketa
                            List<Paket> list = ServiceClient.listPackages();
                            for(Paket paket: list)
                                System.out.println(paket);
                            break;
                        }
                        case "24": { // Dohvatanje svih pretplata za korisnika
                            List<Pretplata> list = ServiceClient.listSubscriptions();
                            for(Pretplata pretplata: list)
                                System.out.println(pretplata);
                            break;
                        }
                        case "25": { // Dohvatanje svih slušanja za audio snimak
                            Integer audiosnimakId = Integer.parseInt(readNonEmptyLine(scanner, "Id audiosnimka: "));
                            List<Slusanje> list = ServiceClient.listAudioListenings(audiosnimakId);
                            for(Slusanje slusanje: list)
                                System.out.println(slusanje);
                            break;
                        }
                        case "26": { // Dohvatanje svih ocena za audio snimak
                            Integer audiosnimakId = Integer.parseInt(readNonEmptyLine(scanner, "Id audiosnimka: "));
                            List<Ocena> list = ServiceClient.listAudioReviews(audiosnimakId);
                            for(Ocena ocena: list)
                                System.out.println(ocena);
                            break;
                        }
                        case "27": { // Dohvatanje liste omiljenih audio snimaka za korisnika
                            List<Audiosnimak> list = ServiceClient.listFavouriteAudios();
                            for(Audiosnimak audiosnimak: list)
                                System.out.println(audiosnimak);
                            break;
                        }
                        case "X":
                            System.out.println("Izlazim iz aplikacije.");
                            return;
                        default: {
                            System.out.println("Nepoznata opcija.");
                        }

                    }
                }
                catch(Exception e) {
                    System.out.println("GREŠKA: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }
    }
    
    // pomoćna funkcija za odabir opcije iz menija
    static String readMenuChoice(Scanner sc) {
        while (true) {
            System.out.println("Izaberite opciju:");
            String line = sc.nextLine().trim();
            if (line.isEmpty()) {
                continue;
            }
            return line;
        }
    }
    
    // pomoćna funkcija za unos tipa string
    static String readNonEmptyLine(Scanner sc, String prompt) {
        while (true) {
            System.out.println(prompt);
            String s = sc.nextLine().trim();
            if (!s.isEmpty()) {
                return s;
            }
            System.out.println("Unos ne sme biti prazan. Pokusajte ponovo.");
        }
    }

    // pomoćna funkcija za unos tipa int
    static int readIntLine(Scanner sc, String prompt) {
        while (true) {
            System.out.println(prompt);
            String line = sc.nextLine().trim();
            if (line.isEmpty()) {
                continue;
            }
            try {
                return Integer.parseInt(line);
            } catch (NumberFormatException e) {
                System.out.println("Morate uneti broj!");
            }
        }
    }

    private static void login(Scanner scanner) {
        String username = readNonEmptyLine(scanner, "Username: ");
        String password = readNonEmptyLine(scanner, "Password: ");
        
        ServiceClient.setUsername(username.trim());
        ServiceClient.setPassword(password.trim());
    }
}
