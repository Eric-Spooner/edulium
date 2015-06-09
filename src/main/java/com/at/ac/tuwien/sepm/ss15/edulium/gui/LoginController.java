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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * Controller used for the Manager View
 */
@Component
public class LoginController implements Initializable {
    private static final Logger LOGGER = LogManager.getLogger(LoginController.class);

    @FXML
    GridPane userGP;
    @FXML
    private ScrollPane scrollPane;

    private UserService userService;
    private ArrayList<Button> buttons = new ArrayList<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ApplicationContext context = new ClassPathXmlApplicationContext("spring/Spring-Edulium.xml");
        userService = context.getBean("userService", UserService.class);

        try {
            userGP.setVgap(4);
            userGP.setHgap(238);
            int row = 0;
            int col = 0;
            for(User user : userService.getAllUsers()) {
                Button button = new Button();
                button.setText(user.getIdentity());
                button.setPrefSize(240, 40);
                button.setMinWidth(240);
                button.setStyle("-fx-font-size: 18px;");
                button.setOnAction(new EventHandler<ActionEvent>() {
                    public void handle(ActionEvent t){
                        System.out.println("You pressed "+button.getText());
                    }
                });

                if(userGP.getRowConstraints().size() <= row) {
                    Separator sepVert1 = new Separator();
                    userGP.setRowSpan(sepVert1, row);
                }
                buttons.add(button);
                userGP.add(button, col, row);
                if(col == 2) row++;
                //col = (col == 0) ? 1 : 0;
                if(col++ == 2)
                    col = 0;
            }
        } catch(ServiceException e) {
            System.out.println(e);
        }

        scrollPane.setStyle("-fx-font-size: 40px;");
    }
}
