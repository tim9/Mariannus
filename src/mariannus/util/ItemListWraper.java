package mariannus.util;

import mariannus.model.Item;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tim on 16.2.2016.
 */
@XmlRootElement(name = "Items")
public class ItemListWraper {
    private List<Item> items = new ArrayList<>();

    @XmlElement(name = "item")
    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

}
