package com.game.snake;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class GameController {
    @FXML
    private Label welcomeText;

    @FXML
    private GridPane gameField;

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
    public GridPane getGameField() {
        return gameField;
    }

}