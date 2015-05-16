package com.at.ac.tuwien.sepm.ss15.edulium;

import com.at.ac.tuwien.sepm.ss15.edulium.gui.Controller;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("gui/TablesOverview.fxml"));
        fxmlLoader.setController(new Controller());
        Parent root = (Parent)fxmlLoader.load();
        Controller controller = fxmlLoader.<Controller>getController();
        primaryStage.setTitle("TablesOverview");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
        controller.setupListeners();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
