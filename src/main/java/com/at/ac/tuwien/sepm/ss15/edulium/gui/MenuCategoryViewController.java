package com.at.ac.tuwien.sepm.ss15.edulium.gui;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.MenuCategory;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.User;
import com.at.ac.tuwien.sepm.ss15.edulium.service.MenuService;
import com.at.ac.tuwien.sepm.ss15.edulium.service.ServiceException;
import com.at.ac.tuwien.sepm.ss15.edulium.service.UserService;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static javafx.collections.FXCollections.observableArrayList;

@Controller
public class MenuCategoryViewController implements Initializable {
    private static final Logger LOGGER = LogManager.getLogger(MenuCategoryViewController.class);

    @FXML
    private TableView<MenuCategory> tableViewMenuCategory;
    @FXML
    private TableColumn<MenuCategory,Long> tableColMenuCategoryID;
    @FXML
    private TableColumn<MenuCategory,String> tableColMenuCategoryName;

    @Autowired
    private MenuService menuService;
    @Autowired
    private TaskScheduler taskScheduler;

    private ObservableList<MenuCategory> menuCategories;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // queued
        try {
            menuCategories = observableArrayList(menuService.getAllMenuCategories());
            tableViewMenuCategory.setItems(menuCategories);
            tableColMenuCategoryID.setCellValueFactory(new PropertyValueFactory<MenuCategory, Long>("identity"));
            tableColMenuCategoryName.setCellValueFactory(new PropertyValueFactory<MenuCategory, String>("name"));
        }catch (ServiceException e){
            LOGGER.error("Initialize MenuCategory View Failed due to" + e);
        }

    }

    public void buttonMenuCategorySearchClicked(ActionEvent actionEvent) {
        try {
            LOGGER.info("Search MenuCategory Button Click");
            Stage stage = new Stage();
            DialogMenuCategoryController.setThisStage(stage);
            DialogMenuCategoryController.setDialogEnumeration(DialogEnumeration.SEARCH);
            stage.setTitle("Search Menu Category");
            AnchorPane myPane = FXMLLoader.load(getClass().getResource("/gui/DialogMenuCategory.fxml"));
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
                ManagerViewController.showErrorDialog
                        ("Error", "Input Validation Error", "You have to select a MenuCategory to Update");
                return;
            }
            DialogMenuCategoryController.setThisStage(stage);
            DialogMenuCategoryController.setDialogEnumeration(DialogEnumeration.UPDATE);
            DialogMenuCategoryController.setMenuCategory(tableViewMenuCategory.getSelectionModel().getSelectedItem());
            stage.setTitle("Update Menu Category");
            AnchorPane myPane = FXMLLoader.load(getClass().getResource("/gui/DialogMenuCategory.fxml"));
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
            DialogMenuCategoryController.setDialogEnumeration(DialogEnumeration.ADD);
            stage.setTitle("Insert Menu Category");
            AnchorPane myPane = FXMLLoader.load(getClass().getResource("/gui/DialogMenuCategory.fxml"));
            Scene scene = new Scene(myPane);
            stage.setScene(scene);
            stage.showAndWait();
            menuCategories.setAll(menuService.getAllMenuCategories());
            DialogMenuCategoryController.resetDialog();
        }catch (Exception e){
            LOGGER.error("Add MenuCategory Button Click did not work" + e);
        }
    }

    public void buttonMenuCategoryRemoveClicked(ActionEvent actionEvent) {
        try {
            LOGGER.info("Delete MenuCategory Button Click");
            if(tableViewMenuCategory.getSelectionModel().getSelectedItem() == null){
                ManagerViewController.showErrorDialog
                        ("Error", "Input Validation Error", "You have to select a Menu Category to Delete");
                return;
            }
            menuService.removeMenuCategory(tableViewMenuCategory.getSelectionModel().getSelectedItem());
            menuCategories.setAll(menuService.getAllMenuCategories());
        }catch (Exception e){
            LOGGER.error("Loading the Menus Categories failed" + e);
        }
    }

    public void buttonShowAllMenuCategoryClicked(ActionEvent actionEvent) {
        try {
            menuCategories.setAll(menuService.getAllMenuCategories());
        } catch (Exception e){
            LOGGER.error("Loading All Menu Categories failed" + e);
        }
    }
}
