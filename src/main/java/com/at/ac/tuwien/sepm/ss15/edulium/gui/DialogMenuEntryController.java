package com.at.ac.tuwien.sepm.ss15.edulium.gui;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.Menu;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.MenuCategory;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.MenuEntry;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.TaxRate;
import com.at.ac.tuwien.sepm.ss15.edulium.service.MenuService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
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
    private static MenuEntry menuEntry;
    private static DialogEnumeration dialogEnumeration;

    public static void setThisStage(Stage thisStage) {
        DialogMenuEntryController.thisStage = thisStage;
    }
    public static void setMenuService(MenuService menuService) {
        DialogMenuEntryController.menuService = menuService;
    }
    public static void setMenuEntry(MenuEntry menuEntry) {DialogMenuEntryController.menuEntry = menuEntry; }
    public static void setDialogEnumeration(DialogEnumeration dialogEnumeration) {
        DialogMenuEntryController.dialogEnumeration = dialogEnumeration;
    }
    public static MenuEntry getMenuEntry() {
        return menuEntry;
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
            if(menuEntry == null){
                MenuEntry menuEntryInit = new MenuEntry();
            }
            dropMenuCategory.setItems(observableArrayList(menuService.getAllMenuCategories()));
            textFieldName.setText(menuEntry.getName());
            textFieldPrice.setText(menuEntry.getPrice().toString());
            checkAvailible.setSelected(menuEntry.getAvailable());
        }catch (Exception e){
            LOGGER.error("Init Menu Entry crashed " + e);
        }
    }

    public void buttonOKClick(ActionEvent actionEvent) {
        LOGGER.info("Dialog MenuEntry OK Button clicked");
        switch (DialogMenuEntryController.dialogEnumeration){
            case ADD:
            case UPDATE:
                if(textFieldName.getText() == null || textFieldName.getText().equals("")){
                    ManagerController.showErrorDialog
                            ("Error", "Input Validation Error", "You have to insert a Name");
                    return;
                }
                if(textFieldPrice.getText() == null || textFieldPrice.getText().equals("")){
                    ManagerController.showErrorDialog
                            ("Error", "Input Validation Error", "You have to insert a Price");
                    return;
                }
                if(textFieldDesription.getText() == null || textFieldDesription.getText().equals("")){
                    ManagerController.showErrorDialog
                            ("Error", "Input Validation Error", "You have to insert a Description");
                    return;
                }
                if(dropMenuCategory.getSelectionModel().getSelectedItem() == null){
                    ManagerController.showErrorDialog
                            ("Error", "Input Validation Error", "You have to select a Category");
                    return;
                }
                break;
        }
        thisStage.close();
    }

    public void buttonCancelClick(ActionEvent actionEvent) {
        LOGGER.info("Dialog MenuEntry Cancel Button clicked");
        thisStage.close();
    }

    public void resetDialog(){
        DialogMenuEntryController.setMenuEntry(new MenuEntry());
    }
}

