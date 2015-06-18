package com.at.ac.tuwien.sepm.ss15.edulium.gui;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.MenuCategory;
import com.at.ac.tuwien.sepm.ss15.edulium.service.MenuService;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.cell.CheckBoxListCell;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.controlsfx.control.CheckListView;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import static javafx.collections.FXCollections.observableArrayList;
import static javafx.collections.FXCollections.observableList;

/**
 * Dialog Controller Cook View Categories
 */
public class DialogCookViewCategories implements Initializable {
    private static final Logger LOGGER = LogManager.getLogger(DialogCookViewCategories.class);

    private static Stage thisStage;
    private static ArrayList<MenuCategory> checkedCategories;
    private ObservableList<MenuCategory> menuCategories;
    private static MenuService menuService;

    @FXML
    CheckListView<MenuCategory> listMenuCats;

    public static void setThisStage(Stage thisStage) {
        DialogCookViewCategories.thisStage = thisStage;
    }
    public static void setMenuService(MenuService menuService) {
        DialogCookViewCategories.menuService = menuService;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            menuCategories = observableArrayList(menuService.getAllMenuCategories());
            listMenuCats.setItems(menuCategories);
            listMenuCats.getCheckModel().getCheckedItems().addListener(new ListChangeListener<MenuCategory>() {
                public void onChanged(ListChangeListener.Change<? extends MenuCategory> c) {
                    System.out.println(listMenuCats.getCheckModel().getCheckedItems());
                }
            });
        }catch (Exception e){
            LOGGER.error("Cook View Dialog initialize failed" + e);
        }

    }


    public void btnBackClicked(ActionEvent actionEvent) {
        thisStage.close();
    }
}
