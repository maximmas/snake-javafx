package com.game.snake;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.io.IOException;

public class GameApplication extends Application {


    int totalRowsNumber = 10;
    int totalColsNumber = 10;
    int fieldGrid = 10; // = rows and cols

    int cellSizePx = 40; //px
    int fieldPaddingPx = 15; //px

    int snakeBodySize = 3;
    int initialSnakePosXIndex = 4;
    int initialSnakePosYIndex = 5;

    int snakeBodyXIndex[]=new int[fieldGrid];
    int snakeBodyYIndex[]=new int[fieldGrid];

    int fruitPosXIndex[]=new int[fieldGrid];
    int fruitPosYIndex[]=new int[fieldGrid];

    int maxPossibleFruits = totalColsNumber * totalRowsNumber;
    int totalFruits = 0;

    int fruitsGenerationSpeed = 1000;
    int snakeMovementSpeed = 1000;
    int speed = 1000;
    Thread fruitsProcess;
    Thread snakeProcess;

    boolean isAlive = true;

    String snakeMovementDirection = "right";

    GridPane field;



    @Override
    public void start(Stage stage) throws IOException {

//        FXMLLoader fxmlLoader = new FXMLLoader(GameApplication.class.getResource("game.fxml"));
//        Scene scene = new Scene(fxmlLoader.load(), 400  , 400);

        int fieldWidthPx = (totalColsNumber * cellSizePx) + (fieldPaddingPx * 2);
        int fieldHeightPx = (totalRowsNumber * cellSizePx) + (fieldPaddingPx * 2);

        /***************************************/
        field = new GridPane();
        field.setPadding(new Insets(fieldPaddingPx, fieldPaddingPx, fieldPaddingPx, fieldPaddingPx));
        field.getStyleClass().addAll("container");

        for (int i = 0; i < totalColsNumber; i++) {
            ColumnConstraints column = new ColumnConstraints(cellSizePx);
            field.getColumnConstraints().add(column);
        }
        for (int i = 0; i < totalRowsNumber; i++) {
            RowConstraints row = new RowConstraints(cellSizePx);
            field.getRowConstraints().add(row);
        }

        for (int i = 0; i < totalColsNumber; i++) {
           for (int j = 0; j < totalRowsNumber; j++) {
                Pane cell = new Pane();
                cell.getStyleClass().add("cell");
                if (i == 0) {
                    cell.getStyleClass().add("first-column");
                }
                if (j == 0) {
                    cell.getStyleClass().add("first-row");
                }
                field.add(cell, i, j);
            }
        }


        Scene scene = new Scene(field, fieldWidthPx, fieldHeightPx);

        setKeysHandler(scene);

        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());

        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();

