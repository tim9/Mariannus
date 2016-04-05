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
import mariannus.util.ItemListWraper;
import mariannus.util.OrderListWraper;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
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
    private Label medzisucet;
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
    private double medzi =0, allSum =0;

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
        if (isAllPaied()){
            getInstance().getActiveOrders()[getInstance().getTabIndex()].setPrice(allSum);
            saveOrderToXML();
            releseSlot();
        }
        stage.close();
    }

    //  funkcia nacita a zobrazi polozky, ktore si ludia objednali pri stole.
    void loadOrderedItems() {
        ObservableList<Item> selectedData = FXCollections.observableArrayList();
        Order order = getInstance().getActiveOrders()[getInstance().getTabIndex()];
        for (Item item : getInstance().getItems()) {
            order.getOrdered().stream().filter(code -> code.equals(item.getCode())).forEach(code -> selectedData.add(item));
        }
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
                        checkBox.setOnAction(event -> {
                            if (checkBox.isSelected()) { //ten if osetruje aby sa po zakliknut odkliknuti priratavala odratavala cena
                                subSum(selectedData.get(this.getIndex()));
                                getInstance().getListPayed()[getInstance().getTabIndex()].set(this.getIndex(), true);
                            }
                            else {
                                addSum(selectedData.get(this.getIndex()));
                                getInstance().getListPayed()[getInstance().getTabIndex()].set(this.getIndex(), false);
                            }
                        });
                    }
//                    setStyle("-fx-background-color: red");
                    else if (getInstance().getListPayed()[getInstance().getTabIndex()].get(this.getIndex()).equals(true)) {
                        checkBox.setSelected(true);
                        checkBox.setDisable(true);
                    }
//                    System.out.println(getInstance().getListPayed()[getInstance().getTabIndex()]);
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
        if (sum <= 0.5) for (Item item : orderedItems.getItems())
            sum += item.getPrice();
        allSum = sum;
        suma.setText(String.valueOf(Math.floor(sum * 100) / 100));
        getInstance().getActiveOrders()[getInstance().getTabIndex()].setPrice(sum);
    }

    //    funkcia odrata zakliknutu sumu od celkovej sumy a aktualizuje vypis a zaroven zobrazi medzisucet
    private void subSum(Item item) {
        double sum = getInstance().getActiveOrders()[getInstance().getTabIndex()].getPrice();
        sum -= item.getPrice();
        medzi+= item.getPrice();
        getInstance().getActiveOrders()[getInstance().getTabIndex()].setPrice(sum);
        suma.setText(String.valueOf(Math.floor(sum * 100) / 100));
        medzisucet.setText(String.valueOf(Math.floor(medzi * 100) / 100));
    }

    private void addSum(Item item) {
        double sum = getInstance().getActiveOrders()[getInstance().getTabIndex()].getPrice();
        sum += item.getPrice();
        medzi-= item.getPrice();
        getInstance().getActiveOrders()[getInstance().getTabIndex()].setPrice(sum);
        suma.setText(String.valueOf(Math.floor(sum * 100) / 100));
        medzisucet.setText(String.valueOf(Math.floor(medzi * 100) / 100));
        medzisucet.setText(String.valueOf(Math.floor(medzi * 100) / 100));
    }

    void setStage(Stage stage) {
        this.stage = stage;
    }

    private void saveOrderToXML(){
        try {
            File file = new File("data/Order.xml");
            FileWriter fw = new FileWriter(file,true);
            BufferedWriter bw = new BufferedWriter(fw);
            JAXBContext context = JAXBContext.newInstance(Order.class);
            Marshaller m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            m.marshal(getInstance().getActiveOrders()[getInstance().getTabIndex()], bw);
            bw.write("",0,10);
            bw.close();


        } catch (Exception e) { // catches ANY exception
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Could not save data to file:\n");
            alert.showAndWait();
        }
    }
}
