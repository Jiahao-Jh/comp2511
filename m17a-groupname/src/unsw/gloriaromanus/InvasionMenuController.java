package unsw.gloriaromanus;

import java.io.IOException;
import java.net.URL;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class InvasionMenuController extends MenuController {
    @FXML
    private TextField invading_province;
    @FXML
    private TextField opponent_province;
    @FXML
    private TextArea output_terminal;

    private static String name = "invasion_menu.fxml";

    // https://stackoverflow.com/a/30171444
    @FXML
    private URL location; // has to be called location

    public void setInvadingProvince(String p) {
        invading_province.setText(p);
    }

    public void setOpponentProvince(String p) {
        opponent_province.setText(p);
    }

    public void appendToTerminal(String message) {
        output_terminal.appendText(message + "\n");
    }

    public void resetTerminal() {
        output_terminal.clear();
    }

    @FXML
    public void clickedInvadeButton(ActionEvent e) throws IOException {
        getParent().clickedInvadeButton(e);
    }

    @FXML
    public void EndTurn(ActionEvent e) throws JsonParseException, JsonMappingException, IOException {
        getParent().EndTurn(e);
    }

    @FXML 
    public void Back(ActionEvent e) {
        getParent().switchMenu(BasicMenuController.getName());
        
    }

    public static String getName() {
        return name;
    }
} 
