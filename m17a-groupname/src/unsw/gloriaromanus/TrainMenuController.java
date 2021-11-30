package unsw.gloriaromanus;

import java.io.IOException;
import java.net.URL;

import com.esri.arcgisruntime.internal.io.handler.request.ServerContextConcurrentHashMap.HashMapChangedEvent.Action;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

public class TrainMenuController extends MenuController {
    @FXML
    private URL location;

    @FXML
    private TextArea output_terminal;

    private static String name = "train_menu.fxml";

    public void setMessage(String message) {
        output_terminal.setText(message);
    }

    @FXML
    private void trainArtillery() throws IOException {
        getParent().train("artillery");
    }

    @FXML
    private void trainBerserker() throws IOException {
        getParent().train("berserker");
    }

    @FXML
    private void trainChariot() throws IOException {
        getParent().train("chariot");
    }

    @FXML
    private void trainDruid() throws IOException {
        getParent().train("druid");
    }

    @FXML
    private void trainElephants() throws IOException {
        getParent().train("elephants");
    }

    @FXML
    private void trainHorseArcher() throws IOException {
        getParent().train("horse archer");
    }

    @FXML
    private void trainJavelin() throws IOException {
        getParent().train("javelin skirmisher");
    }

    @FXML
    private void trainMeleeCavalry() throws IOException {
        getParent().train("melee cavalry");
    }

    @FXML
    private void trainMissileInfantry() throws IOException {
        getParent().train("missile infantry");
    }

    @FXML
    private void trainPikemen() throws IOException {
        getParent().train("pikemen");
    }

    @FXML
    private void trainRomanLegionary() throws IOException {
        getParent().train("roman legionary");
    }

    @FXML
    private void trainSpearmen() throws IOException {
        getParent().train("spearmen");
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
    public void Cancel1(ActionEvent e) {
        getParent().cancelTrain(0);
    }

    @FXML
    public void Cancel2(ActionEvent e) {
        getParent().cancelTrain(1);
    }

    public static String getName() {
        return name;
    }
}
