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
import mariannus.model.Category;
import mariannus.model.Item;
import mariannus.util.ItemListWraper;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Optional;
import java.util.stream.Collectors;

import static mariannus.model.ObjectsStorage.getInstance;

public class ItemViewC {
    @FXML
    private TableView<Category> shortTable;
    @FXML
    private TableView<Item> itemTable;
    @FXML
    private TableColumn<Category, String> category;
    @FXML
    private TableColumn<Item, Integer> code;
    @FXML
    private TableColumn<Item, String> name;
    @FXML
    private TableColumn<Item, Double> price;

    private boolean editclk = false;
    @FXML
    void initialize() {
        category.setCellValueFactory(param ->  param.getValue().categoryProperty());
        shortTable.setItems(getInstance().getCategoryList());
        shortTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> showIDetails(newValue));
        code.setCellValueFactory(param -> param.getValue().codeProperty().asObject());
        name.setCellValueFactory(param -> param.getValue().nameProperty());
        price.setCellValueFactory(param -> param.getValue().priceProperty().asObject());
        itemTable.setItems(getInstance().getItems());
    }

    private void showIDetails(Category category) {
        ObservableList<Item> selectedData = FXCollections.observableArrayList();
        selectedData.addAll(getInstance().getItems().stream().filter(it -> it.getCategory().equals(category.getCategory())).collect(Collectors.toList()));
        code.setCellValueFactory(param -> param.getValue().codeProperty().asObject());
        name.setCellValueFactory(param -> param.getValue().nameProperty());
        price.setCellValueFactory(param -> param.getValue().priceProperty().asObject());
        itemTable.setItems(selectedData);
    }

    @FXML
    void deleteCategory() {
        int index = shortTable.getSelectionModel().getSelectedIndex();
        if (index >= 0) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("vymazanie kategorie");
            alert.setHeaderText("Potvrdenie vymazania");
            alert.setContentText("Pozor vymazanim danej kategorie sa vymazu vsetky polozky ktore do nej patria\nChcete pokracovat");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                getInstance().getItems().removeIf(item -> item.getCategory().equals(shortTable.getSelectionModel().getSelectedItem().getCategory()));
                getInstance().getCategoryList().remove(index);
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No selection");
            alert.setHeaderText("Nezvolena kategoria");
            alert.setContentText("Prosim zvol polozku, ktoru chces vymazat");
            alert.showAndWait();
        }
    }

    @FXML
    void deleteItem() {
        int index = itemTable.getSelectionModel().getSelectedIndex();
        if (index >= 0) {
            getInstance().getItems().remove(itemTable.getItems().get(index));
            showIDetails(shortTable.getSelectionModel().getSelectedItem());
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No selection");
            alert.setHeaderText("Nezvolena polozka");
            alert.setContentText("Prosim zvol polozku, ktoru chces vymazat");
            alert.showAndWait();
        }
    }

    @FXML
    void newCategory() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("pridanie kategorie");
        dialog.setHeaderText(null);
        dialog.setContentText("zadaj novu kategoriu");
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(name -> getInstance().getCategoryList().add(new Category(name)));
    }

    @FXML
    void newItem() throws IOException {
        editclk = false;
        showEdit();
    }
    @FXML
    void editItem() throws IOException {
        editclk = true;
        showEdit();
    }
    @FXML void showAll(){
        code.setCellValueFactory(param -> param.getValue().codeProperty().asObject());
        name.setCellValueFactory(param -> param.getValue().nameProperty());
        price.setCellValueFactory(param -> param.getValue().priceProperty().asObject());
        itemTable.setItems(getInstance().getItems());
    }
    @FXML void saveAll(){
        try {
            FileWriter fileWriter = new FileWriter("data/category.txt"); //ak je true tak je to append
//
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            for (Category category: getInstance().getCategoryList()){
                bufferedWriter.write(category.getCategory());
                bufferedWriter.newLine();
            }
            bufferedWriter.close();
        }
        catch(IOException ex) {
             ex.printStackTrace();
        }
        saveItemToXML();
    }

    private void showEdit() throws IOException {
        int index = shortTable.getSelectionModel().getSelectedIndex();
        if (index>0) {
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("view/ItemEdit.fxml"));
            AnchorPane pane = loader.load();
            stage.setScene(new Scene(pane));
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(itemTable.getScene().getWindow());
            ItemEditC itemEditC = loader.getController();
            itemEditC.setStage(stage);
            itemEditC.getCategory().getSelectionModel().select(shortTable.getSelectionModel().getSelectedItem().getCategory());
            if (editclk) {
                Item item = itemTable.getSelectionModel().getSelectedItem();
                if (item != null) {
                    itemEditC.setClik(editclk);
                    itemEditC.setDialog(item, getInstance().getItems().indexOf(item));
                } else editclk = false;
            }
            itemEditC.setClik(editclk);
            stage.showAndWait();
            if (itemEditC.isValid())
                showIDetails(shortTable.getSelectionModel().getSelectedItem());
            itemEditC.setValid(false);
        }else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No selection");
            alert.setHeaderText("Nezvolena kategoria");
            alert.setContentText("Ak chces pridavat alebo upravovat polozku, musis zvolit kategoriu z vedlajsej tabulky");
            alert.showAndWait();
        }
    }

    private void saveItemToXML(){
        try {
            JAXBContext context = JAXBContext.newInstance(ItemListWraper.class);
            Marshaller m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            ItemListWraper wrapper = new ItemListWraper();
            wrapper.setItems(getInstance().getItems());

            m.marshal(wrapper, new File("data/items.xml"));

        } catch (Exception e) { // catches ANY exception
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Could not save data");
            alert.setContentText("Could not save data to file:\n");
            alert.showAndWait();
        }
    }
}
