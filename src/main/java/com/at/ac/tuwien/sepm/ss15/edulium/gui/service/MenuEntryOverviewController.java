package com.at.ac.tuwien.sepm.ss15.edulium.gui.service;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.MenuCategory;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.MenuEntry;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.Order;
import com.at.ac.tuwien.sepm.ss15.edulium.gui.util.PollingList;
import com.at.ac.tuwien.sepm.ss15.edulium.service.MenuService;
import com.at.ac.tuwien.sepm.ss15.edulium.service.ServiceException;
import javafx.application.Platform;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.*;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.controlsfx.control.GridCell;
import org.controlsfx.control.GridView;
import org.controlsfx.control.PopOver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Controller;

import java.awt.*;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ScheduledFuture;
import java.util.function.Consumer;

@Controller
public class MenuEntryOverviewController implements Initializable {
    private static final Logger LOGGER = LogManager.getLogger(MenuEntryOverviewController.class);

    @FXML
    private ScrollPane scrollPane;

    @Autowired
    private MenuService menuService;
    @Autowired
    private TaskScheduler taskScheduler;

    private PollingList<MenuEntry> menuEntries;
    private SortedList<MenuEntry> sortedMenuEntries;

    private Consumer<Order> menuEntryClickedConsumer = null;

    private TimerTask longPressTask;

    private class UserButtonCell extends GridCell<MenuEntry> {
        private final Button button = new Button();
        private MenuEntry menuEntry = null;

        public UserButtonCell() {
            button.setPrefSize(220, 80);
            button.setStyle("-fx-font-size: 18px;");
            button.setOnMouseReleased(e -> onMenuEntryReleased(menuEntry));
            button.setOnMousePressed(e -> onMenuEntryPressed(menuEntry, button));

            setGraphic(button);
        }

        @Override
        public void updateIndex(int i) {
            if (i >= 0) {
                menuEntry = sortedMenuEntries.get(i);
                button.setText(menuEntry.getName());
                button.setDisable(!menuEntry.getAvailable());
            }
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        menuEntries = new PollingList<>(taskScheduler);
        menuEntries.setInterval(1000);
        setMenuCategory(null); // uses getAllMenuEntries

        sortedMenuEntries = new SortedList<>(menuEntries);
        sortedMenuEntries.setComparator((e1, e2) -> e1.getName().compareToIgnoreCase(e2.getName()));

        GridView<MenuEntry> gridView = new GridView<>(sortedMenuEntries);
        gridView.setCellFactory(view -> new UserButtonCell());
        gridView.setCellWidth(220);
        gridView.setCellHeight(80);

        scrollPane.setContent(gridView);
    }

    public void setMenuCategory(MenuCategory menuCategory)
    {
        menuEntries.setSupplier(() -> {
            try {
                if (menuCategory == null) {
                    return menuService.getAllMenuEntries();
                } else {
                    MenuEntry matcher = new MenuEntry();
                    matcher.setCategory(menuCategory);
                    return menuService.findMenuEntry(matcher);
                }
            } catch (ServiceException e) {
                LOGGER.error("Finding menu entries via menu entry supplier has failed", e);
                return null;
            }
        });
        menuEntries.startPolling();
    }

    public void setOnMenuEntryClicked(Consumer<Order> menuEntryClickedConsumer)
    {
        this.menuEntryClickedConsumer = menuEntryClickedConsumer;
    }

    private void onMenuEntryReleased(MenuEntry menuEntry)
    {
        if(longPressTask != null) {
            longPressTask.cancel();

            if (menuEntryClickedConsumer != null) {
                Order order = new Order();
                order.setAdditionalInformation("");
                order.setMenuEntry(menuEntry);
                menuEntryClickedConsumer.accept(order);
            }
        }
    }

    private void onMenuEntryLongPressed(MenuEntry entry, String info) {
        if(menuEntryClickedConsumer != null) {
            Order order = new Order();
            order.setMenuEntry(entry);
            order.setAdditionalInformation(info);
            menuEntryClickedConsumer.accept(order);
        }
    }

    private void showAdditionalInfoPopOver(MenuEntry entry, Button button) {
        longPressTask = null;
        PopOver popOver = new PopOver();

        TextField textField = new TextField();
        Button okButton = new Button();
        okButton.setText("OK");
        okButton.setOnAction(e -> {
            popOver.hide();
            onMenuEntryLongPressed(entry, textField.getText());
        });

        VBox vBox = new VBox(10.0);
        vBox.setPadding(new Insets(10.0, 10.0, 10.0, 10.0));
        vBox.setAlignment(Pos.CENTER);
        vBox.getChildren().addAll(textField, okButton);

        popOver.setContentNode(vBox);
        popOver.setAutoHide(true);
        popOver.setHideOnEscape(true);

        popOver.show(button);
    }

    private void onMenuEntryPressed(MenuEntry menuEntry, Button button)
    {
        longPressTask = new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    showAdditionalInfoPopOver(menuEntry, button);
                });
            }
        };

        Timer timer = new Timer();
        timer.schedule(longPressTask, 500);
    }
}