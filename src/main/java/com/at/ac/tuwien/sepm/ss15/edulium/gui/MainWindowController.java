package com.at.ac.tuwien.sepm.ss15.edulium.gui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import java.net.URL;
import java.util.ResourceBundle;

@Controller
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
    private FXMLPane loginPane;
    @Autowired
    private ApplicationContext context;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeLoginController();

        showScreen(ScreenType.LoginScreen);
    }

    private void initializeLoginController() {
        LoginController loginController = loginPane.getController();

        // login
        loginController.setOnSuccessfulLoginAs(user -> {
            logoutButton.setDisable(false);
            userNameLabel.setText(user.getName());

            // show the right screen for the role
            switch (user.getRole()) {
                case "ROLE_MANAGER":
                    showScreen(ScreenType.ManagerScreen);
                    break;
                case "ROLE_COOK":
                    showScreen(ScreenType.CookScreen);
                    break;
                case "ROLE_SERVICE":
                    showScreen(ScreenType.ServiceScreen);
                    break;
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
                // create a new manager view for every session
                FXMLPane managerViewPane = context.getBean("managerViewPane", FXMLPane.class);
                borderPane.setCenter(managerViewPane);
                break;
            case CookScreen:
                // create a new cook view for every session
                FXMLPane cookViewPane = context.getBean("cookViewPane", FXMLPane.class);
                borderPane.setCenter(cookViewPane);
                break;
            case ServiceScreen:
                // create a new service view for every session
                FXMLPane serviceViewPane = context.getBean("serviceViewPane", FXMLPane.class);
                borderPane.setCenter(serviceViewPane);
                break;
            case LoginScreen:
            default:
                borderPane.setCenter(loginPane);
        }
    }
}
