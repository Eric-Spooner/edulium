package com.at.ac.tuwien.sepm.ss15.edulium.gui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.net.URL;
import java.util.ResourceBundle;

@Component
public class MainWindowController implements Initializable {
    private static final Logger LOGGER = LogManager.getLogger(MainWindowController.class);

    private enum ScreenType {
        LoginScreen,
        ManagerScreen,
        CookScreen,
        ServiceScreen
    }

    @FXML
    private BorderPane borderPane;
    @FXML
    private Button logoutButton;
    @FXML
    private Label userNameLabel;

    @Resource(name = "loginPane")
    FXMLPane loginPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeLoginController();

        showScreen(ScreenType.LoginScreen);
    }

    private void initializeLoginController() {
        LoginController loginController = loginPane.getController(LoginController.class);

        // login
        loginController.setOnSuccessfulLoginAs(user -> {
            logoutButton.setDisable(false);
            userNameLabel.setText(user.getName());

            // show the right screen for the role
            switch (user.getRole()) {
                default:
                    LOGGER.debug("We have no screen for role '" + user.getRole() + "' -> logout");
                    loginController.logout();
            }
        });

        // logout
        loginController.setOnLogout(reason -> {
            logoutButton.setDisable(true);
            userNameLabel.setText("");

            showScreen(ScreenType.LoginScreen);
        });

        logoutButton.setOnAction(e -> loginController.logout());
    }

    private void showScreen(ScreenType screenType) {
        switch (screenType) {
            case ManagerScreen:
                break;
            case CookScreen:
                break;
            case ServiceScreen:
                break;
            case LoginScreen:
            default:
                borderPane.setCenter(loginPane);
        }
    }
}
