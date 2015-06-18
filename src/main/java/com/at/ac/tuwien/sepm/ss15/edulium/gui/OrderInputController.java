package com.at.ac.tuwien.sepm.ss15.edulium.gui;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.MenuEntry;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.Order;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.Table;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ValidationException;
import com.at.ac.tuwien.sepm.ss15.edulium.service.OrderService;
import com.at.ac.tuwien.sepm.ss15.edulium.service.ServiceException;
import javafx.collections.*;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.controlsfx.control.SegmentedButton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.ResourceBundle;

@Component
public class OrderInputController  implements Initializable, Controller {
    private static final Logger LOGGER = LogManager.getLogger(OrderInputController.class);

    private enum ScreenType {
        MenuCategoryScreen,
        MenuEntryScreen,
        MenuScreen,
        MenuDetailsScreen
    }

    @FXML
    private ListView<Order> ordersView;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private Button orderButton;
    @FXML
    private Button cancelButton;
    @FXML
    private Button backButton;
    @FXML
    private Label headerLabel;
    @FXML
    private HBox headerLayout;

    private class OrderCell extends ListCell<Order> {
        private final Label nameLabel;
        private final Label additionalInformationLabel;
        private final Label amountLabel;
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

            increaseAmountButton = new Button();
            increaseAmountButton.setText("+");
            increaseAmountButton.setMinSize(40, 40);
            increaseAmountButton.setOnAction(action -> orders.computeIfPresent(order, (key, amount) -> amount + 1));

            decreaseAmountButton = new Button();
            decreaseAmountButton.setText("-");
            decreaseAmountButton.setMinSize(40, 40);
            decreaseAmountButton.setOnAction(action -> orders.computeIfPresent(order, (key, amount) -> amount == 1 ? null : amount - 1));

            layout = new HBox();
            layout.setSpacing(5);
            layout.getChildren().setAll(increaseAmountButton, decreaseAmountButton, amountLabel, labelLayout);

            setGraphic(layout);
        }

