package com.at.ac.tuwien.sepm.ss15.edulium.gui;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.Menu;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.MenuCategory;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.MenuEntry;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.TaxRate;
import com.at.ac.tuwien.sepm.ss15.edulium.service.InteriorService;
import com.at.ac.tuwien.sepm.ss15.edulium.service.MenuService;
import com.at.ac.tuwien.sepm.ss15.edulium.service.ServiceException;
import com.at.ac.tuwien.sepm.ss15.edulium.service.TaxRateService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
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
    private InteriorService interiorService;

    @FXML
    private TableView<TaxRate> tableViewTaxRate;
    @FXML
    private TableColumn<TaxRate,Long> tableColTaxRateID;
    @FXML
    private TableColumn<TaxRate,BigDecimal> tableColTaxRateValue;

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
    private TableColumn<MenuEntry,TaxRate> tableColMenuEntryTaxRate;

    @FXML
    private TableView<Menu> tableViewMenu;
    @FXML
    private TableColumn<Menu,Long> tableColMenuId;
    @FXML
    private TableColumn<Menu,String> tableColMenuName;
    @FXML
    private TableColumn<Menu,List<MenuEntry>> tableColMenuEntries;



    @Override
    public void initialize(URL location, ResourceBundle resources){
        try {
            ApplicationContext context = new ClassPathXmlApplicationContext("spring/Spring-Service.xml");
            menuService = context.getBean("menuService", MenuService.class);
            taxRateService = context.getBean("taxRateService",  TaxRateService.class);
            interiorService = context.getBean("interiorService", InteriorService.class);

            showTaxRate(taxRateService.getAllTaxRates());
            tableColTaxRateID.setCellValueFactory(new PropertyValueFactory<TaxRate, Long>("identity"));
            tableColTaxRateValue.setCellValueFactory(new PropertyValueFactory<TaxRate, BigDecimal>("value"));

            showMenuCategories(menuService.getAllMenuCategories());
            tableViewMenuCategory.setItems(observableArrayList(menuService.getAllMenuCategories()));
            tableColMenuCategoryID.setCellValueFactory(new PropertyValueFactory<MenuCategory, Long>("identity"));
            tableColMenuCategoryName.setCellValueFactory(new PropertyValueFactory<MenuCategory, String>("name"));

            showMenuEntries(menuService.getAllMenuEntries());
            tableColMenuEntryId.setCellValueFactory(new PropertyValueFactory<MenuEntry, Long>("identity"));
            tableColMenuEntryName.setCellValueFactory(new PropertyValueFactory<MenuEntry, String>("name"));
            tableColMenuEntryPrice.setCellValueFactory(new PropertyValueFactory<MenuEntry, BigDecimal>("price"));
            tableColMenuEntryCategory.setCellValueFactory(new PropertyValueFactory<MenuEntry, MenuCategory>("category"));
            tableColMenuEntryTaxRate.setCellValueFactory(new PropertyValueFactory<MenuEntry, TaxRate>("taxRate"));

            showMenus(menuService.getAllMenus());
            tableColMenuId.setCellValueFactory(new PropertyValueFactory<Menu, Long>("identity"));
            tableColMenuName.setCellValueFactory(new PropertyValueFactory<Menu, String>("name"));
            tableColMenuEntries.setCellValueFactory(new PropertyValueFactory<Menu, List<MenuEntry>>("entries"));
        }catch (Exception e){
            LOGGER.error("Initialize Manager View Fail" + e);
        }
    }
    private void showTaxRate(List<TaxRate> taxRates) {
        tableViewTaxRate.setItems(observableArrayList(taxRates));
    }
    private void showMenuCategories(List<MenuCategory> menuCategories) {
        tableViewMenuCategory.setItems(observableArrayList(menuCategories));
    }
    private void showMenus(List<Menu> menus) {
        tableViewMenu.setItems(observableArrayList(menus));
    }
    private void showMenuEntries(List<MenuEntry> menuEntries) {
        tableViewMenuEntry.setItems(observableArrayList(menuEntries));
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
            showMenus(menuService.getAllMenus());
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
                showMenus(menuService.findMenu(DialogMenuController.getMenu()));
            }else {
                showMenus(menuService.getAllMenus());
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
            showMenus(menuService.getAllMenus());
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
            showMenus(menuService.getAllMenus());
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
            showMenuEntries(menuService.getAllMenuEntries());
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
                showMenuEntries(menuService.findMenuEntry(DialogMenuEntryController.getMenuEntry()));
            }else {
                showMenuEntries(menuService.getAllMenuEntries());
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
            showMenuEntries(menuService.getAllMenuEntries());
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
            showMenuEntries(menuService.getAllMenuEntries());
            DialogMenuEntryController.resetDialog();
        }catch (Exception e){
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
            showMenuCategories(menuService.getAllMenuCategories());
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
                showMenuCategories(menuService.findMenuCategory(DialogMenuCategoryController.getMenuCategory()));
            } else {
                showMenuCategories(menuService.getAllMenuCategories());
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
            showMenuCategories(menuService.getAllMenuCategories());
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
            showMenuCategories(menuService.getAllMenuCategories());
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
            showTaxRate(taxRateService.getAllTaxRates());
        }catch (Exception e){
            LOGGER.error("Loading the taxRates failed" + e);
        }
    }

    public void buttonTaxRateSearchClicked(ActionEvent actionEvent) {
        try {
            LOGGER.info("Search TaxRate Button Click");
            Stage stage = new Stage();
            DialogTaxRateController.setThisStage(stage);
            DialogTaxRateController.setDialogEnumeration(DialogEnumeration.SEARCH);
            DialogTaxRateController.setTaxRateService(taxRateService);
            stage.setTitle("Search Tax Rate");
            Pane myPane = FXMLLoader.load(getClass().getResource("/gui/DialogTaxRate.fxml"));
            Scene scene = new Scene(myPane);
            stage.setScene(scene);
            stage.showAndWait();
            if(DialogTaxRateController.getTaxRate() != null){
                showTaxRate(taxRateService.findTaxRate(DialogTaxRateController.getTaxRate()));
            } else {
                showTaxRate(taxRateService.getAllTaxRates());
            }
            DialogTaxRateController.resetDialog();
        }catch (IOException e){
            LOGGER.error("Search TaxRate Button Click did not work");
        }catch (Exception e){
            LOGGER.error("Load all TaxRates did not work " + e);
        }
    }

    public void buttonTaxRateUpdateClicked(ActionEvent actionEvent) {
        try {
            LOGGER.info("Update TaxRate Button Click");
            Stage stage = new Stage();
            if(tableViewTaxRate.getSelectionModel().getSelectedItem() == null){
                ManagerController.showErrorDialog
                        ("Error", "Input Validation Error", "You have to select a TaxRate to Update");
                return;
            }
            DialogTaxRateController.setThisStage(stage);
            DialogTaxRateController.setDialogEnumeration(DialogEnumeration.UPDATE);
            DialogTaxRateController.setTaxRateService(taxRateService);
            DialogTaxRateController.setTaxRate(tableViewTaxRate.getSelectionModel().getSelectedItem() );
            stage.setTitle("Update Tax Rate");
            Pane myPane = FXMLLoader.load(getClass().getResource("/gui/DialogTaxRate.fxml"));
            Scene scene = new Scene(myPane);
            stage.setScene(scene);
            stage.showAndWait();
            showTaxRate(taxRateService.getAllTaxRates());
            DialogTaxRateController.resetDialog();
        }catch (IOException e){
            LOGGER.error("Update TaxRate Button Click did not work");
        }catch (Exception e){
            LOGGER.error("Load all TaxRates did not work " + e);
        }
    }

    public void buttonTaxRateAddClicked(ActionEvent actionEvent){
        try {
            LOGGER.info("Add TaxRate Button Click");
            Stage stage = new Stage();
            DialogTaxRateController.setThisStage(stage);
            DialogTaxRateController.setDialogEnumeration(DialogEnumeration.ADD);
            DialogTaxRateController.setTaxRateService(taxRateService);
            stage.setTitle("Insert Tax Rate");
            Pane myPane = FXMLLoader.load(getClass().getResource("/gui/DialogTaxRate.fxml"));
            Scene scene = new Scene(myPane);
            stage.setScene(scene);
            stage.showAndWait();
            showTaxRate(taxRateService.getAllTaxRates());
            DialogTaxRateController.resetDialog();
        }catch (IOException e){
            LOGGER.error("Add TaxRate Button Click did not work");
        }catch (Exception e){
            LOGGER.error("Load all TaxRates did not work " + e);
        }
    }

    public static void showErrorDialog(String title, String head, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(head);
        alert.setContentText(content);

        alert.showAndWait();
    }

    public void buttonShowAllTaxRateClicked(ActionEvent actionEvent) {

        try {
            showTaxRate(taxRateService.getAllTaxRates());
        } catch (Exception e){
            LOGGER.error("Loading All TaxRates failed" + e);
        }
    }

    public void buttonShowAllMenuCategoryClicked(ActionEvent actionEvent) {
        try {
            showMenuCategories(menuService.getAllMenuCategories());
        } catch (Exception e){
            LOGGER.error("Loading All Menu Categories failed" + e);
        }
    }

    public void buttonShowAllMenuEntryClicked(ActionEvent actionEvent) {
        try {
            showMenuEntries(menuService.getAllMenuEntries());
        } catch (Exception e){
            LOGGER.error("Loading All Menu Entries failed" + e);
        }
    }

    public void buttonShowAllMenuClicked(ActionEvent actionEvent) {
        try {
            showMenus(menuService.getAllMenus());
        } catch (Exception e){
            LOGGER.error("Loading All Menu failed" + e);
        }
    }

    public void buttonTableOverview(ActionEvent actionEvent) {
        try {
            LOGGER.info("Add TaxRate Button Click");
            Stage stage = new Stage();
            Controller.setInteriorService(interiorService);
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
