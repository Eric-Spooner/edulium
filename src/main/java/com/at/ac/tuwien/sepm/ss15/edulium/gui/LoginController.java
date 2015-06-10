package com.at.ac.tuwien.sepm.ss15.edulium.gui;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.Section;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.Table;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.User;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ValidationException;
import com.at.ac.tuwien.sepm.ss15.edulium.service.InteriorService;
import com.at.ac.tuwien.sepm.ss15.edulium.service.ServiceException;
import com.at.ac.tuwien.sepm.ss15.edulium.service.UserService;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.controlsfx.control.GridCell;
import org.controlsfx.control.GridView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Supplier;

/**
 * Controller used for the Manager View
 */
@Component
public class LoginController implements Initializable {
    private static final Logger LOGGER = LogManager.getLogger(LoginController.class);

    @FXML
    private ScrollPane scrollPane;

    @Autowired
    private UserService userService;
    @Autowired
    private TaskScheduler taskScheduler;
    @Autowired
    private AuthenticationManager authenticationManager;

    private PollingList<User> users;

    private class UserButtonCell extends GridCell<User> {
        private Button button = new Button();
        private User user = null;

        public UserButtonCell() {
            button.setPrefSize(240, 100);
            button.setStyle("-fx-font-size: 18px;");
            button.setOnAction(e -> loginAs(user));

            setGraphic(button);
        }

        @Override
        public void updateIndex(int i) {
            if (i >= 0) {
                user = users.get(i);
                button.setText(user.getName());
            }
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        users = new PollingList<>(taskScheduler);
        users.setInterval(1000);
        users.setSupplier(new Supplier<List<User>>() {
            @Override
            public List<User> get() {
                try {
                    return userService.getAllUsers();
                } catch (ServiceException e) {
                    LOGGER.error("Getting all users via user supplier has failed", e);
                    return null;
                }
            }
        });
        users.startPolling();

        GridView<User> gridView = new GridView<>(users);
        gridView.setCellFactory(view -> new UserButtonCell());
        gridView.setCellWidth(240);
        gridView.setCellHeight(100);

        scrollPane.setContent(gridView);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setStyle("-fx-font-size: 40px;");
    }

    private void loginAs(User user) {
        assert user != null;

        try {
            Authentication request = new UsernamePasswordAuthenticationToken(user.getIdentity(), "");
            Authentication result = authenticationManager.authenticate(request);

            SecurityContextHolder.getContext().setAuthentication(result);
        } catch (BadCredentialsException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Bad Credentials");
            alert.setHeaderText("Login for " + user.getName() + " denied due bad credentials!");
            alert.setContentText("Login as '" + user.getIdentity() + "' has failed, please try it again or contact your manager.");

            alert.showAndWait();
        }
    }
}
