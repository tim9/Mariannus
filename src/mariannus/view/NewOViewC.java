package mariannus.view;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import mariannus.model.Category;
import mariannus.model.Order;

import static mariannus.model.ObjectsStorage.getInstance;

/**
 * Created by Tim on 19.2.2016.
 */
public class NewOViewC {
    @FXML private GridPane categories;
    @FXML private GridPane items;
    private Stage stage;
    private OrderViewC orderViewC;

    @FXML
    void initialize(){
        int i = 0, j = 0;
        for (Category cat: getInstance().getCategoryList()){
            Button button = new Button(cat.getCategory());
            button.setOnAction(event -> showItemsInCat(button.getText()));
            categories.add(button,j,i);
            i++;
            if (i==10){
                i=0;
                j++;
            }
        }
    }
    private void showItemsInCat(String category){ //funkcia vykresli polozky, ktore su v kategorii
        items.getChildren().clear();
        final int[] i = {0};
        final int[] j = { 0 };
        getInstance().getItems().stream().filter(item -> item.getCategory().equals(category)).forEach(item -> {
            Button button = new Button(item.getName());
            button.setId(String.valueOf(item.getCode()));
            button.setOnAction(this::addItem);
            items.add(button, j[0], i[0]);
            i[0]++;
            if (i[0] == 10) {
                i[0] = 0;
                j[0]++;
            }
        });
    }

//    funkcia prida polozku do objednavky (cize co si dany stol objednal) a zaroven ulozi hodnotu nezaplatenia do pola
    private void addItem(Event event){
        Button button = (Button)event.getSource();
        getInstance().getActiveOrders()[getInstance().getTabIndex()].getOrdered().add(Integer.parseInt(button.getId()));
        getInstance().getListPayed()[getInstance().getTabIndex()].add(false);
    }

    @FXML
    private void handleOk(){
        orderViewC.loadOrderedItems();
        stage.close();
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setOrderViewC(OrderViewC orderViewC) {
        this.orderViewC = orderViewC;
    }
}
