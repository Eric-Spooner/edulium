package com.at.ac.tuwien.sepm.ss15.edulium;

import com.at.ac.tuwien.sepm.ss15.edulium.gui.EduliumApplicationContext;
import com.at.ac.tuwien.sepm.ss15.edulium.gui.FXMLScene;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import org.springframework.context.ApplicationContext;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception{
        ApplicationContext context = EduliumApplicationContext.getContext();

        FXMLScene loginScene = context.getBean("loginScene", FXMLScene.class);
        primaryStage.setScene(loginScene);

        primaryStage.setOnCloseRequest(event -> {
            Platform.exit();
            System.exit(0);
        });

        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
