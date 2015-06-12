package com.at.ac.tuwien.sepm.ss15.edulium.gui;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.User;
import com.at.ac.tuwien.sepm.ss15.edulium.service.UserService;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

import static javafx.collections.FXCollections.observableArrayList;

/**
 * Controller for the TaxRate Dialog
 */
public class DialogUserController implements Initializable{
    private static final Logger LOGGER = LogManager.getLogger(DialogUserController.class);


    private static Stage thisStage;
    private static UserService userService;
    private static User user;
    private static DialogEnumeration dialogEnumeration;


    @FXML
    private TextField textFieldName;
    @FXML
    private TextField textFieldUsername;
    @FXML
    private ChoiceBox<String> dropRole;

    public static User getUser() {
        return user;
    }
    public static void setUser(User user) {
        DialogUserController.user = user;
    }
    public static void setUserService(UserService userService) {
        DialogUserController.userService = userService;
    }

    public static void setThisStage(Stage thisStage) {
        DialogUserController.thisStage = thisStage;
    }
    public static void setDialogEnumeration(DialogEnumeration dialogEnumeration) {
        DialogUserController.dialogEnumeration = dialogEnumeration;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources){
        LOGGER.info("Initialize Dialog MenuEntry");
        try {
            List<String> roles = new LinkedList<>();
            roles.add("MANAGER");
            roles.add("COOK");
            roles.add("SERVICE");
            dropRole.setItems(observableArrayList(roles));
            if(user == null){
                user = new User();
            }
            if(user.getName() != null){
                textFieldName.setText(user.getName());
            }
            if (user.getIdentity() != null){
                textFieldUsername.setText(user.getIdentity());
            }
            if (user.getRole() != null){
                dropRole.getSelectionModel().select(user.getRole());
            }
            //Used to handle the dropDown with the keyboard
            dropRole.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
                @Override
                public void handle(KeyEvent event) {
                    if (event.getCode() == KeyCode.UP || event.getCode() == KeyCode.DOWN) {
                        event.consume();
                    }
                }
            });
        }catch (Exception e){
            LOGGER.error("Init Menu Entry crashed " + e);
        }
    }

    public void buttonOKClick(ActionEvent actionEvent) {
        LOGGER.info("Dialog MenuEntry OK Button clicked");
        switch (DialogUserController.dialogEnumeration){
            case ADD:
            case UPDATE:
                if(textFieldUsername == null || textFieldUsername.getText().isEmpty()){
                    ManagerViewController.showErrorDialog
                            ("Error", "Input Validation Error", "You have to insert a Username");
                    return;
                }
                if(textFieldName == null || textFieldName.getText().isEmpty()){
                    ManagerViewController.showErrorDialog
                            ("Error", "Input Validation Error", "You have to insert a Username");
                    return;
                }
                if(dropRole.getSelectionModel().getSelectedItem() == null){
                    ManagerViewController.showErrorDialog
                            ("Error", "Input Validation Error", "You have to select a Role");
                    return;
                }
                break;
        }
        if(textFieldName != null && !textFieldName.getText().isEmpty()){
            user.setName(textFieldName.getText());
        }
        if(textFieldUsername != null && !textFieldUsername.getText().isEmpty()){
            user.setIdentity(textFieldUsername.getText());
        }
        if(dropRole.getSelectionModel().getSelectedItem() != null){
            user.setRole(dropRole.getSelectionModel().getSelectedItem());
        }
        try {
            switch (DialogUserController.dialogEnumeration){
                case ADD:
                    userService.addUser(user);
                    break;
                case UPDATE:
                    userService.updateUser(user);
                    break;
            }
        } catch (Exception e) {
            ManagerViewController.showErrorDialog
                    ("Error", "Input Validation Error", "Updating the User in The Database Failed\n" +
                            "Maybe the Username already exists");
            LOGGER.error("Updating the User in The Database Failed " + e);
            return;
        }
        thisStage.close();
    }

    public void buttonCancelClick(ActionEvent actionEvent) {
        LOGGER.info("Dialog User Cancel Button clicked");
        thisStage.close();
    }

    public static void resetDialog(){
        DialogUserController.setUser(null);
    }
}

