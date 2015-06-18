package com.at.ac.tuwien.sepm.ss15.edulium.gui;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.Menu;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.MenuCategory;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.MenuEntry;
import com.at.ac.tuwien.sepm.ss15.edulium.service.MenuService;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.awt.*;
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
    private ObservableList<MenuCategory> observableList;
    private static MenuService menuService;

    @FXML
    ListView<MenuCategory> listMenuCats;

    public static void setThisStage(Stage thisStage) {
        DialogCookViewCategories.thisStage = thisStage;
    }
    public static void setMenuService(MenuService menuService) {
        DialogCookViewCategories.menuService = menuService;
    }

    private class MenuCategoryCheckCell extends ListCell<MenuCategory> {
        private CheckBox checkbox = new CheckBox();
        private MenuCategory menuCategory = null;

        public MenuCategoryCheckCell() {
            checkbox.setText(menuCategory.getName());
            setGraphic(checkbox);
        }
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            observableList = observableArrayList(menuService.getAllMenuCategories());
            listMenuCats.setItems(observableList);
            listMenuCats.setCellFactory(new Callback<ListView<MenuCategory>, ListCell<MenuCategory>>(){

                @Override
                public ListCell<MenuCategory> call(ListView<MenuCategory> p) {

                    ListCell<MenuCategory> cell = new ListCell<MenuCategory>(){
                        @Override
                        protected void updateItem(MenuCategory t, boolean bln) {
                            super.updateItem(t, bln);
                            if (t != null) {
                                setText(t.getName());
                            }
                        }
                    };

                    return cell;
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
