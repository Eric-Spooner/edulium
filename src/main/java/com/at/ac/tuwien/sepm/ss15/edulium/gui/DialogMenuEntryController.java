package com.at.ac.tuwien.sepm.ss15.edulium.gui;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.MenuCategory;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.TaxRate;
import com.at.ac.tuwien.sepm.ss15.edulium.service.MenuService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URL;
import java.util.ResourceBundle;

import static javafx.collections.FXCollections.observableArrayList;

/**
 * Controller for the TaxRate Dialog
 */
public class DialogMenuEntryController implements Initializable{
    private static final Logger LOGGER = LogManager.getLogger(DialogTaxRateController.class);

    private static Stage thisStage;
    private static MenuService menuService;

    public static void setThisStage(Stage thisStage) {
        DialogMenuEntryController.thisStage = thisStage;
    }
    public static void setMenuService(MenuService menuService) {
        DialogMenuEntryController.menuService = menuService;
    }

    @FXML
    private TextField textFieldName;
    @FXML
    private TextField textFieldPrice;
    @FXML
    private CheckBox checkAvailible;
    @FXML
    private TextArea textFieldDesription;
    @FXML
    private ChoiceBox<MenuCategory> dropMenuCategory;
    @FXML
    private ChoiceBox<TaxRate> dropTaxRate;

    @Override
    public void initialize(URL location, ResourceBundle resources){
        LOGGER.info("Initialize Dialog MenuEntry");
        try {
            dropMenuCategory.setItems(observableArrayList(menuService.getAllMenuCategories()));
        }catch (Exception e){
            LOGGER.error("Init Menu Entry crashed " + e);
        }
    }

    public void buttonOKClick(ActionEvent actionEvent) {
        LOGGER.info("Dialog MenuEntry OK Button clicked");
        thisStage.close();
    }

    public void buttonCancelClick(ActionEvent actionEvent) {
        LOGGER.info("Dialog MenuEntry Cancel Button clicked");
        thisStage.close();
    }
}

