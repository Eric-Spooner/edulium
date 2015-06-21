package com.at.ac.tuwien.sepm.ss15.edulium.gui.service;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.Reservation;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ValidationException;
import com.at.ac.tuwien.sepm.ss15.edulium.gui.Controller;
import com.at.ac.tuwien.sepm.ss15.edulium.gui.util.AlertPopOver;
import com.at.ac.tuwien.sepm.ss15.edulium.gui.util.PollingList;
import com.at.ac.tuwien.sepm.ss15.edulium.service.ReservationService;
import com.at.ac.tuwien.sepm.ss15.edulium.service.ServiceException;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * Created by phili on 6/17/15.
 */
public class ReservationOverviewController implements Initializable, Controller {
    private static final Logger LOGGER = LogManager.getLogger(ReservationOverviewController.class);

    @FXML
    private BorderPane borderPane;
    @FXML
    private Button btnBack;
    @FXML
    private Button btnAdd;
    @FXML
    private Button btnDelete;
    @FXML
    private Button btnEdit;
    @FXML
    private TextField tfNameFilter;
    @FXML
    private DatePicker datePickerFilter;
    @FXML
    private Button btnClearDate;

    private AlertPopOver cancelPopOver;

    @Autowired
    private TaskScheduler taskScheduler;
    @FXML
    ListView<Reservation> lvReservations;
    @Autowired
    private ReservationService reservationService;

    private Consumer<Reservation> editConsumer = null;

    private PollingList<Reservation> reservations;
    private FilteredList<Reservation> filteredReservations;

    private class ReservationListCell extends ListCell<Reservation> {
        @Override
        protected void updateItem(Reservation item, boolean empty) {
            super.updateItem(item, empty);

            if(empty) {
                setGraphic(null);
            }

            // prepare layout
            HBox hbox = new HBox(30);
            VBox vBox = new VBox(10);

            Label lblName = new Label();
            Label lblQuantity = new Label();
            Label lblDuration = new Label();
            Label lblDate = new Label();

            vBox.getChildren().addAll(lblName, lblQuantity, lblDuration);
            hbox.getChildren().addAll(vBox, lblDate);
            hbox.setAlignment(Pos.CENTER_LEFT);

            if(item != null) {
                // set data
                lblName.setText(item.getName());
                lblName.setFont(new Font(15));
                lblQuantity.setText(item.getQuantity() + " Persons");
                lblDate.setText(item.getTime().format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)));
                lblDate.setFont(new Font(15));
                lblDuration.setText(item.getDuration().toHours() + " Hours");

                setGraphic(hbox);
            }
        }
    }

    @Override
    public void disable(boolean disabled) {
        if(disabled) {
            reservations.stopPolling();
        } else {
            reservations.startPolling();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        reservations = new PollingList<>(taskScheduler);
        reservations.setInterval(1000);
        reservations.setSupplier(() -> {
            try {
                return reservationService.getAllReservations();
            } catch (ServiceException e) {
                LOGGER.error("Getting all tables via user supplier has failed", e);
                return null;
            }
        });

        initializeFiltering();
        initializeCancelPopOver();

        btnClearDate.setOnAction(event -> datePickerFilter.setValue(null));
        lvReservations.setItems(filteredReservations);
        lvReservations.setCellFactory(param -> new ReservationListCell());
    }

    public void setOnBackButtonAction(EventHandler<ActionEvent> event) {
        btnBack.setOnAction(event);
    }

    public void setOnEditConsumer(Consumer<Reservation> consumer) {
        editConsumer = consumer;
    }

    public void setOnAddButtonAction(EventHandler<ActionEvent> event) {
        btnAdd.setOnAction(event);
    }

    @FXML
    public void on_btnEdit_clicked() {
        editConsumer.accept(lvReservations.getSelectionModel().getSelectedItem());
    }

    @FXML
    public void on_btnDelete_clicked() {
        if(cancelPopOver.isShowing()) {
            cancelPopOver.hide();
        } else {
            cancelPopOver.show(btnDelete);
        }
    }

    private void initializeFiltering() {
        filteredReservations = new FilteredList<>(reservations);

        Predicate<Reservation> filterPredicate = reservation -> {
            boolean containsName = reservation.getName().contains(tfNameFilter.getText());

            LocalDate dateFilter = datePickerFilter.getValue();
            if(dateFilter != null) {
                return reservation.getTime().toLocalDate().isEqual(dateFilter) && containsName;
            } else {
                return reservation.getTime().plus(reservation.getDuration()).isAfter(LocalDateTime.now()) && containsName;
            }
        };

        filteredReservations.setPredicate(filterPredicate);

        tfNameFilter.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredReservations.setPredicate(null);
            filteredReservations.setPredicate(filterPredicate);
        });

        datePickerFilter.valueProperty().addListener((observable1, oldValue1, newValue) -> {
            filteredReservations.setPredicate(null);
            filteredReservations.setPredicate(filterPredicate);
        });

        lvReservations.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            // set edit button disabled if reservation is bygone
            boolean disable = newValue == null? true : newValue.getTime().plus(newValue.getDuration()).isBefore(LocalDateTime.now());
            btnEdit.setDisable(disable);
            btnDelete.setDisable(disable);
        });
    }

    private void initializeCancelPopOver() {
        cancelPopOver = new AlertPopOver();
        cancelPopOver.getLabel().setText("Do you really want to cancel\n the selected reservation?");
        cancelPopOver.getOkButton().setText("Yes");
        cancelPopOver.getCancelButton().setText("No");

        cancelPopOver.getOkButton().setOnAction(event -> {
            cancelPopOver.hide();
            try {
                reservationService.cancelReservation(lvReservations.getSelectionModel().getSelectedItem());
            } catch (ServiceException | ValidationException e) {
                displayErrorMessage("error cancelling reservation", e);
            }
        });

        cancelPopOver.getCancelButton().setOnAction(event -> cancelPopOver.hide());
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
