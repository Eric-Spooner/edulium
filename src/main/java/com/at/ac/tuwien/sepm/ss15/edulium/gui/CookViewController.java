package com.at.ac.tuwien.sepm.ss15.edulium.gui;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.MenuEntry;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.Order;
import com.at.ac.tuwien.sepm.ss15.edulium.service.MenuService;
import com.at.ac.tuwien.sepm.ss15.edulium.service.TaxRateService;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

/**
 * Created by - on 09.06.2015.
 */
public class CookViewController implements Initializable {

    private static Stage thisStage;
    private static OrderService orderService;

    @FXML
    private TableView tableViewQueued;
    @FXML
    private TableColumn<Order, Integer> tableColQueudTisch;
    @FXML
    private TableColumn<Order, String> tableColQueudMenuEntry;
    @FXML
    private TableColumn<Order, String> tableColQueudAddInfo;
    @FXML
    private TableColumn<Order, LocalDateTime> tableColQueudTime;

    @FXML
    private TableView tableViewInProgress;
    @FXML
    private TableColumn<Order, Integer> tableColProgTisch;
    @FXML
    private TableColumn<Order, String> tableColProgMenuEntry;
    @FXML
    private TableColumn<Order, String> tableColProgAddInfo;
    @FXML
    private TableColumn<Order, LocalDateTime> tableColProgdTime;


    private ObservableList<Order> observableListQueued;
    private ObservableList<Order> observableListInProgress;

    @Override
    public void initialize(URL location, ResourceBundle resources){
        Order order = new Order();
        order.setState(Order.State.QUEUED);
        observableListQueued = ObservableList(orderService.;
    }

    public void btnInProgressToReadyToDeliver(ActionEvent actionEvent) {

    }

    public void btnQueuedToInProgress(ActionEvent actionEvent) {
    }
}
