package com.at.ac.tuwien.sepm.ss15.edulium.gui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.util.Pair;
import org.springframework.context.ApplicationContext;

import java.io.IOException;
import java.util.function.Consumer;

public class FXMLScene extends Scene {
    private Object controller = null;

    public FXMLScene(String fxml) {
        this(loadFXML(fxml));
    }

    private FXMLScene(Pair<Pane, Object> paneControllerPair) {
        super(paneControllerPair.getKey());

        this.controller = paneControllerPair.getValue();
    }

    /**
     * @param aClass Controller class type
     * @return Controller of this scene as object of class type aClass
     */
    public <T> T getController(Class<T> aClass) {
        return (T)controller;
    }

    private static Pair<Pane, Object> loadFXML(String fxml) {
        ApplicationContext context = EduliumApplicationContext.getContext();

        FXMLLoader loader = new FXMLLoader();
        loader.setControllerFactory(aClass -> context.getBean(aClass));

        try {
            Pane pane = loader.load(context.getClassLoader().getResourceAsStream(fxml));
            Object controller = loader.getController();
            return new Pair<>(pane, controller);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}