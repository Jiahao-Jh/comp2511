package unsw.gloriaromanus;

import java.io.IOException;
import java.net.URL;

import com.esri.arcgisruntime.internal.io.handler.request.ServerContextConcurrentHashMap.HashMapChangedEvent.Action;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class InfrastructureMenuController extends MenuController {

    // https://stackoverflow.com/a/30171444
    @FXML
    private URL location; // has to be called location

    @FXML
    private TextArea output_terminal;

    private static String name = "Infrastructure_menu.fxml";

    public void appendToTerminal(String message) {
        output_terminal.setText(message + "\n");
    }

    @FXML
    public void Back(ActionEvent e) {
        getParent().switchMenu(BasicMenuController.getName());
    }

    @FXML
    public void EndTurn(ActionEvent e) throws JsonParseException, JsonMappingException, IOException {
        getParent().EndTurn(e);
    }

    @FXML
    public void buildFarm(ActionEvent e) {
        getParent().build("farm");
    }

    @FXML 
    public void buildMine(ActionEvent e) {
        getParent().build("mine");
    }

    @FXML 
    public void buildMarket(ActionEvent e) {
        getParent().build("market");
    }

    @FXML 
    public void buildPort(ActionEvent e) {
        getParent().build("port");
    }
    
    @FXML 
    public void upgradeFarm(ActionEvent e) {
        getParent().upgrade("farm");
    }

    @FXML 
    public void upgradeMine(ActionEvent e) {
        getParent().upgrade("mine");
    }

    @FXML 
    public void upgradeMarket(ActionEvent e) {
        getParent().upgrade("market");
    }

    @FXML 
    public void upgradePort(ActionEvent e) {
        getParent().upgrade("port");
    }

    @FXML
    public void buildTrain(ActionEvent e) {
        getParent().build("train");
    }

    @FXML
    public void cancel(ActionEvent e) {
        getParent().cancelInfra();
    }

    public static String getName() {
        return name;
    }
}
