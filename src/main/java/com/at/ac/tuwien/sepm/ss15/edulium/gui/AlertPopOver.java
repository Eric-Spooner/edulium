package com.at.ac.tuwien.sepm.ss15.edulium.gui;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.Order;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ValidationException;
import com.at.ac.tuwien.sepm.ss15.edulium.service.ServiceException;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.controlsfx.control.PopOver;

import java.util.function.Consumer;

/**
 * Created by phili on 6/18/15.
 */
public class AlertPopOver extends org.controlsfx.control.PopOver {

    private Button okButton = new Button();
    private Button cancelButton = new Button();
    private Label label = new Label();

    public AlertPopOver() {
        initLayout();
    }

    private void initLayout() {
        HBox buttonLayout = new HBox();
        buttonLayout.setAlignment(Pos.TOP_CENTER);
        buttonLayout.setSpacing(5);
        buttonLayout.getChildren().setAll(okButton, cancelButton);

        VBox layout = new VBox();
        layout.setSpacing(5);
        layout.setStyle("-fx-font-size: 18px; -fx-padding: 10px;");
        layout.getChildren().setAll(label, buttonLayout);

        setHideOnEscape(true);
        setAutoHide(true);
        setDetachable(false);
        setArrowLocation(PopOver.ArrowLocation.BOTTOM_LEFT);

        setContentNode(layout);
    }

    public Button getCancelButton() {
        return cancelButton;
    }

    public Button getOkButton() {
        return okButton;
    }

    public Label getLabel() {
        return label;
    }
}
