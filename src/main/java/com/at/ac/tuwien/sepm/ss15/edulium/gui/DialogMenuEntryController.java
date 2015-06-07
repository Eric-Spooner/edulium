package com.at.ac.tuwien.sepm.ss15.edulium.gui;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.Menu;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.MenuCategory;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.MenuEntry;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.TaxRate;
import com.at.ac.tuwien.sepm.ss15.edulium.service.MenuService;
import com.at.ac.tuwien.sepm.ss15.edulium.service.TaxRateService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.net.URL;
import java.util.ResourceBundle;

import static javafx.collections.FXCollections.observableArrayList;

/**
 * Controller for the TaxRate Dialog
 */
public class DialogMenuEntryController implements Initializable{
    private static final Logger LOGGER = LogManager.getLogger(DialogMenuEntryController.class);

    private static Stage thisStage;
    private static MenuService menuService;
    private static TaxRateService taxRateService;
    private static MenuEntry menuEntry;
    private static DialogEnumeration dialogEnumeration;


    public static void setTaxRateService(TaxRateService taxRateService) {
        DialogMenuEntryController.taxRateService = taxRateService;
    }
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
                menuEntry = new MenuEntry();
                menuEntry.setAvailable(true);
            }
            dropTaxRate.setItems(observableArrayList(taxRateService.getAllTaxRates()));
            if (menuEntry.getTaxRate() != null){
                dropTaxRate.getSelectionModel().select(menuEntry.getTaxRate());
            }
            dropMenuCategory.setItems(observableArrayList(menuService.getAllMenuCategories()));
            if (menuEntry.getCategory() != null){
               dropMenuCategory.getSelectionModel().select(menuEntry.getCategory());
            }
            textFieldName.setText(menuEntry.getName());
            if(menuEntry.getPrice()!= null) {
                textFieldPrice.setText(menuEntry.getPrice().toString());
            }
            if(menuEntry.getDescription() != null) {
                textFieldDesription.setText(menuEntry.getDescription());
            }
            checkAvailible.setSelected(menuEntry.getAvailable());
        }catch (Exception e){
            LOGGER.error("Init Menu Entry crashed " + e);
        }
    }

    public void buttonOKClick(ActionEvent actionEvent) {
        LOGGER.info("Dialog MenuEntry OK Button clicked");
        BigDecimal price = null;
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

                try {
                    price = BigDecimal.valueOf(Double.parseDouble(textFieldPrice.getText()));
                } catch (NumberFormatException e) {
                    ManagerController.showErrorDialog("Error", "Input Validation Error", "Price must be a number");
                    LOGGER.info("Dialog MenuEntry Price must be number " + e);
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
                if(dropTaxRate.getSelectionModel().getSelectedItem() == null){
                    ManagerController.showErrorDialog
                            ("Error", "Input Validation Error", "You have to select a TaxRate");
                    return;
                }
                break;
            case SEARCH:
                if(textFieldPrice.getText() != null && !textFieldPrice.getText().equals("")) {
                    try {
                        price = BigDecimal.valueOf(Double.parseDouble(textFieldPrice.getText()));
                    } catch (NumberFormatException e) {
                        ManagerController.showErrorDialog("Error", "Input Validation Error", "Price must be a number");
                        LOGGER.info("Dialog MenuEntry Price must be number " + e);
                        return;
                    }
                }
        }
        menuEntry.setPrice(price);
        menuEntry.setCategory(dropMenuCategory.getSelectionModel().getSelectedItem());
        menuEntry.setTaxRate(dropTaxRate.getSelectionModel().getSelectedItem());
        menuEntry.setAvailable(checkAvailible.isSelected());
        if(DialogMenuEntryController.dialogEnumeration == DialogEnumeration.SEARCH){
            if(!textFieldDesription.getText().isEmpty()) menuEntry.setDescription(textFieldDesription.getText());
            if(textFieldName.getText() != null) menuEntry.setName(textFieldName.getText());
        }else {
            menuEntry.setName(textFieldName.getText());
            menuEntry.setDescription(textFieldDesription.getText());
        }

        try {
            switch (DialogMenuEntryController.dialogEnumeration){
                case ADD:
                    menuService.addMenuEntry(menuEntry);
                    break;
                case UPDATE:
                    menuService.updateMenuEntry(menuEntry);
                    break;
            }
        }catch (Exception e){
            LOGGER.error("Updating the menuEntry in The Database Failed " + e);
            return;
        }
        thisStage.close();
    }

    public void buttonCancelClick(ActionEvent actionEvent) {
        LOGGER.info("Dialog MenuEntry Cancel Button clicked");
        thisStage.close();
    }

    public static void resetDialog(){
        DialogMenuEntryController.setMenuEntry(null);
    }
}

