package com.at.ac.tuwien.sepm.ss15.edulium.gui;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.Menu;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.MenuCategory;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.MenuEntry;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.controlsfx.control.SegmentedButton;

import java.net.URL;
import java.util.*;
import java.util.function.Consumer;

public class MenuDetailsController implements Initializable, Controller {
    private static final Logger LOGGER = LogManager.getLogger(MenuDetailsController.class);

    @FXML
    private ListView categoriesView;

    private Consumer<Menu> menuClickedConsumer = null;

    private ObservableMap<MenuCategory, List<MenuEntry>> menuEntries = FXCollections.observableHashMap();

    private class MenuCategoryCell extends ListCell<MenuCategory> {
        private VBox layout = new VBox();
        private Label menuCategoryNameLabel = new Label();
        private SegmentedButton menuEntryButtons = new SegmentedButton();
        private Menu menu = null;

        public MenuCategoryCell() {
            menuCategoryNameLabel.setStyle("-fx-font-size: 18px;");

            menuEntryButtons.setStyle("-fx-font-size: 18px;");
            menuEntryButtons.getStyleClass().add(SegmentedButton.STYLE_CLASS_DARK);

            layout.getChildren().setAll(menuCategoryNameLabel, menuEntryButtons);

            setGraphic(layout);
        }

        @Override
        public void updateItem(MenuCategory menuCategory, boolean empty) {
            super.updateItem(menuCategory, empty);

            if (menuCategory != null) {
                menuCategoryNameLabel.setText(menuCategory.getName());

                menuEntryButtons.getButtons().clear();
                for (MenuEntry menuEntry : menuEntries.get(menuCategory)) {
                    ToggleButton menuEntryButton = new ToggleButton();
                    menuEntryButton.setText(menuEntry.getName());
                    menuEntryButton.setSelected(true);

                    menuEntryButtons.getButtons().add(menuEntryButton);
                }

                layout.setVisible(true);
            } else {
                layout.setVisible(false);
            }
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ObservableList<MenuCategory> displayedMenuCategories = FXCollections.observableArrayList();
        menuEntries.addListener((MapChangeListener<MenuCategory, List<MenuEntry>>) change -> {
            displayedMenuCategories.removeAll(change.getKey());
            if (change.wasAdded()) {
                displayedMenuCategories.add(change.getKey());
            }
        });

        // sort the displayed categories by name
        SortedList<MenuCategory> sortedDisplayedMenuCategories = new SortedList<>(displayedMenuCategories);
        sortedDisplayedMenuCategories.setComparator((c1, c2) -> c1.getName().compareToIgnoreCase(c2.getName()));

        categoriesView.setCellFactory(view -> new MenuCategoryCell());
        categoriesView.setItems(sortedDisplayedMenuCategories);
        categoriesView.setStyle("-fx-font-size: 18px;");
    }

    public void setMenu(Menu menu) {
        HashMap<MenuCategory, List<MenuEntry>> tempMenuEntries = new HashMap<>();
        for (MenuEntry menuEntry : menu.getEntries()) {
            tempMenuEntries.computeIfAbsent(menuEntry.getCategory(), category -> new ArrayList<MenuEntry>());
            tempMenuEntries.get(menuEntry.getCategory()).add(menuEntry);
        }

        menuEntries.clear();
        tempMenuEntries.forEach((category, entries) -> menuEntries.put(category, entries));
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

    }
}