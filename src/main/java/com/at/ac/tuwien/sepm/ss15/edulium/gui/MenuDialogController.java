package com.at.ac.tuwien.sepm.ss15.edulium.gui;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.Menu;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.MenuEntry;
import com.at.ac.tuwien.sepm.ss15.edulium.service.MenuService;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.math.BigDecimal;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

import static javafx.collections.FXCollections.observableArrayList;

/**
 * Controller for the TaxRate Dialog
 */
@Controller
public class MenuDialogController implements Initializable {
    private static final Logger LOGGER = LogManager.getLogger(MenuDialogController.class);

    private static Stage thisStage;

    @Autowired
    private MenuService menuService;

    private static Menu menu;
    private static DialogEnumeration dialogEnumeration;

    public static void setThisStage(Stage thisStage) {
        MenuDialogController.thisStage = thisStage;
    }
    public static void setMenu(Menu menu) {
        MenuDialogController.menu = menu; }
    public static void setDialogEnumeration(DialogEnumeration dialogEnumeration) {
        MenuDialogController.dialogEnumeration = dialogEnumeration;
    }
    public static Menu getMenu() {
        return menu;
    }

    @FXML
    private TextField textFieldName;
    @FXML
    private TextField textFieldPrice;

    @FXML
    private TableView<MenuEntry> tableViewData;
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
    private ObservableList<MenuEntry> inMenuMenuEntries;



    /**
     * Function is used to init the Menu Dialog
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        LOGGER.info("Initialize Dialog Menu");

        tableColNameData.setCellValueFactory(new PropertyValueFactory<MenuEntry, String>("name"));
        tableColCategoryData.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<MenuEntry, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<MenuEntry, String> p) {
                // p.getValue() returns the Person instance for a particular TableView row
                return new SimpleStringProperty(p.getValue().getCategory().getName());
            }
        });
        tableColPriceData.setCellValueFactory(new PropertyValueFactory<MenuEntry, BigDecimal>("price"));

        if(menu == null){
            Menu menuForInit = new Menu();
            menuForInit.setEntries(new LinkedList<>());
            MenuDialogController.setMenu(menuForInit);
        }
        if(menu.getEntries() == null) {
            menu.setEntries(new LinkedList<>());
        }
        try {
            allMenuEntries = observableArrayList(menuService.getAllMenuEntries());
            inMenuMenuEntries = observableArrayList(menu.getEntries());
        }catch (Exception e){
            ManagerViewController.showErrorDialog
                    ("Error", "Refreshing View", "An Error occured during initializing the View /n" + e.toString());
        }
        if(menu.getName() != null) textFieldName.setText(menu.getName());
        tableViewData.setItems(allMenuEntries);
        tableViewInMenu.setItems(inMenuMenuEntries);
        tableColNameInMenu.setCellValueFactory(new PropertyValueFactory<MenuEntry, String>("name"));
        tableColCategoryInMen.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<MenuEntry, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<MenuEntry, String> p) {
                // p.getValue() returns the Person instance for a particular TableView row
                return new SimpleStringProperty(p.getValue().getCategory().getName());
            }
        });
        tableColPriceInMen.setCellValueFactory(new PropertyValueFactory<MenuEntry, BigDecimal>("price"));

    }

    public void buttonOKClick(ActionEvent actionEvent) {
        LOGGER.info("Dialog Menu OK Button clicked");
        if ((textFieldName.getText() == null || textFieldName.getText().equals("")) &&
                MenuDialogController.dialogEnumeration != DialogEnumeration.SEARCH) {
            ManagerViewController.showErrorDialog("Error", "Input Validation Error", "Name must have a value");
            return;
        }
        if (MenuDialogController.dialogEnumeration == DialogEnumeration.SEARCH) {
            if(!textFieldName.getText().isEmpty()) menu.setName(textFieldName.getText());
        } else{
            menu.setName(textFieldName.getText());
        }
        if(MenuDialogController.dialogEnumeration != DialogEnumeration.SEARCH){
            if (menu.getEntries().size() == 0) {
                ManagerViewController.showErrorDialog
                        ("Error", "Input Validation Error", "There hast to be at least one Menu Entry");
                return;
            }
        }
        try {
            switch (MenuDialogController.dialogEnumeration) {
                case ADD:
                    menuService.addMenu(menu);
                    break;
                case UPDATE:
                    menuService.updateMenu(menu);
                    break;
            }
        }catch (Exception e){
            ManagerViewController.showErrorDialog
                    ("Error", "Menu Service Error", "The Service was unable to handle the required Menu action/n" + e.toString());
            LOGGER.error("The Service was unable to handle the required Menu action " + e);
            return;
        }
        thisStage.close();
    }

    public void buttonCancelClick(ActionEvent actionEvent) {
        LOGGER.info("Dialog Menu Cancel Button clicked");
        resetDialog();
        thisStage.close();
    }

    public void buttonAddClick(ActionEvent actionEvent) {
        if((textFieldPrice.getText() == null || textFieldPrice.getText().equals("")) &&
                MenuDialogController.dialogEnumeration != DialogEnumeration.SEARCH){
            switch (MenuDialogController.dialogEnumeration) {
                case UPDATE:
                case ADD: //There has to be a Price, if the User want's to ADD or UPDATE
                    ManagerViewController.showErrorDialog("Error", "Input Validation Error", "Price must have a value");
                    return;
            }
        }
        if(tableViewData.getSelectionModel().getSelectedItem() == null){
            ManagerViewController.showErrorDialog
                    ("Error", "Input Validation Error", "You have to select a Menu Entry from the left side");
            return;
        }

        try {
            MenuEntry menuEntry = tableViewData.getSelectionModel().getSelectedItem();
            BigDecimal price = null;
            switch (MenuDialogController.dialogEnumeration) {
                case UPDATE:
                case ADD:
                    price = BigDecimal.valueOf(Double.parseDouble(textFieldPrice.getText()));
                    menuEntry.setPrice(price);
                    break;
                case SEARCH:
                    if (!textFieldPrice.getText().equals("")) {
                        price = BigDecimal.valueOf(Double.parseDouble(textFieldPrice.getText()));
                        menuEntry.setPrice(price);
                    }
                    break;
            }
            List<MenuEntry> list = menu.getEntries();
            list.add(menuEntry);
            menu.setEntries(list);
            inMenuMenuEntries.setAll(menu.getEntries());
        } catch (NumberFormatException e) {
            ManagerViewController.showErrorDialog("Error", "Input Validation Error", "Price must be a number/n" + e.toString());
            LOGGER.info("Dialog Menu Add Button Clicked Price must be number " + e);
        } catch (Exception e) {
            ManagerViewController.showErrorDialog
                    ("Error", "Data Validation", "An Error occured during adding MenuEntry/n" + e.toString());
            LOGGER.info("Dialog Menu Add Button Menu Entry handling Error" + e);
        }
    }

    public void buttonRemoveClick(ActionEvent actionEvent) {
        if(tableViewInMenu.getSelectionModel().getSelectedItem() == null){
            ManagerViewController.showErrorDialog
                    ("Error", "Input Validation Error", "You have to select a Menu Entry from the right side");
            return;
        }
        MenuEntry menuEntry = tableViewInMenu.getSelectionModel().getSelectedItem();
        List<MenuEntry> list = menu.getEntries();
        list.remove(menuEntry);
        menu.setEntries(list);
        inMenuMenuEntries.setAll(menu.getEntries());
    }

    /**
     * this function is used to rest the static members of the class
     */
    public static void resetDialog(){
        MenuDialogController.setMenu(null);
    }
}