        @Override
        protected void updateItem(Order item, boolean empty) {
            super.updateItem(item, empty);

            order = item;

            if (order != null) {
                Integer amount = orders.get(order);

                nameLabel.setText(order.getMenuEntry().getName());
                additionalInformationLabel.setText(order.getAdditionalInformation());
                amountLabel.setText(amount == null ? "-" : amount.toString());
                layout.setVisible(true);
            } else {
                layout.setVisible(false);
            }
        }
    }

    @Resource(name = "menuCategoryOverviewPane")
    private FXMLPane menuCategoryOverviewPane;
    private MenuCategoryOverviewController menuCategoryOverviewController;

    @Resource(name = "menuEntryOverviewPane")
    private FXMLPane menuEntryOverviewPane;
    private MenuEntryOverviewController menuEntryOverviewController;

    @Resource(name = "menuOverviewPane")
    private FXMLPane menuOverviewPane;
    private MenuOverviewController menuOverviewController;

    @Resource(name = "menuDetailsPane")
    private FXMLPane menuDetailsPane;
    private MenuDetailsController menuDetailsController;

    @Autowired
    private OrderService orderService;

    private Table table;

    private ObservableMap<Order, Integer> orders = FXCollections.observableHashMap();

    private EventHandler<ActionEvent> doneEventHandler;

    private ToggleButton menuCategoryScreenButton;
    private ToggleButton menuScreenButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeHeaderButtons();
        initializeControllers();
        initializeOrdersView();

        reset();
    }

    private void initializeControllers() {
        menuCategoryOverviewController = menuCategoryOverviewPane.getController(MenuCategoryOverviewController.class);
        menuEntryOverviewController = menuEntryOverviewPane.getController(MenuEntryOverviewController.class);
        menuOverviewController = menuOverviewPane.getController(MenuOverviewController.class);
        menuDetailsController = menuDetailsPane.getController(MenuDetailsController.class);

        menuCategoryOverviewController.setOnMenuCategoryClicked(menuCategory -> {
            menuEntryOverviewController.setMenuCategory(menuCategory);
            showScreen(ScreenType.MenuEntryScreen);
            headerLabel.setText(menuCategory.getName());
        });

        menuOverviewController.setOnMenuClicked(menu -> {
            menuDetailsController.setMenu(menu);
            showScreen(ScreenType.MenuDetailsScreen);
            headerLabel.setText(menu.getName());
        });

        menuEntryOverviewController.setOnMenuEntryClicked(menuEntry -> {
            Order order = new Order();
            order.setMenuEntry(menuEntry);
            order.setAdditionalInformation("blablabla");

            orders.compute(order, (key, amount) -> (amount == null) ? 1 : amount + 1);
        });
    }

    private void initializeOrdersView() {
        // put all keys of the orders map into a list so that we can use it in the list view
        ObservableList<Order> displayedOrders = FXCollections.observableArrayList();
        orders.addListener((MapChangeListener<Order, Integer>) change -> {
            displayedOrders.removeAll(change.getKey());
            if (change.wasAdded()) {
                displayedOrders.add(change.getKey());
            }
        });

        // sort the displayed orders by name
        SortedList<Order> sortedDisplayedOrders = new SortedList<>(displayedOrders);
        sortedDisplayedOrders.setComparator((o1, o2) -> o1.getMenuEntry().getName().compareToIgnoreCase(o2.getMenuEntry().getName()));

        ordersView.setCellFactory(view -> new OrderCell());
        ordersView.setItems(sortedDisplayedOrders);
        ordersView.setStyle("-fx-font-size: 18px;");
    }

    private void initializeHeaderButtons() {
        // disable order button if there are no orders
        orders.addListener(new MapChangeListener<Order, Integer>() {
            @Override
            public void onChanged(Change<? extends Order, ? extends Integer> change) {
                orderButton.setDisable(orders.isEmpty());
            }
        });

        menuCategoryScreenButton = new ToggleButton();
        menuCategoryScreenButton.setText("Categories");
        menuCategoryScreenButton.setMinHeight(33);
        menuCategoryScreenButton.setOnAction(action -> showScreen(ScreenType.MenuCategoryScreen));

        menuScreenButton = new ToggleButton();
        menuScreenButton.setText("Menus");
        menuScreenButton.setMinHeight(33);
        menuScreenButton.setOnAction(action -> showScreen(ScreenType.MenuScreen));

        SegmentedButton headerButtons = new SegmentedButton();
        headerButtons.setStyle("-fx-font-size: 18px;");
        headerButtons.getStyleClass().add(SegmentedButton.STYLE_CLASS_DARK);
        headerButtons.getButtons().setAll(menuCategoryScreenButton, menuScreenButton);

        headerLayout.getChildren().add(headerButtons);
    }

    @FXML
    private void onOrderButtonClicked(ActionEvent actionEvent) {
        try {
            for (Map.Entry<Order, Integer> entry : orders.entrySet()) {
                MenuEntry menuEntry = entry.getKey().getMenuEntry();
                String additionalInformation = entry.getKey().getAdditionalInformation();

                Order order = new Order();
                order.setMenuEntry(menuEntry);
                order.setTime(LocalDateTime.now());
                order.setBrutto(menuEntry.getPrice());
                order.setTax(menuEntry.getTaxRate().getValue());
                order.setState(Order.State.QUEUED);
                order.setTable(table);
                order.setAdditionalInformation(additionalInformation);

                Integer amount = entry.getValue();
                for (int i = 0; i < amount; i++) {
                    orderService.addOrder(order);
                }
            }
        } catch (ValidationException | ServiceException e) {
            LOGGER.error("Adding orders did not work", e);

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Adding orders failed");
            alert.setContentText(e.toString());

            alert.showAndWait();
        }

        doneEventHandler.handle(actionEvent);
        reset();
    }

    @FXML
    private void onCancelButtonClicked(ActionEvent actionEvent) {
        doneEventHandler.handle(actionEvent);
        reset();
    }

    @FXML
    private void onBackButtonClicked(ActionEvent actionEvent) {
        showScreen((ScreenType)backButton.getUserData());
    }

    private void showScreen(ScreenType screenType) {
        switch (screenType) {
            case MenuEntryScreen:
                scrollPane.setContent(menuEntryOverviewPane);
                backButton.setDisable(false);
                backButton.setUserData(ScreenType.MenuCategoryScreen);
                break;
            case MenuDetailsScreen:
                scrollPane.setContent(menuDetailsPane);
                backButton.setDisable(false);
                backButton.setUserData(ScreenType.MenuScreen);
                break;
            case MenuScreen:
                scrollPane.setContent(menuOverviewPane);
                backButton.setDisable(true);
                menuScreenButton.setSelected(true);
                headerLabel.setText("All Menus");
                break;
            case MenuCategoryScreen:
            default:
                scrollPane.setContent(menuCategoryOverviewPane);
                backButton.setDisable(true);
                menuCategoryScreenButton.setSelected(true);
                headerLabel.setText("All Categories");
        }
    }

    public void setTable(Table table) {
        this.table = table;
        reset();
    }

    private void reset() {
        showScreen(ScreenType.MenuCategoryScreen);
        orderButton.setDisable(true);
        orders.clear();
    }

    public void setOnDone(EventHandler<ActionEvent> doneEventHandler) {
        this.doneEventHandler = doneEventHandler;
    }

    @Override
    public void disable(boolean disabled) {
        menuCategoryOverviewController.disable(disabled);
        menuEntryOverviewController.disable(disabled);
    }
}
