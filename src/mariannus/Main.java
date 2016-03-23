package mariannus;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import mariannus.model.Category;
import mariannus.model.ObjectsStorage;
import mariannus.util.ItemListWraper;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import java.io.*;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("view/MainView.fxml"));
        primaryStage.setTitle("Evidencia Mariannus Pub");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
        loadCategories();
        loadItem();
    }
    public static void main(String[] args) {
        launch(args);
    }

    public void loadCategories() throws IOException {
        File f = new File("data/category.txt");
        if(f.exists()){
            try {
                BufferedReader br = new BufferedReader(new FileReader(f));
                String line;
                while ((line = br.readLine()) != null) {
                    ObjectsStorage.getInstance().getCategoryList().add(new Category(line));
                }
            } catch (IOException ignored){
            }
        }
    }
    void loadItem(){
        File file = new File("data/items.xml");
        try {
            JAXBContext context = JAXBContext.newInstance(ItemListWraper.class);
            Unmarshaller um = context.createUnmarshaller();

            // Reading XML from the file and unmarshalling.
            ItemListWraper wrapper = (ItemListWraper) um.unmarshal(file);

            ObjectsStorage.getInstance().getItems().clear();
            ObjectsStorage.getInstance().getItems().addAll(wrapper.getItems());

        } catch (Exception e) { // catches ANY exception
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Could not load data");
            alert.setContentText("Could not load data from file:\n");

            alert.showAndWait();
//            e.printStackTrace();
        }
    }
}
