package com.at.ac.tuwien.sepm.ss15.edulium.gui;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.*;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ValidationException;
import com.at.ac.tuwien.sepm.ss15.edulium.service.*;
import javafx.collections.ListChangeListener;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.controlsfx.control.PopOver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.net.URL;
import java.util.*;
import java.util.function.Supplier;

@Component
public class OrderOverviewController implements Initializable, Controller {
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

    @Autowired
    private OrderService orderService;
    @Autowired
    private TaskScheduler taskScheduler;

    @Resource(name = "tableViewPane")
    private FXMLPane tableViewPane; // for move to table pop over
    @Resource(name = "orderInputPane")
    private FXMLPane orderInputPane;

    private AlertPopOver cancelPopOver;
    private PopOver moveToTablePopOver;
    private PopOver newOrderPopOver;

    private PollingList<Order> queuedOrders;
    private SortedList<Order> sortedQueuedOrders;

    private PollingList<Order> inProgressOrders;
    private SortedList<Order> sortedInProgressOrders;

    private PollingList<Order> readyForDeliveryOrders;
    private SortedList<Order> sortedReadyForDeliveryOrders;

    private PollingList<Order> deliveredOrders;
    private SortedList<Order> sortedDeliveredOrders;

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

        sortedQueuedOrders = new SortedList<>(queuedOrders);
        sortedQueuedOrders.setComparator((c1, c2) -> c1.getMenuEntry().getName().compareToIgnoreCase(c2.getMenuEntry().getName()));

        queuedOrdersView.setItems(sortedQueuedOrders);
        queuedOrdersView.setCellFactory(view -> new OrderCell());
        queuedOrdersView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        queuedOrdersView.getSelectionModel().getSelectedItems().addListener(new ListChangeListener<Order>() {
            @Override
            public void onChanged(Change<? extends Order> c) {
                cancelButton.setDisable(c.getList().isEmpty());
            }
        });
    }

    private void initializeInProgressOrders() {
        inProgressOrders = new PollingList<>(taskScheduler);
        inProgressOrders.setInterval(1000);

        sortedInProgressOrders = new SortedList<>(inProgressOrders);
        sortedInProgressOrders.setComparator((c1, c2) -> c1.getMenuEntry().getName().compareToIgnoreCase(c2.getMenuEntry().getName()));

        inProgressOrdersView.setItems(sortedInProgressOrders);
        inProgressOrdersView.setCellFactory(view -> new OrderCell());
        inProgressOrdersView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

    private void initializeReadyForDeliveryOrders() {
        readyForDeliveryOrders = new PollingList<>(taskScheduler);
        readyForDeliveryOrders.setInterval(1000);

        sortedReadyForDeliveryOrders = new SortedList<>(readyForDeliveryOrders);
        sortedReadyForDeliveryOrders.setComparator((c1, c2) -> c1.getMenuEntry().getName().compareToIgnoreCase(c2.getMenuEntry().getName()));

        readyForDeliveryOrdersView.setItems(sortedReadyForDeliveryOrders);
        readyForDeliveryOrdersView.setCellFactory(view -> new OrderCell());
        readyForDeliveryOrdersView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        readyForDeliveryOrdersView.getSelectionModel().getSelectedItems().addListener(new ListChangeListener<Order>() {
            @Override
            public void onChanged(Change<? extends Order> c) {
                deliverButton.setDisable(c.getList().isEmpty());
            }
        });
    }

    private void initializeDeliveredOrders() {
        deliveredOrders = new PollingList<>(taskScheduler);
        deliveredOrders.setInterval(1000);

        sortedDeliveredOrders = new SortedList<>(deliveredOrders);
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
                List<Order> orders = new ArrayList<Order>(); // TODO maybe replace this by a "merged observable list"
                orders.addAll(queuedOrdersView.getSelectionModel().getSelectedItems());
                orders.addAll(inProgressOrdersView.getSelectionModel().getSelectedItems());
                orders.addAll(readyForDeliveryOrdersView.getSelectionModel().getSelectedItems());
                orders.addAll(deliveredOrdersView.getSelectionModel().getSelectedItems());

                for (Order order : orders) {
                    order.setTable(table);
                    orderService.updateOrder(order);
                }
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
        queuedOrdersView.getSelectionModel().clearSelection();
        inProgressOrdersView.getSelectionModel().clearSelection();
        readyForDeliveryOrdersView.getSelectionModel().clearSelection();
        deliveredOrdersView.getSelectionModel().clearSelection();
    }

    /**
     *
     * @param table (must not be null)
     */
    public void setTable(Table table)
    {
        assert table != null;

        headerLabel.setText("Table " + table.getNumber() + " in " + table.getSection().getName());

        queuedOrders.setSupplier(new Supplier<List<Order>>() {
            @Override
            public List<Order> get() {
                try {
                    Order matcher = new Order();
                    matcher.setTable(table);
                    matcher.setState(Order.State.QUEUED);
                    return orderService.findOrder(matcher);
                } catch (ServiceException e) {
                    LOGGER.error("Finding orders via order supplier has failed", e);
                    return null;
                }
            }
        });

        inProgressOrders.setSupplier(new Supplier<List<Order>>() {
            @Override
            public List<Order> get() {
                try {
                    Order matcher = new Order();
                    matcher.setTable(table);
                    matcher.setState(Order.State.IN_PROGRESS);
                    return orderService.findOrder(matcher);
                } catch (ServiceException e) {
                    LOGGER.error("Finding orders via order supplier has failed", e);
                    return null;
                }
            }
        });

        readyForDeliveryOrders.setSupplier(new Supplier<List<Order>>() {
            @Override
            public List<Order> get() {
                try {
                    Order matcher = new Order();
                    matcher.setTable(table);
                    matcher.setState(Order.State.READY_FOR_DELIVERY);
                    return orderService.findOrder(matcher);
                } catch (ServiceException e) {
                    LOGGER.error("Finding orders via order supplier has failed", e);
                    return null;
                }
            }
        });

        deliveredOrders.setSupplier(new Supplier<List<Order>>() {
            @Override
            public List<Order> get() {
                try {
                    Order matcher = new Order();
                    matcher.setTable(table);
                    matcher.setState(Order.State.DELIVERED);
                    return orderService.findOrder(matcher);
                } catch (ServiceException e) {
                    LOGGER.error("Finding orders via order supplier has failed", e);
                    return null;
                }
            }
        });

        OrderInputController orderInputController = orderInputPane.getController(OrderInputController.class);
        orderInputController.setTable(table);
    }

    public void setOnBackButtonAction(EventHandler<ActionEvent> event) {
        backButton.setOnAction(event);
    }

    @Override
    public void disable(boolean disabled) {
        if (disabled) {
            queuedOrders.stopPolling();
            inProgressOrders.stopPolling();
            readyForDeliveryOrders.stopPolling();
            deliveredOrders.stopPolling();
        } else {
            queuedOrders.startPolling();
            inProgressOrders.startPolling();
            readyForDeliveryOrders.startPolling();
            deliveredOrders.startPolling();
        }
    }
}
