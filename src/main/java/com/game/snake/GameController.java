package com.game.snake;

import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class GameController {
    @FXML
    private Label welcomeText;

    @FXML
    private GridPane gameField;
    @FXML
    private Group root;

    @FXML
    private Label timer;

    @FXML
    private Label score;

    @FXML
    private Label end;


    GameApplication gameApp;


    public void setApp(GameApplication app){
        this.gameApp = app;
    }

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }

//    @FXML
//    protected void onStartGameButtonClick() {
//        gameApp.setGameScene();
//        gameApp.runGame();
//    }

    @FXML
    public Group getRoot() {
//        return gameField;
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

    @FXML
    public Label getEnd() {
        return end;
    }



}