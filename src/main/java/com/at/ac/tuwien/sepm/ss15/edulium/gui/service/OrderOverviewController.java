package com.at.ac.tuwien.sepm.ss15.edulium.gui.service;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.Order;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.Table;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.User;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ValidationException;
import com.at.ac.tuwien.sepm.ss15.edulium.gui.FXMLPane;
import com.at.ac.tuwien.sepm.ss15.edulium.gui.util.AlertPopOver;
import com.at.ac.tuwien.sepm.ss15.edulium.gui.util.PollingList;
import com.at.ac.tuwien.sepm.ss15.edulium.service.InteriorService;
import com.at.ac.tuwien.sepm.ss15.edulium.service.OrderService;
import com.at.ac.tuwien.sepm.ss15.edulium.service.ServiceException;
import javafx.collections.ListChangeListener;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.controlsfx.control.PopOver;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

@Controller
public class OrderOverviewController implements Initializable {
    private static final Logger LOGGER = LogManager.getLogger(OrderOverviewController.class);

    @FXML
    private ListView<Order> queuedOrdersView;
    @FXML
    private ListView<Order> inProgressOrdersView;
    @FXML
    private ListView<Order> readyForDeliveryOrdersView;
    @FXML
    private ListView<Order> deliveredOrdersView;
    @FXML
    private Button cancelButton;
    @FXML
    private Button deliverButton;
    @FXML
    private Button moveToTableButton;
    @FXML
    private Button backButton;
    @FXML
    private Button newOrderButton;
    @FXML
    private Button clearSelectionButton;
    @FXML
    private Button selectAllButton;
    @FXML
    private Button takeOverButton;
    @FXML
    private Label headerLabel;

    private class OrderCell extends ListCell<Order> {
        private final Label nameLabel;
        private final Label additionalInformationLabel;
        private Order order = null;

        public OrderCell() {
            VBox layout = new VBox();

            nameLabel = new Label();
            nameLabel.setStyle("-fx-font-size: 18px;");

            additionalInformationLabel = new Label();
            additionalInformationLabel.setStyle("-fx-font-size: 14px;");

            layout.getChildren().setAll(nameLabel, additionalInformationLabel);

            setGraphic(layout);
        }

        @Override
        protected void updateItem(Order item, boolean empty) {
            super.updateItem(item, empty);

            order = item;

            if (order != null) {
                nameLabel.setText(order.getMenuEntry().getName());
                additionalInformationLabel.setText(order.getAdditionalInformation());
            } else {
                nameLabel.setText("");
                additionalInformationLabel.setText("");
            }
        }
    }

    @Resource(name = "orderService")
    private OrderService orderService;
    @Resource(name = "interiorService")
    private InteriorService interiorService;
    @Resource(name = "taskScheduler")
    private TaskScheduler taskScheduler;

    @Resource(name = "tableViewPane")
    private FXMLPane tableViewPane; // for move to table pop over
    @Resource(name = "orderInputPane")
    private FXMLPane orderInputPane;

    private AlertPopOver cancelPopOver;
    private PopOver moveToTablePopOver;
    private PopOver newOrderPopOver;

    private PollingList<Order> queuedOrders;
    private PollingList<Order> inProgressOrders;
    private PollingList<Order> readyForDeliveryOrders;
    private PollingList<Order> deliveredOrders;

    private Table table;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeQueuedOrders();
        initializeInProgressOrders();
        initializeReadyForDeliveryOrders();
        initializeDeliveredOrders();

        initializeCancelPopOver();
        initializeMoveToTablePopOver();
        initializeNewOrderPopOver();

