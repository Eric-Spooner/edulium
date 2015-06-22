package com.at.ac.tuwien.sepm.ss15.edulium.gui;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.User;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.springframework.stereotype.Controller;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for the User Dialog
 */
@Controller
public class UserDialogController implements Initializable, InputDialogController<User> {

    @FXML
    private TextField textFieldName;
    @FXML
    private TextField textFieldUsername;
    @FXML
    private ChoiceBox<String> dropRole;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        dropRole.getItems().addAll("ROLE_MANAGER", "ROLE_COOK", "ROLE_SERVICE");
        dropRole.getSelectionModel().selectFirst();

        //Used to handle the dropDown with the keyboard
        dropRole.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.UP || event.getCode() == KeyCode.DOWN) {
                event.consume();
            }
        });
    }

    @Override
    public void prepareForCreate() {
        textFieldName.clear();
        textFieldUsername.clear();
        dropRole.getSelectionModel().selectFirst();

        textFieldUsername.setDisable(false);
    }

    @Override
    public void prepareForUpdate(User user) {
        assert user != null;

        textFieldName.setText(user.getName());
        textFieldUsername.setText(user.getIdentity());
        dropRole.getSelectionModel().select(user.getRole());
        dropRole.setValue(user.getRole());

        textFieldUsername.setDisable(true);
    }

    @Override
    public void prepareForSearch() {
        textFieldName.clear();
        textFieldUsername.clear();
        dropRole.getSelectionModel().clearSelection();
        dropRole.setValue("");

        textFieldUsername.setDisable(false);
    }

    @Override
    public User toDomainObject() {
        User user = new User();
        user.setName(textFieldName.getText().isEmpty() ? null : textFieldName.getText());
        user.setIdentity(textFieldUsername.getText().isEmpty() ? null : textFieldUsername.getText());
        user.setRole(dropRole.getValue().isEmpty() ? null : dropRole.getValue());
        return user;
    }
}

