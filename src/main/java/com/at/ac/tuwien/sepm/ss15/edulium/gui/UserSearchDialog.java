package com.at.ac.tuwien.sepm.ss15.edulium.gui;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.User;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class UserSearchDialog extends Dialog<User> {

    @Resource(name = "userDialogPane")
    public void setUserDialogPane(FXMLPane userDialogPane) {
        UserDialogController userDialogController = userDialogPane.getController(UserDialogController.class);

        setTitle("Search for Employees");
        setHeaderText("Search for existing employees");

        getDialogPane().setContent(userDialogPane);

        ButtonType searchButtonType = new ButtonType("Search", ButtonBar.ButtonData.OK_DONE);
        getDialogPane().getButtonTypes().setAll(searchButtonType, ButtonType.CANCEL);

        setResultConverter(buttonType -> {
            if (buttonType == searchButtonType) {
                return userDialogController.toUser();
            } else {
                return null;
            }
        });

        setOnShowing(event -> userDialogController.prepareForSearch());
    }
}