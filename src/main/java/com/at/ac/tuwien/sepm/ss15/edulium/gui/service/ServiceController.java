package com.at.ac.tuwien.sepm.ss15.edulium.gui.service;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.Reservation;
import com.at.ac.tuwien.sepm.ss15.edulium.gui.FXMLPane;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import java.net.URL;
import java.util.ResourceBundle;

@Controller
public class ServiceController implements Initializable {
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

        TableOverviewController tableOverviewController = tableOverviewPane.getController();
        OrderOverviewController orderOverviewController = orderOverviewPane.getController();

        ReservationOverviewController reservationOverviewController = reservationOverviewPane.getController();
        ReservationEditViewController reservationEditViewController = reservationEditViewPane.getController();

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
}
