package com.at.ac.tuwien.sepm.ss15.edulium.gui;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.Menu;
import com.at.ac.tuwien.sepm.ss15.edulium.service.MenuService;
import com.at.ac.tuwien.sepm.ss15.edulium.service.ServiceException;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

import static javafx.collections.FXCollections.observableArrayList;

@Controller
public class MenuViewController implements Initializable {
    private static final Logger LOGGER = LogManager.getLogger(MenuViewController.class);

    @FXML
    private TableView<Menu> tableViewMenu;
    @FXML
    private TableColumn<Menu,Long> tableColMenuId;
    @FXML
    private TableColumn<Menu,String> tableColMenuName;
    @FXML
    private TableColumn<Menu,String> tableColMenuEntries;

    @Resource(name = "menuDialogPane")
    private FXMLPane menuDialogPane;

    @Autowired
    private MenuService menuService;
    @Autowired
    private TaskScheduler taskScheduler;

    private ObservableList<Menu> menus;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // queued
        try {
            menus = observableArrayList(menuService.getAllMenus());
            tableViewMenu.setItems(menus);
            tableColMenuId.setCellValueFactory(new PropertyValueFactory<Menu, Long>("identity"));
            tableColMenuName.setCellValueFactory(new PropertyValueFactory<Menu, String>("name"));
            tableColMenuEntries.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Menu, String>, ObservableValue<String>>() {
                public ObservableValue<String> call(TableColumn.CellDataFeatures<Menu, String> p) {
                    // p.getValue() returns the Person instance for a particular TableView row
                    List<String> list = new LinkedList<String>();
                    p.getValue().getEntries().forEach(entry -> list.add(entry.getName()));
                    return new SimpleStringProperty(list.toString());
                }
            });
        }catch (ServiceException e){
            LOGGER.error("Initialize Menu View Failed due to" + e);
        }

    }

    public void buttonMenuUpdateClicked(ActionEvent actionEvent) {
        try {
            LOGGER.info("Update Menu Button Click");
            Stage stage = new Stage();
            if(tableViewMenu.getSelectionModel().getSelectedItem() == null){
                ManagerViewController.showErrorDialog
                        ("Error", "Input Validation Error", "You have to select a Menu to Update");
                return;
            }
            MenuDialogController.resetDialog();
            MenuDialogController.setThisStage(stage);
            MenuDialogController.setDialogEnumeration(DialogEnumeration.UPDATE);
            MenuDialogController.setMenu(tableViewMenu.getSelectionModel().getSelectedItem());
            stage.setTitle("Update Menu");
            Scene scene = new Scene(menuDialogPane);
            stage.setScene(scene);
            stage.showAndWait();
            menus.setAll(menuService.getAllMenus());
            MenuDialogController.resetDialog();
        }catch (Exception e){
            LOGGER.error("Loading the Menus failed" + e);
        }
    }

    public void buttonMenuSearchClicked(ActionEvent actionEvent) {
        try {
            LOGGER.info("Search Menu Button Click");
            Stage stage = new Stage();
            MenuDialogController.resetDialog();
            MenuDialogController.setThisStage(stage);
            MenuDialogController.setDialogEnumeration(DialogEnumeration.SEARCH);
            stage.setTitle("Search Menu");
            Scene scene = new Scene(menuDialogPane);
            stage.setScene(scene);
            stage.showAndWait();
            if(MenuDialogController.getMenu() != null){
                menus.setAll(menuService.findMenu(MenuDialogController.getMenu()));
            }else {
                menus.setAll(menuService.getAllMenus());
            }
            MenuDialogController.resetDialog();
        }catch (ServiceException e){
            LOGGER.error("Menu Service finding Menus did not work" + e);
        }
    }

    public void buttonMenuRemoveClicked(ActionEvent actionEvent) {
        try {
            LOGGER.info("Delete Menu Button Click");
            if(tableViewMenu.getSelectionModel().getSelectedItem() == null){
                ManagerViewController.showErrorDialog
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
            MenuDialogController.resetDialog();
            MenuDialogController.setThisStage(stage);
            MenuDialogController.setDialogEnumeration(DialogEnumeration.ADD);
            stage.setTitle("Add Menu");
            Scene scene = new Scene(menuDialogPane);
            stage.setScene(scene);
            stage.showAndWait();
            menus.setAll(menuService.getAllMenus());
        }catch (Exception e){
            LOGGER.error("Loading the Menus failed" + e);
        }
    }

    public void buttonShowAllMenuClicked(ActionEvent actionEvent) {
        try {
            menus.setAll(menuService.getAllMenus());
        } catch (Exception e){
            LOGGER.error("Loading All Menu failed" + e);
        }
    }
}