        cancelButton.setDisable(true);
        deliverButton.setDisable(true);
    }

    private void initializeQueuedOrders() {
        queuedOrders = new PollingList<>(taskScheduler);
        queuedOrders.setInterval(1000);

        SortedList<Order> sortedQueuedOrders = new SortedList<>(queuedOrders);
        sortedQueuedOrders.setComparator((c1, c2) -> c1.getMenuEntry().getName().compareToIgnoreCase(c2.getMenuEntry().getName()));

        queuedOrdersView.setItems(sortedQueuedOrders);
        queuedOrdersView.setCellFactory(view -> new OrderCell());
        queuedOrdersView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        queuedOrdersView.getSelectionModel().getSelectedItems().addListener((ListChangeListener<Order>) c -> {
            cancelButton.setDisable(c.getList().isEmpty());
        });
    }

    private void initializeInProgressOrders() {
        inProgressOrders = new PollingList<>(taskScheduler);
        inProgressOrders.setInterval(1000);

        SortedList<Order> sortedInProgressOrders = new SortedList<>(inProgressOrders);
        sortedInProgressOrders.setComparator((c1, c2) -> c1.getMenuEntry().getName().compareToIgnoreCase(c2.getMenuEntry().getName()));

        inProgressOrdersView.setItems(sortedInProgressOrders);
        inProgressOrdersView.setCellFactory(view -> new OrderCell());
        inProgressOrdersView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

    private void initializeReadyForDeliveryOrders() {
        readyForDeliveryOrders = new PollingList<>(taskScheduler);
        readyForDeliveryOrders.setInterval(1000);

        SortedList<Order> sortedReadyForDeliveryOrders = new SortedList<>(readyForDeliveryOrders);
        sortedReadyForDeliveryOrders.setComparator((c1, c2) -> c1.getMenuEntry().getName().compareToIgnoreCase(c2.getMenuEntry().getName()));

        readyForDeliveryOrdersView.setItems(sortedReadyForDeliveryOrders);
        readyForDeliveryOrdersView.setCellFactory(view -> new OrderCell());
        readyForDeliveryOrdersView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        readyForDeliveryOrdersView.getSelectionModel().getSelectedItems().addListener((ListChangeListener<Order>) c -> {
            deliverButton.setDisable(c.getList().isEmpty());
        });
    }

    private void initializeDeliveredOrders() {
        deliveredOrders = new PollingList<>(taskScheduler);
        deliveredOrders.setInterval(1000);

        SortedList<Order> sortedDeliveredOrders = new SortedList<>(deliveredOrders);
        sortedDeliveredOrders.setComparator((c1, c2) -> c1.getMenuEntry().getName().compareToIgnoreCase(c2.getMenuEntry().getName()));

        deliveredOrdersView.setItems(sortedDeliveredOrders);
        deliveredOrdersView.setCellFactory(view -> new OrderCell());
        deliveredOrdersView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

    private void initializeCancelPopOver() {
        cancelPopOver = new AlertPopOver();
        cancelPopOver.getLabel().setText("Do you really want to cancel\nthe selected orders?");
        cancelPopOver.getOkButton().setText("Yes");
        cancelPopOver.getCancelButton().setText("No");

        cancelPopOver.getOkButton().setOnAction(action -> {
            try {
                for (Order order : queuedOrdersView.getSelectionModel().getSelectedItems()) {
                    orderService.cancelOrder(order);
                }

                queuedOrdersView.getSelectionModel().clearSelection();
            } catch (ValidationException | ServiceException e) {
                LOGGER.error("Cancel orders did not work", e);

                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Cancel orders failed");
                alert.setContentText(e.toString());

                alert.showAndWait();
            } finally {
                queuedOrders.immediateUpdate();
                cancelPopOver.hide();
            }
        });

        cancelPopOver.getCancelButton().setOnAction(action -> cancelPopOver.hide());
    }

    private void initializeMoveToTablePopOver() {
        TableViewController tableViewController = tableViewPane.getController(TableViewController.class);
        tableViewController.setOnTableClicked(table -> {
            try {
                List<Order> orders = new ArrayList<>(); // TODO maybe replace this by a "merged observable list"
                orders.addAll(queuedOrdersView.getSelectionModel().getSelectedItems());
                orders.addAll(inProgressOrdersView.getSelectionModel().getSelectedItems());
                orders.addAll(readyForDeliveryOrdersView.getSelectionModel().getSelectedItems());
                orders.addAll(deliveredOrdersView.getSelectionModel().getSelectedItems());

                for (Order order : orders) {
                    order.setTable(table);
                    orderService.updateOrder(order);
                }

                clearSelection();
            } catch (ValidationException | ServiceException e) {
                LOGGER.error("Move orders did not work", e);

                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Move orders failed");
                alert.setContentText(e.toString());

                alert.showAndWait();
            } finally {
                queuedOrders.immediateUpdate();
                inProgressOrders.immediateUpdate();
                readyForDeliveryOrders.immediateUpdate();
                deliveredOrders.immediateUpdate();
                moveToTablePopOver.hide();
            }
        });

        tableViewPane.setStyle("-fx-padding: 5px;");

        moveToTablePopOver = new PopOver(tableViewPane);
        moveToTablePopOver.setHideOnEscape(true);
        moveToTablePopOver.setAutoHide(true);
        moveToTablePopOver.setDetachable(false);
        moveToTablePopOver.setMinSize(1200, 700);
        moveToTablePopOver.setArrowLocation(PopOver.ArrowLocation.TOP_RIGHT);
    }

    private void initializeNewOrderPopOver() {
        OrderInputController orderInputController = orderInputPane.getController(OrderInputController.class);
        orderInputController.setOnDone(action -> newOrderPopOver.hide());

        orderInputPane.setStyle("-fx-padding: 5px;");

        newOrderPopOver = new PopOver(orderInputPane);
        newOrderPopOver.setHideOnEscape(true);
        newOrderPopOver.setAutoHide(true);
        newOrderPopOver.setDetachable(false);
        newOrderPopOver.setMinSize(1200, 700);
        newOrderPopOver.setArrowLocation(PopOver.ArrowLocation.TOP_RIGHT);
    }

    @FXML
    public void onCancelButtonClicked(ActionEvent actionEvent) {
        if (cancelPopOver.isShowing()) {
            cancelPopOver.hide();
        } else {
            cancelPopOver.show(cancelButton);
        }
    }

    @FXML
    public void onDeliverButtonClicked(ActionEvent actionEvent) {
        try {
            for (Order order : readyForDeliveryOrdersView.getSelectionModel().getSelectedItems()) {
                orderService.markAsDelivered(order);
            }

            readyForDeliveryOrdersView.getSelectionModel().clearSelection();
        } catch (ValidationException | ServiceException e) {
            LOGGER.error("Putting State to Delivered did not work", e);

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Putting State to Delivered");
            alert.setHeaderText("The State shifting did not work");
            alert.setContentText(e.toString());

            alert.showAndWait();
        } finally {
            readyForDeliveryOrders.immediateUpdate();
            deliveredOrders.immediateUpdate();
        }
    }

    @FXML
    public void onMoveToTableButtonClicked(ActionEvent actionEvent) {
        if (moveToTablePopOver.isShowing()) {
            moveToTablePopOver.hide();
        } else if (!queuedOrdersView.getSelectionModel().getSelectedItems().isEmpty() ||
                   !inProgressOrdersView.getSelectionModel().getSelectedItems().isEmpty() ||
                   !readyForDeliveryOrdersView.getSelectionModel().getSelectedItems().isEmpty() ||
                   !deliveredOrdersView.getSelectionModel().getSelectedItems().isEmpty()) {
            moveToTablePopOver.show(moveToTableButton);
        }
    }

    @FXML
    public void onNewOrderButtonClicked(ActionEvent actionEvent) {
        if (newOrderPopOver.isShowing()) {
            newOrderPopOver.hide();
        } else {
            newOrderPopOver.show(newOrderButton);
        }
    }

    @FXML
    public void onClearSelectionButtonClicked(ActionEvent actionEvent) {
        clearSelection();
    }

    @FXML
    public void onSelectAllButtonClicked(ActionEvent actionEvent) {
        queuedOrdersView.getSelectionModel().selectAll();
        inProgressOrdersView.getSelectionModel().selectAll();
        readyForDeliveryOrdersView.getSelectionModel().selectAll();
        deliveredOrdersView.getSelectionModel().selectAll();
    }

    @FXML
    public void onTakeOverButtonButtonClicked(ActionEvent actionEvent) {
        assert table != null;

        User user = getLoggedInUser();
        if (user != null) {
            table.setUser(user);

            try {
                interiorService.updateTable(table);
                takeOverButton.setVisible(false);
            } catch (ValidationException | ServiceException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error while setting the responsible waiter for the table");
                alert.setHeaderText("Could not set '" + user.getName() + "' as responsible waiter for table " +
                        table.getNumber() + " in " + table.getSection().getName());
                alert.setContentText(e.getMessage());
                alert.showAndWait();
            }
        }
    }

    private void clearSelection() {
        queuedOrdersView.getSelectionModel().clearSelection();
        inProgressOrdersView.getSelectionModel().clearSelection();
        readyForDeliveryOrdersView.getSelectionModel().clearSelection();
        deliveredOrdersView.getSelectionModel().clearSelection();
    }

    private User getLoggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return null;
        }

        return (User) authentication.getPrincipal();
    }

    /**
     *
     * @param table (must not be null)
     */
    public void setTable(Table table) {
        assert table != null;

        this.table = table;

        headerLabel.setText("Table " + table.getNumber() + " in " + table.getSection().getName());
        takeOverButton.setVisible(!table.getUser().equals(getLoggedInUser())); // only visible if the waiter isn't already responsible for the table

        queuedOrders.setSupplier(() -> {
            try {
                Order matcher = new Order();
                matcher.setTable(table);
                matcher.setState(Order.State.QUEUED);
                return orderService.findOrder(matcher);
            } catch (ServiceException e) {
                LOGGER.error("Finding orders via order supplier has failed", e);
                return null;
            }
        });
        queuedOrders.startPolling();

        inProgressOrders.setSupplier(() -> {
            try {
                Order matcher = new Order();
                matcher.setTable(table);
                matcher.setState(Order.State.IN_PROGRESS);
                return orderService.findOrder(matcher);
            } catch (ServiceException e) {
                LOGGER.error("Finding orders via order supplier has failed", e);
                return null;
            }
        });
        inProgressOrders.startPolling();

        readyForDeliveryOrders.setSupplier(() -> {
            try {
                Order matcher = new Order();
                matcher.setTable(table);
                matcher.setState(Order.State.READY_FOR_DELIVERY);
                return orderService.findOrder(matcher);
            } catch (ServiceException e) {
                LOGGER.error("Finding orders via order supplier has failed", e);
                return null;
            }
        });
        readyForDeliveryOrders.startPolling();

        deliveredOrders.setSupplier(() -> {
            try {
                Order matcher = new Order();
                matcher.setTable(table);
                matcher.setState(Order.State.DELIVERED);
                return orderService.findOrder(matcher);
            } catch (ServiceException e) {
                LOGGER.error("Finding orders via order supplier has failed", e);
                return null;
            }
        });
        deliveredOrders.startPolling();

        OrderInputController orderInputController = orderInputPane.getController(OrderInputController.class);
        orderInputController.setTable(table);
    }

    public void setOnBackButtonAction(EventHandler<ActionEvent> event) {
        backButton.setOnAction(event);
    }
}
