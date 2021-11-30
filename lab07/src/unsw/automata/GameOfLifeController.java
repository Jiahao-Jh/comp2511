package unsw.automata;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;

/**
 * A JavaFX controller for the Conway's Game of Live Application.
 *
 * @author Robert Clifton-Everest
 *
 */
public class GameOfLifeController {
    private GameOfLife game;
    private Timeline time;


	@FXML
	private GridPane GridPane;

    @FXML
    private Button TickButton;

    @FXML
	private Button PlayButton;


	public GameOfLifeController() {
        game = new GameOfLife();
        time = new Timeline();
	}

    @FXML
	public void initialize() {
        GridPane.setVgap(5); 
        GridPane.setHgap(5);
        for (int i = 0; i < GameOfLife.BOARD_SIZE; i++) {
            for (int j = 0; j < GameOfLife.BOARD_SIZE; j++) {
                CheckBox tmp = new CheckBox();
                GridPane.add(tmp, i, j);
                game.cellProperty(i,j).bindBidirectional(tmp.selectedProperty());
            }
        }
        KeyFrame key = new KeyFrame(Duration.seconds(0.5), e -> {
            tick();
        });
        time.setCycleCount(Timeline.INDEFINITE); 
        time.getKeyFrames().add(key);

    }
    

    @FXML
	void handleTickbutton(ActionEvent event) {
		tick();
	}

	@FXML
	void handlePlaybutton(ActionEvent event) {
        if (PlayButton.getText().equals("Play")){
            time.play();
            PlayButton.setText("Stop");
        } else {
            PlayButton.setText("Play");
            time.stop();
        }


    }
    
    public void tick() {
        game.tick();
    }


}

