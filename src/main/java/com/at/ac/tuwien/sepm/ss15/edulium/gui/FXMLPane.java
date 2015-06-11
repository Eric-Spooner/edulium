package com.at.ac.tuwien.sepm.ss15.edulium.gui;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import org.springframework.context.ApplicationContext;

import java.io.IOException;

public class FXMLPane extends Pane {
    private Object controller = null;

    public FXMLPane(String fxml) {
        ApplicationContext context = EduliumApplicationContext.getContext();

        FXMLLoader loader = new FXMLLoader();
        loader.setControllerFactory(aClass -> context.getBean(aClass));

        try {
            Pane pane = loader.load(context.getClassLoader().getResourceAsStream(fxml));
            this.controller = loader.getController();
            this.getChildren().setAll(pane);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param aClass Controller class type
     * @return Controller of this scene as object of class type aClass
     */
    public <T> T getController(Class<T> aClass) {
        return (T)controller;
    }
}