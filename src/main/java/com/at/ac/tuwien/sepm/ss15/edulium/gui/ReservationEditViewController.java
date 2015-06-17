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
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.function.Consumer;

/**
 * Created by phili on 6/17/15.
 */
public class ReservationEditViewController implements Initializable, Controller {
    enum Mode {EDIT, ADD}

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
    private Reservation reservationCopy = new Reservation();
    private Mode mode;

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
    }

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;

        // clear inputs
        tfName.clear();
        tfQuantity.clear();
        tfDuration.clear();
        tfHour.clear();
        tfMinute.clear();
        datePicker.setValue(null);

        if(reservation.getIdentity() == null) {
            mode = Mode.ADD;
        } else {
            mode = Mode.EDIT;
        }

        if(mode == Mode.EDIT) {
            // copy reservation in case the user cancels the edit process
            reservationCopy.setIdentity(reservation.getIdentity());
            reservationCopy.setDuration(reservation.getDuration());
            reservationCopy.setTime(reservation.getTime());
            reservationCopy.setName(reservation.getName());
            reservationCopy.setQuantity(reservation.getQuantity());
            reservationCopy.setTables(new ArrayList<>(reservation.getTables()));

            displayReservationData();
        }
    }

    public void onAccept(Consumer<Reservation> consumer) {
        onAcceptedConsumer = consumer;
    }

    public void onCancel(Consumer<Reservation> consumer) {
        onCanceledConsumer = consumer;
    }

    public void setReservationData() {
        reservation.setName(tfName.getText());
        reservation.setDuration(Duration.ofHours(Long.valueOf(tfDuration.getText())));
        reservation.setQuantity(Integer.valueOf(tfQuantity.getText()));

        LocalDateTime dateTime = LocalDateTime.of(datePicker.getValue(), LocalTime.of(Integer.valueOf(tfHour.getText()), Integer.valueOf(tfMinute.getText())));
        reservation.setTime(dateTime);
    }

    public void displayReservationData() {
        if (reservation.getName() != null) {
            tfName.setText(reservation.getName());
        }

        if (reservation.getQuantity() != null) {
            tfQuantity.setText(String.valueOf(reservation.getQuantity()));
        }

        if (reservation.getDuration() != null) {
            tfDuration.setText(String.valueOf(reservation.getDuration().toHours()));
        }

        LocalDateTime dateTime = reservation.getTime();
        if (dateTime != null) {
            tfHour.setText(String.valueOf(dateTime.toLocalTime().getHour()));
            tfMinute.setText(String.valueOf(dateTime.toLocalTime().getMinute()));
            datePicker.setValue(dateTime.toLocalDate());
        }

        if (reservation.getTables() != null) {
            reservation.getTables().stream().forEach(t -> tableViewController.setTableColor(t, Color.BLUE));
        }
    }

    @FXML
    public void on_btnSave_clicked() {
        if(reservation.getTables() != null) {
            reservation.getTables().stream().forEach(t -> tableViewController.setTableColor(t, Color.BLACK));
        }

        setReservationData();

        try {
            if (mode == Mode.ADD && reservation.getIdentity() == null) {
                // add reservation
                reservationService.addReservation(reservation);
            } else if (reservation.getIdentity() != null) {
                // if in EDIT or ADD mode with set identity -> update
                reservationService.updateReservation(reservation);
            }
        } catch (ServiceException e) {
            e.printStackTrace();
        } catch (ValidationException e) {
            e.printStackTrace();
        }

        onAcceptedConsumer.accept(reservation);
    }

    @FXML
    public void on_btnCancel_clicked() {
        if(reservation.getTables() != null) {
            reservation.getTables().stream().forEach(t -> tableViewController.setTableColor(t, Color.BLACK));
        }

        try {
            if (mode == Mode.ADD && reservation.getIdentity() != null) {
                // reservation was added because of the auto create function
                reservationService.cancelReservation(reservation);
            } else if (mode == Mode.EDIT) {
                // reset reservation to previous state
                reservationService.updateReservation(reservationCopy);
            }
        } catch (ServiceException e) {
            // TODO print message
        } catch (ValidationException e) {
            // TODO print message
        }

        onCanceledConsumer.accept(reservation);
    }

    @FXML
    public void on_autoCreate_clicked() {
        // delete tables
        if(reservation.getTables() != null) {
            reservation.getTables().stream().forEach(t -> tableViewController.setTableColor(t, Color.BLACK));
            reservation.getTables().clear();
        }

        setReservationData();

        // add reservation, so that the tables are automatically set
        try {
            reservationService.addReservation(reservation);
        } catch (ServiceException e) {
            e.printStackTrace();
        } catch (ValidationException e) {
            e.printStackTrace();
        }

        // display selected tables
        if(reservation.getTables() != null) {
            reservation.getTables().stream().forEach(t -> tableViewController.setTableColor(t, Color.BLUE));
        }
    }
}
