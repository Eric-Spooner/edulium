package com.at.ac.tuwien.sepm.ss15.edulium.gui;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.MenuCategory;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.Order;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ValidationException;
import com.at.ac.tuwien.sepm.ss15.edulium.gui.util.PollingList;
import com.at.ac.tuwien.sepm.ss15.edulium.service.MenuService;
import com.at.ac.tuwien.sepm.ss15.edulium.service.OrderService;
import com.at.ac.tuwien.sepm.ss15.edulium.service.ServiceException;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ListChangeListener;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.controlsfx.control.CheckListView;
import org.controlsfx.control.PopOver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Controller;

import java.net.URL;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ResourceBundle;

import static javafx.collections.FXCollections.observableArrayList;

/**
 * Controller used for the Cook View
 */
@Controller
public class CookViewController implements Initializable {
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

    @FXML
    private Button  btnOpenMenuCats;

    @Autowired
    private MenuService menuService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private TaskScheduler taskScheduler;

    private PollingList<Order> ordersQueued;
    private PollingList<Order> ordersInProgress;
    private PollingList<Order> ordersReadyForDelivery;
    private List<MenuCategory> checkedCategories;
    private FilteredList<Order> ordersQueuedFiltered;
    private FilteredList<Order> ordersInProgressFiltered;
    private FilteredList<Order> ordersReadyForDeliverFiltered;


    private PopOver menuSelectionPopOver;

