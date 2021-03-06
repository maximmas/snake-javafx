package com.game.snake;

import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

public class GameController {

    @FXML
    private GridPane gameField;
    @FXML
    private Group root;

    @FXML
    private Label timer;

    @FXML
    private Label score;


    @FXML
    public Group getRoot() {
        return root;
    }

    @FXML
    public GridPane getGameField() {
        return gameField;
    }

    @FXML
    public Label getTimer() {
        return timer;
    }

    @FXML
    public Label getScore() {
        return score;
    }


}