package com.at.ac.tuwien.sepm.ss15.edulium.gui;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.Menu;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.MenuCategory;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.MenuEntry;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.TaxRate;
import com.at.ac.tuwien.sepm.ss15.edulium.service.MenuService;
import com.at.ac.tuwien.sepm.ss15.edulium.service.ServiceException;
import com.at.ac.tuwien.sepm.ss15.edulium.service.TaxRateService;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import static javafx.collections.FXCollections.observableArrayList;

/**
 * Controller used for the Manager View
 */
@Component
public class ManagerController implements Initializable {
    private static final Logger LOGGER = LogManager.getLogger(ManagerController.class);

    private MenuService menuService;
    private TaxRateService taxRateService;

    @FXML
    private TableView<TaxRate> tableViewTaxRate;
    @FXML
    private TableColumn<TaxRate,Long> tableColTaxRateID;
    @FXML
    private TableColumn<TaxRate,BigDecimal> tableColTaxRateValue;
    @FXML
    private TextField txtTaxRateValue;

    @FXML
    private TableView<MenuCategory> tableViewMenuCategory;
    @FXML
    private TableColumn<MenuCategory,Long> tableColMenuCategoryID;
    @FXML
    private TableColumn<MenuCategory,String> tableColMenuCategoryName;

    @FXML
    private TableView<MenuEntry> tableViewMenuEntry;
    @FXML
    private TableColumn<MenuEntry,Long> tableColMenuEntryId;
    @FXML
    private TableColumn<MenuEntry,String> tableColMenuEntryName;
    @FXML
    private TableColumn<MenuEntry,BigDecimal> tableColMenuEntryPrice;
    @FXML
    private TableColumn<MenuEntry, MenuCategory> tableColMenuEntryCategory;
    @FXML
    private TableColumn<MenuEntry, Boolean> tableColMenuEntryAvailable;

    @FXML
    private TableView<Menu> tableViewMenu;
    @FXML
    private TableColumn<Menu,Long> tableColMenuId;
    @FXML
    private TableColumn<Menu,String> tableColMenuName;
    @FXML
    private TableColumn<Menu,List<MenuEntry>> tableColMenuEntries;


    private ObservableList<TaxRate> taxRates;
    private ObservableList<Menu> menus;
    private ObservableList<MenuEntry> menuEntries;
    private ObservableList<MenuCategory> menuCategories;

    @Override
    public void initialize(URL location, ResourceBundle resources){
        try {
            ApplicationContext context = new ClassPathXmlApplicationContext("spring/Spring-Service.xml");
            menuService = context.getBean("menuService", MenuService.class);
            taxRateService = context.getBean("taxRateService",  TaxRateService.class);

            taxRates = observableArrayList(taxRateService.getAllTaxRates());
            tableViewTaxRate.setItems(taxRates);
            tableColTaxRateID.setCellValueFactory(new PropertyValueFactory<TaxRate, Long>("identity"));
            tableColTaxRateValue.setCellValueFactory(new PropertyValueFactory<TaxRate, BigDecimal>("value"));

            menuEntries = observableArrayList(menuService.getAllMenuEntries());
            tableViewMenuEntry.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
            tableViewMenuEntry.setItems(menuEntries);
            tableColMenuEntryId.setCellValueFactory(new PropertyValueFactory<MenuEntry, Long>("identity"));
            tableColMenuEntryName.setCellValueFactory(new PropertyValueFactory<MenuEntry, String>("name"));
            tableColMenuEntryPrice.setCellValueFactory(new PropertyValueFactory<MenuEntry, BigDecimal>("price"));
            tableColMenuEntryCategory.setCellValueFactory(new PropertyValueFactory<MenuEntry, MenuCategory>("category"));
            tableColMenuEntryAvailable.setCellValueFactory(new PropertyValueFactory<MenuEntry, Boolean>("available"));

            menuCategories = observableArrayList(menuService.getAllMenuCategories());
            tableViewMenuCategory.setItems(menuCategories);
            tableColMenuCategoryID.setCellValueFactory(new PropertyValueFactory<MenuCategory, Long>("identity"));
            tableColMenuCategoryName.setCellValueFactory(new PropertyValueFactory<MenuCategory, String>("name"));

            menus = observableArrayList(menuService.getAllMenus());
            tableViewMenu.setItems(menus);
            tableColMenuId.setCellValueFactory(new PropertyValueFactory<Menu, Long>("identity"));
            tableColMenuName.setCellValueFactory(new PropertyValueFactory<Menu, String>("name"));
            tableColMenuEntries.setCellValueFactory(new PropertyValueFactory<Menu, List<MenuEntry>>("entries"));
        }catch (Exception e){
            LOGGER.error("Initialize Manager View Fail" + e);
        }
    }

