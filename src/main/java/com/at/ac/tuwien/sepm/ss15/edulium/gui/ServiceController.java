package com.at.ac.tuwien.sepm.ss15.edulium.gui;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.Table;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.User;
import com.at.ac.tuwien.sepm.ss15.edulium.service.ServiceException;
import com.at.ac.tuwien.sepm.ss15.edulium.service.UserService;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.controlsfx.control.GridCell;
import org.controlsfx.control.GridView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Controller used for the Manager View
 */
public class ServiceController implements Initializable, Controller {
    private static final Logger LOGGER = LogManager.getLogger(LoginController.class);

    @Resource(name = "tablePane")
    FXMLPane tablePane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        TableOverviewController tableOverviewController = tablePane.getController(TableOverviewController.class);
        tableOverviewController.setOnTableClicked(table -> {
            OrderOverviewController.setSelectedTable(table);
        });
    }

    @Override
    public void disable(boolean disabled) {
    }
}
