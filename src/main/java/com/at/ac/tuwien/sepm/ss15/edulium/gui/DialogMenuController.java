package com.at.ac.tuwien.sepm.ss15.edulium.gui;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.MenuCategory;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.MenuEntry;
import com.at.ac.tuwien.sepm.ss15.edulium.service.MenuService;
import javafx.collections.ObservableArray;
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
import java.util.List;
import java.util.ResourceBundle;

import static javafx.collections.FXCollections.observableArrayList;

/**
 * Controller for the TaxRate Dialog
 */
public class DialogMenuController implements Initializable{
    private static final Logger LOGGER = LogManager.getLogger(DialogTaxRateController.class);

    private static Stage thisStage;
    private static MenuService menuService;

    public static void setThisStage(Stage thisStage) {
        DialogMenuController.thisStage = thisStage;
    }
    public static void setMenuService(MenuService menuService) {
        DialogMenuController.menuService = menuService;
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
    private TableView tableViewInMenu;
    @FXML
    private TableColumn<MenuEntry, String> tableColNameInMenu;
    @FXML
    private TableColumn<MenuEntry, MenuCategory> tableColCategoryInMen;
    @FXML
    private TableColumn<MenuEntry, BigDecimal> tableColPriceInMen;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        LOGGER.info("Initialize Dialog Menu");
        try {
            tableViewData.setItems(observableArrayList(menuService.getAllMenuEntries()));
            tableColNameData.setCellValueFactory(new PropertyValueFactory<MenuEntry, String>("name"));
            tableColCategoryData.setCellValueFactory(new PropertyValueFactory<MenuEntry, MenuCategory>("category"));
            tableColPriceData.setCellValueFactory(new PropertyValueFactory<MenuEntry, BigDecimal>("price"));
        }catch (Exception e){
            LOGGER.error("Initilization of Dialog Menu did not work " + e);
        }

    }

    public void buttonOKClick(ActionEvent actionEvent) {

        LOGGER.info("Dialog Menu OK Button clicked");
        if(textFieldName.getText().equals("")){
            ManagerController.showErrorDialog("Error", "Input Validation Error", "Name must have a value");
        }else {
            thisStage.close();
        }
    }

    public void buttonCancelClick(ActionEvent actionEvent) {
        LOGGER.info("Dialog Menu Cancel Button clicked");
        thisStage.close();
    }

    public void buttonAddClick(ActionEvent actionEvent) {

    }

    public void buttonRemoveClick(ActionEvent actionEvent) {

    }

}
