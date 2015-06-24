package com.at.ac.tuwien.sepm.ss15.edulium.gui.service;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.Invoice;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.Order;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.Table;
import com.at.ac.tuwien.sepm.ss15.edulium.gui.util.PollingList;
import com.at.ac.tuwien.sepm.ss15.edulium.service.InvoiceService;
import com.at.ac.tuwien.sepm.ss15.edulium.service.OrderService;
import com.at.ac.tuwien.sepm.ss15.edulium.service.ServiceException;
import javafx.collections.*;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class InvoiceViewController  implements Initializable {
    private static final Logger LOGGER = LogManager.getLogger(InvoiceViewController.class);

    @FXML
    private ListView<Order> ordersView;
    @FXML
    private ListView<Invoice> invoiceView;
    @FXML
    private Button createInvoiceButton;
    @FXML
    private Label headerLabel;
    @FXML
    private HBox headerLayout;

    private class OrderCell extends ListCell<Order> {
        private final Label nameLabel;
        private final Label additionalInformationLabel;
        private final Label amountLabel;
        private final Label availableLabel;
        private final Button increaseAmountButton;
        private final Button decreaseAmountButton;
        private final HBox layout;
        private Order order = null;

        public OrderCell() {
            nameLabel = new Label();

            additionalInformationLabel = new Label();
            additionalInformationLabel.setStyle("-fx-font-size: 14px;");

            VBox labelLayout = new VBox();
            labelLayout.getChildren().setAll(nameLabel, additionalInformationLabel);

            amountLabel = new Label();

            availableLabel = new Label();
            availableLabel.setStyle("-fx-font-size: 14px;");

            VBox amountLayout = new VBox();
            amountLayout.setAlignment(Pos.CENTER_RIGHT);
            amountLayout.getChildren().setAll(amountLabel, availableLabel);

            increaseAmountButton = new Button();
            increaseAmountButton.setText("+");
            increaseAmountButton.setMinSize(40, 40);
            increaseAmountButton.setOnAction(action -> ordersForInvoice.computeIfPresent(order, (key, amount) -> amount + 1));

            decreaseAmountButton = new Button();
            decreaseAmountButton.setText("-");
            decreaseAmountButton.setMinSize(40, 40);
            decreaseAmountButton.setOnAction(action -> ordersForInvoice.computeIfPresent(order, (key, amount) -> amount - 1));

            layout = new HBox();
            layout.setSpacing(5);
            layout.getChildren().setAll(increaseAmountButton, decreaseAmountButton, amountLayout, labelLayout);

            setGraphic(layout);
        }

        @Override
        protected void updateItem(Order item, boolean empty) {
            super.updateItem(item, empty);

            order = item;

            if (order != null) {
                Integer amount = ordersForInvoice.get(order);
                Integer maximum = orders.get(order);

                assert amount != null;
                assert maximum != null;

                Integer available = maximum - amount;

                increaseAmountButton.setDisable(available.equals(0));
                decreaseAmountButton.setDisable(amount.equals(0));

                nameLabel.setText(order.getMenuEntry().getName());
                additionalInformationLabel.setText(order.getAdditionalInformation());
                amountLabel.setText(amount.toString());
                availableLabel.setText("(" + available.toString() + ")");

                layout.setVisible(true);
            } else {
                layout.setVisible(false);
            }
        }
    }

    @Resource(name = "orderService")
    private OrderService orderService;
    @Resource(name = "invoiceService")
    private InvoiceService invoiceService;
    @Resource(name = "taskScheduler")
    private TaskScheduler taskScheduler;

    private PollingList<Order> allOrders;

    private Table table;

    private final Map<Order, Integer> orders = new HashMap<>();
    private final ObservableMap<Order, Integer> ordersForInvoice = FXCollections.observableHashMap();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeAllOrders();
        initializeOrdersView();

        reset();
    }

    private void initializeAllOrders() {
        allOrders = new PollingList<>(taskScheduler);
        allOrders.setInterval(1000);
        allOrders.addListener((ListChangeListener<Order>) change -> {
            while (change.next()) {
                for (Order order : cloneOrders(change.getRemoved())) {
                    Integer count = orders.get(order);
                    if (count == null) {
                        continue;
                    } else if (count == 1) {
                        orders.remove(order);
                        ordersForInvoice.remove(order);
                    } else {
                        orders.put(order, count - 1);
                        ordersForInvoice.compute(order, (key, value) -> (value >= count ? count - 1 : value));
                    }
                }
                for (Order order : cloneOrders(change.getAddedSubList())) {
                    Integer count = orders.get(order);
                    if (count == null) {
                        orders.put(order, 1);
                        ordersForInvoice.put(order, 0);
                    } else {
                        orders.put(order, count + 1);
                    }
                }
            }
        });
    }

    private List<Order> cloneOrders(List<? extends Order> orderList) {
        List<Order> cloned = new ArrayList<>();
        for (Order o : orderList) {
            Order order = new Order();
            order.setBrutto(o.getBrutto());
            order.setAdditionalInformation(o.getAdditionalInformation());
            order.setTax(o.getTax());
            order.setMenuEntry(o.getMenuEntry());

            cloned.add(order);
        }

        return cloned;
    }

    private void initializeOrdersView() {
        // put all keys of the orders map into a list so that we can use it in the list view
        ObservableList<Order> displayedOrders = FXCollections.observableArrayList();
        ordersForInvoice.addListener((MapChangeListener<Order, Integer>) change -> {
            displayedOrders.removeAll(change.getKey());
            if (change.wasAdded()) {
                displayedOrders.add(change.getKey());
            }
        });

        // sort the displayed orders by name
        SortedList<Order> sortedDisplayedOrders = new SortedList<>(displayedOrders);
        sortedDisplayedOrders.setComparator((o1, o2) -> {
            int result = o1.getMenuEntry().getName().compareToIgnoreCase(o2.getMenuEntry().getName());
            if (result == 0) {
                result = o1.getAdditionalInformation().compareToIgnoreCase(o2.getAdditionalInformation());
            }
            return result;
        });

        ordersView.setCellFactory(view -> new OrderCell());
        ordersView.setItems(sortedDisplayedOrders);
        ordersView.setStyle("-fx-font-size: 18px;");
    }

    public void setTable(Table table) {
        this.table = table;

        allOrders.setSupplier(() -> {
            try {
                Order orderMatcher = new Order();
                orderMatcher.setTable(table);
                return orderService.findOrder(orderMatcher).stream().filter(order -> {
                                        // show only orders which don't belong to an invoice yet
                                        try {
                                            Invoice invoiceMatcher = new Invoice();
                                            invoiceMatcher.setOrders(Collections.singletonList(order));
                                            return invoiceService.findInvoices(invoiceMatcher).isEmpty();
                                        } catch (ServiceException e) {
                                            return true;
                                        }
                                    }).collect(Collectors.toList());
            } catch (ServiceException e) {
                return null;
            }
        });
        allOrders.startPolling();

        reset();
    }

    private void reset() {
        ordersForInvoice.clear();
        orders.clear();
    }
}
