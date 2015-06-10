package com.at.ac.tuwien.sepm.ss15.edulium.gui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import org.springframework.context.ApplicationContext;

import java.io.IOException;

public class FXMLScene extends Scene {

    public FXMLScene(String fxml) {
        super(loadFXML(fxml));
    }

    private static Pane loadFXML(String fxml) {
        ApplicationContext context = EduliumApplicationContext.getContext();

        FXMLLoader loader = new FXMLLoader();
        loader.setControllerFactory(aClass -> context.getBean(aClass));

        try {
            return loader.load(context.getClassLoader().getResourceAsStream(fxml));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}