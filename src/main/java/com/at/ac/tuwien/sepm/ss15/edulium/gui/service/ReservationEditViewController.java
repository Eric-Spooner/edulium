package com.at.ac.tuwien.sepm.ss15.edulium.gui.service;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.Reservation;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.Table;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ValidationException;
import com.at.ac.tuwien.sepm.ss15.edulium.gui.FXMLPane;
import com.at.ac.tuwien.sepm.ss15.edulium.gui.util.AlertPopOver;
import com.at.ac.tuwien.sepm.ss15.edulium.gui.util.NumericTextField;
import com.at.ac.tuwien.sepm.ss15.edulium.service.ReservationService;
import com.at.ac.tuwien.sepm.ss15.edulium.service.ServiceException;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import java.net.URL;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.function.Consumer;

/**
 * Controller for the ReservationEditViewPane
 */
@Controller
public class ReservationEditViewController implements Initializable {
    enum Mode {EDIT, ADD}

    @FXML
    private Button btnCancel;
    @FXML
    private Button btnSave;
    @FXML
    private Button btnAuto;
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
    @FXML
    private Label lblSeats;

    private AlertPopOver cancelPopOver;
    private AlertPopOver autoPopOver;

    @Autowired
    private ReservationService reservationService;

    @Resource(name = "tableViewPane")
    private FXMLPane tableViewPane;

    private TableViewController tableViewController;
    private Consumer<Reservation> onAcceptedConsumer;
    private Consumer<Reservation> onCanceledConsumer;
    private Reservation reservation;
    private Mode mode;

    private Set<Table> selectedTables = new HashSet<>();
    private Set<Table> occupiedTables = new HashSet<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tableViewController = tableViewPane.getController(TableViewController.class);
        tableViewController.showSeats(true);

        splitPane.getItems().add(0, tableViewPane);

        tableViewController.setOnTableClicked(table -> {
            if(selectedTables.contains(table)) {
                selectedTables.remove(table);
            } else {
                selectedTables.add(table);
            }

            updateUI();
        });

        ChangeListener<Object> changeListener = (observable, oldValue, newValue) -> {
            updateOccupiedTables();
            updateUI();
        };


        // update ui
        tfName.textProperty().addListener((observable, oldValue, newValue) -> updateUI());

        // clear tables and update ui
        datePicker.valueProperty().addListener(changeListener);
        tfDuration.textProperty().addListener(changeListener);
        tfHour.textProperty().addListener(changeListener);
        tfMinute.textProperty().addListener(changeListener);
        tfQuantity.textProperty().addListener(changeListener);

        tfHour.setMinMax(0, 24);
        tfMinute.setMinMax(0, 60);

