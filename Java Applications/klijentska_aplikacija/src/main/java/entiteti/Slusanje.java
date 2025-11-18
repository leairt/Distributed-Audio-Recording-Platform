/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entiteti;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Slusanje implements Serializable {

    private static final long serialVersionUID = 1L;
  
    private Integer id;
    private LocalDateTime datumVremePocetka;
    private int sekundaPocetka;
    private int trajanjeOdslusanihSekundi;
    private Korisnik idKorisnik;
    private Audiosnimak idAudioSnimak;

    public Slusanje() {
    }

    public Slusanje(Integer id) {
        this.id = id;
    }

    public Slusanje(Integer id, LocalDateTime datumVremePocetka, int sekundaPocetka, int trajanjeOdslusanihSekundi) {
        this.id = id;
        this.datumVremePocetka = datumVremePocetka;
        this.sekundaPocetka = sekundaPocetka;
        this.trajanjeOdslusanihSekundi = trajanjeOdslusanihSekundi;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDateTime getDatumVremePocetka() {
        return datumVremePocetka;
    }

    public void setDatumVremePocetka(LocalDateTime datumVremePocetka) {
        this.datumVremePocetka = datumVremePocetka;
    }

    public int getSekundaPocetka() {
        return sekundaPocetka;
    }

    public void setSekundaPocetka(int sekundaPocetka) {
        this.sekundaPocetka = sekundaPocetka;
    }

    public int getTrajanjeOdslusanihSekundi() {
        return trajanjeOdslusanihSekundi;
    }

    public void setTrajanjeOdslusanihSekundi(int trajanjeOdslusanihSekundi) {
        this.trajanjeOdslusanihSekundi = trajanjeOdslusanihSekundi;
    }

    public Korisnik getIdKorisnik() {
        return idKorisnik;
    }

    public void setIdKorisnik(Korisnik idKorisnik) {
        this.idKorisnik = idKorisnik;
    }

    public Audiosnimak getIdAudioSnimak() {
        return idAudioSnimak;
    }

    public void setIdAudioSnimak(Audiosnimak idAudioSnimak) {
        this.idAudioSnimak = idAudioSnimak;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Slusanje)) {
            return false;
        }
        Slusanje other = (Slusanje) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("korisnik: " + idKorisnik.getIme() + ", ");
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
        String formatted = datumVremePocetka.format(format);
        sb.append("datum i vreme slušanja: " + formatted + ", ");
        sb.append("početak: " + Audiosnimak.formatSeconds(sekundaPocetka) + ", ");
        sb.append("odslušano: " + trajanjeOdslusanihSekundi + "s");
        return sb.toString();
    }
    
}
