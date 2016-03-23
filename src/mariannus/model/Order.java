package mariannus.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tim on 19.2.2016.
 */
public class Order {
    private final StringProperty name;
    private final List<Integer> ordered;
//    private final int tableId;

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

    public void setName(String name) {
        this.name.set(name);
    }

    public List<Integer> getOrdered() {
        return ordered;
    }
}
