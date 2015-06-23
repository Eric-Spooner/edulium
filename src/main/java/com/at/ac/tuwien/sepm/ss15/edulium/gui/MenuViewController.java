package com.at.ac.tuwien.sepm.ss15.edulium.gui;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.Menu;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ValidationException;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.Validator;
import com.at.ac.tuwien.sepm.ss15.edulium.service.MenuService;
import com.at.ac.tuwien.sepm.ss15.edulium.service.ServiceException;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
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
    @FXML
    private Button buttonMenuUpdate;
    @FXML
    private Button buttonMenuRemove;

    @Autowired
    private Validator<Menu> menuValidator;

    @Resource(name = "menuDialogPane")
    private FXMLPane menuDialogPane;
    private MenuDialogController menuDialogController;

    @Autowired
    private MenuService menuService;

    private ObservableList<Menu> menus;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        menuDialogController = menuDialogPane.getController();

        // queued
        try {
            menus = observableArrayList(menuService.getAllMenus());
            tableViewMenu.setItems(menus);
        }catch (ServiceException e){
            LOGGER.error("Initialize Menu View Failed due to" + e);
        }

        tableColMenuId.setCellValueFactory(new PropertyValueFactory<>("identity"));
        tableColMenuName.setCellValueFactory(new PropertyValueFactory<>("name"));
        tableColMenuEntries.setCellValueFactory(p -> {
            // p.getValue() returns the Person instance for a particular TableView row
            List<String> list = new LinkedList<>();
            p.getValue().getEntries().forEach(entry->list.add(entry.getName()));
            return new SimpleStringProperty(list.toString());
        });
    }

    @FXML
    public void buttonMenuUpdateClicked() {
        Menu selectedMenu = tableViewMenu.getSelectionModel().getSelectedItem();
        if (selectedMenu != null) {
            UpdateInputDialog<Menu> menuInputDialog = new UpdateInputDialog<>("menu", selectedMenu);
            menuInputDialog.setValidator(menuValidator);
            menuInputDialog.setContent(menuDialogPane);
            menuInputDialog.setController(menuDialogController);
            menuInputDialog.showAndWait().ifPresent(editedMenu -> {
                try {
                    menuService.updateMenu(editedMenu);
                    menus.remove(selectedMenu);
                    menus.add(editedMenu);
                } catch (ValidationException | ServiceException e) {
                    LOGGER.error("Could not change menu category " + editedMenu, e);

                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error while updating menu category");
                    alert.setHeaderText("Could not update menu category '" + selectedMenu.getName() + "'");
                    alert.setContentText(e.getMessage());
                    alert.showAndWait();
                }
            });
        }
    }

    @FXML
    public void buttonMenuSearchClicked() {
        SearchInputDialog<Menu> menuSearchDialog = new SearchInputDialog<>("menus");
        menuSearchDialog.setContent(menuDialogPane);
        menuSearchDialog.setController(menuDialogController);
        menuSearchDialog.showAndWait().ifPresent(menuMatcher -> {
            try {
                menus.setAll(menuService.findMenu(menuMatcher));
            } catch (ServiceException e) {
                LOGGER.error("Could not search for menus with matcher " + menuMatcher, e);

                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error while searching for menus");
                alert.setHeaderText("Could not search for menus");
                alert.setContentText(e.getMessage());
                alert.showAndWait();
            }
        });
    }

    @FXML
    public void buttonMenuRemoveClicked() {
        Menu selectedMenu = tableViewMenu.getSelectionModel().getSelectedItem();

        if(selectedMenu != null) {
            try {
                menuService.removeMenu(selectedMenu);
                menus.remove(selectedMenu);
            } catch (ValidationException | ServiceException e) {
                LOGGER.error("Could not remove menu entry " + selectedMenu, e);

                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error while removing menus");
                alert.setHeaderText("Could not removing menus");
                alert.setContentText(e.getMessage());
                alert.showAndWait();
            }
        }
    }

    @FXML
    public void buttonMenuAddClicked(){
        CreateInputDialog<Menu> menuInputDialog = new CreateInputDialog<>("menu");
        menuInputDialog.setValidator(menuValidator);
        menuInputDialog.setContent(menuDialogPane);
        menuInputDialog.setController(menuDialogController);
        menuInputDialog.showAndWait().ifPresent(menu -> {
            try {
                menuService.addMenu(menu);
                menus.add(menu);
            } catch (ValidationException | ServiceException e) {
                LOGGER.error("Could not add menu " + menu, e);

                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error while adding menu");
                alert.setHeaderText("Could not add menu '" + menu.getName() + "'");
                alert.setContentText(e.getMessage());
                alert.showAndWait();
            }
        });
    }

    @FXML
    public void on_tableView_clicked() {
        boolean disable = tableViewMenu.getSelectionModel().isEmpty();

        buttonMenuRemove.setDisable(disable);
        buttonMenuUpdate.setDisable(disable);
    }

    @FXML
    public void buttonShowAllClicked() {
        menus.clear();
        try {
            menus.addAll(menuService.getAllMenus());
        } catch (ServiceException e) {
            LOGGER.error("Could not show menus ", e);

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error while loading menus");
            alert.setHeaderText("Could not load menus");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }
}
