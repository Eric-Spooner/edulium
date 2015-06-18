package com.at.ac.tuwien.sepm.ss15.edulium.gui;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.MenuCategory;
import com.at.ac.tuwien.sepm.ss15.edulium.service.MenuService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.URL;
import java.util.ResourceBundle;

import static javafx.collections.FXCollections.observableArrayList;

/**
 * Dialog Controller Cook View Categories
 */
public class DialogCookViewCategories implements Initializable {
    private static final Logger LOGGER = LogManager.getLogger(DialogCookViewCategories.class);

    private static Stage thisStage;

    @Autowired
    private MenuService menuService;

    @FXML
    ListView<MenuCategory> listMenuCats;

    public static void setThisStage(Stage thisStage) {
        DialogCookViewCategories.thisStage = thisStage;
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            listMenuCats.setItems(observableArrayList(menuService.getAllMenuCategories()));
        }catch (Exception e){

        }

    }


    public void btnBackClicked(ActionEvent actionEvent) {
        thisStage.close();
    }
}
