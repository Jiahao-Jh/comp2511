package unsw.gloriaromanus;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import com.esri.arcgisruntime.internal.io.handler.request.ServerContextConcurrentHashMap.HashMapChangedEvent.Action;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class SaveMenuController extends MenuController {

    @FXML
    private TextField savegame;

    private static String name = "save_menu.fxml";

    @FXML
    public void clickedSaveButton(ActionEvent e) throws ClassNotFoundException, IOException {
        getParent().clickedSaveButton(e, savegame.getText());
        Back(e);
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
