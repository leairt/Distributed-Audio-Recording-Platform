/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entiteti;

import java.io.Serializable;
import java.time.LocalDateTime;


public class Audiosnimak implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private Integer id;
    private String naziv;
    private int trajanje;
    private LocalDateTime postavljeno;
    private Korisnik idVlasnik;
    
    public Audiosnimak() {
    }
    
    public Audiosnimak(Integer id) {
        this.id = id;
    }

    public Audiosnimak(Integer id, String naziv, int trajanje, LocalDateTime postavljeno) {
        this.id = id;
        this.naziv = naziv;
        this.trajanje = trajanje;
        this.postavljeno = postavljeno;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public int getTrajanje() {
        return trajanje;
    }

    public void setTrajanje(int trajanje) {
        this.trajanje = trajanje;
    }

    public LocalDateTime getPostavljeno() {
        return postavljeno;
    }

    public void setPostavljeno(LocalDateTime postavljeno) {
        this.postavljeno = postavljeno;
    }

    public Korisnik getIdVlasnik() {
        return idVlasnik;
    }

    public void setIdVlasnik(Korisnik idVlasnik) {
        this.idVlasnik = idVlasnik;
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
        if (!(object instanceof Audiosnimak)) {
            return false;
        }
        Audiosnimak other = (Audiosnimak) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entiteti.Audiosnimak[ id=" + id + " ]";
    }
    
}
