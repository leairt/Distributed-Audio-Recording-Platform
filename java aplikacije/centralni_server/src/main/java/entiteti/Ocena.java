/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entiteti;

import java.io.Serializable;
import java.time.LocalDateTime;


public class Ocena implements Serializable {

    private static final long serialVersionUID = 1L;
   
    private Integer id;
    private int ocena;
    private LocalDateTime datumVreme;
    private Korisnik idKorisnik;
    private Audiosnimak idAudioSnimak;

    public Ocena() {
    }

    public Ocena(Integer id) {
        this.id = id;
    }

    public Ocena(Integer id, int ocena, LocalDateTime datumVreme) {
        this.id = id;
        this.ocena = ocena;
        this.datumVreme = datumVreme;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getOcena() {
        return ocena;
    }

    public void setOcena(int ocena) {
        this.ocena = ocena;
    }

    public LocalDateTime getDatumVreme() {
        return datumVreme;
    }

    public void setDatumVreme(LocalDateTime datumVreme) {
        this.datumVreme = datumVreme;
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
        if (!(object instanceof Ocena)) {
            return false;
        }
        Ocena other = (Ocena) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entiteti.Ocena[ id=" + id + " ]";
    }
    
}
