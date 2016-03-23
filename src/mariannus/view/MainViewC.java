package mariannus.view;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import mariannus.Main;
import mariannus.model.Order;

import java.io.IOException;
import java.util.ArrayList;

import static mariannus.model.ObjectsStorage.getInstance;

/**
 * Created by Tim on 19.2.2016.
 */
public class MainViewC {
    @FXML private Label tab1;
    @FXML private Label tab2;
    @FXML private Label tab3;
    @FXML private Label tab4;
    @FXML private Label tab5;
    @FXML private Label tab6;
    @FXML private Label tab7;
    @FXML private Label tab8;
    @FXML private Label tab9;
    @FXML private Label tab10;

    @FXML
    void initialize (){
        tab1.addEventHandler(MouseEvent.MOUSE_CLICKED,new HandleClickTable());
        tab2.addEventHandler(MouseEvent.MOUSE_CLICKED,new HandleClickTable());
        tab3.addEventHandler(MouseEvent.MOUSE_CLICKED,new HandleClickTable());
        tab4.addEventHandler(MouseEvent.MOUSE_CLICKED,new HandleClickTable());
        tab5.addEventHandler(MouseEvent.MOUSE_CLICKED,new HandleClickTable());
        tab6.addEventHandler(MouseEvent.MOUSE_CLICKED,new HandleClickTable());
        tab7.addEventHandler(MouseEvent.MOUSE_CLICKED,new HandleClickTable());
        tab8.addEventHandler(MouseEvent.MOUSE_CLICKED,new HandleClickTable());
        tab9.addEventHandler(MouseEvent.MOUSE_CLICKED,new HandleClickTable());
        tab10.addEventHandler(MouseEvent.MOUSE_CLICKED,new HandleClickTable());
    }


    private class HandleClickTable implements EventHandler<Event>{
        @Override
        public void handle(Event event) {
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("view/OrderView.fxml"));
            AnchorPane pane = null;
            try {
                pane = loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
            stage.setScene(new Scene(pane));
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(tab1.getScene().getWindow());
            Label label = (Label) event.getSource();
            int tabId = Integer.parseInt(label.getId());
            getInstance().setTabIndex(tabId);
            OrderViewC orderViewC = loader.getController();
            orderViewC.setStage(stage);

            if (getInstance().getActiveOrders()[getInstance().getTabIndex()] == null) {
                getInstance().getActiveOrders()[getInstance().getTabIndex()] = new Order("test");
                getInstance().getListPayed()[getInstance().getTabIndex()] = new ArrayList();
            }
            else {

                orderViewC.loadOrderedItems();
            }
            stage.showAndWait();
        }
    }
}
