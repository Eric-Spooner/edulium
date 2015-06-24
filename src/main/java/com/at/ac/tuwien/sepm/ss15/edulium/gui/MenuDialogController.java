package com.at.ac.tuwien.sepm.ss15.edulium.gui;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.Menu;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.MenuEntry;
import com.at.ac.tuwien.sepm.ss15.edulium.service.MenuService;
import com.at.ac.tuwien.sepm.ss15.edulium.service.ServiceException;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Controller for the TaxRate Dialog
 */
@Controller
public class MenuDialogController implements Initializable, InputDialogController<Menu> {
    private static final Logger LOGGER = LogManager.getLogger(MenuDialogController.class);

    @Autowired
    private MenuService menuService;

    @FXML
    private TextField textFieldName;
    @FXML
    private TextField textFieldPrice;
    @FXML
    private Button buttonAdd;
    @FXML
    private Button buttonRemove;

    @FXML
    private TableView<MenuEntry> tableViewAll;
    @FXML
    private TableColumn<MenuEntry, String> tableColNameData;
    @FXML
    private TableColumn<MenuEntry, String> tableColCategoryData;
    @FXML
    private TableColumn<MenuEntry, BigDecimal> tableColPriceData;


    @FXML
    private TableView<MenuEntry> tableViewInMenu;
    @FXML
    private TableColumn<MenuEntry, String> tableColNameInMenu;
    @FXML
    private TableColumn<MenuEntry, String> tableColCategoryInMen;
    @FXML
    private TableColumn<MenuEntry, BigDecimal> tableColPriceInMen;

    private ObservableList<MenuEntry> allMenuEntries;
    private final ObservableList<MenuEntry> selectedMenuEntries = FXCollections.observableArrayList();

    private Long identity = null;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        LOGGER.info("Initialize Dialog Menu");

        textFieldPrice.textProperty().addListener(t -> updateAddButton());


        // setup table which contains all menuEntries
        tableColNameData.setCellValueFactory(new PropertyValueFactory<>("name"));
        tableColCategoryData.setCellValueFactory(p -> {
            // p.getValue() returns the Person instance for a particular TableView row
            return new SimpleStringProperty(p.getValue().getCategory().getName());
        });
        tableColPriceData.setCellValueFactory(new PropertyValueFactory<>("price"));

        allMenuEntries = FXCollections.observableArrayList(getAllMenuEntries());
        tableViewAll.setItems(allMenuEntries);

        // setup table which contains added menuEntries
        tableColNameInMenu.setCellValueFactory(new PropertyValueFactory<>("name"));
        tableColCategoryInMen.setCellValueFactory(p -> {
            // p.getValue() returns the Person instance for a particular TableView row
            return new SimpleStringProperty(p.getValue().getCategory().getName());
        });
        tableColPriceInMen.setCellValueFactory(new PropertyValueFactory<>("price"));

        tableViewInMenu.setItems(selectedMenuEntries);
    }

    @Override
    public void prepareForCreate() {
        resetDialog();
    }

    @Override
    public void prepareForUpdate(Menu menu) {
        resetDialog();

        identity = menu.getIdentity();
        selectedMenuEntries.setAll(menu.getEntries());

        textFieldName.setText(menu.getName());
    }

    @Override
    public void prepareForSearch() {
        resetDialog();
        textFieldPrice.setVisible(false);
        tableColPriceInMen.setVisible(false);
        textFieldPrice.setText("0.0");
    }

    @Override
    public Menu toDomainObject() {
        Menu menu = new Menu();
        menu.setIdentity(identity);
        menu.setEntries(selectedMenuEntries.isEmpty() ? null : new ArrayList<>(selectedMenuEntries));
        menu.setName(textFieldName.getText().isEmpty() ? null : textFieldName.getText());
        return menu;
    }

    @FXML
    public void buttonAddClick() {
        MenuEntry selectedEntry = tableViewAll.getSelectionModel().getSelectedItem();

        if(selectedEntry != null) {
            BigDecimal entryPrice;
            try {
                entryPrice = new BigDecimal(textFieldPrice.getText());
            } catch (NumberFormatException e) {
                LOGGER.error("invalid number", e);

                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Invalid Number");
                alert.setHeaderText("'" + textFieldPrice.getText() + " is not a number");
                alert.setContentText(e.getMessage());
                alert.showAndWait();
                return;
            }
            allMenuEntries.remove(selectedEntry);

            selectedEntry.setPrice(entryPrice);
            selectedMenuEntries.add(selectedEntry);
        }
    }

    @FXML
    public void buttonRemoveClick() {
        MenuEntry selectedEntry = tableViewInMenu.getSelectionModel().getSelectedItem();
        if(selectedEntry != null) {
            selectedMenuEntries.remove(selectedEntry);
            // load menu entries to display original price
            MenuEntry entry = getMenuEntryByIdentity(selectedEntry.getIdentity());
            if(entry != null) {
                allMenuEntries.add(entry);
            }
        }
    }

    @FXML
    public void on_tableViewAll_clicked() {
        updateAddButton();
    }

    @FXML
    public void on_tableViewInMenu_clicked() {
        buttonRemove.setDisable(tableViewInMenu.getSelectionModel().isEmpty());
    }

    private void resetDialog() {
        tableColPriceInMen.setVisible(true);
        textFieldPrice.setVisible(true);
        textFieldName.setText("");
        textFieldPrice.setText("");
        selectedMenuEntries.clear();
        allMenuEntries.setAll(getAllMenuEntries());
        identity = null;
    }

    private void updateAddButton() {
        buttonAdd.setDisable(tableViewAll.getSelectionModel().isEmpty() || textFieldPrice.getText().isEmpty());
    }

    private MenuEntry getMenuEntryByIdentity(long id) {
        List<MenuEntry> entries;
        try {
            entries = menuService.findMenuEntry(MenuEntry.withIdentity(id));
        } catch (ServiceException e) {
            LOGGER.error("Could not read menu entries", e);

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error while reading menu entries");
            alert.setHeaderText("Could not read menu entries");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
            return null;
        }

        if(entries.size() != 1) {
            return null;
        }

        return entries.get(0);
    }

    private List<MenuEntry> getAllMenuEntries() {
        try {
            return menuService.getAllMenuEntries();
        } catch (ServiceException e) {
            LOGGER.error("Could not read menu entries", e);

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error while reading menu entries");
            alert.setHeaderText("Could not read menu entries");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }

        return new ArrayList<>();
    }

}
