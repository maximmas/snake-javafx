package com.game.snake;

import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

import java.io.IOException;

public class ResultsController {


    @FXML
    private Label timer;

    @FXML
    private Label score;

    @FXML
    private Label validation;

    @FXML
    private Group root;

    @FXML
    private TextField name;

    ResultsApplication app;

    public void setApp(ResultsApplication app){
        this.app = app;
    }

    @FXML
    protected void onSaveButtonClick() {
        if  (! name.getText().isEmpty()) {
            app.saveResults(name.getText());
        } else {
            validation.setText("Please, insert your name");
        }
    }

    @FXML
    protected void onReturnButtonClick() throws IOException {
            app.goBack();
    }

    @FXML
    public Group getRoot() {
        return root;
    }


    @FXML
    public Label getTimerElement() {
        return timer;
    }

    @FXML
    public Label getScoreElement() {
        return score;
    }


}