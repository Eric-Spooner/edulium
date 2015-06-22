package com.at.ac.tuwien.sepm.ss15.edulium.gui.service;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.Menu;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.MenuCategory;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.MenuEntry;
import com.at.ac.tuwien.sepm.ss15.edulium.gui.util.PersistentButtonToggleGroup;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.VBox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.controlsfx.control.SegmentedButton;
import org.springframework.stereotype.Controller;

import java.net.URL;
import java.util.*;
import java.util.function.Consumer;

@Controller
public class MenuDetailsController implements Initializable {
    private static final Logger LOGGER = LogManager.getLogger(MenuDetailsController.class);

    @FXML
    private ListView categoriesView;

    private Menu menu;

    private Consumer<Menu> menuAcceptedConsumer = null;

    private final ObservableMap<MenuCategory, List<MenuEntry>> menuEntries = FXCollections.observableHashMap();
    private final Map<MenuCategory, MenuEntry> selectedMenuEntries = new HashMap<>();

    private class MenuCategoryCell extends ListCell<MenuCategory> {
        private MenuCategory menuCategory;
        private final VBox layout = new VBox();
        private final Label menuCategoryNameLabel = new Label();
        private final SegmentedButton menuEntryButtons = new SegmentedButton();

        public MenuCategoryCell() {
            menuCategoryNameLabel.setStyle("-fx-font-size: 18px;");

            menuEntryButtons.setToggleGroup(new PersistentButtonToggleGroup());
            menuEntryButtons.setStyle("-fx-font-size: 18px;");
            menuEntryButtons.getStyleClass().add(SegmentedButton.STYLE_CLASS_DARK);
            menuEntryButtons.getToggleGroup().selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue != null) {
                    MenuEntry menuEntry = (MenuEntry) newValue.getUserData();
                    selectedMenuEntries.put(menuCategory, menuEntry);
                }
            });

            layout.getChildren().setAll(menuCategoryNameLabel, menuEntryButtons);

            setGraphic(layout);
        }

        @Override
        public void updateItem(MenuCategory menuCategory, boolean empty) {
            super.updateItem(menuCategory, empty);

            this.menuCategory = menuCategory;

            if (menuCategory != null) {
                menuCategoryNameLabel.setText(menuCategory.getName());

                // add a button for each menu entry in the current category
                menuEntryButtons.getButtons().clear();
                for (MenuEntry menuEntry : menuEntries.get(menuCategory)) {
                    ToggleButton menuEntryButton = new ToggleButton();
                    menuEntryButton.setText(menuEntry.getName());
                    menuEntryButton.setMinHeight(60);
                    menuEntryButton.setUserData(menuEntry);

                    menuEntryButtons.getButtons().add(menuEntryButton);
                }

                assert !menuEntryButtons.getButtons().isEmpty();

                // select the first button in the group
                menuEntryButtons.getButtons().get(0).setSelected(true);

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
        this.menu = menu;

        menuEntries.clear();
        selectedMenuEntries.clear();

        HashMap<MenuCategory, List<MenuEntry>> tempMenuEntries = new HashMap<>();
        for (MenuEntry menuEntry : menu.getEntries()) {
            tempMenuEntries.computeIfAbsent(menuEntry.getCategory(), category -> new ArrayList<>());
            tempMenuEntries.get(menuEntry.getCategory()).add(menuEntry);
        }

        tempMenuEntries.forEach(menuEntries::put);
    }

    public void setOnMenuAccepted(Consumer<Menu> menuAcceptedConsumer)
    {
        this.menuAcceptedConsumer = menuAcceptedConsumer;
    }

    @FXML
    private void onAcceptButtonClicked(ActionEvent actionEvent) {
        if (menuAcceptedConsumer != null) {
            Menu configuredMenu = new Menu();
            configuredMenu.setName(menu.getName());
            configuredMenu.setEntries(new ArrayList<>(selectedMenuEntries.values()));

            menuAcceptedConsumer.accept(configuredMenu);
        }
    }
}