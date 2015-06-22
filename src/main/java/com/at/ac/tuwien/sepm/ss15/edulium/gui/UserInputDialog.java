package com.at.ac.tuwien.sepm.ss15.edulium.gui;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.User;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ValidationException;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.Validator;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Optional;

@Component
public class UserInputDialog extends Dialog<User> {

    private UserDialogController userDialogController;

    @Autowired
    private Validator<User> userValidator;

    @Resource(name = "userDialogPane")
    public void setUserDialogPane(FXMLPane userDialogPane) {
        userDialogController = userDialogPane.getController(UserDialogController.class);

        getDialogPane().setContent(userDialogPane);

        getDialogPane().getButtonTypes().setAll(ButtonType.OK, ButtonType.CANCEL);

        Node okButton = getDialogPane().lookupButton(ButtonType.OK);
        okButton.addEventFilter(ActionEvent.ACTION, event -> {
            try {
                userValidator.validateForCreate(userDialogController.toUser());
            } catch (ValidationException e) {
                event.consume();

                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Invalid Employee Data");
                alert.setHeaderText("Could not validate employee");
                alert.setContentText(e.getMessage());
                alert.showAndWait();
            }
        });

        setResultConverter(buttonType -> {
            if (buttonType == ButtonType.OK) {
                return userDialogController.toUser();
            } else {
                return null;
            }
        });
    }

    public Optional<User> showAndWaitForCreate() {
        setTitle("Add Employee");
        setHeaderText("Add a new employee");

        userDialogController.prepareForCreate();

        return super.showAndWait();
    }

    public Optional<User> showAndWaitForUpdate(User user) {
        assert user != null;

        setTitle("Change Employee");
        setHeaderText("Change an existing employee");

        userDialogController.prepareForUpdate(user);

        return super.showAndWait();
    }
}
