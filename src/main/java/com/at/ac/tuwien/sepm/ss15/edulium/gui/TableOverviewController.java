package com.at.ac.tuwien.sepm.ss15.edulium.gui;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.Table;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;

import javax.annotation.Resource;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

public class TableOverviewController implements Initializable, Controller {
    @FXML
    private BorderPane borderPane;
    @FXML
    private Button btnReservation;

    @Resource(name = "tableViewPane")
    private FXMLPane tableViewPane;

    private TableViewController tableViewController;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {

        tableViewController = tableViewPane.getController(TableViewController.class);

        borderPane.setCenter(tableViewPane);
    }

    @Override
    public void disable(boolean disabled) {

    }

    public void setOnReservationButtonAction(EventHandler<ActionEvent> event) {
        btnReservation.setOnAction(event);
    }

    public void setOnTableClicked(Consumer<Table> consumer) {
        tableViewController.setOnTableClicked(consumer);
    }

}
