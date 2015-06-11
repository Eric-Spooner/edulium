package com.at.ac.tuwien.sepm.ss15.edulium.gui;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import org.springframework.context.ApplicationContext;

import java.io.IOException;

public class FXMLPane extends Pane {
    private Controller controller = null;

    public FXMLPane(String fxml) {
        ApplicationContext context = EduliumApplicationContext.getContext();

        FXMLLoader loader = new FXMLLoader();
        loader.setControllerFactory(aClass -> context.getBean(aClass));

        try {
            Pane pane = loader.load(context.getClassLoader().getResourceAsStream(fxml));
            controller = loader.getController();
            getChildren().setAll(pane);

            parentProperty().addListener(p -> disableController());
            visibleProperty().addListener(p -> disableController());
            disabledProperty().addListener(p -> disableController());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param aClass Controller class type
     * @return Controller of this scene as object of class type aClass
     */
    public <T extends Controller> T getController(Class<T> aClass) {
        return (T)controller;
    }

    /**
     * Calls Controller.disable(true/false) when the pane is/isn't visible or enabled/disabled, so that the
     * controller can e.g. enable/disable polling
     */
    private void disableController() {
        controller.disable(getParent() == null || !isVisible() || isDisabled());
    }
}