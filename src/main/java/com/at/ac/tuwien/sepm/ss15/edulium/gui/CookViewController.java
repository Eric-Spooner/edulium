package com.at.ac.tuwien.sepm.ss15.edulium.gui;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.MenuEntry;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.Order;
import com.at.ac.tuwien.sepm.ss15.edulium.service.MenuService;
import com.at.ac.tuwien.sepm.ss15.edulium.service.OrderService;
import com.at.ac.tuwien.sepm.ss15.edulium.service.TaxRateService;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.math.BigDecimal;
import java.net.URL;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.ResourceBundle;

import static javafx.collections.FXCollections.observableArrayList;

/**
 * Created by - on 09.06.2015.
 */
public class CookViewController implements Initializable {
    private static final Logger LOGGER = LogManager.getLogger(DialogMenuController.class);

    private static Stage thisStage;
    private static OrderService orderService;
    private static MenuService menuService;


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


    public static void setThisStage(Stage thisStage) {
        CookViewController.thisStage = thisStage;
    }
    public static void setOrderService(OrderService orderService) {
        CookViewController.orderService = orderService;
    }
    public static void setMenuService(MenuService menuService) {
        CookViewController.menuService = menuService;
    }


    private ObservableList<Order> observableListQueued;
    private ObservableList<Order> observableListInProgress;

    @Override
    public void initialize(URL location, ResourceBundle resources){
        try {
            observableListQueued = (observableArrayList(orderService.getAllOrdersToPrepare(
                            menuService.getAllMenuCategories(), Order.State.QUEUED)));
            observableListInProgress = (observableArrayList(orderService.getAllOrdersToPrepare(
                    menuService.getAllMenuCategories(), Order.State.IN_PROGRESS)));

            tableViewQueued.setItems(observableListQueued);
            tableViewQueued.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
            tableColQueudTisch.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Order, Long>, ObservableValue<Long>>() {
                public ObservableValue<Long> call(TableColumn.CellDataFeatures<Order, Long> p) {
                    // p.getValue() returns the Person instance for a particular TableView row
                    return new SimpleObjectProperty<Long>(p.getValue().getTable().getNumber());
                }
            });
            tableColQueudMenuEntry.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Order, String>, ObservableValue<String>>() {
                public ObservableValue<String> call(TableColumn.CellDataFeatures<Order, String> p) {
                    // p.getValue() returns the Person instance for a particular TableView row
                    return new SimpleStringProperty(p.getValue().getMenuEntry().getName());
                }
            });
            tableColQueudTime.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Order, Long>, ObservableValue<Long>>() {
                public ObservableValue<Long> call(TableColumn.CellDataFeatures<Order, Long> p) {
                    // p.getValue() returns the Person instance for a particular TableView row
                    return new SimpleObjectProperty<Long>(Duration.between(p.getValue().getTime(), LocalDateTime.now()).toMinutes());
                }
            });
            tableColQueudAddInfo.setCellValueFactory(new PropertyValueFactory<Order, String>("additionalInformation"));


            tableViewInProgress.setItems(observableListInProgress);
            tableViewInProgress.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
            tableColProgTisch.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Order, Long>, ObservableValue<Long>>() {
                public ObservableValue<Long> call(TableColumn.CellDataFeatures<Order, Long> p) {
                    // p.getValue() returns the Person instance for a particular TableView row
                    return new SimpleObjectProperty<Long>(p.getValue().getTable().getNumber());
                }
            });
            tableColProgMenuEntry.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Order, String>, ObservableValue<String>>() {
                public ObservableValue<String> call(TableColumn.CellDataFeatures<Order, String> p) {
                    // p.getValue() returns the Person instance for a particular TableView row
                    return new SimpleStringProperty(p.getValue().getMenuEntry().getName());
                }
            });
            tableColProgTime.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Order, Long>, ObservableValue<Long>>() {
                public ObservableValue<Long> call(TableColumn.CellDataFeatures<Order, Long> p) {
                    // p.getValue() returns the Person instance for a particular TableView row
                    return new SimpleObjectProperty<Long>(Duration.between(p.getValue().getTime(),LocalDateTime.now()).toMinutes());
                }
            });
            tableColProgAddInfo.setCellValueFactory(new PropertyValueFactory<Order, String>("additionalInformation"));
        }catch (Exception e){
            ManagerController.showErrorDialog
                    ("Error", "Initialize Cook View", "The initialization did not work");
            LOGGER.error("The initialization did not work" + e);
        }
    }

    public void btnInProgressToReadyToDeliver(ActionEvent actionEvent) {
        LOGGER.info("Entered btnInProgressToReadyToDeliver");
        try {
            if(tableViewInProgress.getSelectionModel().getSelectedItems() != null){
                for(Order order : tableViewInProgress.getSelectionModel().getSelectedItems()){
                    orderService.markAsReadyForDelivery(order);
                }
            }
            observableListQueued.setAll(observableArrayList(orderService.getAllOrdersToPrepare(
                    menuService.getAllMenuCategories(), Order.State.QUEUED)));
            observableListInProgress.setAll(observableArrayList(orderService.getAllOrdersToPrepare(
                    menuService.getAllMenuCategories(), Order.State.IN_PROGRESS)));
        }catch (Exception e){
            ManagerController.showErrorDialog
                    ("Error", "Putting State to in Progress", "The State shifting did not work");
            LOGGER.error("Putting State to in Progress did not work" + e);
        }
    }

    public void btnQueuedToInProgress(ActionEvent actionEvent) {
        LOGGER.info("Entered btnQueuedToInProgress");
        try {
            if(tableViewInProgress.getSelectionModel().getSelectedItems() != null){
                for(Order order : tableViewQueued.getSelectionModel().getSelectedItems()){
                    orderService.markAsInProgress(order);
                }
            }
            observableListQueued.setAll(observableArrayList(orderService.getAllOrdersToPrepare(
                    menuService.getAllMenuCategories(), Order.State.QUEUED)));
            observableListInProgress.setAll(observableArrayList(orderService.getAllOrdersToPrepare(
                    menuService.getAllMenuCategories(), Order.State.IN_PROGRESS)));
        }catch (Exception e){
            ManagerController.showErrorDialog
                    ("Error", "Putting State to in Ready to Deliver", "The State shifting did not work");
            LOGGER.error("Putting State to in Ready to Deliver did not work" + e);
        }
    }

    public void btnRefresh(ActionEvent actionEvent) throws Exception{
        observableListQueued.setAll(observableArrayList(orderService.getAllOrdersToPrepare(
                menuService.getAllMenuCategories(), Order.State.QUEUED)));
        observableListInProgress.setAll(observableArrayList(orderService.getAllOrdersToPrepare(
                menuService.getAllMenuCategories(), Order.State.IN_PROGRESS)));
    }
}
