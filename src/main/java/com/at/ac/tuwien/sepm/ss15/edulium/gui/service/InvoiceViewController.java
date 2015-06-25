package com.at.ac.tuwien.sepm.ss15.edulium.gui.service;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.*;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ValidationException;
import com.at.ac.tuwien.sepm.ss15.edulium.gui.util.PollingList;
import com.at.ac.tuwien.sepm.ss15.edulium.service.InvoiceManager;
import com.at.ac.tuwien.sepm.ss15.edulium.service.InvoiceService;
import com.at.ac.tuwien.sepm.ss15.edulium.service.OrderService;
import com.at.ac.tuwien.sepm.ss15.edulium.service.ServiceException;
import javafx.collections.*;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.controlsfx.control.action.Action;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import java.beans.EventHandler;
import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDateTime;
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

    @Resource(name = "taskScheduler")
    private TaskScheduler taskScheduler;
    @Resource(name = "invoiceManager")
    private InvoiceManager invoiceManager;
    @Resource(name = "invoiceService")
    private InvoiceService invoiceService;
    @Resource(name = "orderService")
    private OrderService orderService;

    private PollingList<Order> allOrders;
    private PollingList<Invoice> allInvoices;

    private class OrderCell extends ListCell<Order> {
        private final Label nameLabel;
        private final Label additionalInformationLabel;
        private final Label amountLabel;
        private final Label availableLabel;
        private final Button increaseAmountButton;
        private final Button decreaseAmountButton;
        private final HBox layout;
        private Order group = null;

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
            increaseAmountButton.setOnAction(action -> ordersForInvoice.computeIfPresent(group, (key, amount) -> amount + 1));

            decreaseAmountButton = new Button();
            decreaseAmountButton.setText("-");
            decreaseAmountButton.setMinSize(40, 40);
            decreaseAmountButton.setOnAction(action -> ordersForInvoice.computeIfPresent(group, (key, amount) -> amount - 1));

            layout = new HBox();
            layout.setSpacing(5);
            layout.getChildren().setAll(increaseAmountButton, decreaseAmountButton, amountLayout, labelLayout);

            setGraphic(layout);
        }

        @Override
        protected void updateItem(Order item, boolean empty) {
            super.updateItem(item, empty);

            group = item;

            Set<Order> ordersInGroup = orders.get(group);
            Integer amount = ordersForInvoice.get(group);

            if (group != null && ordersInGroup != null & amount != null) {
                Integer available = ordersInGroup.size() - amount;

                increaseAmountButton.setDisable(available.equals(0));
                decreaseAmountButton.setDisable(amount.equals(0));

                nameLabel.setText(group.getMenuEntry().getName());
                additionalInformationLabel.setText(group.getAdditionalInformation());
                amountLabel.setText(amount.toString());
                availableLabel.setText("(" + available.toString() + ")");

                layout.setVisible(true);
            } else {
                layout.setVisible(false);
            }
        }
    }

    private class InvoiceCell extends ListCell<Invoice> {
        private final Label nameLabel;
        private final Label grossLabel;
        private final Label paidLabel;
        private final Button printButton;
        private final HBox layout;
        private Invoice tempInvoice;

        public InvoiceCell() {
            nameLabel = new Label();
            grossLabel = new Label();
            paidLabel = new Label();
            tempInvoice = new Invoice();
            printButton = new Button();
            printButton.setStyle("-fx-font-size: 18px");
            printButton.setText("Print");
            printButton.setOnAction(event -> {
                manageInvoice(tempInvoice);
            });

            layout = new HBox(10);
            layout.getChildren().setAll(nameLabel, grossLabel, paidLabel, printButton);

            setGraphic(layout);
        }

        @Override
        protected void updateItem(Invoice item, boolean empty) {
            super.updateItem(item, empty);

            if (item != null) {
                nameLabel.setText("ID: " + item.getIdentity());
                grossLabel.setText("Total: " + item.getGross());
                tempInvoice = item;

                Instalment matcher = new Instalment();
                matcher.setInvoice(item);
                List<Instalment> instalments = null;
                try {
                    instalments = invoiceService.findInstalments(matcher);
                } catch (ServiceException e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Could not retrieve instalments");
                    alert.setHeaderText("Instalments could not be retrieved");
                    alert.setContentText(e.getMessage());

                    alert.showAndWait();
                    return;
                }
                BigDecimal paidAmount = BigDecimal.ZERO;
                for (Instalment i : instalments) {
                    paidAmount = paidAmount.add(i.getAmount());
                }

                paidLabel.setText("Paid: " + paidAmount);

                layout.setVisible(true);
            } else {
                layout.setVisible(false);
            }
        }
    }

    private Table table;

    private final Map<Order, Set<Order>> orders = new HashMap<>();
    private final ObservableMap<Order, Integer> ordersForInvoice = FXCollections.observableHashMap();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeAllOrders();
        initializeOrdersView();
        initializeInvoiceView();

        reset();
    }

    private void initializeInvoiceView() {
        allInvoices = new PollingList<>(taskScheduler);
        allInvoices.setInterval(1000);

//        SortedList<Invoice> sortedInvoices = new SortedList<Invoice>(allInvoices);
        invoiceView.setCellFactory(view -> new InvoiceCell());
        invoiceView.setItems(allInvoices);
        invoiceView.setStyle("-fx-font-size: 18px;");
    }

    @FXML
    public void onCashButtonClicked() {
        Instalment instalment = new Instalment();
        instalment.setTime(LocalDateTime.now());
        instalment.setAmount(new BigDecimal("5")); // TODO: get value from text field
        instalment.setType("CASH");
        instalment.setPaymentInfo("Paid in cash");
        instalment.setInvoice(null); // TODO get selected invoice
        addInstalment(instalment);
    }

    @FXML
    public void onCreditButtonClicked() {
        Instalment instalment = new Instalment();
        instalment.setTime(LocalDateTime.now());
        instalment.setAmount(new BigDecimal("5")); // TODO: get value from text field
        instalment.setType("CREDIT");
        instalment.setPaymentInfo("Paid by credit card");
        instalment.setInvoice(null); // TODO get selected invoice
        addInstalment(instalment);
    }

    @FXML
    public void onDebitButtonClicked() {
        Instalment instalment = new Instalment();
        instalment.setTime(LocalDateTime.now());
        instalment.setAmount(new BigDecimal("5")); // TODO: get value from text field
        instalment.setType("DEBIT");
        instalment.setPaymentInfo("Paid by debit card");
        instalment.setInvoice(null); // TODO get selected invoice
        addInstalment(instalment);
    }

    @FXML
    public void onCreateInvoiceButtonClicked() {
        Invoice invoice = new Invoice();
        invoice.setOrders(getSelectedOrdersForInvoice());
        invoice.setTime(LocalDateTime.now());
        invoice.setCreator(getLoggedInUser());

        try {
            invoiceService.addInvoice(invoice);
        } catch (ServiceException e) {
            LOGGER.error("An error occurred while trying to create the invoice", e);
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Failed to create the invoice");
            alert.setHeaderText("An error occurred while trying to create the invoice");
            alert.setContentText(e.getMessage());

            alert.showAndWait();
            return;
        } catch (ValidationException e) {
            LOGGER.error("Validation of the invoice failed", e);
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invoice validation failed");
            alert.setHeaderText("The information provided for the invoice is incomplete " +
                    "or violates the invoice validation");
            alert.setContentText(e.getMessage());

            alert.showAndWait();
            return;
        }

        manageInvoice(invoice);

        allOrders.immediateUpdate();
        allInvoices.immediateUpdate();
    }

    private void manageInvoice(Invoice invoice) {
        try {
            invoiceManager.manageInvoice(invoice);
        } catch (ServiceException e) {
            LOGGER.error("An error occurred while trying to manage the invoice", e);
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invoice manager failure");
            alert.setHeaderText("The invoice manager failed unexpectedly");
            alert.setContentText(e.getMessage());

            alert.showAndWait();
        }
    }

    private void addInstalment(Instalment instalment) {
        try {
            invoiceService.addInstalment(instalment);
        } catch (ServiceException e) {
            LOGGER.error("An error occurred while trying to create the instalment");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Failed to create the instalment");
            alert.setHeaderText("An error occurred while trying to create the instalment");
            alert.setContentText(e.getMessage());

            alert.showAndWait();
        } catch (ValidationException e) {
            LOGGER.error("Validation of the instalment failed", e);
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Instalment validation failed");
            alert.setHeaderText("The information provided for the instalment is incomplete " +
                    "or violates the instalment validation");
            alert.setContentText(e.getMessage());

            alert.showAndWait();
        }
    }

    private User getLoggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return null;
        }

        return (User) authentication.getPrincipal();
    }

    private void initializeAllOrders() {
        allOrders = new PollingList<>(taskScheduler);
        allOrders.setInterval(1000);
        allOrders.addListener((ListChangeListener<Order>) change -> {
            while (change.next()) {
                for (Order order : change.getRemoved()) {
                    Order group = stripDownOrder(order);

                    Set<Order> ordersInGroup = orders.get(group);
                    if (ordersInGroup == null) {
                        continue;
                    } else if (ordersInGroup.size() == 1) {
                        orders.remove(group);
                        ordersForInvoice.remove(group);
                    } else {
                        ordersInGroup.remove(order);
                        orders.put(group, ordersInGroup);

                        ordersForInvoice.compute(group, (key, value) -> value > ordersInGroup.size() ? ordersInGroup.size() : value);
                    }
                }
                for (Order order : change.getAddedSubList()) {
                    Order group = stripDownOrder(order);

                    Set<Order> ordersInGroup = orders.get(group);
                    if (ordersInGroup == null) {
                        ordersInGroup = new HashSet<>();
                        ordersForInvoice.put(group, 0);
                    }

                    ordersInGroup.add(order);
                    orders.put(group, ordersInGroup);
                }
            }
        });
    }

    private Order stripDownOrder(Order o) {
        Order order = new Order();
        order.setBrutto(o.getBrutto());
        order.setAdditionalInformation(o.getAdditionalInformation());
        order.setTax(o.getTax());
        order.setMenuEntry(o.getMenuEntry());
        return order;
    }

    private List<Order> getSelectedOrdersForInvoice() {
        List<Order> selectedOrders = new ArrayList<>();

        for (Map.Entry<Order, Integer> entry : ordersForInvoice.entrySet()) {
            Order group = entry.getKey();
            Integer amount = entry.getValue();

            // get as many orders from the current "order group" as the user has selected
            Iterator<Order> it = orders.get(group).iterator();
            for (int i = 0; i < amount && it.hasNext(); i++) {
                selectedOrders.add(it.next());
            }
        }

        return selectedOrders;
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

        allOrders.stopPolling();
        allInvoices.stopPolling();

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

        allInvoices.setSupplier(() -> {
            try {
                Order orderMatcher = new Order();
                orderMatcher.setTable(table);
                List<Order> orderListMatcher = orderService.findOrder(orderMatcher);
                Invoice invoiceMatcher = new Invoice();
                invoiceMatcher.setClosed(false);
                invoiceMatcher.setOrders(orderListMatcher);
                return invoiceService.findInvoices(invoiceMatcher);
            } catch (ServiceException e) {
                return null;
            }
        });

        reset();

        allOrders.startPolling();
        allInvoices.startPolling();
    }

    private void reset() {
        ordersForInvoice.clear();
        orders.clear();
    }
}
