package com.at.ac.tuwien.sepm.ss15.edulium.gui;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.Menu;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.MenuCategory;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.MenuEntry;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.TaxRate;
import com.at.ac.tuwien.sepm.ss15.edulium.service.MenuService;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Callback;
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

            //tableViewTaxRate.setItems(observableArrayList(menuService.get));
            tableColTaxRateID.setCellValueFactory(new PropertyValueFactory<TaxRate, Long>("identity"));
            tableColTaxRateValue.setCellValueFactory(new PropertyValueFactory<TaxRate, BigDecimal>("value"));

            tableViewMenuCategory.setItems(observableArrayList(menuService.getAllMenuCategories()));
            tableColMenuCategoryID.setCellValueFactory(new PropertyValueFactory<MenuCategory, Long>("identity"));
            tableColMenuCategoryName.setCellValueFactory(new PropertyValueFactory<MenuCategory, String>("name"));

            tableViewMenuEntry.setItems(observableArrayList(menuService.getAllMenuEntries()));
            tableColMenuEntryId.setCellValueFactory(new PropertyValueFactory<MenuEntry, Long>("identity"));
            tableColMenuEntryName.setCellValueFactory(new PropertyValueFactory<MenuEntry, String>("name"));
            tableColMenuEntryPrice.setCellValueFactory(new PropertyValueFactory<MenuEntry, BigDecimal>("price"));
            tableColMenuEntryCategory.setCellValueFactory(new PropertyValueFactory<MenuEntry, MenuCategory>("category"));
            tableColMenuEntryTaxRate.setCellValueFactory(new PropertyValueFactory<MenuEntry, TaxRate>("taxRate"));

            tableViewMenu.setItems(observableArrayList(menuService.getAllMenus()));
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
            DialogMenuController.setThisStage(stage);
            DialogMenuController.setMenuService(menuService);
            stage.setTitle("Update Menu");
            Pane myPane = FXMLLoader.load(getClass().getResource("/gui/DialogMenu.fxml"));
            Scene scene = new Scene(myPane);
            stage.setScene(scene);
            stage.showAndWait();
        }catch (IOException e){
            LOGGER.error("Add Menu Button Click did not work");
        }
    }

    public void buttonMenuSearchClicked(ActionEvent actionEvent) {
        try {
            LOGGER.info("Search Menu Button Click");
            Stage stage = new Stage();
            DialogMenuController.setThisStage(stage);
            DialogMenuController.setMenuService(menuService);
            stage.setTitle("Search Menu");
            Pane myPane = FXMLLoader.load(getClass().getResource("/gui/DialogMenu.fxml"));
            Scene scene = new Scene(myPane);
            stage.setScene(scene);
            stage.showAndWait();
        }catch (IOException e){
            LOGGER.error("Search Menu Button Click did not work");
        }
    }

    public void buttonMenuRemoveClicked(ActionEvent actionEvent) {
    }

    public void buttonMenuAddClicked(ActionEvent actionEvent){
        try {
            LOGGER.info("Add Menu Button Click");
            Stage stage = new Stage();
            DialogMenuController.setThisStage(stage);
            DialogMenuController.setMenuService(menuService);
            stage.setTitle("Add Menu");
            Pane myPane = FXMLLoader.load(getClass().getResource("/gui/DialogMenu.fxml"));
            Scene scene = new Scene(myPane);
            stage.setScene(scene);
            stage.showAndWait();
        }catch (IOException e){
            LOGGER.error("Add Menu Button Click did not work");
        }
    }

    public void buttonMenuEntryRemoveClicked(ActionEvent actionEvent) {
    }

    public void buttonMenuEntrySearchClicked(ActionEvent actionEvent) {
        try {
            LOGGER.info("Search MenuEntry Button Click");
            Stage stage = new Stage();
            DialogMenuEntryController.setThisStage(stage);
            DialogMenuEntryController.setMenuService(menuService);
            stage.setTitle("Search MenuEntry");
            Pane myPane = FXMLLoader.load(getClass().getResource("/gui/DialogMenuEntry.fxml"));
            Scene scene = new Scene(myPane);
            stage.setScene(scene);
            stage.showAndWait();
        }catch (IOException e){
            LOGGER.error("Search MenuEntry Button Click did not work");
        }
    }

    public void buttonMenuEntryUpdateClicked(ActionEvent actionEvent) {
        try {
            LOGGER.info("Update MenuEntry Button Click");
            Stage stage = new Stage();
            DialogMenuEntryController.setThisStage(stage);
            DialogMenuEntryController.setMenuService(menuService);
            stage.setTitle("Update MenuEntry");
            Pane myPane = FXMLLoader.load(getClass().getResource("/gui/DialogMenuEntry.fxml"));
            Scene scene = new Scene(myPane);
            stage.setScene(scene);
            stage.showAndWait();
        }catch (IOException e){
            LOGGER.error("Update MenuEntry Button Click did not work");
        }
    }

    public void buttonMenuEntryAddClicked(ActionEvent actionEvent) {
        try {
            LOGGER.info("Add MenuEntry Button Click");
            Stage stage = new Stage();
            DialogMenuEntryController.setThisStage(stage);
            DialogMenuEntryController.setMenuService(menuService);
            stage.setTitle("Insert MenuEntry");
            Pane myPane = FXMLLoader.load(getClass().getResource("/gui/DialogMenuEntry.fxml"));
            Scene scene = new Scene(myPane);
            stage.setScene(scene);
            stage.showAndWait();
        }catch (IOException e){
            LOGGER.error("Add MenuEntry Button Click did not work");
        }
    }

    public void buttonMenuCategoryRemoveClicked(ActionEvent actionEvent) {
    }

    public void buttonMenuCategorySearchClicked(ActionEvent actionEvent) {
        try {
            LOGGER.info("Search MenuCategory Button Click");
            Stage stage = new Stage();
            DialogMenuCategoryController.setThisStage(stage);
            DialogMenuCategoryController.setMenuService(menuService);
            stage.setTitle("Search Menu Category");
            Pane myPane = FXMLLoader.load(getClass().getResource("/gui/DialogMenuCategory.fxml"));
            Scene scene = new Scene(myPane);
            stage.setScene(scene);
            stage.showAndWait();
        }catch (IOException e){
            LOGGER.error("Search MenuCategory Button Click did not work");
        }
    }

    public void buttonMenuCategoryUpdateClicked(ActionEvent actionEvent) {
        try {
            LOGGER.info("Update MenuCategory Button Click");
            Stage stage = new Stage();
            DialogMenuCategoryController.setThisStage(stage);
            DialogMenuCategoryController.setMenuService(menuService);
            stage.setTitle("Update Menu Category");
            Pane myPane = FXMLLoader.load(getClass().getResource("/gui/DialogMenuCategory.fxml"));
            Scene scene = new Scene(myPane);
            stage.setScene(scene);
            stage.showAndWait();
            tableViewMenuCategory.setItems(observableArrayList(menuService.getAllMenuCategories()));
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
            stage.setTitle("Insert Menu Category");
            Pane myPane = FXMLLoader.load(getClass().getResource("/gui/DialogMenuCategory.fxml"));
            Scene scene = new Scene(myPane);
            stage.setScene(scene);
            stage.showAndWait();
            tableViewMenuCategory.setItems(observableArrayList(menuService.getAllMenuCategories()));
        }catch (Exception e){
            LOGGER.error("Add MenuCategory Button Click did not work" + e);
        }
    }

    public void buttonTaxRateRemoveClicked(ActionEvent actionEvent) {
    }

    public void buttonTaxRateSearchClicked(ActionEvent actionEvent) {
        try {
            LOGGER.info("Search TaxRate Button Click");
            Stage stage = new Stage();
            DialogTaxRateController.setThisStage(stage);
            stage.setTitle("Search Tax Rate");
            Pane myPane = FXMLLoader.load(getClass().getResource("/gui/DialogTaxRate.fxml"));
            Scene scene = new Scene(myPane);
            stage.setScene(scene);
            stage.showAndWait();
        }catch (IOException e){
            LOGGER.error("Search TaxRate Button Click did not work");
        }
    }

    public void buttonTaxRateUpdateClicked(ActionEvent actionEvent) {
        try {
            LOGGER.info("Update TaxRate Button Click");
            Stage stage = new Stage();
            DialogTaxRateController.setThisStage(stage);
            stage.setTitle("Update Tax Rate");
            Pane myPane = FXMLLoader.load(getClass().getResource("/gui/DialogTaxRate.fxml"));
            Scene scene = new Scene(myPane);
            stage.setScene(scene);
            stage.showAndWait();
        }catch (IOException e){
            LOGGER.error("Update TaxRate Button Click did not work");
        }
    }

    public void buttonTaxRateAddClicked(ActionEvent actionEvent){
        try {
            LOGGER.info("Add TaxRate Button Click");
            Stage stage = new Stage();
            DialogTaxRateController.setThisStage(stage);
            stage.setTitle("Insert Tax Rate");
            Pane myPane = FXMLLoader.load(getClass().getResource("/gui/DialogTaxRate.fxml"));
            Scene scene = new Scene(myPane);
            stage.setScene(scene);
            stage.showAndWait();
        }catch (IOException e){
            LOGGER.error("Add TaxRate Button Click did not work");
        }
    }

    public static void showErrorDialog(String title, String head, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(head);
        alert.setContentText(content);

        alert.showAndWait();
    }
}
