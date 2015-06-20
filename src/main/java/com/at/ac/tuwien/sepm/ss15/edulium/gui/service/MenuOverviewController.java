package com.at.ac.tuwien.sepm.ss15.edulium.gui.service;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.Menu;
import com.at.ac.tuwien.sepm.ss15.edulium.gui.Controller;
import com.at.ac.tuwien.sepm.ss15.edulium.gui.util.PollingList;
import com.at.ac.tuwien.sepm.ss15.edulium.service.MenuService;
import com.at.ac.tuwien.sepm.ss15.edulium.service.ServiceException;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.controlsfx.control.GridCell;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class MenuOverviewController implements Initializable, Controller {
    private static final Logger LOGGER = LogManager.getLogger(MenuOverviewController.class);

    @FXML
    private ScrollPane scrollPane;

    @Autowired
    private MenuService menuService;
    @Autowired
    private TaskScheduler taskScheduler;

    private PollingList<Menu> menus;
    private SortedList<Menu> sortedMenus;

    private Consumer<Menu> menuClickedConsumer = null;

    private class MenuCell extends GridCell<Menu> {
        private final Button button = new Button();
        private Menu menu = null;

        public MenuCell() {
            button.setPrefSize(220, 80);
            button.setStyle("-fx-font-size: 18px;");
            button.setOnAction(e -> onMenuClicked(menu));

            setGraphic(button);
        }

        @Override
        public void updateIndex(int i) {
            if (i >= 0) {
                menu = sortedMenus.get(i);
                button.setText(menu.getName());
            }
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        menus = new PollingList<>(taskScheduler);
        menus.setInterval(1000);
        menus.setSupplier(() -> {
            try {
                return menuService.getAllMenus();
            } catch (ServiceException e) {
                LOGGER.error("Getting all menus via menu supplier has failed", e);
                return null;
            }
        });

        sortedMenus = new SortedList<>(menus);
        sortedMenus.setComparator((c1, c2) -> c1.getName().compareToIgnoreCase(c2.getName()));

        org.controlsfx.control.GridView<Menu> gridView = new org.controlsfx.control.GridView<>(sortedMenus);
        gridView.setCellFactory(view -> new MenuCell());
        gridView.setCellWidth(220);
        gridView.setCellHeight(80);

        scrollPane.setContent(gridView);
    }

    public void setOnMenuClicked(Consumer<Menu> menuClickedConsumer)
    {
        this.menuClickedConsumer = menuClickedConsumer;
    }

    private void onMenuClicked(Menu menu)
    {
        if (menuClickedConsumer != null) {
            menuClickedConsumer.accept(menu);
        }
    }

    @Override
    public void disable(boolean disabled) {
        if (disabled) {
            menus.stopPolling();
        } else {
            menus.startPolling();
        }
    }
}