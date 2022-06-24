package com.game.snake;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.io.IOException;

public class GameApplication extends Application {

    int fieldGrid = 10; // = rows and cols

    int totalRowsNumber = fieldGrid;
    int totalColsNumber = fieldGrid;

    int cellSizePx = 40; //px
    int fieldPaddingPx = 15; //px

    int snakeBodySize = 3;
    int initialSnakePosXIndex = 4;
    int initialSnakePosYIndex = 5;

    int maxPossibleFruits = totalColsNumber * totalRowsNumber - snakeBodySize;
    int fruitPosXIndex[]=new int[maxPossibleFruits];
    int fruitPosYIndex[]=new int[maxPossibleFruits];
    int totalFruits = 0;

    int snakeBodyXIndex[]=new int[totalRowsNumber * totalColsNumber];
    int snakeBodyYIndex[]=new int[totalRowsNumber * totalColsNumber];

    /**
     *  Флаг активности игры
     */
    boolean isAlive = true;

    /**
     *  Результат игры - сколько фруктов съедено
     */
    int gameScore = 0;

    /**
     *  Время игры
     *
     */
    int timer = 200;

    /**
     *  Направление движения змеи
     */
    String snakeMovementDirection = "right";

    GridPane field;

    Thread fruitsProcess;
    Thread snakeProcess;
    Thread timerProcess;

    int timerDelay = 500;  // ms
    int snakeDelay = 1000;
    int fruitsDelay = 5000;

    Stage primaryStage;

    Label displayTimer;
    Label displayScore;
    Label displayEnd;

    int stageWidthPx = (totalColsNumber * cellSizePx) + (fieldPaddingPx * 2) + 200;
    int stageHeightPx = (totalRowsNumber * cellSizePx) + (fieldPaddingPx * 2);

    @Override
    public void start(Stage primaryStage) throws IOException {

        this.primaryStage = primaryStage;

        FXMLLoader menuLoader = new FXMLLoader(GameApplication.class.getResource("menu.fxml"));
        Scene menu = new Scene(menuLoader.load(), stageWidthPx  , stageHeightPx);
        MenuController menuController = menuLoader.getController();
        menuController.setApp(this);
       primaryStage.setScene(menu);
       primaryStage.show();

    }

    public void setGameScene() throws IOException {

        FXMLLoader gameLoader = new FXMLLoader(GameApplication.class.getResource("game.fxml"));
        gameLoader.load();

        GameController gameController = gameLoader.getController();
        gameController.setApp(this);

        Group root = gameController.getRoot();
        field = gameController.getGameField();

        displayTimer = gameController.getTimer();
        displayScore = gameController.getScore();
        displayEnd = gameController.getEnd();

        field.setPadding(new Insets(fieldPaddingPx, fieldPaddingPx , fieldPaddingPx, fieldPaddingPx));
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

        Scene gameScene = new Scene(root, stageWidthPx ,stageHeightPx);

        gameScene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        primaryStage.setScene(gameScene);

        keysPressHandler(gameScene);

    }

    public void runGame(){

        setInitialSnakePosition();
        showSnake();
        showFruits();
        displayScore();

        timerProcess = new Thread(new Runnable() {
            @Override
            public void run() {

                while(timer > 0 && isAlive){
                    timer--;
                    displayTimer();
                    try{
                        Thread.sleep(timerDelay);
                    } catch(Exception e){};
                }
                isAlive = false;
//                System.out.println("Time is out! ");
                displayEnd();
            }
        });
        timerProcess.start();


        fruitsProcess = new Thread(new Runnable() {
            @Override
            public void run() {

                while(isAlive && timer > 0){
                        showFruits();
                    try{
                        Thread.sleep(fruitsDelay);
                    } catch(Exception e){};
                }

            }
        });
        fruitsProcess.start();

        snakeProcess = new Thread(new Runnable() {

            @Override
            public void run() {

                while(isAlive && timer > 0){
                     snakeMakeStep();
                    try{
                        Thread.sleep(snakeDelay);
                    } catch(Exception e){};
                }
//                    System.out.println("End game !");
                displayEnd();
//                displayEnd.setText("Game over!");
            }
        });
        snakeProcess.start();
    }


    private void keysPressHandler(Scene scene){

//        field.requestFocus();

        scene.setOnKeyPressed(e -> {
                        KeyCode key=e.getCode();

                        if(key.equals(KeyCode.UP)) {
                    System.out.println("UP");
                            // змея не может поменять напраление движения на противоположное
                            if(snakeMovementDirection != "down" ) {
                                snakeMovementDirection = "up";
                            }
                        };
                        if(key.equals(KeyCode.DOWN)) {
                            System.out.println("Down");
                            if(snakeMovementDirection != "up" ) {
                                snakeMovementDirection = "down";
                            }
                        };
                        if(key.equals(KeyCode.LEFT)) {
                            System.out.println("Left");
                            if(snakeMovementDirection != "right" ) {
                                snakeMovementDirection = "left";
                            }

                        };
                        if(key.equals(KeyCode.RIGHT)) {
                            System.out.println("Right");
                            if(snakeMovementDirection != "left" ) {
                                snakeMovementDirection = "right";
                            }
                        };

                        if(key.equals(KeyCode.Q)) {
                            isAlive = false;
                        };

                    });
    }


    private void displayTimer(){
        https://stackoverflow.com/questions/21083945/how-to-avoid-not-on-fx-application-thread-currentthread-javafx-application-th
        Platform.runLater(new Runnable(){
            @Override
            public void run() {
                displayTimer.setText(String.valueOf(timer));
            }
        });
    }

    private void displayScore(){
        Platform.runLater(new Runnable(){
            @Override
            public void run() {
                displayScore.setText(String.valueOf(gameScore));
            }
        });
    }

