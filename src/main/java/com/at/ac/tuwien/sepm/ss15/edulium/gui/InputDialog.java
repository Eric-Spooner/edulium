package com.at.ac.tuwien.sepm.ss15.edulium.gui;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ValidationException;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.Validator;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

public abstract class InputDialog<Domain> extends Dialog<Domain> {

    private InputDialogController<Domain> controller;
    private Validator<Domain> validator;
    private String domainName;

    public InputDialog(String domainName) {
        this.domainName = domainName;
    }

    public void setContent(FXMLPane content) {
        getDialogPane().setContent(content);
    }

    protected void setValidator(Validator<Domain> validator) {
        this.validator = validator;
    }

    public void setController(InputDialogController<Domain> controller) {
        this.controller = controller;

        getDialogPane().getButtonTypes().setAll(ButtonType.OK, ButtonType.CANCEL);

        Node okButton = getDialogPane().lookupButton(ButtonType.OK);
        okButton.addEventFilter(ActionEvent.ACTION, event -> {
            if (validator != null) {
                try {
                    validator.validateForCreate(controller.toDomainObject());
                } catch (ValidationException e) {
                    event.consume();

                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Invalid " + domainName + " data");
                    alert.setHeaderText("Could not validate " + domainName);
                    alert.setContentText(e.getMessage());
                    alert.showAndWait();
                }
            }
        });

        setResultConverter(buttonType -> {
            if (buttonType == ButtonType.OK) {
                return controller.toDomainObject();
            } else {
                return null;
            }
        });
    }
}