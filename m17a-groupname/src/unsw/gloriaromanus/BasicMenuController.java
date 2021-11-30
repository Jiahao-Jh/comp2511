package unsw.gloriaromanus;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;

import com.esri.arcgisruntime.internal.io.handler.request.ServerContextConcurrentHashMap.HashMapChangedEvent.Action;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class BasicMenuController extends MenuController {

    // https://stackoverflow.com/a/30171444
    @FXML
    private URL location; // has to be called location

    private static String name = "basic_menu.fxml";

    @FXML
    public void clickedInvadeButton(ActionEvent e) throws IOException {
        getParent().clickedInvadeButton(e);
    }

    @FXML
    public void changeToInvadeMenu(ActionEvent e) {
        getParent().switchMenu(InvasionMenuController.getName());
        getParent().resetInvadeMenuOutput();
    }

    @FXML
    public void changeToInfrastructureMenu(ActionEvent e) {
        getParent().switchMenu(InfrastructureMenuController.getName());
        getParent().displayInfrastructureFromBasicMenu();
    }

    @FXML
    public void changeToTaxMenu(ActionEvent e) {
        getParent().switchMenu(TaxMenuController.getName());
        getParent().displayInfoFromBasicMenu();
    }

    @FXML
    public void changeToSoldierMenu(ActionEvent e) throws FileNotFoundException {
        getParent().switchMenu(SoldierMenuController.getName());
        getParent().displaySoldier();
    }

    @FXML 
    public void changeToSaveMenu(ActionEvent e) {
        getParent().switchMenu(SaveMenuController.getName());
    }

    @FXML
    public void changeToTrainMenu(ActionEvent e) {
        getParent().switchMenu(TrainMenuController.getName());
        getParent().displayTrainFromBasicMenu();
    }

    @FXML
    public void EndTurn(ActionEvent e) throws JsonParseException, JsonMappingException, IOException {
        getParent().EndTurn(e);
    }

    public static String getName() {
        return name;
    }
}
