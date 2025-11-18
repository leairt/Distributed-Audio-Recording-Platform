/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entiteti;

import java.io.Serializable;
import java.math.BigDecimal;

public class Cenapaketa implements Serializable {

    private static final long serialVersionUID = 1L;
   
    private Integer id;
    private BigDecimal cena;
    private String mesec;
    private Paket idPaket;

    public Cenapaketa() {
    }

    public Cenapaketa(Integer id) {
        this.id = id;
    }

    public Cenapaketa(Integer id, BigDecimal cena, String mesec) {
        this.id = id;
        this.cena = cena;
        this.mesec = mesec;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BigDecimal getCena() {
        return cena;
    }

    public void setCena(BigDecimal cena) {
        this.cena = cena;
    }

    public String getMesec() {
        return mesec;
    }

    public void setMesec(String mesec) {
        this.mesec = mesec;
    }

    public Paket getIdPaket() {
        return idPaket;
    }

    public void setIdPaket(Paket idPaket) {
        this.idPaket = idPaket;
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
        if (!(object instanceof Cenapaketa)) {
            return false;
        }
        Cenapaketa other = (Cenapaketa) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entiteti.Cenapaketa[ id=" + id + " ]";
    }
    
}
