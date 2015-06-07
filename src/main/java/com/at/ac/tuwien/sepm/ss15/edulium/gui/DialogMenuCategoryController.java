package com.at.ac.tuwien.sepm.ss15.edulium.gui;

import com.at.ac.tuwien.sepm.ss15.edulium.dao.DAOException;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.MenuCategory;
import com.at.ac.tuwien.sepm.ss15.edulium.service.MenuService;
import com.at.ac.tuwien.sepm.ss15.edulium.service.ServiceException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for the TaxRate Dialog
 */
public class DialogMenuCategoryController implements Initializable{
    private static final Logger LOGGER = LogManager.getLogger(DialogMenuCategoryController.class);

    private static MenuService menuService;
    private static Stage thisStage;
    private static MenuCategory menuCategory;
    private static DialogEnumeration dialogEnumeration;

    public static void setThisStage(Stage thisStage) {
        DialogMenuCategoryController.thisStage = thisStage;
    }
    public static void setMenuService(MenuService menuService) {
        DialogMenuCategoryController.menuService = menuService;
    }
    public static MenuCategory getMenuCategory() {
        return menuCategory;
    }
    public static void setMenuCategory(MenuCategory menuCategory) {
        DialogMenuCategoryController.menuCategory = menuCategory;
    }
    public static void setDialogEnumeration(DialogEnumeration dialogEnumeration) {
        DialogMenuCategoryController.dialogEnumeration = dialogEnumeration;
    }


    @FXML
    private TextField textFieldName;



    @Override
    public void initialize(URL location, ResourceBundle resources){
        LOGGER.info("Initialize Dialog MenuCategory");
        if (menuCategory == null){
            menuCategory = new MenuCategory();
        }
        textFieldName.setText(menuCategory.getName());
    }

    public void buttonOKClick(ActionEvent actionEvent) {
        LOGGER.info("Dialog MenuCategory OK Button clicked");
        switch (DialogMenuCategoryController.dialogEnumeration){
            case ADD:
            case UPDATE:
                if(textFieldName != null || textFieldName.getText().equals("") &&
                    DialogMenuCategoryController.dialogEnumeration != DialogEnumeration.SEARCH) {
                ManagerController.showErrorDialog("Error", "Input Validation Error", "Name must have a value");
                return;
                }
        }
        try {
            menuCategory.setName(textFieldName.getText());
            switch (DialogMenuCategoryController.dialogEnumeration) {
                case ADD:
                    menuService.addMenuCategory(menuCategory);
                    break;
                case UPDATE:
                    menuService.updateMenuCategory(menuCategory);
                    break;
            }
            thisStage.close();
        }catch (Exception e) {
            LOGGER.error("Was not able to create Menu Category " + e);
        }

    }

    public void buttonCancelClick(ActionEvent actionEvent) {
        LOGGER.info("Dialog MenuCategory Cancel Button clicked");
        resetDialog();
        thisStage.close();
    }

    public static void resetDialog(){
        menuCategory = null;
    }
}
