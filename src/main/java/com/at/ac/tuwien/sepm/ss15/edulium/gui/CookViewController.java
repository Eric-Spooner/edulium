package com.at.ac.tuwien.sepm.ss15.edulium.gui;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.Order;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ValidationException;
import com.at.ac.tuwien.sepm.ss15.edulium.gui.util.PollingList;
import com.at.ac.tuwien.sepm.ss15.edulium.service.MenuService;
import com.at.ac.tuwien.sepm.ss15.edulium.service.OrderService;
import com.at.ac.tuwien.sepm.ss15.edulium.service.ServiceException;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;

import java.net.URL;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Supplier;

/**
 * Controller used for the Cook View
 */
public class CookViewController implements Initializable, Controller {
    private static final Logger LOGGER = LogManager.getLogger(CookViewController.class);

    @FXML
    private TableView<Order> tableViewQueued;
    @FXML
    private TableColumn<Order, Long> tableColQueudTisch;
    @FXML
    private TableColumn<Order, String> tableColQueudMenuEntry;
    @FXML
    private TableColumn<Order, String> tableColQueudAddInfo;
    @FXML
    private TableColumn<Order, Long> tableColQueudTime;

    @FXML
    private TableView<Order> tableViewInProgress;
    @FXML
    private TableColumn<Order, Long> tableColProgTisch;
    @FXML
    private TableColumn<Order, String> tableColProgMenuEntry;
    @FXML
    private TableColumn<Order, String> tableColProgAddInfo;
    @FXML
    private TableColumn<Order, Long> tableColProgTime;

    @FXML
    private TableView<Order> tableViewReadyForDelivery;
    @FXML
    private TableColumn<Order, Long> tableColDeliveryTable;
    @FXML
    private TableColumn<Order, String> tableColDeliveryMenuEntry;
    @FXML
    private TableColumn<Order, String> tableColDeliveryAddInfo;
    @FXML
    private TableColumn<Order, Long> tableColDeliveryTime;

    @Autowired
    private MenuService menuService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private TaskScheduler taskScheduler;

    private PollingList<Order> ordersQueued;
    private PollingList<Order> ordersInProgress;
    private PollingList<Order> ordersReadyForDelivery;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // queued
        ordersQueued = new PollingList<>(taskScheduler);
        ordersQueued.setInterval(1000);
        ordersQueued.setSupplier(new Supplier<List<Order>>() {
            @Override
            public List<Order> get() {
                try {
                    Order matcher = new Order();
                    matcher.setState(Order.State.QUEUED);
                    return orderService.findOrder(matcher);
                } catch (ServiceException e) {
                    LOGGER.error("Getting all queued orders via order supplier has failed", e);
                    return null;
                }
            }
        });


