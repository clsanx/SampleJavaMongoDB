package app.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Factura Entity
 * @author Carlos Santos
 */
@Entity
@Table(name="factura", schema="servtecdbh")
public class FacturaEntity implements Serializable{
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    
    @ManyToOne(cascade = CascadeType.PERSIST)
    private ClienteEntity cliente;
    
    @Column(name = "date", nullable = false)
    @Basic
    @Temporal(TemporalType.DATE)
    private Date date;
    
    @ManyToMany(cascade=CascadeType.PERSIST)  
    @JoinTable(name="factura_servicio", joinColumns=@JoinColumn(name="factura_id"), inverseJoinColumns=@JoinColumn(name="servicio_id")) 
    private List<ServicioEntity> servicios;
    
    @Column(name = "total", nullable = false)
    private Double total;

    
    public FacturaEntity(){
        
    }
    
    public FacturaEntity(ClienteEntity cliente, Date date, List<ServicioEntity> servicios, Double total){
        this.cliente=cliente;
        this.date=date;
        this.servicios=servicios;
        this.total=total;
    }

    /**
     * @return the id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return the cliente
     */
    public ClienteEntity getCliente() {
        return cliente;
    }

    /**
     * @param cliente the cliente to set
     */
    public void setCliente(ClienteEntity cliente) {
        this.cliente = cliente;
    }

    /**
     * @return the date
     */
    public Date getDate() {
        return date;
    }

    /**
     * @param date the date to set
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * @return the servicios
     */
    public List<ServicioEntity> getServicios() {
        return servicios;
    }

    /**
     * @param servicios the servicios to set
     */
    public void setServicios(List<ServicioEntity> servicios) {
        this.servicios = servicios;
    }

    /**
     * @return the total
     */
    public Double getTotal() {
        return total;
    }

    /**
     * @param total the total to set
     */
    public void setTotal(Double total) {
        this.total = total;
    }

}
