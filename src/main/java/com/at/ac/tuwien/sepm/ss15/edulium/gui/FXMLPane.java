package com.at.ac.tuwien.sepm.ss15.edulium.gui;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class FXMLPane extends AnchorPane {

    @Autowired
    private ApplicationContext context;

    private Initializable controller = null;

    public void setFXML(String fxml) {
        FXMLLoader loader = new FXMLLoader();
        loader.setControllerFactory(aClass -> context.getBean(aClass));

        try {
            Node node = loader.load(context.getClassLoader().getResourceAsStream(fxml));
            controller = loader.getController();
            getChildren().setAll(node);

            AnchorPane.setTopAnchor(node, 0.0);
            AnchorPane.setBottomAnchor(node, 0.0);
            AnchorPane.setLeftAnchor(node, 0.0);
            AnchorPane.setRightAnchor(node, 0.0);

            setMinSize(USE_COMPUTED_SIZE, USE_COMPUTED_SIZE);
            setMaxSize(USE_COMPUTED_SIZE, USE_COMPUTED_SIZE);
            setPrefSize(USE_COMPUTED_SIZE, USE_COMPUTED_SIZE);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param aClass Controller class type
     * @return Controller of this scene as object of class type aClass
     */
    public <T extends Initializable> T getController(Class<T> aClass) {
        return (T)controller;
    }
}