package com.at.ac.tuwien.sepm.ss15.edulium.gui;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.MenuEntry;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ValidationException;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.Validator;
import com.at.ac.tuwien.sepm.ss15.edulium.service.MenuService;
import com.at.ac.tuwien.sepm.ss15.edulium.service.ServiceException;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

@Controller
public class MenuEntryViewController implements Initializable {
    private static final Logger LOGGER = LogManager.getLogger(MenuEntryViewController.class);

    @FXML
    private TableView<MenuEntry> tableViewMenuEntry;
    @FXML
    private TableColumn<MenuEntry,Long> tableColMenuEntryId;
    @FXML
    private TableColumn<MenuEntry,String> tableColMenuEntryName;
    @FXML
    private TableColumn<MenuEntry,BigDecimal> tableColMenuEntryPrice;
    @FXML
    private TableColumn<MenuEntry, String> tableColMenuEntryCategory;
    @FXML
    private TableColumn<MenuEntry, Boolean> tableColMenuEntryAvailable;
    @FXML
    private Button buttonMenuEntryUpdate;
    @FXML
    private Button buttonMenuEntryRemove;
    @FXML
    private Button buttonMenuEntryAvailable;

    @Autowired
    private MenuService menuService;
    @Autowired
    private Validator<MenuEntry> menuEntryValidator;

    @Resource(name = "menuEntryDialogPane")
    private FXMLPane menuEntryDialogPane;
    private MenuEntryDialogController menuEntryDialogController;

    private ObservableList<MenuEntry> menuEntries = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        menuEntryDialogController = menuEntryDialogPane.getController(MenuEntryDialogController.class);

        tableViewMenuEntry.setItems(menuEntries);
        tableViewMenuEntry.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        tableColMenuEntryId.setCellValueFactory(new PropertyValueFactory<>("identity"));
        tableColMenuEntryName.setCellValueFactory(new PropertyValueFactory<>("name"));
        tableColMenuEntryPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        tableColMenuEntryAvailable.setCellValueFactory(new PropertyValueFactory<>("available"));
        tableColMenuEntryCategory.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getCategory().getName()));

        tableViewMenuEntry.getSelectionModel().selectedIndexProperty().addListener(event -> {
            final boolean hasSelection = tableViewMenuEntry.getSelectionModel().getSelectedIndex() >= 0;

            buttonMenuEntryUpdate.setDisable(!hasSelection);
            buttonMenuEntryRemove.setDisable(!hasSelection);
            buttonMenuEntryAvailable.setDisable(!hasSelection);
        });

        loadAllMenuEntries();
    }

    @FXML
    public void buttonMenuEntryRemoveClicked(ActionEvent actionEvent) {
        List<MenuEntry> selectedMenuEntries = tableViewMenuEntry.getSelectionModel().getSelectedItems();
        for (MenuEntry menuEntry : selectedMenuEntries) {
            try {
                menuService.removeMenuEntry(menuEntry);
            } catch (ValidationException | ServiceException e) {
                LOGGER.error("Could not delete menu entry " + menuEntry, e);

                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error while deleting menu entry");
                alert.setHeaderText("Could not delete menu entry '" + menuEntry.getName() + "'");
                alert.setContentText(e.getMessage());
                alert.showAndWait();
            }
        }
        menuEntries.removeAll(selectedMenuEntries);
    }

    @FXML
    public void buttonMenuEntrySearchClicked(ActionEvent actionEvent) {
        SearchInputDialog<MenuEntry> menuEntrySearchDialog = new SearchInputDialog<>("menu entries");
        menuEntrySearchDialog.setContent(menuEntryDialogPane);
        menuEntrySearchDialog.setController(menuEntryDialogController);
        menuEntrySearchDialog.showAndWait().ifPresent(menuEntryMatcher -> {
            try {
                menuEntries.setAll(menuService.findMenuEntry(menuEntryMatcher));
            } catch (ServiceException e) {
                LOGGER.error("Could not search for menu entries with matcher " + menuEntryMatcher, e);

                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error while searching for menu entries");
                alert.setHeaderText("Could not search for menu entries");
                alert.setContentText(e.getMessage());
                alert.showAndWait();
            }
        });
    }

    @FXML
    public void buttonMenuEntryUpdateClicked(ActionEvent actionEvent) {
        MenuEntry selectedMenuEntry = tableViewMenuEntry.getSelectionModel().getSelectedItem();
        if (selectedMenuEntry != null) {
            UpdateInputDialog<MenuEntry> menuEntryInputDialog = new UpdateInputDialog<>("menu entry", selectedMenuEntry);
            menuEntryInputDialog.setValidator(menuEntryValidator);
            menuEntryInputDialog.setContent(menuEntryDialogPane);
            menuEntryInputDialog.setController(menuEntryDialogController);
            menuEntryInputDialog.showAndWait().ifPresent(editedMenuEntry -> {
                try {
                    menuService.updateMenuEntry(editedMenuEntry);
                    menuEntries.remove(selectedMenuEntry);
                    menuEntries.add(editedMenuEntry);
                } catch (ValidationException | ServiceException e) {
                    LOGGER.error("Could not change menu entry " + editedMenuEntry, e);

                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error while updating menu entry");
                    alert.setHeaderText("Could not update menu entry '" + selectedMenuEntry.getName() + "'");
                    alert.setContentText(e.getMessage());
                    alert.showAndWait();
                }
            });
        }
    }

    @FXML
    public void buttonMenuEntryAddClicked(ActionEvent actionEvent) {
        CreateInputDialog<MenuEntry> menuEntryInputDialog = new CreateInputDialog<>("menu entry");
        menuEntryInputDialog.setValidator(menuEntryValidator);
        menuEntryInputDialog.setContent(menuEntryDialogPane);
        menuEntryInputDialog.setController(menuEntryDialogController);
        menuEntryInputDialog.showAndWait().ifPresent(menuEntry -> {
            try {
                menuService.addMenuEntry(menuEntry);
                menuEntries.add(menuEntry);
            } catch (ValidationException | ServiceException e) {
                LOGGER.error("Could not add menu entry " + menuEntry, e);

                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error while adding menu entry");
                alert.setHeaderText("Could not add menu entry '" + menuEntry.getName() + "'");
                alert.setContentText(e.getMessage());
                alert.showAndWait();
            }
        });
    }

    @FXML
    public void buttonMenuEntryAvailableClicked(ActionEvent actionEvent) {
        List<MenuEntry> selectedMenuEntries = tableViewMenuEntry.getSelectionModel().getSelectedItems();
        for (MenuEntry menuEntry : selectedMenuEntries) {
            try {
                MenuEntry editedMenuEntry = menuEntry.clone();
                editedMenuEntry.setAvailable(true);
                menuService.updateMenuEntry(editedMenuEntry);

                menuEntries.remove(menuEntry);
                menuEntries.add(editedMenuEntry);
            } catch (ValidationException | ServiceException e) {
                LOGGER.error("Could not reset availability of menu entry " + menuEntry, e);

                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error while resetting the availability a menu entry");
                alert.setHeaderText("Could not reset the availability of the menu entry '" + menuEntry.getName() + "'");
                alert.setContentText(e.getMessage());
                alert.showAndWait();
            }
        }
    }

    @FXML
    public void buttonShowAllMenuEntryClicked(ActionEvent actionEvent) {
        loadAllMenuEntries();
    }

    private void loadAllMenuEntries() {
        try {
            menuEntries.setAll(menuService.getAllMenuEntries());
        } catch (ServiceException e){
            LOGGER.error("Could not load all menu entries", e);
        }
    }
}
