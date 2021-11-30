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

public class TaxMenuController extends MenuController {

    // https://stackoverflow.com/a/30171444
    @FXML
    private URL location; // has to be called location

    @FXML
    private TextArea output_terminal;

    private static String name = "tax_menu.fxml";

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
    public void SetLowTax(ActionEvent e) {
        getParent().setTax("low");
    }
    @FXML
    public void SetNormalTax(ActionEvent e) {
        getParent().setTax("normal");
    }
    @FXML
    public void SetHighTax(ActionEvent e) {
        getParent().setTax("high");
    }
    @FXML
    public void SetVeryHighTax(ActionEvent e) {
        getParent().setTax("very high");
    }

    public static String getName() {
        return name;
    }
}
