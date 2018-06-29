package app.logic.pojo;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author Carlos
 */
public class ServicioBean {
    private final SimpleIntegerProperty id;
    private final SimpleStringProperty name; 
    private final SimpleStringProperty description;
    private final SimpleDoubleProperty price;
    
    public ServicioBean(Integer id, String name, String description, Double price){
        this.id=new SimpleIntegerProperty(id);
        this.name=new SimpleStringProperty(name);
        this.description=new SimpleStringProperty(description);
        this.price=new SimpleDoubleProperty(price);
    }
    
    public ServicioBean(String name, String description, Double price){
        this.id=new SimpleIntegerProperty();
        this.name=new SimpleStringProperty(name);
        this.description=new SimpleStringProperty(description);
        this.price=new SimpleDoubleProperty(price);
    }

    public ServicioBean(){
        this.id=new SimpleIntegerProperty(0);
        this.name=new SimpleStringProperty("");
        this.description=new SimpleStringProperty("");
        this.price=new SimpleDoubleProperty(0);
        
    }

    public Integer getId() {
        return id.get();
    }

    public String getName() {
        return name.get();
    }

    public String getDescription() {
        return description.get();
    }

    public Double getPrice() {
        return price.get();
    }

    public void setId(Integer id){
        this.id.set(id);
    }
    
    public void setName(String name){
        this.name.set(name);
    }
    
    public void setDescription(String description){
        this.description.set(description);
    }
    
    public void setPrice(Double price){
        this.price.set(price);
    }
    
    @Override
    public String toString(){
        return this.getName();
    }
}