    private void initMenuSelectionPopOver(){
        try {
            CheckListView<MenuCategory> listView = new CheckListView<>();
            listView.setItems(observableArrayList(menuService.getAllMenuCategories()));
            listView.getItems().forEach(MenuCategory -> listView.getCheckModel().check(MenuCategory));
            listView.getCheckModel().getCheckedItems().addListener((ListChangeListener.Change<? extends MenuCategory> c) -> {
                checkedCategories.clear();
                checkedCategories.addAll(listView.getCheckModel().getCheckedItems());
                ordersQueuedFiltered.setPredicate(order -> checkedCategories.contains(order.getMenuEntry().getCategory()));
                ordersReadyForDeliverFiltered.setPredicate(order -> checkedCategories.contains(order.getMenuEntry().getCategory()));
                ordersInProgressFiltered.setPredicate(order -> checkedCategories.contains(order.getMenuEntry().getCategory()));
            });
            // set up listview (listeners; set observablelist,...)
            menuSelectionPopOver = new PopOver(listView);
            // setup menuSelectionPopOver
            menuSelectionPopOver.setHideOnEscape(true);
            menuSelectionPopOver.setAutoHide(true);
            menuSelectionPopOver.setDetachable(false);
            menuSelectionPopOver.setMinSize(1200, 700);
            menuSelectionPopOver.setStyle("-fx-font-size: 25px;");
            menuSelectionPopOver.setArrowLocation(PopOver.ArrowLocation.TOP_RIGHT);
        }catch (ServiceException e ){
            LOGGER.error("Init Cook View Menu Categories failed", e);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initMenuSelectionPopOver();

        try {
            checkedCategories = menuService.getAllMenuCategories();
        }catch (ServiceException e ){
            LOGGER.error("Getting all MenuCategories has failed", e);
        }
        // queued
        ordersQueued = new PollingList<>(taskScheduler);
        ordersQueued.setInterval(1000);
        ordersQueued.setSupplier(() -> {
            try {
                Order matcher = new Order();
                matcher.setState(Order.State.QUEUED);
                return orderService.findOrder(matcher);
            } catch (ServiceException e) {
                LOGGER.error("Getting all queued orders via order supplier has failed", e);
                return null;
            }
        });
        ordersQueued.startPolling();
        ordersQueuedFiltered = new FilteredList<>(ordersQueued);
        ordersQueuedFiltered.setPredicate(order -> checkedCategories.contains(order.getMenuEntry().getCategory()));
        SortedList<Order> sortedDataQueued = new SortedList<>(ordersQueuedFiltered);
        sortedDataQueued.comparatorProperty().bind(tableViewQueued.comparatorProperty());
        tableViewQueued.setItems(sortedDataQueued);
        tableViewQueued.setStyle("-fx-font-size: 25px;");
        tableViewQueued.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        tableViewQueued.setRowFactory(tableView -> {
            final TableRow<Order> row = new TableRow<>();
            row.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
                final int index = row.getIndex();
                if (index >= 0 && index < tableView.getItems().size() && tableView.getSelectionModel().isSelected(index)) {
                    tableView.getSelectionModel().clearSelection();
                    event.consume();
                } else if (index >= 0 && index < tableView.getItems().size()) {
                    tableView.getSelectionModel().select(index);
                    event.consume();
                }
            });
            return row;
        });
        tableColQueudTisch.setCellValueFactory(p -> new SimpleObjectProperty<>(p.getValue().getTable().getNumber()));
        tableColQueudMenuEntry.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getMenuEntry().getName()));
        tableColQueudTime.setCellValueFactory(p -> new SimpleObjectProperty<>(Duration.between(p.getValue().getTime(), LocalDateTime.now()).toMinutes()));
        tableColQueudAddInfo.setCellValueFactory(new PropertyValueFactory<>("additionalInformation"));
        // in progress
        ordersInProgress = new PollingList<>(taskScheduler);
        ordersInProgress.setInterval(1000);
        ordersInProgress.setSupplier(() -> {
            try {
                Order matcher = new Order();
                matcher.setState(Order.State.IN_PROGRESS);
                return orderService.findOrder(matcher);
            } catch (ServiceException e) {
                LOGGER.error("Getting all orders in progress via order supplier has failed", e);
                return null;
            }
        });
        ordersInProgress.startPolling();
        ordersInProgressFiltered = new FilteredList<>(ordersInProgress);
        ordersInProgressFiltered.setPredicate(order -> checkedCategories.contains(order.getMenuEntry().getCategory()));
        SortedList<Order> sortedDataInProgress = new SortedList<>(ordersInProgressFiltered);
        sortedDataInProgress.comparatorProperty().bind(tableViewInProgress.comparatorProperty());
        tableViewInProgress.setItems(sortedDataInProgress);
        tableViewInProgress.setStyle("-fx-font-size: 25px;");
        tableViewInProgress.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        tableViewInProgress.setRowFactory(tableView -> {
            final TableRow<Order> row = new TableRow<>();
            row.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
                final int index = row.getIndex();
                if (index >= 0 && index < tableView.getItems().size() && tableView.getSelectionModel().isSelected(index)) {
                    tableView.getSelectionModel().clearSelection();
                    event.consume();
                } else if (index >= 0 && index < tableView.getItems().size()) {
                    tableView.getSelectionModel().select(index);
                    event.consume();
                }
            });
            return row;
        });
        tableColProgTisch.setCellValueFactory(p -> new SimpleObjectProperty<>(p.getValue().getTable().getNumber()));
        tableColProgMenuEntry.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getMenuEntry().getName()));
        tableColProgTime.setCellValueFactory(p -> new SimpleObjectProperty<>(Duration.between(p.getValue().getTime(), LocalDateTime.now()).toMinutes()));
        tableColProgAddInfo.setCellValueFactory(new PropertyValueFactory<>("additionalInformation"));

        // ready for delivery
        ordersReadyForDelivery = new PollingList<>(taskScheduler);
        ordersReadyForDelivery.setInterval(1000);
        ordersReadyForDelivery.setSupplier(() -> {
            try {
                Order matcher = new Order();
                matcher.setState(Order.State.READY_FOR_DELIVERY);
                return orderService.findOrder(matcher);
            } catch (ServiceException e) {
                LOGGER.error("Getting all ready for delivery orders via order supplier has failed", e);
                return null;
            }
        });
        ordersReadyForDelivery.startPolling();
        ordersReadyForDeliverFiltered = new FilteredList<>(ordersReadyForDelivery);
        ordersReadyForDeliverFiltered.setPredicate(order -> checkedCategories.contains(order.getMenuEntry().getCategory()));
        SortedList<Order> sortedDataReady = new SortedList<>(ordersReadyForDeliverFiltered);
        sortedDataReady.comparatorProperty().bind(tableViewInProgress.comparatorProperty());
        tableViewReadyForDelivery.setItems(sortedDataReady);
        tableViewReadyForDelivery.setStyle("-fx-font-size: 25px;");
        tableColDeliveryTable.setCellValueFactory(p -> new SimpleObjectProperty<>(p.getValue().getTable().getNumber()));
        tableColDeliveryMenuEntry.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getMenuEntry().getName()));
        tableColDeliveryTime.setCellValueFactory(p -> new SimpleObjectProperty<>(Duration.between(p.getValue().getTime(), LocalDateTime.now()).toMinutes()));
        tableColDeliveryAddInfo.setCellValueFactory(new PropertyValueFactory<>("additionalInformation"));
    }


    public void btnInProgressToReadyToDeliver() {
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

    public void btnQueuedToInProgress() {
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

    public void btnSelectShownCats(){
        if (menuSelectionPopOver.isShowing()) {
            menuSelectionPopOver.hide();
        } else {
            menuSelectionPopOver.show(btnOpenMenuCats);
        }
        /*try {
            Stage stage = new Stage();
            stage.setTitle("Menu Categories");
            DialogCookviewCategories dialogController = cookViewDialogPane.getController(DialogCookviewCategories.class);
            dialogController.setCheckedCategories(checkedCategories);
            dialogController.setThisStage(stage);
            Scene scene = new Scene(cookViewDialogPane);
            stage.setScene(scene);
            stage.showAndWait();
            checkedCategories = dialogController.getCheckedCategories();
            ordersQueuedFiltered.setPredicate(order -> checkedCategories.contains(order.getMenuEntry().getCategory()));
            ordersReadyForDeliverFiltered.setPredicate(order -> checkedCategories.contains(order.getMenuEntry().getCategory()));
            ordersInProgressFiltered.setPredicate(order -> checkedCategories.contains(order.getMenuEntry().getCategory()));
        }catch (Exception e){
            LOGGER.error("Open the Cook View Menu Categories selection Dialog failed", e);
            ManagerViewController.showErrorDialog("Error", "Cook View open Menu Categories Error", "Open the Cook View Menu Categories selection Dialog failed \n"  + e.toString());
        }
*/
    }
}
