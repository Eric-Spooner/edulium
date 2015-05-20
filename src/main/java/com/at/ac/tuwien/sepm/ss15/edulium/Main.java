package com.at.ac.tuwien.sepm.ss15.edulium;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.setWidth(300);
        primaryStage.setHeight(300);
        primaryStage.setTitle("Edulium");
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