        SortedList<Order> sortedDataQueued = new SortedList<>(ordersQueued);
        sortedDataQueued.comparatorProperty().bind(tableViewQueued.comparatorProperty());
        tableViewQueued.setItems(sortedDataQueued);
        tableViewQueued.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        tableColQueudTisch.setCellValueFactory(p -> new SimpleObjectProperty<Long>(p.getValue().getTable().getNumber()));
        tableColQueudMenuEntry.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getMenuEntry().getName()));
        tableColQueudTime.setCellValueFactory(p -> new SimpleObjectProperty<Long>(Duration.between(p.getValue().getTime(), LocalDateTime.now()).toMinutes()));
        tableColQueudAddInfo.setCellValueFactory(new PropertyValueFactory<Order, String>("additionalInformation"));

        // in progress
        ordersInProgress = new PollingList<>(taskScheduler);
        ordersInProgress.setInterval(1000);
        ordersInProgress.setSupplier(new Supplier<List<Order>>() {
            @Override
            public List<Order> get() {
                try {
                    Order matcher = new Order();
                    matcher.setState(Order.State.IN_PROGRESS);
                    return orderService.findOrder(matcher);
                } catch (ServiceException e) {
                    LOGGER.error("Getting all orders in progress via order supplier has failed", e);
                    return null;
                }
            }
        });
        SortedList<Order> sortedDataInProgress = new SortedList<>(ordersInProgress);
        sortedDataInProgress.comparatorProperty().bind(tableViewInProgress.comparatorProperty());
        tableViewInProgress.setItems(sortedDataInProgress);
        tableViewInProgress.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        tableColProgTisch.setCellValueFactory(p -> new SimpleObjectProperty<Long>(p.getValue().getTable().getNumber()));
        tableColProgMenuEntry.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getMenuEntry().getName()));
        tableColProgTime.setCellValueFactory(p -> new SimpleObjectProperty<Long>(Duration.between(p.getValue().getTime(), LocalDateTime.now()).toMinutes()));
        tableColProgAddInfo.setCellValueFactory(new PropertyValueFactory<Order, String>("additionalInformation"));

        // ready for delivery
        ordersReadyForDelivery = new PollingList<>(taskScheduler);
        ordersReadyForDelivery.setInterval(1000);
        ordersReadyForDelivery.setSupplier(new Supplier<List<Order>>() {
            @Override
            public List<Order> get() {
                try {
                    Order matcher = new Order();
                    matcher.setState(Order.State.READY_FOR_DELIVERY);
                    return orderService.findOrder(matcher);
                } catch (ServiceException e) {
                    LOGGER.error("Getting all ready for delivery orders via order supplier has failed", e);
                    return null;
                }
            }
        });
        SortedList<Order> sortedDataReady = new SortedList<>(ordersReadyForDelivery);
        sortedDataReady.comparatorProperty().bind(tableViewInProgress.comparatorProperty());
        tableViewReadyForDelivery.setItems(sortedDataReady);

        tableColDeliveryTable.setCellValueFactory(p -> new SimpleObjectProperty<Long>(p.getValue().getTable().getNumber()));
        tableColDeliveryMenuEntry.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getMenuEntry().getName()));
        tableColDeliveryTime.setCellValueFactory(p -> new SimpleObjectProperty<Long>(Duration.between(p.getValue().getTime(), LocalDateTime.now()).toMinutes()));
        tableColDeliveryAddInfo.setCellValueFactory(new PropertyValueFactory<Order, String>("additionalInformation"));
    }

    public void btnInProgressToReadyToDeliver(ActionEvent actionEvent) {
        LOGGER.info("Entered btnInProgressToReadyToDeliver");

        if (tableViewInProgress.getSelectionModel().getSelectedItems() != null) {
            try {
                for (Order order : tableViewInProgress.getSelectionModel().getSelectedItems()) {
                    orderService.markAsReadyForDelivery(order);
                }
            } catch (ValidationException | ServiceException e) {
                LOGGER.error("Putting State to in Progress did not work", e);

                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Putting State to in Progress");
                alert.setHeaderText("The State shifting did not work");
                alert.setContentText(e.toString());

                alert.showAndWait();
            } finally {
                ordersInProgress.immediateUpdate();
                ordersReadyForDelivery.immediateUpdate();
            }
        }
    }

    public void btnQueuedToInProgress(ActionEvent actionEvent) {
        LOGGER.info("Entered btnQueuedToInProgress");

        if (tableViewInProgress.getSelectionModel().getSelectedItems() != null) {
            try {
                for (Order order : tableViewQueued.getSelectionModel().getSelectedItems()) {
                    orderService.markAsInProgress(order);
                }
            } catch (Exception e) {
                LOGGER.error("Putting State to in Ready to Deliver did not work", e);

                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Putting State to in Ready to Deliver");
                alert.setHeaderText("The State shifting did not work");
                alert.setContentText(e.toString());

                alert.showAndWait();
            } finally {
                ordersQueued.immediateUpdate();
                ordersInProgress.immediateUpdate();
            }
        }
    }

    @Override
    public void disable(boolean disabled) {
        if (disabled) {
            ordersQueued.stopPolling();
            ordersInProgress.stopPolling();
            ordersReadyForDelivery.stopPolling();
        } else {
            ordersQueued.startPolling();
            ordersInProgress.startPolling();
            ordersReadyForDelivery.startPolling();
        }
    }
}