    public void buttonEmployeesAddClicked(ActionEvent actionEvent) {
    }

    public void buttonEmployeesUpdateClicked(ActionEvent actionEvent) {
    }

    public void buttonEmployeesSearchClicked(ActionEvent actionEvent) {
    }

    public void buttonEmployeesRemoveClicked(ActionEvent actionEvent) {
    }

    public void buttonMenuUpdateClicked(ActionEvent actionEvent) {
        try {
            LOGGER.info("Update Menu Button Click");
            Stage stage = new Stage();
            if(tableViewMenu.getSelectionModel().getSelectedItem() == null){
                ManagerController.showErrorDialog
                        ("Error", "Input Validation Error", "You have to select a Menu to Update");
                return;
            }
            DialogMenuController.resetDialog();
            DialogMenuController.setThisStage(stage);
            DialogMenuController.setMenuService(menuService);
            DialogMenuController.setDialogEnumeration(DialogEnumeration.UPDATE);
            DialogMenuController.setMenu(tableViewMenu.getSelectionModel().getSelectedItem());
            stage.setTitle("Update Menu");
            Pane myPane = FXMLLoader.load(getClass().getResource("/gui/DialogMenu.fxml"));
            Scene scene = new Scene(myPane);
            stage.setScene(scene);
            stage.showAndWait();
            menus.setAll(menuService.getAllMenus());
            DialogMenuController.resetDialog();
        }catch (IOException e){
            LOGGER.error("Add Menu Button Click did not work");
        }catch (Exception e){
            LOGGER.error("Loading the Menus failed" + e);
        }
    }

    public void buttonMenuSearchClicked(ActionEvent actionEvent) {
        try {
            LOGGER.info("Search Menu Button Click");
            Stage stage = new Stage();
            DialogMenuController.resetDialog();
            DialogMenuController.setThisStage(stage);
            DialogMenuController.setMenuService(menuService);
            DialogMenuController.setDialogEnumeration(DialogEnumeration.SEARCH);
            stage.setTitle("Search Menu");
            Pane myPane = FXMLLoader.load(getClass().getResource("/gui/DialogMenu.fxml"));
            Scene scene = new Scene(myPane);
            stage.setScene(scene);
            stage.showAndWait();
            if(DialogMenuController.getMenu() != null){
                menus.setAll(menuService.findMenu(DialogMenuController.getMenu()));
            }else {
                menus.setAll(menuService.getAllMenus());
            }
            DialogMenuController.resetDialog();
        }catch (IOException e){
            LOGGER.error("Search Menu Button Click did not work" + e);
        }catch (ServiceException e){
            LOGGER.error("Menu Service finding Menus did not work" + e);
        }
    }

    public void buttonMenuRemoveClicked(ActionEvent actionEvent) {
        try {
            LOGGER.info("Delete Menu Button Click");
            if(tableViewMenu.getSelectionModel().getSelectedItem() == null){
                ManagerController.showErrorDialog
                        ("Error", "Input Validation Error", "You have to select a Menu to Delete");
                return;
            }
            menuService.removeMenu(tableViewMenu.getSelectionModel().getSelectedItem());
            menus.setAll(menuService.getAllMenus());
        }catch (Exception e){
            LOGGER.error("Loading the Menus failed" + e);
        }
    }

