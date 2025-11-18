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
@Table(name = "ocena")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Ocena.findAll", query = "SELECT o FROM Ocena o"),
    @NamedQuery(name = "Ocena.findById", query = "SELECT o FROM Ocena o WHERE o.id = :id"),
    @NamedQuery(name = "Ocena.findByOcena", query = "SELECT o FROM Ocena o WHERE o.ocena = :ocena"),
    @NamedQuery(name = "Ocena.findByKorisnik", query = "SELECT o FROM Ocena o WHERE o.idAudioSnimak = :Audiosnimak AND o.idKorisnik = :Korisnik"),
    @NamedQuery(name = "Ocena.findByAudio", query = "SELECT o FROM Ocena o WHERE o.idAudioSnimak = :Audiosnimak"),
    @NamedQuery(name = "Ocena.findByDatumVreme", query = "SELECT o FROM Ocena o WHERE o.datumVreme = :datumVreme")})
public class Ocena implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "Id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Ocena")
    private int ocena;
    @Basic(optional = false)
    @NotNull
    @Column(name = "DatumVreme")
    private LocalDateTime datumVreme;
    @JoinColumn(name = "IdKorisnik", referencedColumnName = "Id")
    @ManyToOne(optional = false)
    private Korisnik idKorisnik;
    @JoinColumn(name = "IdAudioSnimak", referencedColumnName = "Id")
    @ManyToOne(optional = false)
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
