/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.riyoce.odin.entities;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

/**
 *
 * @author ricardo
 */
@Entity
public class GrupoAutomotriz implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String nombre;
    private String clave;
    
    @OneToMany(mappedBy="grupo", cascade=CascadeType.ALL)
    private List<Agencia> agencias;
    
    @OneToOne
    private Imagen logo;
    
    public GrupoAutomotriz(){
        agencias = new LinkedList<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
        if (!(object instanceof GrupoAutomotriz)) {
            return false;
        }
        GrupoAutomotriz other = (GrupoAutomotriz) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return String.valueOf(id);
    }

    /**
     * @return the nombre
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * @param nombre the nombre to set
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * @return the agencias
     */
    public List<Agencia> getAgencias() {
        return agencias;
    }

    /**
     * @param agencias the agencias to set
     */
    public void setAgencias(List<Agencia> agencias) {
        this.agencias = agencias;
    }

    /**
     * @return the logo
     */
    public Imagen getLogo() {
        return logo;
    }

    /**
     * @param logo the logo to set
     */
    public void setLogo(Imagen logo) {
        this.logo = logo;
    }

    /**
     * @return the clave
     */
    public String getClave() {
        return clave;
    }

    /**
     * @param clave the clave to set
     */
    public void setClave(String clave) {
        this.clave = clave;
    }
    
}
