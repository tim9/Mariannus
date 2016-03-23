package mariannus.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tim on 11.2.2016.
 */
public class ObjectsStorage {
    private static ObjectsStorage instance;
    private ObservableList<Item> items;
    private ObservableList<Category> categoryList;
    private Order[] activeOrders;
    private static int tabIndex;  //index ktory uklada objednavku do pola na zaklade id stola.
    private ArrayList[] listPayed; // pole kde bude ulozeny stav o zaplateni polozky.

    public ObjectsStorage() {
        items = FXCollections.observableArrayList();
        categoryList = FXCollections.observableArrayList();
        activeOrders = new Order[10];
        listPayed = new ArrayList[10];
    }

    public static ObjectsStorage getInstance() {
        if (instance == null) {
            instance = new ObjectsStorage();
            return instance;
        }
        else return instance;
    }

    public ObservableList<Item> getItems() {
        return items;
    }

    public void setItems(ObservableList<Item> items) {
        this.items = items;
    }

    public ObservableList<Category> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(ObservableList<Category> categoryList) {
        this.categoryList = categoryList;
    }

    public int getTabIndex() {
        return tabIndex;
    }

    public void setTabIndex(int tabIndex) {
        this.tabIndex = tabIndex;
    }

    public Order[] getActiveOrders() {
        return activeOrders;
    }

    public void setActiveOrders(Order[] activeOrders) {
        this.activeOrders = activeOrders;
    }

    public ArrayList[] getListPayed() {
        return listPayed;
    }

    public void setListPayed(ArrayList[] listPayed) {
        this.listPayed = listPayed;
    }
}
