package com.game.snake;

import javafx.fxml.FXML;

import java.io.IOException;

public class MenuController {

    GameApplication app;

    public void setApp(GameApplication app){
        this.app = app;
    }

    @FXML
    protected void onStartGameButtonClick() throws IOException {
        app.setGameScene();
        app.startGame();
    }


    @FXML
    protected void onViewResultsButtonClick() {
        // TODO доделать
    }

    @FXML
    protected void onExitButtonClick() {
        // TODO доделать
    }

}