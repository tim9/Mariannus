package mariannus.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import mariannus.model.Category;
import mariannus.model.Item;
import mariannus.model.ObjectsStorage;

import java.util.stream.Collectors;

/**
 * Created by Tim on 13.2.2016.
 */
public class ItemEditC {
    @FXML private TextField code;
    @FXML private TextField name;
    @FXML private ComboBox<String> category;
    @FXML private TextField price;

    private Stage stage;
    private int index;
    private ObservableList<String> sCategories = FXCollections.observableArrayList();
    private boolean clik=false,valid = false;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public boolean isValid() {
        return valid;
    }

    public void setClik(boolean clik) {
        this.clik = clik;
    }

    public ComboBox<String> getCategory() {
        return category;
    }

    public void setCategory(ComboBox<String> category) {
        this.category = category;
    }

    @FXML private void initialize(){
        sCategories.addAll(ObjectsStorage.getInstance().getCategoryList().stream().map(Category::getCategory).collect(Collectors.toList()));
        category.setItems(sCategories);
    }
    @FXML private void handleOk() {
    if (isInputValid()) {
        if (clik) {
            Item item = new Item(1, "", "", 1.0);
            item.setCode(Integer.parseInt(code.getText()));
            item.setName(name.getText());
            item.setCategory(category.getSelectionModel().getSelectedItem());
            item.setPrice(Double.parseDouble(price.getText()));
            ObjectsStorage.getInstance().getItems().set(index, item);
        } else {
            valid = true;
            Item item = new Item(Integer.parseInt(code.getText()), name.getText(), category.getSelectionModel().getSelectedItem(), Double.parseDouble(price.getText()));
            ObjectsStorage.getInstance().getItems().add(item);
        }
        valid = true;
    }
    stage.close();
}

    @FXML private void handleCancel(){
        stage.close();
    }

    @FXML public void setDialog(Item item, int index){
        code.setText(String.valueOf(item.getCode()));
        name.setText(item.getName());
        price.setText(String.valueOf(item.getPrice()));
        this.index = index;
    }

    public boolean isInputValid() {
        String errorMessage = "";

        if (code.getText() == null || code.getText().length() == 0) {
            errorMessage += "do pola zadaj cislo\n";
        }else {
            try {
                Integer.parseInt(code.getText());
            } catch (NumberFormatException e) {
                errorMessage += "kod musi byt cislo\n";
            }
        }

        if (name.getText() == null || name.getText().length() == 0) {
            errorMessage += "nazov nie je zadane\n";
        }

        if (price.getText() == null || price.getText().length() == 0) {
            errorMessage += "cena nie je zadana!\n";
        }else {
            try {
                Double.parseDouble(price.getText());
            } catch (NumberFormatException e) {
                errorMessage += "cena musi byt cislo\n";
            }
        }
            if (errorMessage.length() == 0)
            return true;
            else {
                // Show the error message.
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.initOwner(stage);
                alert.setTitle("Invalid Fields");
                alert.setHeaderText("Please correct invalid fields");
                alert.setContentText(errorMessage);
                alert.showAndWait();
            return false;
        }
    }
}
