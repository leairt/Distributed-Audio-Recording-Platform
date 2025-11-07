/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entiteti;

import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author sergej
 */
@Entity
@Table(name = "slusanje")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Slusanje.findAll", query = "SELECT s FROM Slusanje s"),
    @NamedQuery(name = "Slusanje.findById", query = "SELECT s FROM Slusanje s WHERE s.id = :id"),
    @NamedQuery(name = "Slusanje.findByAudio", query = "SELECT s FROM Slusanje s WHERE s.idAudioSnimak = :Audiosnimak"),
    @NamedQuery(name = "Slusanje.findByDatumVremePocetka", query = "SELECT s FROM Slusanje s WHERE s.datumVremePocetka = :datumVremePocetka"),
    @NamedQuery(name = "Slusanje.findBySekundaPocetka", query = "SELECT s FROM Slusanje s WHERE s.sekundaPocetka = :sekundaPocetka"),
    @NamedQuery(name = "Slusanje.findByTrajanjeOdslusanihSekundi", query = "SELECT s FROM Slusanje s WHERE s.trajanjeOdslusanihSekundi = :trajanjeOdslusanihSekundi")})
public class Slusanje implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "Id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "DatumVremePocetka")
    private LocalDateTime datumVremePocetka;
    @Basic(optional = false)
    @NotNull
    @Column(name = "SekundaPocetka")
    private int sekundaPocetka;
    @Basic(optional = false)
    @NotNull
    @Column(name = "TrajanjeOdslusanihSekundi")
    private int trajanjeOdslusanihSekundi;
    @JoinColumn(name = "IdKorisnik", referencedColumnName = "Id")
    @ManyToOne(optional = false)
    private Korisnik idKorisnik;
    @JoinColumn(name = "IdAudioSnimak", referencedColumnName = "Id")
    @ManyToOne(optional = false)
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
        return "entiteti.Slusanje[ id=" + id + " ]";
    }
    
}
