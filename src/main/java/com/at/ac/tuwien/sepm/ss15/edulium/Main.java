package com.at.ac.tuwien.sepm.ss15.edulium;

import com.at.ac.tuwien.sepm.ss15.edulium.gui.FXMLPane;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception{
        ApplicationContext context = new ClassPathXmlApplicationContext("spring/Spring-Edulium.xml");

        FXMLPane mainWindowPane = context.getBean("mainWindowPane", FXMLPane.class);
        primaryStage.setScene(new Scene(mainWindowPane));

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
