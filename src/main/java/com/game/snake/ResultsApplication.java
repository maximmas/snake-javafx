package com.game.snake;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Stage;

import java.io.IOException;

public class ResultsApplication extends GameApplication {

    public String playerName;
    public int playerId;

    public Stage stage;

    public void setResultsScene(Stage stage) throws IOException {

        this.stage = stage;

        FXMLLoader loader = new FXMLLoader(GameApplication.class.getResource("gameResults.fxml"));
        loader.load();
        ResultsController resultsController = loader.getController();

        resultsController.setApp(this);
        Group root = resultsController.getRoot();

        timerElement = resultsController.getTimerElement();
        scoreElement = resultsController.getScoreElement();

        Scene scene = new Scene(root, stageWidthPx, stageHeightPx);
        scene.getStylesheets().add(getClass().getResource("results.css").toExternalForm());
        stage.setScene(scene);
        showGameResults();
    }

    private void showGameResults(){
        timerElement.setText(String.valueOf(timer));
        scoreElement.setText(String.valueOf(gameScore));
    }

    public void  saveResults(String name){
        System.out.println("name " + name);
    }

    public void goBack() throws IOException {
        setMenuScene(stage);
    }


}

