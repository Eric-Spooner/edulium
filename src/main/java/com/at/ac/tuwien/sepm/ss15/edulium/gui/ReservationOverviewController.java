package com.at.ac.tuwien.sepm.ss15.edulium.gui;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.Reservation;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.Table;
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

import javax.annotation.Resource;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import java.util.function.Supplier;

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

        filteredReservations = new FilteredList<>(reservations);

        tfNameFilter.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredReservations.setPredicate(reservation -> reservation.getName().contains(newValue));
        });

        datePickerFilter.valueProperty().addListener((observable1, oldValue1, newValue) -> {
            filteredReservations.setPredicate(reservation -> {
                if (newValue == null) {
                    return true;
                }
                return reservation.getTime().toLocalDate().isEqual(newValue);
            });
        });

        lvReservations.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            // set edit button disabled if reservation is bygone
            btnEdit.setDisable(newValue.getTime().plus(newValue.getDuration()).isBefore(LocalDateTime.now()));
        });

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
}