         runGame();

    }

    private void runGame(){

        setInitialSnakePosition();
        showSnake();
        showFruits();

        fruitsProcess = new Thread(new Runnable() {
            @Override
            public void run() {

                while(isAlive){
//                        checkFood();
                        showFruits();

                    try{
                        Thread.sleep(fruitsGenerationSpeed);
                    } catch(Exception e){};
                }
            }
        });
        fruitsProcess.start();


        snakeProcess = new Thread(new Runnable() {

            @Override
            public void run() {
//                int test = 0;
                while(isAlive){
//                    System.out.println(test);
                     snakeMakeStep();
//                    test++;
//                    if(test > 20) isAlive = false;

                    try{
                        Thread.sleep(snakeMovementSpeed);
                    } catch(Exception e){};
                }
                    System.out.println("End game !");
            }
        });
        snakeProcess.start();

    }


    private void setKeysHandler(Scene scene){

        scene.setOnKeyPressed(e -> {
                        KeyCode key=e.getCode();
                        System.out.println("key: " + key);

                        if(key.equals(KeyCode.UP)) {

                            // змея не может поменять напраление движения на противоположное
                            if(snakeMovementDirection != "down" ) {
                                snakeMovementDirection = "up";
                            }
                        };
                        if(key.equals(KeyCode.DOWN)) {
                            if(snakeMovementDirection != "up" ) {
                                snakeMovementDirection = "down";
                            }
                        };
                        if(key.equals(KeyCode.LEFT)) {
                            if(snakeMovementDirection != "right" ) {
                                snakeMovementDirection = "left";
                            }

                        };
                        if(key.equals(KeyCode.RIGHT)) {
                            if(snakeMovementDirection != "left" ) {
                                snakeMovementDirection = "right";
                            }
                        };

                        if(key.equals(KeyCode.Q)) {
                            isAlive = false;
                            System.out.println(snakeMovementDirection);
                        };
                    });

    }

    private void snakeMakeStep(){

        hideSnake();

        // сдвигаем ячейки от головы к хвосту
        // проходим все ячейки от хвоста к голове, кроме головы
        // зампсываем в текущую значение следующей
        for(int  j = snakeBodySize-1; j > 0 ; j-- ){
            snakeBodyXIndex[j] = snakeBodyXIndex[j-1];
            snakeBodyYIndex[j] = snakeBodyYIndex[j-1];
        }

        // меняем координаты головы
        if(snakeMovementDirection == "up"){
            snakeBodyYIndex[0] = snakeBodyYIndex[0] -1;
        }
        if(snakeMovementDirection == "down"){
            snakeBodyYIndex[0] = snakeBodyYIndex[0] + 1;
        }
        if(snakeMovementDirection == "right")
        {
            snakeBodyXIndex[0] = snakeBodyXIndex[0] + 1 ;

        };
        if(snakeMovementDirection == "left"){
            snakeBodyXIndex[0] = snakeBodyXIndex[0] -1;

        }
        if (
                (snakeBodyXIndex[0] > totalColsNumber || snakeBodyXIndex[0] < 1) ||
                ( snakeBodyYIndex[0] > totalColsNumber  || snakeBodyYIndex[0] < 1) )
        {
            isAlive = false;
            return;
        } else {
            // рисуем по новым координатам
            showSnake();
        }


    }

    private void hideSnake(){
        for (int i = 0; i<snakeBodyXIndex.length; i++) {
            int x = snakeBodyXIndex[i];
            int y = snakeBodyYIndex[i];
            if(x > 0 && y > 0 ){
                Node cell = getCell(x, y, field);
                cell.getStyleClass().remove("on");
            }
        }
    }

    private void showSnake(){

        for (int i = 0; i<snakeBodyXIndex.length; i++) {
            int x = snakeBodyXIndex[i];
            int y = snakeBodyYIndex[i];

                if( x > 0 &&  y > 0){
                Node cell = getCell(x, y, field);
                cell.getStyleClass().add("on");
            }

        }
    }

    private void showFruits() {

        totalFruits++;

        //        rand = Min + (int)(Math.random() * ((Max - Min) + 1))

        // генерируем новые координаты фрукта
        int x = 1 + (int)(Math.random() * ((totalColsNumber -1) + 1));
        int y = 1 + (int)(Math.random() * ((totalRowsNumber -1) + 1));

        // если фруктов не макс кол-во - добавляем новый фрукт
        if (totalFruits <= maxPossibleFruits - 1){
            for (int i = 0; i < fruitPosXIndex.length; i++) {
                if (fruitPosXIndex[i] == 0) {
                    fruitPosXIndex[i] = x;
                    fruitPosYIndex[i] = y;
                }
            }
        }

       Node cell = getCell(x, y, field);

        int imageID = (int)(Math.random() * 11);

        String fruitClassName  = "fruit" + String.valueOf(imageID) ;

        cell.getStyleClass().add(fruitClassName);

    }



    /**
     *  Получаем элемент ячейки по ее координатам
     *
     * @param rowIndex
     * @param columnIndex
     * @param field
     * @return
     */
    public Node getCell(final int columnIndex, final int rowIndex,  GridPane field) {

        for (Node cell : field.getChildren()) {
            if(
                    field.getRowIndex(cell) == (rowIndex-1) &&
                    field.getColumnIndex(cell) == (columnIndex-1))
            {
                    return  cell;
            }
        }
        return null;
    }


    /**
     *  Устанавливаем начальное положение змеи
     */
    private void setInitialSnakePosition(){
        for (int i = 0;  i < this.snakeBodySize;  i++){
            snakeBodyXIndex[i] = initialSnakePosXIndex - i;
            snakeBodyYIndex[i] = initialSnakePosYIndex;
        }
    }



    public static void main(String[] args) {
        launch();
    }


}