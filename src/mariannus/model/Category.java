package mariannus.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Created by Tim on 12.2.2016.
 */
public class Category {
    private final StringProperty category;

    public Category(String category) {
        this.category = new SimpleStringProperty(category);
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
}
