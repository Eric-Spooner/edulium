package com.at.ac.tuwien.sepm.ss15.edulium.gui.service;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.Reservation;
import com.at.ac.tuwien.sepm.ss15.edulium.gui.Controller;
import com.at.ac.tuwien.sepm.ss15.edulium.gui.FXMLPane;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Resource;
import java.net.URL;
import java.util.ResourceBundle;

public class ServiceController implements Initializable, Controller {
    private static final Logger LOGGER = LogManager.getLogger(ServiceController.class);

    @FXML
    private BorderPane borderPane;

    @Resource(name = "tableOverviewPane")
    private FXMLPane tableOverviewPane;
    @Resource(name = "reservationOverviewPane")
    private FXMLPane reservationOverviewPane;
    @Resource(name = "reservationEditViewPane")
    private FXMLPane reservationEditViewPane;
    @Resource(name = "orderOverviewPane")
    private FXMLPane orderOverviewPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        borderPane.setCenter(tableOverviewPane);

        TableOverviewController tableOverviewController = tableOverviewPane.getController(TableOverviewController.class);
        OrderOverviewController orderOverviewController = orderOverviewPane.getController(OrderOverviewController.class);

        ReservationOverviewController reservationOverviewController = reservationOverviewPane.getController(ReservationOverviewController.class);
        ReservationEditViewController reservationEditViewController = reservationEditViewPane.getController(ReservationEditViewController.class);

        reservationOverviewController.setOnBackButtonAction(event -> borderPane.setCenter(tableOverviewPane));
        tableOverviewController.setOnReservationButtonAction(event -> borderPane.setCenter(reservationOverviewPane));

        reservationOverviewController.setOnAddButtonAction(event -> {
            borderPane.setCenter(reservationEditViewPane);
            reservationEditViewController.setReservation(new Reservation());
        });

        reservationOverviewController.setOnEditConsumer(reservation -> {
            borderPane.setCenter(reservationEditViewPane);
            reservationEditViewController.setReservation(reservation);
        });

        tableOverviewController.setOnReservationButtonAction(event -> borderPane.setCenter(reservationOverviewPane));

        tableOverviewController.setOnTableClicked(table -> {
            orderOverviewController.setTable(table);
            borderPane.setCenter(orderOverviewPane);
        });

        orderOverviewController.setOnBackButtonAction(event -> borderPane.setCenter(tableOverviewPane));

        reservationEditViewController.onCancel(reservation -> borderPane.setCenter(reservationOverviewPane));
        reservationEditViewController.onAccept(reservation -> borderPane.setCenter(reservationOverviewPane));
    }

    @Override
    public void disable(boolean disabled) {

    }
}