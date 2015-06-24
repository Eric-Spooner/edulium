package com.at.ac.tuwien.sepm.ss15.edulium.gui.service;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.MenuCategory;
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
import org.controlsfx.control.GridView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Controller;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

@Controller
public class MenuCategoryOverviewController implements Initializable {
    private static final Logger LOGGER = LogManager.getLogger(MenuCategoryOverviewController.class);

    @FXML
    private ScrollPane scrollPane;

    @Autowired
    private MenuService menuService;
    @Autowired
    private TaskScheduler taskScheduler;

    private SortedList<MenuCategory> sortedMenuCategories;

    private Consumer<MenuCategory> menuCategoryClickedConsumer = null;

    private class UserButtonCell extends GridCell<MenuCategory> {
        private final Button button = new Button();
        private MenuCategory menuCategory = null;

        public UserButtonCell() {
            button.setPrefSize(220, 80);
            button.setStyle("-fx-font-size: 18px;");
            button.setOnAction(e -> onMenuCategoryClicked(menuCategory));

            setGraphic(button);
        }

        @Override
        public void updateIndex(int i) {
            if (i >= 0) {
                menuCategory = sortedMenuCategories.get(i);
                button.setText(menuCategory.getName());
            }
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        PollingList<MenuCategory> menuCategories = new PollingList<>(taskScheduler);
        menuCategories.setInterval(1000);
        menuCategories.setSupplier(() -> {
            try {
                return menuService.getAllMenuCategories();
            } catch (ServiceException e) {
                LOGGER.error("Getting all menu categories via menu category supplier has failed", e);
                return null;
            }
        });
        menuCategories.startPolling();

        sortedMenuCategories = new SortedList<>(menuCategories);
        sortedMenuCategories.setComparator((c1, c2) -> c1.getName().compareToIgnoreCase(c2.getName()));

        GridView<MenuCategory> gridView = new GridView<>(sortedMenuCategories);
        gridView.setCellFactory(view -> new UserButtonCell());
        gridView.setCellWidth(220);
        gridView.setCellHeight(80);

        scrollPane.setContent(gridView);
    }

    public void setOnMenuCategoryClicked(Consumer<MenuCategory> menuCategoryClickedConsumer)
    {
        this.menuCategoryClickedConsumer = menuCategoryClickedConsumer;
    }

    private void onMenuCategoryClicked(MenuCategory menuCategory)
    {
        if (menuCategoryClickedConsumer != null) {
            menuCategoryClickedConsumer.accept(menuCategory);
        }
    }
}
