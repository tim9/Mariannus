package mariannus.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import mariannus.Main;
import mariannus.model.Item;
import mariannus.model.Order;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

import static mariannus.model.ObjectsStorage.getInstance;

/**
 * Created by Tim on 19.2.2016.
 */
public class OrderViewC {
    @FXML
    private Label tableName;
    @FXML
    private Label suma;
    @FXML
    private TableView<Item> orderedItems;
    @FXML
    private TableColumn<Item, Integer> code;
    @FXML
    private TableColumn<Item, String> name;
    @FXML
    private TableColumn<Item, Double> price;
    @FXML
    private TableColumn<ArrayList[], String> paid;

    private Stage stage;

    @FXML
    void initialize() {
    }

    @FXML
    void add() throws IOException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("view/NewOView.fxml"));
        AnchorPane pane = loader.load();
        stage.setScene(new Scene(pane));
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(suma.getScene().getWindow());
        NewOViewC newOViewC = loader.getController();
        newOViewC.setStage(stage);
        newOViewC.setOrderViewC(this);
        stage.showAndWait();
    }

    @FXML
    void delete() {
        int delIndex = orderedItems.getSelectionModel().getSelectedIndex();
        System.out.println(delIndex);
        getInstance().getListPayed()[getInstance().getTabIndex()].remove(delIndex);
        getInstance().getActiveOrders()[getInstance().getTabIndex()].getOrdered().remove(delIndex);
        loadOrderedItems();
    }

    @FXML
    private void handleOk() {
        if (isAllPaied())
            releseSlot();
        stage.close();
    }

    //  funkcia nacita a zobrazi polozky, ktore si ludia objednali pri stole.
    void loadOrderedItems() {
        ObservableList<Item> selectedData = FXCollections.observableArrayList();
        Order order = getInstance().getActiveOrders()[getInstance().getTabIndex()];
        for (Item item : getInstance().getItems()) {
            order.getOrdered().stream().filter(code -> code.equals(item.getCode())).forEach(code -> selectedData.add(item));
        }
//        System.out.println(getInstance().getListPayed()[0]);
//        System.out.println(getInstance().getActiveOrders()[getInstance().getTabIndex()]);
        code.setCellValueFactory(param -> param.getValue().codeProperty().asObject());
        name.setCellValueFactory(param -> param.getValue().nameProperty());
        price.setCellValueFactory(param -> param.getValue().priceProperty().asObject());
        paid.setCellFactory(param -> new TableCell<ArrayList[], String>() { //cellfactory ktory prida do tabulky checkbox a jemu eventhandler.
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                if (item != null || empty) {
                    setText(null);
                    setStyle("");
                } else {
                    CheckBox checkBox = new CheckBox();
//                    System.out.println("index tohto riadku je "+this.getIndex());
//                    System.out.println("index stola je "+getInstance().getTabIndex());
                    if (getInstance().getListPayed()[getInstance().getTabIndex()].get(this.getIndex()).equals(false)) {
                        checkBox.setSelected(false);
                        checkBox.setOnAction(event -> {
                            getInstance().getListPayed()[getInstance().getTabIndex()].set(this.getIndex(), true);
                            if (checkBox.isSelected()) //ten if osetruje aby sa po zakliknut odkliknuti priratavala odratavala cena
                                subSum(selectedData.get(this.getIndex()));
                            else addSum(selectedData.get(this.getIndex()));
                        });
                    }
//                    setStyle("-fx-background-color: red");
                    else if (getInstance().getListPayed()[getInstance().getTabIndex()].get(this.getIndex()).equals(true)) {
                        checkBox.setSelected(true);
                        checkBox.setOnAction(event -> {
                            getInstance().getListPayed()[getInstance().getTabIndex()].set(this.getIndex(), false);
                            if (checkBox.isSelected())
                                subSum(selectedData.get(this.getIndex()));
                            else addSum(selectedData.get(this.getIndex()));
                        });
                    }
                    setGraphic(checkBox);
                }
            }
        });
        orderedItems.setItems(selectedData);
        setSum();
    }

    //funkcia zisti ci bol vsetok tovar v konkretnej objednavke zaplateny
    private boolean isAllPaied() {
        for (Object bol : getInstance().getListPayed()[getInstance().getTabIndex()]) {
            if (bol.equals(false))
                return false;
        }
//        System.out.println("vsetko checked");
        return true;
    }

    //    funkcia uvolni prislusny slot
    private void releseSlot() {
        getInstance().getActiveOrders()[getInstance().getTabIndex()] = null;
        getInstance().getListPayed()[getInstance().getTabIndex()] = null;
    }

    void addTableName(Label tab) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Zadaj názov");
        dialog.setHeaderText(null);
        dialog.setContentText("zadaj meno stola");
        Optional<String> name = dialog.showAndWait();
        name.ifPresent(nm -> {
            getInstance().getActiveOrders()[getInstance().getTabIndex()].setName(nm);
            tab.setText(nm);
            tableName.setText(nm);
        });
    }

    private void setSum() {
        double sum = getInstance().getActiveOrders()[getInstance().getTabIndex()].getPrice();
        if (sum == 0) for (Item item : orderedItems.getItems())
            sum += item.getPrice();
        suma.setText(String.valueOf(Math.floor(sum * 100) / 100));
        getInstance().getActiveOrders()[getInstance().getTabIndex()].setPrice(sum);
    }

    //    funkcia odrata zakliknutu sumu od celkovej sumy a aktualizuje vypis
    void subSum(Item item) {
        double sum = getInstance().getActiveOrders()[getInstance().getTabIndex()].getPrice();
        sum -= item.getPrice();
        getInstance().getActiveOrders()[getInstance().getTabIndex()].setPrice(sum);
        suma.setText(String.valueOf(Math.floor(sum * 100) / 100));
    }

    void addSum(Item item) {
        double sum = getInstance().getActiveOrders()[getInstance().getTabIndex()].getPrice();
        sum += item.getPrice();
        getInstance().getActiveOrders()[getInstance().getTabIndex()].setPrice(sum);
        suma.setText(String.valueOf(Math.floor(sum * 100) / 100));
    }

    void setStage(Stage stage) {
        this.stage = stage;
    }
}
