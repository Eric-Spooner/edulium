package com.at.ac.tuwien.sepm.ss15.edulium.gui;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.Reservation;
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
    @Resource(name = "menuCategoryOverviewPane")
    private FXMLPane menuCategoryOverviewPane;
    @Resource(name = "menuEntryOverviewPane")
    private FXMLPane menuEntryOverviewPane;
    @Resource(name = "reservationOverviewPane")
    private FXMLPane reservationOverviewPane;
    @Resource(name = "reservationEditViewPane")
    private FXMLPane reservationEditViewPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        borderPane.setCenter(tableOverviewPane);

        TableOverviewController tableOverviewController = tableOverviewPane.getController(TableOverviewController.class);
        MenuCategoryOverviewController menuCategoryOverviewController = menuCategoryOverviewPane.getController(MenuCategoryOverviewController.class);
        MenuEntryOverviewController menuEntryOverviewController = menuEntryOverviewPane.getController(MenuEntryOverviewController.class);
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

        reservationEditViewController.onCancel(reservation -> borderPane.setCenter(reservationOverviewPane));
        reservationEditViewController.onAccept(reservation -> borderPane.setCenter(reservationOverviewPane));

        menuCategoryOverviewController.setOnMenuCategoryClicked(menuCategory -> {
            menuEntryOverviewController.setMenuCategory(menuCategory);
            borderPane.setCenter(menuEntryOverviewPane);
        });

        menuEntryOverviewController.setOnMenuEntryClicked(menuCategory -> {
            borderPane.setCenter(tableOverviewPane);
        });
    }

    @Override
    public void disable(boolean disabled) {

    }
}
