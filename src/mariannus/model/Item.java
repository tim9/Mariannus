package mariannus.model;

import javafx.beans.property.*;

/**
 * Created by Tim on 11.2.2016.
 */
public class Item {
    private final IntegerProperty code;
    private final StringProperty name;
    private final StringProperty category;
    private final DoubleProperty price;

    public Item(Integer code, String name, String category, Double price) {
        this.code = new SimpleIntegerProperty(code);
        this.name = new SimpleStringProperty(name);
        this.category = new SimpleStringProperty(category);
        this.price =new SimpleDoubleProperty(price);

    }

    public Item() {
        code = new SimpleIntegerProperty(8);
        name = new SimpleStringProperty("a");
        category = new SimpleStringProperty("a");
        price = new SimpleDoubleProperty(9.1);
    }

    public int getCode() {
        return code.get();
    }

    public IntegerProperty codeProperty() {
        return code;
    }

    public void setCode(int code) {
        this.code.set(code);
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

    public String getCategory() {
        return category.get();
    }

    public StringProperty categoryProperty() {
        return category;
    }

    public void setCategory(String category) {
        this.category.set(category);
    }

    public double getPrice() {
        return price.get();
    }

    public DoubleProperty priceProperty() {
        return price;
    }

    public void setPrice(double price) {
        this.price.set(price);
    }

}