    public void buttonMenuAddClicked(ActionEvent actionEvent){
        try {
            LOGGER.info("Add Menu Button Click");
            Stage stage = new Stage();
            DialogMenuController.resetDialog();
            DialogMenuController.setThisStage(stage);
            DialogMenuController.setMenuService(menuService);
            DialogMenuController.setDialogEnumeration(DialogEnumeration.ADD);
            stage.setTitle("Add Menu");
            Pane myPane = FXMLLoader.load(getClass().getResource("/gui/DialogMenu.fxml"));
            Scene scene = new Scene(myPane);
            stage.setScene(scene);
            stage.showAndWait();
            menus.setAll(menuService.getAllMenus());
        }catch (IOException e){
            LOGGER.error("Add Menu Button Click did not work");
        }catch (Exception e){
            LOGGER.error("Loading the Menus failed" + e);
        }
    }

    public void buttonMenuEntryRemoveClicked(ActionEvent actionEvent) {
        try {
            LOGGER.info("Delete MenuEntry Button Click");
            if(tableViewMenuEntry.getSelectionModel().getSelectedItem() == null){
                ManagerController.showErrorDialog
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
            DialogMenuEntryController.setMenuService(menuService);
            DialogMenuEntryController.setTaxRateService(taxRateService);
            DialogMenuEntryController.setDialogEnumeration(DialogEnumeration.SEARCH);
            stage.setTitle("Search MenuEntry");
            Pane myPane = FXMLLoader.load(getClass().getResource("/gui/DialogMenuEntry.fxml"));
            Scene scene = new Scene(myPane);
            stage.setScene(scene);
            stage.showAndWait();
            if(DialogMenuEntryController.getMenuEntry() != null){
                menuEntries.setAll(menuService.findMenuEntry(DialogMenuEntryController.getMenuEntry()));
            }else {
                menuEntries.setAll(menuService.getAllMenuEntries());
            }
            DialogMenuEntryController.resetDialog();
        }catch (IOException e){
            LOGGER.error("Search MenuEntry Button Click did not work");
        }catch (Exception e){
            LOGGER.error("Loading the Menus Entries failed" + e);
        }
    }

    public void buttonMenuEntryUpdateClicked(ActionEvent actionEvent) {
        try {
            LOGGER.info("Update MenuEntry Button Click");
            Stage stage = new Stage();
            if(tableViewMenuEntry.getSelectionModel().getSelectedItem() == null){
                ManagerController.showErrorDialog
                        ("Error", "Input Validation Error", "You have to select a MenuEntry to Update");
                return;
            }
            if(tableViewMenuEntry.getSelectionModel().getSelectedItems().size() >1){
                ManagerController.showErrorDialog
                        ("Error", "Input Validation Error", "You have to select only one MenuEntry to Update");
                return;
            }
            DialogMenuEntryController.resetDialog();
            DialogMenuEntryController.setThisStage(stage);
            DialogMenuEntryController.setMenuService(menuService);
            DialogMenuEntryController.setTaxRateService(taxRateService);
            DialogMenuEntryController.setDialogEnumeration(DialogEnumeration.UPDATE);
            DialogMenuEntryController.setMenuEntry(tableViewMenuEntry.getSelectionModel().getSelectedItem());
            stage.setTitle("Update MenuEntry");
            Pane myPane = FXMLLoader.load(getClass().getResource("/gui/DialogMenuEntry.fxml"));
            Scene scene = new Scene(myPane);
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
            DialogMenuEntryController.setMenuService(menuService);
            DialogMenuEntryController.setTaxRateService(taxRateService);
            DialogMenuEntryController.setDialogEnumeration(DialogEnumeration.ADD);
            stage.setTitle("Insert MenuEntry");
            Pane myPane = FXMLLoader.load(getClass().getResource("/gui/DialogMenuEntry.fxml"));
            Scene scene = new Scene(myPane);
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
                ManagerController.showErrorDialog
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

    public void buttonMenuCategoryRemoveClicked(ActionEvent actionEvent) {
        try {
            LOGGER.info("Delete MenuCategory Button Click");
            if(tableViewMenuCategory.getSelectionModel().getSelectedItem() == null){
                ManagerController.showErrorDialog
                        ("Error", "Input Validation Error", "You have to select a Menu Category to Delete");
                return;
            }
            menuService.removeMenuCategory(tableViewMenuCategory.getSelectionModel().getSelectedItem());
            menuCategories.setAll(menuService.getAllMenuCategories());
        }catch (Exception e){
            LOGGER.error("Loading the Menus Categories failed" + e);
        }
    }

    public void buttonMenuCategorySearchClicked(ActionEvent actionEvent) {
        try {
            LOGGER.info("Search MenuCategory Button Click");
            Stage stage = new Stage();
            DialogMenuCategoryController.setThisStage(stage);
            DialogMenuCategoryController.setMenuService(menuService);
            DialogMenuCategoryController.setDialogEnumeration(DialogEnumeration.SEARCH);
            stage.setTitle("Search Menu Category");
            Pane myPane = FXMLLoader.load(getClass().getResource("/gui/DialogMenuCategory.fxml"));
            Scene scene = new Scene(myPane);
            stage.setScene(scene);
            stage.showAndWait();
            if (DialogMenuCategoryController.getMenuCategory() != null) {
                menuCategories.setAll(menuService.findMenuCategory(DialogMenuCategoryController.getMenuCategory()));
            } else {
                menuCategories.setAll(menuService.getAllMenuCategories());
            }
            DialogMenuCategoryController.resetDialog();
        }catch (Exception e){
            LOGGER.error("Search MenuCategory Button Click did not work");
        }
    }

    public void buttonMenuCategoryUpdateClicked(ActionEvent actionEvent) {
        try {
            LOGGER.info("Update MenuCategory Button Click");
            Stage stage = new Stage();
            if(tableViewMenuCategory.getSelectionModel().getSelectedItem() == null){
                ManagerController.showErrorDialog
                        ("Error", "Input Validation Error", "You have to select a MenuCategory to Update");
                return;
            }
            DialogMenuCategoryController.setThisStage(stage);
            DialogMenuCategoryController.setMenuService(menuService);
            DialogMenuCategoryController.setDialogEnumeration(DialogEnumeration.UPDATE);
            DialogMenuCategoryController.setMenuCategory(tableViewMenuCategory.getSelectionModel().getSelectedItem());
            stage.setTitle("Update Menu Category");
            Pane myPane = FXMLLoader.load(getClass().getResource("/gui/DialogMenuCategory.fxml"));
            Scene scene = new Scene(myPane);
            stage.setScene(scene);
            stage.showAndWait();
            menuCategories.setAll(menuService.getAllMenuCategories());
            DialogMenuCategoryController.resetDialog();
        }catch (Exception e){
            LOGGER.error("Update MenuCategory Button Click did not work" + e);
        }
    }

    public void buttonMenuCategoryAddClicked(ActionEvent actionEvent) {
        try {
            LOGGER.info("Add MenuCategory Button Click");
            Stage stage = new Stage();
            DialogMenuCategoryController.setThisStage(stage);
            DialogMenuCategoryController.setMenuService(menuService);
            DialogMenuCategoryController.setDialogEnumeration(DialogEnumeration.ADD);
            stage.setTitle("Insert Menu Category");
            Pane myPane = FXMLLoader.load(getClass().getResource("/gui/DialogMenuCategory.fxml"));
            Scene scene = new Scene(myPane);
            stage.setScene(scene);
            stage.showAndWait();
            menuCategories.setAll(menuService.getAllMenuCategories());
            DialogMenuCategoryController.resetDialog();
        }catch (Exception e){
            LOGGER.error("Add MenuCategory Button Click did not work" + e);
        }
    }

    public void buttonTaxRateRemoveClicked(ActionEvent actionEvent) {
        try {
            LOGGER.info("Delete TaxRate Button Click");
            if(tableViewTaxRate.getSelectionModel().getSelectedItem() == null){
                ManagerController.showErrorDialog
                        ("Error", "Input Validation Error", "You have to select a Tax Rate to Delete");
                return;
            }
            taxRateService.removeTaxRate(tableViewTaxRate.getSelectionModel().getSelectedItem());
            taxRates.setAll(taxRateService.getAllTaxRates());
        }catch (Exception e){
            LOGGER.error("Loading the taxRates failed" + e);
        }
    }


    public void buttonTaxRateUpdateClicked(ActionEvent actionEvent) {
        try {
            LOGGER.info("Delete TaxRate Button Click");
            if(tableViewTaxRate.getSelectionModel().getSelectedItem() == null){
                ManagerController.showErrorDialog
                        ("Error", "Input Validation Error", "You have to select a Tax Rate to Delete");
                return;
            }
            BigDecimal value = BigDecimal.valueOf(0.0);
            try {
                value = BigDecimal.valueOf(Double.parseDouble(txtTaxRateValue.getText()));
            } catch (NumberFormatException e) {
                ManagerController.showErrorDialog("Error", "Input Validation Error", "Value must be a number");
                LOGGER.info("Tax Rate Value must be a number" + e);
            }
            TaxRate taxRate = tableViewTaxRate.getSelectionModel().getSelectedItem();
            taxRate.setValue(value);
            taxRateService.updateTaxRate(taxRate);
            taxRates.setAll(taxRateService.getAllTaxRates());
        }catch (Exception e){
            LOGGER.error("Update the taxRates failed" + e);
        }
    }

    public void buttonTaxRateAddClicked(ActionEvent actionEvent){
        try {
            LOGGER.info("Add TaxRate Button Click");
            BigDecimal value = BigDecimal.valueOf(0.0);
            try {
                value = BigDecimal.valueOf(Double.parseDouble(txtTaxRateValue.getText()));
                TaxRate taxRate = new TaxRate();
                taxRate.setValue(value);
                taxRateService.addTaxRate(taxRate);
                taxRates.setAll(taxRateService.getAllTaxRates());
            } catch (NumberFormatException e) {
                ManagerController.showErrorDialog("Error", "Input Validation Error", "Value must be a number");
                LOGGER.info("Tax Rate Value must be a number" + e);
            }
        }catch (Exception e){
            LOGGER.error("Update the taxRates failed" + e);
        }
    }

    public static void showErrorDialog(String title, String head, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(head);
        alert.setContentText(content);

        alert.showAndWait();
    }


    public void buttonShowAllMenuCategoryClicked(ActionEvent actionEvent) {
        try {
            menuCategories.setAll(menuService.getAllMenuCategories());
        } catch (Exception e){
            LOGGER.error("Loading All Menu Categories failed" + e);
        }
    }

    public void buttonShowAllMenuEntryClicked(ActionEvent actionEvent) {
        try {
            menuEntries.setAll(menuService.getAllMenuEntries());
        } catch (Exception e){
            LOGGER.error("Loading All Menu Entries failed" + e);
        }
    }

    public void buttonShowAllMenuClicked(ActionEvent actionEvent) {
        try {
            menus.setAll(menuService.getAllMenus());
        } catch (Exception e){
            LOGGER.error("Loading All Menu failed" + e);
        }
    }

    public void buttonTableOverview(ActionEvent actionEvent) {
        try {
            LOGGER.info("Add TaxRate Button Click");
            Stage stage = new Stage();
            stage.setTitle("TablesOverview");
            AnchorPane myPane = FXMLLoader.load(getClass().getResource("/gui/TablesOverview.fxml"));
            Scene scene = new Scene(myPane);
            stage.setScene(scene);
            stage.showAndWait();
        } catch (IOException e) {
            LOGGER.error("Unable to Load Tables Overview" + e);
        }
    }


}
