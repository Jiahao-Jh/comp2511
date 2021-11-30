package unsw.gloriaromanus;

import java.io.IOException;
import java.net.URL;

import com.esri.arcgisruntime.internal.io.handler.request.ServerContextConcurrentHashMap.HashMapChangedEvent.Action;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class BasicMenu2Controller extends MenuController{

    // https://stackoverflow.com/a/30171444
    @FXML
    private URL location; // has to be called location

    private static String name = "basic_menu2.fxml";

    @FXML
    private TextField treasury;

    @FXML
    private TextField currFaction;

    public void setTreasury(int money) {
        treasury.setText(Integer.toString(money));
    }

    public void setFaction(String name) {
        currFaction.setText(name);
    }

    public static String getName() {
        return name;
    }
}
