package com.at.ac.tuwien.sepm.ss15.edulium;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception{
        ApplicationContext context = new ClassPathXmlApplicationContext("spring/Spring-Edulium.xml");

        AuthenticationManager authenticationManager = context.getBean(AuthenticationManager.class);
        Authentication request = new UsernamePasswordAuthenticationToken("servicetester", "");
        Authentication result = authenticationManager.authenticate(request);
        SecurityContextHolder.getContext().setAuthentication(result);

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/gui/Login.fxml"));
        Parent root = (Parent)fxmlLoader.load();
        primaryStage.setTitle("Login");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
