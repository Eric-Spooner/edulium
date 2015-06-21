package com.at.ac.tuwien.sepm.ss15.edulium.gui;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.Menu;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.MenuEntry;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.TaxRate;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.User;
import com.at.ac.tuwien.sepm.ss15.edulium.service.MenuService;
import com.at.ac.tuwien.sepm.ss15.edulium.service.ServiceException;
import com.at.ac.tuwien.sepm.ss15.edulium.service.TaxRateService;
import com.at.ac.tuwien.sepm.ss15.edulium.service.UserService;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;

import javax.annotation.Resource;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ResourceBundle;

import static javafx.collections.FXCollections.observableArrayList;

/**
 * Created by - on 12.06.2015.
 */
public class MenuEntryViewController implements Initializable, Controller {
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

    @Resource(name = "menuEntryDialogPane")
    private FXMLPane menuEntryDialogPane;


    @Autowired
    private MenuService menuService;
    @Autowired
    private TaxRateService taxRateService;
    @Autowired
    private TaskScheduler taskScheduler;

    private ObservableList<MenuEntry> menuEntries;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // queued
        try {
            menuEntries = observableArrayList(menuService.getAllMenuEntries());
            tableViewMenuEntry.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
            tableViewMenuEntry.setItems(menuEntries);
            tableColMenuEntryId.setCellValueFactory(new PropertyValueFactory<MenuEntry, Long>("identity"));
            tableColMenuEntryName.setCellValueFactory(new PropertyValueFactory<MenuEntry, String>("name"));
            tableColMenuEntryPrice.setCellValueFactory(new PropertyValueFactory<MenuEntry, BigDecimal>("price"));
            tableColMenuEntryCategory.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<MenuEntry, String>, ObservableValue<String>>() {
                public ObservableValue<String> call(TableColumn.CellDataFeatures<MenuEntry, String> p) {
                    // p.getValue() returns the Person instance for a particular TableView row
                    return new SimpleStringProperty(p.getValue().getCategory().getName());
                }
            });
        }catch (ServiceException e){
            LOGGER.error("Initialize Menu Entry View Failed due to" + e);
        }

    }

    public void buttonMenuEntryRemoveClicked(ActionEvent actionEvent) {
        try {
            LOGGER.info("Delete MenuEntry Button Click");
            if(tableViewMenuEntry.getSelectionModel().getSelectedItem() == null){
                ManagerViewController.showErrorDialog
                        ("Error", "Input Validation Error", "You have to select a MenuEntry to Delete");
                return;
            }
            menuService.removeMenuEntry(tableViewMenuEntry.getSelectionModel().getSelectedItem());
            menuEntries.setAll(menuService.getAllMenuEntries());
        }catch (Exception e){
            LOGGER.error("Loading the Menus Entries failed" + e);
        }
    }

    public void buttonMenuEntrySearchClicked(ActionEvent actionEvent) {
        try {
            LOGGER.info("Search MenuEntry Button Click");
            Stage stage = new Stage();
            DialogMenuEntryController.resetDialog();
            DialogMenuEntryController.setThisStage(stage);
            DialogMenuEntryController.setDialogEnumeration(DialogEnumeration.SEARCH);
            stage.setTitle("Search MenuEntry");
            Scene scene = new Scene(menuEntryDialogPane);
            stage.setScene(scene);
            stage.showAndWait();
            if(DialogMenuEntryController.getMenuEntry() != null){
                menuEntries.setAll(menuService.findMenuEntry(DialogMenuEntryController.getMenuEntry()));
            }else {
                menuEntries.setAll(menuService.getAllMenuEntries());
            }
            DialogMenuEntryController.resetDialog();
        }catch (Exception e){
            LOGGER.error("Loading the Menus Entries failed" + e);
        }
    }

    public void buttonMenuEntryUpdateClicked(ActionEvent actionEvent) {
        try {
            LOGGER.info("Update MenuEntry Button Click");
            Stage stage = new Stage();
            if(tableViewMenuEntry.getSelectionModel().getSelectedItem() == null){
                ManagerViewController.showErrorDialog
                        ("Error", "Input Validation Error", "You have to select a MenuEntry to Update");
                return;
            }
            if(tableViewMenuEntry.getSelectionModel().getSelectedItems().size() >1){
                ManagerViewController.showErrorDialog
                        ("Error", "Input Validation Error", "You have to select only one MenuEntry to Update");
                return;
            }
            DialogMenuEntryController.resetDialog();
            DialogMenuEntryController.setThisStage(stage);
            DialogMenuEntryController.setDialogEnumeration(DialogEnumeration.UPDATE);
            DialogMenuEntryController.setMenuEntry(tableViewMenuEntry.getSelectionModel().getSelectedItem());
            stage.setTitle("Update MenuEntry");
            Scene scene = new Scene(menuEntryDialogPane);
            stage.setScene(scene);
            stage.showAndWait();
            menuEntries.setAll(menuService.getAllMenuEntries());
            DialogMenuEntryController.resetDialog();
        }catch (Exception e){
            LOGGER.error("Loading the Menus Entries failed" + e);
        }
    }

    public void buttonMenuEntryAddClicked(ActionEvent actionEvent) {
        try {
            LOGGER.info("Add MenuEntry Button Click");
            Stage stage = new Stage();
            DialogMenuEntryController.resetDialog();
            DialogMenuEntryController.setThisStage(stage);
            DialogMenuEntryController.setDialogEnumeration(DialogEnumeration.ADD);
            stage.setTitle("Insert MenuEntry");
            Scene scene = new Scene(menuEntryDialogPane);
            stage.setScene(scene);
            stage.showAndWait();
            menuEntries.setAll(menuService.getAllMenuEntries());
            DialogMenuEntryController.resetDialog();
        }catch (Exception e){
            LOGGER.error("Loading the Menus Entries failed" + e);
        }
    }

    public void buttonMenuEntryAvailableClicked(ActionEvent actionEvent) {
        try {
            LOGGER.info("Update MenuEntry Button Click");
            if (tableViewMenuEntry.getSelectionModel().getSelectedItem() == null) {
                ManagerViewController.showErrorDialog
                        ("Error", "Input Validation Error", "You have to select a MenuEntry to Update");
                return;
            }
            for(MenuEntry menuEntry: tableViewMenuEntry.getSelectionModel().getSelectedItems()){
                menuEntry.setAvailable(true);
                menuService.updateMenuEntry(menuEntry);
            }
            menuEntries.setAll(menuService.getAllMenuEntries());
        } catch (Exception e){
            LOGGER.error("Loading the Menus Entries failed" + e);
        }
    }

    public void buttonShowAllMenuEntryClicked(ActionEvent actionEvent) {
        try {
            menuEntries.setAll(menuService.getAllMenuEntries());
        } catch (Exception e){
            LOGGER.error("Loading All Menu Entries failed" + e);
        }
    }


    @Override
    public void disable(boolean disabled) {

    }
}
