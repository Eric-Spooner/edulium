package com.at.ac.tuwien.sepm.ss15.edulium.gui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import org.springframework.context.ApplicationContext;

import java.io.IOException;

public class FXMLPane extends AnchorPane {
    private Controller controller = null;

    public FXMLPane(String fxml) {
        ApplicationContext context = EduliumApplicationContext.getContext();

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