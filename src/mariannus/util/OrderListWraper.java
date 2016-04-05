package mariannus.util;

import mariannus.model.Order;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tim on 30.3.2016.
 */
@XmlRootElement(name = "Orders")
public class OrderListWraper {
    private List<Order> orders = new ArrayList<>();

    @XmlElement(name = "order")
    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }
}