    private void displayEnd(){
        Platform.runLater(new Runnable(){
            @Override
            public void run() {
                displayEnd.setText("Game over !");
            }
        });
    }


    private void snakeMakeStep(){

        hideSnake();

        int nextCellX = 0;
         int  nextCellY = 0;

        // меняем координаты головы в зависимости от направления
        if(snakeMovementDirection == "up"){
            nextCellX = snakeBodyXIndex[0];
            nextCellY = snakeBodyYIndex[0] -1;
        }

        if(snakeMovementDirection == "down"){
            nextCellX = snakeBodyXIndex[0];
            nextCellY = snakeBodyYIndex[0] +1;
        }

        if(snakeMovementDirection == "right"){
            nextCellX = snakeBodyXIndex[0]+1;
            nextCellY = snakeBodyYIndex[0] ;
       }

        if(snakeMovementDirection == "left"){
            nextCellX = snakeBodyXIndex[0]-1;
            nextCellY = snakeBodyYIndex[0] ;
        }

        boolean isFruit = fruitEatHandler(nextCellX, nextCellY);

        if (isFruit) {
            snakeBodySize++;
        }

        // сдвигаем ячейки от головы к хвосту
        // проходим все ячейки от хвоста к голове, кроме головы
        // зампсываем в текущую значение следующей
        // т.е. коорд головы -> в предголову и т.д.
        for(int  j = snakeBodySize-1;  j > 0 ;  j-- ){
            snakeBodyXIndex[j] = snakeBodyXIndex[j-1];
            snakeBodyYIndex[j] = snakeBodyYIndex[j-1];
        }

        // меняем коорд головы
        snakeBodyXIndex[0] = nextCellX;
        snakeBodyYIndex[0] = nextCellY;

        if ( isBorder()) {
            isAlive = false;
        } else {
            showSnake();
        }

    }

    /**
     *  Проверка и обработка наезда на фрукт
     *
     * @return
     */
    private boolean fruitEatHandler(int snakeHeadX, int snakeHeadY){
        boolean isFruit = false;


        for (int i = 0; i < fruitPosXIndex.length; i++){

            if (snakeHeadX == fruitPosXIndex[i] && snakeHeadY == fruitPosYIndex[i]){
                System.out.println("is fruit");
                isFruit = true;
                 removeFruit(i);
                 gameScore++;
                displayScore();
            }
        }
        return isFruit;
    }

    /**
     *  Удаление фрукта
     *
     * @param index
     */
    private void removeFruit(int index){
        int fruitX = fruitPosXIndex[index];
        int fruitY = fruitPosYIndex[index];
        Node fruit = getCell(fruitX, fruitY);
        if (fruit != null){
            fruit.getStyleClass().remove("fruit");
        }
         fruitPosXIndex[index] = fruitPosYIndex[index] = 0;
    }


    /**
     *  Проверка выхода за границу поля
     *
     * @return
     */
    private boolean isBorder(){
         return
                 (snakeBodyXIndex[0] > totalColsNumber || snakeBodyXIndex[0] < 1) ||
                ( snakeBodyYIndex[0] > totalColsNumber  || snakeBodyYIndex[0] < 1);
    }


    private void hideSnake(){
        for (int i = 0; i<snakeBodyXIndex.length; i++) {
            int x = snakeBodyXIndex[i];
            int y = snakeBodyYIndex[i];
            if(x > 0 && y > 0 ){
                Node cell = getCell(x, y);
                cell.getStyleClass().remove("on");
            }
        }
    }


    /**
     *  Вывод змеи
     */
    private void showSnake(){

        for (int i = 0; i<snakeBodyXIndex.length; i++) {
            int x = snakeBodyXIndex[i];
            int y = snakeBodyYIndex[i];

                if( x > 0 &&  y > 0){
                Node cell = getCell(x, y);
                cell.getStyleClass().add("on");
            }

        }
    }

    /**
     *  Вывод фруктов
     */
    private void showFruits() {

        int x  = 0;
        int y  = 0;

        // ячейка занята змеей
        boolean isCellSnake = true;

        //        rand = Min + (int)(Math.random() * ((Max - Min) + 1))

        while(isCellSnake){
            // генерируем новые координаты фрукта
            x = 1 + (int)(Math.random() * ((totalColsNumber -1) + 1));
            y = 1 + (int)(Math.random() * ((totalRowsNumber -1) + 1));

            isCellSnake = isSnake(x,y);
        }


        // если фруктов не макс кол-во - добавляем
        //  координаты нового фрукта в массивы координат
        if (totalFruits < maxPossibleFruits){

            for (int i = 0;  i < fruitPosXIndex.length;  i++) {
                if (fruitPosXIndex[i] == 0) {
                    fruitPosXIndex[i] = x;
                    fruitPosYIndex[i] = y;
                    totalFruits++;
                    break;
                }
            }
        }

       Node cell = getCell(x, y);
        int imageID = (int)(Math.random() * 11);
        String fruitClassName  = "fruit" + String.valueOf(imageID) ;
        cell.getStyleClass().addAll( "fruit", fruitClassName);
    }


    /**
     *  Проверка, занята ли ячейка змеей
     * @param x координата X ячейки
     * @param y координата Y ячейки
     * @return true - занята, false - нет
     */
    private boolean isSnake(int x, int y){
        for (int i = 0; i<snakeBodyXIndex.length; i++) {
          if(x == snakeBodyXIndex[i] && y == snakeBodyYIndex[i]){
              return true;
          }
        }
        return false;
    }

    /**
     *  Получаем элемент ячейки по ее координатам
     *
     * @param rowIndex
     * @param columnIndex
     * @return
     */
    public Node getCell(final int columnIndex, final int rowIndex) {

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