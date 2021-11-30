package unsw.gloriaromanus;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.esri.arcgisruntime.internal.io.handler.request.ServerContextConcurrentHashMap.HashMapChangedEvent.Action;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Orientation;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

public class SoldierMenuController extends MenuController {

    // https://stackoverflow.com/a/30171444
    @FXML
    private URL location; // has to be called location

    @FXML
    private VBox vbox;

    @FXML
    private TextArea MovingTroops;

    @FXML
    private ComboBox<String> destination;

    private List<Province> provinces = new ArrayList<>();
    private List<Soldier> troops = new ArrayList<>();
    private static String name = "soldier_menu.fxml";

    public void showSoldiers(List<Soldier> soldiers) throws FileNotFoundException {
        troops.clear();
        vbox.getChildren().clear();
        destination.getItems().clear();
        for (Soldier s : soldiers) {
            Image image = new Image((new File(s.getImageFileName())).toURI().toString(), 40, 40, false, false);
            ImageView imageView = new ImageView(image);

            TextField t = new TextField();
            t.setText(s.toString());
            vbox.getChildren().add(imageView);
            vbox.getChildren().add(t);
            
            Button moveButton = new Button();
            moveButton.setOnAction(e -> {
                moveToTroops(e,s);
                
            });
            moveButton.setText("add" +" "+ s.getType() + " " + "to moving troop" );
            // Button disbandButton = new Button();
            // disbandButton.setOnAction(e -> {
            //     disband(e);
            // });
            // disbandButton.setText("disband" + s.getType());
            // vbox.getChildren().add(disbandButton);
            vbox.getChildren().add(moveButton);
            
        }
        provinces = getParent().getProvinces();

        for (Province province : provinces) {
            destination.getItems().add(province.getName());
        }
        showTroops();
    }

    // @FXML
    // public void disband(ActionEvent e) {

    // }

    @FXML
    public void moveToTroops(ActionEvent e, Soldier soldier) {
        if (!troops.contains(soldier)){
            troops.add(soldier);
        }

        showTroops();
    }

    public void showTroops() {
        MovingTroops.clear();
        for (Soldier soldier : troops) {
            MovingTroops.appendText(soldier.getType() + "\n");  
        }

    }

    @FXML
    public void move(ActionEvent e) throws JsonParseException, JsonMappingException, IOException {
        Province tmp = null;
        for (Province province : provinces) {
            if (province.getName().equals(destination.getValue())){
                tmp = province;
            }
        }
        

        for (Soldier soldier : troops) {
            soldier.move(tmp);
        }
        getParent().updateTroopNum();
        showSoldiers(troops);
    }

    @FXML
    public void Back(ActionEvent e) {
        getParent().switchMenu(BasicMenuController.getName());
    }

    @FXML
    public void EndTurn(ActionEvent e) throws JsonParseException, JsonMappingException, IOException {
        getParent().EndTurn(e);
    }


    public static String getName() {
        return name;
    }
}
