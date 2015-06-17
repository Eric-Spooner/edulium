package com.at.ac.tuwien.sepm.ss15.edulium.gui;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.Reservation;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.Table;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ValidationException;
import com.at.ac.tuwien.sepm.ss15.edulium.service.ReservationService;
import com.at.ac.tuwien.sepm.ss15.edulium.service.ServiceException;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.awt.event.WindowEvent;
import java.net.URL;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
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
    private NumericTextField tfQuantity;
    @FXML
    private NumericTextField tfHour;
    @FXML
    private NumericTextField tfMinute;
    @FXML
    private NumericTextField tfDuration;
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


        ChangeListener<Object> dateTimeChangeListener = new ChangeListener<Object>() {
            @Override
            public void changed(ObservableValue<?> observable, Object oldValue, Object newValue) {
                LocalDateTime dateTime = getLocalDateTime();
                try {
                    if(tfDuration.isEmpty() || dateTime == null) {
                        return;
                    }
                    List<Reservation> reservations = reservationService.findReservationBetween(dateTime, dateTime.plusHours((int) tfDuration.getValue()));
                    displayOccupiedTables(reservations);
                } catch (ServiceException | ValidationException e) {
                    displayErrorMessage("error retrieving reservations", e);
                }
            }
        };

        datePicker.valueProperty().addListener(dateTimeChangeListener);
        tfDuration.textProperty().addListener(dateTimeChangeListener);
        tfHour.textProperty().addListener(dateTimeChangeListener);
        tfMinute.textProperty().addListener(dateTimeChangeListener);

        tfHour.setMinMax(0, 24);
        tfMinute.setMinMax(0, 60);
    }

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;

        // clear inputs
        tableViewController.clear();
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
        } else {
            reservation.setTables(new ArrayList<>());
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

        reservation.setTime(getLocalDateTime());
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
        setReservationData();

        try {
            if (mode == Mode.ADD && reservation.getIdentity() == null) {
                // add reservation
                reservationService.addReservation(reservation);
            } else if (reservation.getIdentity() != null) {
                // if in EDIT or ADD mode with set identity -> update
                reservationService.updateReservation(reservation);
            }
        } catch (ServiceException | ValidationException e) {
            displayErrorMessage("error adding/editing reservation", e);
        }

        onAcceptedConsumer.accept(reservation);
    }

    @FXML
    public void on_btnCancel_clicked() {
        try {
            if (mode == Mode.ADD && reservation.getIdentity() != null) {
                // reservation was added because of the auto create function
                reservationService.cancelReservation(reservation);
            } else if (mode == Mode.EDIT) {
                // reset reservation to previous state
                reservationService.updateReservation(reservationCopy);
            }
        } catch (ServiceException | ValidationException e) {
            displayErrorMessage("error cancelling request", e);
        }

        onCanceledConsumer.accept(reservation);
    }

    @FXML
    public void on_autoCreate_clicked() {
        // delete tables
        reservation.getTables().clear();

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

    private LocalDateTime getLocalDateTime() {
        LocalDate date = datePicker.getValue();
        if(date == null) {
            return null;
        }

        return LocalDateTime.of(datePicker.getValue(), LocalTime.of((int) tfHour.getValue(), (int) tfMinute.getValue()));
    }

    private void displayOccupiedTables(List<Reservation> reservations) {
        tableViewController.clear();

        // remove current reservation
        reservations.remove(reservation);

        for(Reservation res : reservations) {
            for(Table t : res.getTables()) {
                tableViewController.setTableColor(t, Color.RED);
                tableViewController.setTableDisable(t, true);
            }
        }
    }

    private void displayErrorMessage(String message, Exception e) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error Dialog");
        alert.setHeaderText(message);
        if(e != null) {
            alert.setContentText(e.getMessage());
        }

        alert.showAndWait();
    }
}
