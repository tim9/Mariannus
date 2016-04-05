package mariannus.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tim on 19.2.2016.
 */
@XmlRootElement( name = "Order" )
public class Order {
    private final StringProperty name;
    private final List<Integer> ordered;
    private double price = 0;

    public Order(String name) {
        this.name = new SimpleStringProperty(name);
        ordered = new ArrayList<>();
    }
    public Order(){
        name = new SimpleStringProperty("");
        ordered = FXCollections.observableArrayList();
    }

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    @XmlElement( name = "name" )
    public void setName(String name) {
        this.name.set(name);
    }

    @XmlElement( name = "ordered" )
    public List<Integer> getOrdered() {
        return ordered;
    }

    public double getPrice() {
        return price;
    }
    @XmlElement( name = "price" )
    public void setPrice(double price) {
        this.price = price;
    }
}
