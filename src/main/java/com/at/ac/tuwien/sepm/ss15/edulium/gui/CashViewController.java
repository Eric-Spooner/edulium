package com.at.ac.tuwien.sepm.ss15.edulium.gui;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.Order;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.Table;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ValidationException;
import com.at.ac.tuwien.sepm.ss15.edulium.service.MenuService;
import com.at.ac.tuwien.sepm.ss15.edulium.service.OrderService;
import com.at.ac.tuwien.sepm.ss15.edulium.service.ServiceException;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.TaskScheduler;

import java.math.BigDecimal;
import java.net.URL;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Supplier;

/**
 * Controller used for the Cook View
 */
public class CashViewController implements Initializable, Controller {
    private static final Logger LOGGER = LogManager.getLogger(CashViewController.class);

    @FXML
    private TableView<Order> tableViewOrders;
    @FXML
    private TableColumn<Order, String> tableColOrdersName;
    @FXML
    private TableColumn<Order, String> tableColOrdersPrice;

    @FXML
    private TableView<Order> tableViewInvoice;
    @FXML
    private TableColumn<Order, String> tableColInvoiceName;
    @FXML
    private TableColumn<Order, String> tableColInvoicePrice;

    @FXML
    private Text ordersLb;
    @FXML
    private Label totalLb;

    @Autowired
    private MenuService menuService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private TaskScheduler taskScheduler;

    private static Table selectedTable = null;

    private PollingList<Order> orders;
    private PollingList<Order> invoiceItems;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            Order matcher = new Order();
            //matcher.setState(Order.State.DELIVERED); TODO set
            matcher.setTable(selectedTable);
            ArrayList<Order> orderList = new ArrayList<>();
            for(Order order : orderService.findOrder(matcher)) {
                orderList.add(order);
            }
            tableViewOrders.setItems(FXCollections.observableArrayList(orderList));
        } catch (ServiceException e) {
            LOGGER.error("Could not retrieve orders", e);
        }

        ordersLb.setText(ordersLb.getText() + " TABLE " + selectedTable.getNumber());
        totalLb.setText(" 0.00");

        tableViewInvoice.setStyle("-fx-font-size: 18px;");
        tableViewOrders.setStyle("-fx-font-size: 18px;");

        tableViewOrders.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        tableColOrdersName.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getMenuEntry().getName()));
        tableColOrdersPrice.setCellValueFactory(p -> new SimpleStringProperty(String.valueOf(p.getValue().getMenuEntry().getPrice())));

        tableViewInvoice.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        tableColInvoiceName.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getMenuEntry().getName()));
        tableColInvoicePrice.setCellValueFactory(p -> new SimpleStringProperty(String.valueOf(p.getValue().getMenuEntry().getPrice())));
    }

    public void btnToInvoiceClicked(ActionEvent actionEvent) {
        LOGGER.info("Entered btnToInvoiceClicked");

        if (tableViewOrders.getSelectionModel().getSelectedItems() != null) {
            ArrayList<Order> orderList = new ArrayList<>();
            ArrayList<Order> removeList = new ArrayList<>();
            removeList.addAll(tableViewOrders.getItems());
            for (Order order : tableViewOrders.getSelectionModel().getSelectedItems()) {
                orderList.add(order);
                removeList.remove(order);
            }
            orderList.addAll(tableViewInvoice.getItems());
            tableViewInvoice.setItems(FXCollections.observableArrayList(orderList));
            tableViewOrders.setItems(FXCollections.observableArrayList(removeList));
        }
        setTotalPrice();
    }

    public void btnAllToInvoiceClicked(ActionEvent event) {
        LOGGER.info("Entered btnAllToInvoiceClicked");

        ArrayList<Order> orderList = new ArrayList<>();
        ArrayList<Order> removeList = new ArrayList<>();
        removeList.clear();
        orderList.addAll(tableViewOrders.getItems());
        orderList.addAll(tableViewInvoice.getItems());
        tableViewInvoice.setItems(FXCollections.observableArrayList(orderList));
        tableViewOrders.setItems(FXCollections.observableArrayList(removeList));
        setTotalPrice();
    }

    public void btnToOrdersClicked(ActionEvent actionEvent) {
        LOGGER.info("Entered btnToOrdersClicked");

        if (tableViewOrders.getSelectionModel().getSelectedItems() != null) {
            ArrayList<Order> orderList = new ArrayList<>();
            ArrayList<Order> removeList = new ArrayList<>();
            removeList.addAll(tableViewInvoice.getItems());
            for (Order order : tableViewInvoice.getSelectionModel().getSelectedItems()) {
                orderList.add(order);
                removeList.remove(order);
            }
            orderList.addAll(tableViewOrders.getItems());
            tableViewOrders.setItems(FXCollections.observableArrayList(orderList));
            tableViewInvoice.setItems(FXCollections.observableArrayList(removeList));
        }
        setTotalPrice();
    }

    public void btnOKClicked(ActionEvent event) {
        LOGGER.info("Entered btnOKClicked");
    }

    public static void setSelectedTable(Table table) {
        CashViewController.selectedTable = table;
    }

    private void setTotalPrice() {
        BigDecimal total = new BigDecimal(0);

        for (Order order : tableViewInvoice.getItems()) {
            total = total.add(order.getMenuEntry().getPrice());
        }

        if(total.equals(0))
            totalLb.setText(" 0.00");
        else
            totalLb.setText(" "+String.valueOf(total));
    }

    @Override
    public void disable(boolean disabled) {
    }
}
