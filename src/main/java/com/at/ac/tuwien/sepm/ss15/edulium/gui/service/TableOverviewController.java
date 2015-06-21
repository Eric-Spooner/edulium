package com.at.ac.tuwien.sepm.ss15.edulium.gui.service;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.Order;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.Table;
import com.at.ac.tuwien.sepm.ss15.edulium.gui.Controller;
import com.at.ac.tuwien.sepm.ss15.edulium.gui.FXMLPane;
import com.at.ac.tuwien.sepm.ss15.edulium.gui.util.PollingList;
import com.at.ac.tuwien.sepm.ss15.edulium.service.OrderService;
import com.at.ac.tuwien.sepm.ss15.edulium.service.ServiceException;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;

import javax.annotation.Resource;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

public class TableOverviewController implements Initializable, Controller {
    private static final Logger LOGGER = LogManager.getLogger(TableOverviewController.class);

    @FXML
    private SplitPane splitPane;
    @FXML
    private ListView<Table> lvDelivery;
    @FXML
    private Button btnReservation;

    @Resource(name = "tableViewPane")
    private FXMLPane tableViewPane;

    @Autowired
    private TaskScheduler taskScheduler;
    @Autowired
    private OrderService orderService;

    private TableViewController tableViewController;
    private PollingList<Order> ordersReadyForDelivery;
    private ObservableList<Table> tablesOfOrders;

    private Consumer<Table> onTableClickedConsumer;

    private class DeliveryCell extends ListCell<Table> {
        @Override
        protected void updateItem(Table item, boolean empty) {
            super.updateItem(item, empty);

            if(empty) {
                setGraphic(null);
            }

            if(item != null) {
                setText(item.getSection().getName() + "\nTable: " + item.getNumber());
                setFont(new Font(25.0));
            }
        }
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        tableViewController = tableViewPane.getController(TableViewController.class);
        splitPane.getItems().add(0, tableViewPane);

        tablesOfOrders = FXCollections.observableArrayList();

        lvDelivery.setCellFactory(param -> new DeliveryCell());
        lvDelivery.setItems(tablesOfOrders);

        ordersReadyForDelivery = new PollingList<>(taskScheduler);
        ordersReadyForDelivery.setInterval(1000);
        ordersReadyForDelivery.addListener(new ListChangeListener<Order>() {
            @Override
            public void onChanged(Change<? extends Order> c) {
                while(c.next()) {
                    for(Order o : c.getRemoved()) {
                        tablesOfOrders.remove(o.getTable());
                    }
                    for(Order o : c.getAddedSubList()) {
                        if(!tablesOfOrders.contains(o.getTable())) {
                            tablesOfOrders.add(o.getTable());
                        }
                    }
                }
            }
        });

        ordersReadyForDelivery.setSupplier(() -> {
            Order matcher = new Order();
            matcher.setState(Order.State.READY_FOR_DELIVERY);

            try {
                return orderService.findOrder(matcher);
            } catch (ServiceException e) {
                LOGGER.error("Getting orders via user supplier has failed", e);
                return null;
            }
        });
    }

    @Override
    public void disable(boolean disabled) {
        if(disabled) {
            ordersReadyForDelivery.stopPolling();
        } else {
            ordersReadyForDelivery.startPolling();
        }
    }

    @FXML
    public void on_lvDelivery_clicked(MouseEvent arg0) {
        Table selectedTable = lvDelivery.getSelectionModel().getSelectedItem();

        if(onTableClickedConsumer != null && selectedTable != null) {
            onTableClickedConsumer.accept(selectedTable);
        }
    }

    public void setOnReservationButtonAction(EventHandler<ActionEvent> event) {
        btnReservation.setOnAction(event);
    }

    public void setOnTableClicked(Consumer<Table> consumer) {
        onTableClickedConsumer = consumer;
        tableViewController.setOnTableClicked(consumer);
    }

}
