package com.game.snake;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class MenuController {
    @FXML
    private Label welcomeText;

//    Stage menuStage;
//    Stage primaryStage;
    GameApplication gameApp;

//    public void setStage(Stage stage){
//        this.primaryStage = stage;
//    }

    public void setApp(GameApplication app){
        this.gameApp = app;
    }

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }

    @FXML
    protected void onStartGameButtonClick() throws IOException {
        gameApp.setGameScene();
        gameApp.startGame();
    }

    @FXML
    protected void onViewResultsButtonClick() {
        welcomeText.setText("View Results");
    }

    @FXML
    protected void onExitButtonClick() {
        welcomeText.setText("Exit Game");
    }



}