package app.logic.pojo;

import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import app.logic.pojo.ServicioBean;
import java.util.Date;

/**
 *
 * @author Carlos
 */
public class FacturaBean {
    private SimpleIntegerProperty id;
    private SimpleObjectProperty<ClienteBean> cliente;
    private SimpleObjectProperty<List<ServicioBean>> servicios;
    private SimpleObjectProperty<Date> date;
    private SimpleDoubleProperty total;
    
    public FacturaBean(Integer id, ClienteBean cliente,List<ServicioBean> servicios, Date date, Double total){
        this.id=new SimpleIntegerProperty(id);
        this.cliente=new SimpleObjectProperty<ClienteBean>(cliente);
        this.servicios = new SimpleObjectProperty<List<ServicioBean>>(servicios);
        this.date = new SimpleObjectProperty<Date>(date);
        this.total = new SimpleDoubleProperty(total);
    }
    
    public FacturaBean(){
        this.id=new SimpleIntegerProperty(0);
        this.cliente=new SimpleObjectProperty<ClienteBean>();
        this.servicios = new SimpleObjectProperty<List<ServicioBean>>(new ArrayList<>());
        this.date = new SimpleObjectProperty<Date>();
        this.total = new SimpleDoubleProperty(0);
    }

    /**
     * @return the servicios
     */
    public List<ServicioBean> getServicios() {
        return servicios.get();
    }

    /**
     * @param servicios the servicios to set
     */
    public void setServicios(List<ServicioBean> servicios) {
        this.servicios.set(servicios);
    }

    /**
     * @return the id
     */
    public Integer getId() {
        return id.get();
    }

    /**
     * @param id the id to set
     */
    public void setId(Integer id) {
        this.id.set(id);
    }

    /**
     * @return the cliente
     */
    public ClienteBean getCliente() {
        return cliente.get();
    }

    /**
     * @param cliente the idcliente to set
     */
    public void setCliente(ClienteBean cliente) {
        this.cliente.set(cliente);
    }

    /**
     * @return the date
     */
    public Date getDate() {
        return date.get();
    }

    /**
     * @param date the date to set
     */
    public void setDate(Date date) {
        this.date.set(date);
    }

    /**
     * @return the total
     */
    public Double getTotal() {
        return total.get();
    }

    /**
     * @param total the total to set
     */
    public void setTotal(Double total) {
        this.total.set(total);
    }
 
    public String toString(){
        return this.getId()+", "+this.getCliente()+", "+this.getDate()+", "+this.getServicios()+", "+this.getTotal();
    }
    
}