        initializePopOver();
    }

    public void setReservation(Reservation reservation) {
        selectedTables.clear();
        occupiedTables.clear();

        // clear inputs
        tfName.clear();
        tfQuantity.clear();
        tfDuration.clear();
        tfHour.clear();
        tfMinute.clear();
        datePicker.setValue(null);
        tableViewController.clear();

        if(reservation.getIdentity() == null) {
            mode = Mode.ADD;
        } else {
            mode = Mode.EDIT;
        }

        this.reservation = reservation;

        if(mode == Mode.EDIT) {
            displayReservationData();
        } else {
            reservation.setTables(new ArrayList<>());
        }

        updateUI();
    }

    public void onAccept(Consumer<Reservation> consumer) {
        onAcceptedConsumer = consumer;
    }

    public void onCancel(Consumer<Reservation> consumer) {
        onCanceledConsumer = consumer;
    }

    public void updateUI() {
        // clear table view controller
        tableViewController.clear();

        // display occupied tables
        occupiedTables.stream().forEach(t -> {tableViewController.setTableColor(t, Color.RED); tableViewController.setTableDisable(t, true);});

        // display selected tables
        selectedTables.stream().forEach(t -> tableViewController.setTableColor(t, Color.BLUE));

        // handle buttons
        boolean notReady = tfDuration.isEmpty() || tfHour.isEmpty() || tfMinute.isEmpty()
                || tfQuantity.isEmpty() || datePicker.getValue() == null;

        btnAuto.setDisable(notReady);
        btnSave.setDisable(notReady || selectedTables.isEmpty() || tfName.getText().isEmpty());

        updateSeatsLabel();
    }

    @FXML
    public void on_btnSave_clicked() {
        setReservationData();

        try {
            if (mode == Mode.ADD) {
                // add mode: add reservation
                reservationService.addReservation(reservation);
            } else if (mode == Mode.EDIT){
                // edit mode: update reservation
                reservationService.updateReservation(reservation);
            }
        } catch (ServiceException | ValidationException e) {
            displayErrorMessage("error adding/editing reservation", e);
        }

        onAcceptedConsumer.accept(reservation);
    }

    @FXML
    public void on_btnCancel_clicked() {
        if(cancelPopOver.isShowing()) {
            cancelPopOver.hide();
        } else {
            cancelPopOver.show(btnCancel);
        }
    }

    @FXML
    public void on_btnAuto_clicked() {
        setReservationData();
        tableViewController.clear();

        try {
            reservationService.findTablesForReservation(reservation);

            selectedTables.clear();
            selectedTables.addAll(reservation.getTables());

        } catch (ServiceException | ValidationException e) {
            autoPopOver.show(btnAuto);
        }

        updateUI();
    }

    private void initializePopOver() {
        cancelPopOver = new AlertPopOver();
        cancelPopOver.getLabel().setText("Do you really want to cancel\nthe current changes?");
        cancelPopOver.getOkButton().setText("Yes");
        cancelPopOver.getCancelButton().setText("No");

        cancelPopOver.getOkButton().setOnAction(event -> {
            cancelPopOver.hide();
            onCanceledConsumer.accept(reservation);
        });

        cancelPopOver.getCancelButton().setOnAction(event -> cancelPopOver.hide());

        autoPopOver = new AlertPopOver();
        autoPopOver.getLabel().setText("No free tables available");
        autoPopOver.getOkButton().setText("Ok");
        autoPopOver.getCancelButton().setVisible(false);

        autoPopOver.getOkButton().setOnAction(event -> {
            autoPopOver.hide();
        });
    }

    private void setReservationData() {
        reservation.setName(tfName.getText());
        reservation.setDuration(Duration.ofHours((long) tfDuration.getValue()));
        reservation.setQuantity((int) tfQuantity.getValue());
        reservation.setTime(getLocalDateTime());
        reservation.setTables(new ArrayList<>(selectedTables));
    }

    private void displayReservationData() {
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

        selectedTables.addAll(reservation.getTables());

        // delay setting table colors; workaround: wait for tableview to load tables
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                selectedTables.stream().forEach(t -> tableViewController.setTableColor(t, Color.BLUE));
            }
        };

        Timer timer = new Timer();
        timer.schedule(task, 100);
    }

    private void updateSeatsLabel() {
        int sumSeats = 0;
        for(Table t : selectedTables) {
            sumSeats += t.getSeats();
        }
        lblSeats.setText(sumSeats + " / " + (int) tfQuantity.getValue());
    }

    private LocalDateTime getLocalDateTime() {
        LocalDate date = datePicker.getValue();
        if(date == null) {
            return null;
        }

        return LocalDateTime.of(datePicker.getValue(), LocalTime.of((int) tfHour.getValue(), (int) tfMinute.getValue()));
    }

    private void updateOccupiedTables() {
        LocalDateTime start = getLocalDateTime();

        if(start == null || tfDuration.isEmpty()) {
            return;
        }

        Duration duration = Duration.ofMinutes((long) tfDuration.getValue());
        List<Reservation> reservations = new ArrayList<>();

        // get other reservations
        try {
            reservations = reservationService.findReservationBetween(start, start.plus(duration));
        } catch (ServiceException | ValidationException e) {
            displayErrorMessage("error retrieving reservations", e);
        }

        // remove current reservation
        reservations.removeIf(res -> res.getIdentity().equals(reservation.getIdentity()));

        occupiedTables.clear();
        for(Reservation res : reservations) {
            occupiedTables.addAll(res.getTables());
        }
        // remove occupied tables from selected
        selectedTables.removeAll(occupiedTables);
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
