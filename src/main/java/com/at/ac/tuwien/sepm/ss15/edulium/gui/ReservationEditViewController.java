package com.at.ac.tuwien.sepm.ss15.edulium.gui;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.Reservation;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.Table;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ValidationException;
import com.at.ac.tuwien.sepm.ss15.edulium.service.ReservationService;
import com.at.ac.tuwien.sepm.ss15.edulium.service.ServiceException;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.awt.event.WindowEvent;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ResourceBundle;
import java.util.function.Consumer;

/**
 * Created by phili on 6/17/15.
 */
public class ReservationEditViewController implements Initializable, Controller {
    @FXML
    private Button btnCancel;
    @FXML
    private Button btnSave;
    @FXML
    private SplitPane splitPane;
    @FXML
    private TextField tfName;
    @FXML
    private TextField tfQuantity;
    @FXML
    private TextField tfHour;
    @FXML
    private TextField tfMinute;
    @FXML
    private TextField tfDuration;
    @FXML
    private DatePicker datePicker;

    @Autowired
    private ReservationService reservationService;

    @Resource(name = "tableViewPane")
    private FXMLPane tableViewPane;

    private TableViewController tableViewController;
    private Consumer<Reservation> onAcceptedConsumer;
    private Consumer<Reservation> onCanceledConsumer;
    private Reservation reservation;

    @Override
    public void disable(boolean disabled) {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        splitPane.getItems().add(0, tableViewPane);
        tableViewController = tableViewPane.getController(TableViewController.class);

        tableViewController.setOnTableClicked(table -> {
            if(reservation.getTables().contains(table)) {
                // remove table from selection
                reservation.getTables().remove(table);
                tableViewController.setTableColor(table, Color.BLACK);
            } else {
                // add table to selection
                reservation.getTables().add(table);
                tableViewController.setTableColor(table, Color.BLUE);
            }
        });

        // clear inputs
        tfName.clear();
        tfQuantity.clear();
        tfDuration.clear();
        tfHour.clear();
        tfMinute.clear();
        datePicker.setValue(null);
    }

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;

        if(reservation.getName() != null) {
            tfName.setText(reservation.getName());
        }

        if(reservation.getQuantity() != null) {
            tfQuantity.setText(String.valueOf(reservation.getQuantity()));
        }

        if(reservation.getDuration() != null) {
            tfDuration.setText(String.valueOf(reservation.getDuration().toHours()));
        }

        LocalDateTime dateTime = reservation.getTime();
        if(dateTime != null) {
            tfHour.setText(String.valueOf(dateTime.toLocalTime().getHour()));
            tfMinute.setText(String.valueOf(dateTime.toLocalTime().getMinute()));
            datePicker.setValue(dateTime.toLocalDate());
        }

        if(reservation.getTables() != null) {
            reservation.getTables().stream().forEach(t -> tableViewController.setTableColor(t, Color.BLUE));
        }
    }

    public void onAccept(Consumer<Reservation> consumer) {
        onAcceptedConsumer = consumer;
    }

    public void onCancel(Consumer<Reservation> consumer) {
        onCanceledConsumer = consumer;
    }

    @FXML
    public void on_btnSave_clicked() {
        if(reservation.getTables() != null) {
            reservation.getTables().stream().forEach(t -> tableViewController.setTableColor(t, Color.BLACK));
        }

        onAcceptedConsumer.accept(reservation);
    }

    @FXML
    public void on_btnCancel_clicked() {
        if(reservation.getTables() != null) {
            reservation.getTables().stream().forEach(t -> tableViewController.setTableColor(t, Color.BLACK));
        }

        onCanceledConsumer.accept(reservation);
    }

    @FXML
    public void on_autoCreate_clicked() {
        try {
            reservationService.addReservation(reservation);
        } catch (ServiceException e) {
            // TODO print message
        } catch (ValidationException e) {
            // TODO print message
        }
    }
}
