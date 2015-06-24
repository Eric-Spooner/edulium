package com.at.ac.tuwien.sepm.ss15.edulium.gui.service;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.Menu;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.MenuCategory;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.MenuEntry;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.Order;
import com.at.ac.tuwien.sepm.ss15.edulium.gui.util.PersistentButtonToggleGroup;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.controlsfx.control.PopOver;
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

    private Consumer<List<Order>> menuAcceptedConsumer = null;

    private final ObservableMap<MenuCategory, List<MenuEntry>> menuEntries = FXCollections.observableHashMap();
    private final Map<MenuCategory, Order> selectedMenuEntries = new HashMap<>();

    private TimerTask longPressTask;

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
                    Order order = new Order();
                    order.setMenuEntry(menuEntry);
                    order.setAdditionalInformation("");
                    selectedMenuEntries.put(menuCategory, order);
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

                    menuEntryButton.setOnMouseReleased(e -> onMenuEntryReleased(menuEntry));
                    menuEntryButton.setOnMousePressed(e -> onMenuEntryPressed(menuEntry, menuEntryButton));

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

    public void setOnMenuAccepted(Consumer<List<Order>> menuAcceptedConsumer)
    {
        this.menuAcceptedConsumer = menuAcceptedConsumer;
    }

    @FXML
    private void onAcceptButtonClicked() {
        if (menuAcceptedConsumer != null) {
            menuAcceptedConsumer.accept(new ArrayList(selectedMenuEntries.values()));
        }
    }

    private void onMenuEntryReleased(MenuEntry entry)
    {
        if(longPressTask != null) {
            longPressTask.cancel();
        }
    }

    private void onMenuEntryLongPressed(MenuEntry entry, String info) {
        if(menuAcceptedConsumer != null) {
            Order order = new Order();
            order.setMenuEntry(entry);
            order.setAdditionalInformation(info);
            selectedMenuEntries.put(entry.getCategory(), order);
        }
    }

    public String getMenuName() {
        return menu.getName();
    }

    private void showAdditionalInfoPopOver(MenuEntry entry, ToggleButton button) {
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
        popOver.setAutoHide(false);
        popOver.setHideOnEscape(false);

        popOver.show(button);
    }

    private void onMenuEntryPressed(MenuEntry menuEntry, ToggleButton button)
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