package com.at.ac.tuwien.sepm.ss15.edulium.gui;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.Menu;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.MenuCategory;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.MenuEntry;
import com.at.ac.tuwien.sepm.ss15.edulium.service.MenuService;
import com.at.ac.tuwien.sepm.ss15.edulium.service.ServiceException;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

import static javafx.collections.FXCollections.observableArrayList;

/**
 * Controller for the TaxRate Dialog
 */
public class DialogMenuController implements Initializable{
    private static final Logger LOGGER = LogManager.getLogger(DialogMenuController.class);

    private static Stage thisStage;
    private static MenuService menuService;
    private static Menu menu;
    private static DialogEnumeration dialogEnumeration;

    public static void setThisStage(Stage thisStage) {
        DialogMenuController.thisStage = thisStage;
    }
    public static void setMenuService(MenuService menuService) {
        DialogMenuController.menuService = menuService;
    }
    public static void setMenu(Menu menu) {DialogMenuController.menu = menu; }
    public static void setDialogEnumeration(DialogEnumeration dialogEnumeration) {
        DialogMenuController.dialogEnumeration = dialogEnumeration;
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
    private TableColumn<MenuEntry, MenuCategory> tableColCategoryData;
    @FXML
    private TableColumn<MenuEntry, BigDecimal> tableColPriceData;


    @FXML
    private TableView<MenuEntry> tableViewInMenu;
    @FXML
    private TableColumn<MenuEntry, String> tableColNameInMenu;
    @FXML
    private TableColumn<MenuEntry, MenuCategory> tableColCategoryInMen;
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
        tableColCategoryData.setCellValueFactory(new PropertyValueFactory<MenuEntry, MenuCategory>("category"));
        tableColPriceData.setCellValueFactory(new PropertyValueFactory<MenuEntry, BigDecimal>("price"));

        if(menu == null){
            Menu menuForInit = new Menu();
            menuForInit.setEntries(new LinkedList<>());
            DialogMenuController.setMenu(menuForInit);
        }
        if(menu.getEntries() == null) {
            menu.setEntries(new LinkedList<>());
        }
        try {
            allMenuEntries = observableArrayList(menuService.getAllMenuEntries());
            inMenuMenuEntries = observableArrayList(menu.getEntries());
        }catch (Exception e){
            ManagerController.showErrorDialog
                    ("Error", "Refreshing View", "An Error occured during initializing the View");
        }
        if(menu.getName() != null) textFieldName.setText(menu.getName());
        tableViewData.setItems(allMenuEntries);
        tableViewInMenu.setItems(inMenuMenuEntries);
        tableColNameInMenu.setCellValueFactory(new PropertyValueFactory<MenuEntry, String>("name"));
        tableColCategoryInMen.setCellValueFactory(new PropertyValueFactory<MenuEntry, MenuCategory>("category"));
        tableColPriceInMen.setCellValueFactory(new PropertyValueFactory<MenuEntry, BigDecimal>("price"));

    }

    public void buttonOKClick(ActionEvent actionEvent) {
        LOGGER.info("Dialog Menu OK Button clicked");
        if ((textFieldName.getText() == null || textFieldName.getText().equals("")) &&
                DialogMenuController.dialogEnumeration != DialogEnumeration.SEARCH) {
            ManagerController.showErrorDialog("Error", "Input Validation Error", "Name must have a value");
            return;
        }
        if (DialogMenuController.dialogEnumeration == DialogEnumeration.SEARCH) {
            if(!textFieldName.getText().isEmpty()) menu.setName(textFieldName.getText());
        } else{
            menu.setName(textFieldName.getText());
        }
        if(DialogMenuController.dialogEnumeration != DialogEnumeration.SEARCH){
            if (menu.getEntries().size() == 0) {
                ManagerController.showErrorDialog
                        ("Error", "Input Validation Error", "There hast to be at least one Menu Entry");
                return;
            }
        }
        try {
            switch (DialogMenuController.dialogEnumeration) {
                case ADD:
                    menuService.addMenu(menu);
                    break;
                case UPDATE:
                    menuService.updateMenu(menu);
                    break;
            }
        }catch (Exception e){
            ManagerController.showErrorDialog
                    ("Error", "Menu Service Error", "The Service was unable to handle the required Menu action");
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
                DialogMenuController.dialogEnumeration != DialogEnumeration.SEARCH){
            switch (DialogMenuController.dialogEnumeration) {
                case UPDATE:
                case ADD: //There has to be a Price, if the User want's to ADD or UPDATE
                    ManagerController.showErrorDialog("Error", "Input Validation Error", "Price must have a value");
                    return;
            }
        }
        if(tableViewData.getSelectionModel().getSelectedItem() == null){
            ManagerController.showErrorDialog
                    ("Error", "Input Validation Error", "You have to select a Menu Entry from the left side");
            return;
        }

        try {
            MenuEntry menuEntry = tableViewData.getSelectionModel().getSelectedItem();
            BigDecimal price = null;
            switch (DialogMenuController.dialogEnumeration) {
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
            ManagerController.showErrorDialog("Error", "Input Validation Error", "Price must be a number");
            LOGGER.info("Dialog Menu Add Button Clicked Price must be number " + e);
        } catch (Exception e) {
            ManagerController.showErrorDialog
                    ("Error", "Data Validation", "An Error occured during adding MenuEntry");
            LOGGER.info("Dialog Menu Add Button Menu Entry handling Error" + e);
        }
    }

    public void buttonRemoveClick(ActionEvent actionEvent) {
        if(tableViewInMenu.getSelectionModel().getSelectedItem() == null){
            ManagerController.showErrorDialog
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
        DialogMenuController.setMenu(null);
    }
}
