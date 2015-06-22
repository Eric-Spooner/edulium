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
import org.springframework.beans.factory.annotation.Autowired;

import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

import static javafx.collections.FXCollections.observableArrayList;

/**
 * Dialog Controller Cook View Categories
 */
public class DialogCookviewCategories implements Initializable, Controller{
    private static final Logger LOGGER = LogManager.getLogger(DialogCookviewCategories.class);

    private Stage thisStage;
    private List<MenuCategory> checkedCategories;
    private ObservableList<MenuCategory> menuCategories;
    @Autowired
    private MenuService menuService;

    @FXML
    private CheckListView<MenuCategory> listMenuCats;

    public void setCheckedCategories(List<MenuCategory> checkedCategories) {
        this.checkedCategories = checkedCategories;
    }
    public void setThisStage(Stage thisStage) {
        this.thisStage = thisStage;
    }
    public List<MenuCategory> getCheckedCategories() {
        return this.checkedCategories;
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            menuCategories = observableArrayList(menuService.getAllMenuCategories());
            listMenuCats.setItems(menuCategories);
            listMenuCats.setStyle("-fx-font-size: 25px;");
            checkedCategories.forEach(order -> listMenuCats.getCheckModel().check(order));
            listMenuCats.getCheckModel().getCheckedItems().addListener(new ListChangeListener<MenuCategory>() {
                public void onChanged(ListChangeListener.Change<? extends MenuCategory> c) {
                    checkedCategories.clear();
                    checkedCategories.addAll(listMenuCats.getCheckModel().getCheckedItems());
                }
            });
        }catch (Exception e){
            LOGGER.error("Cook View Dialog initialize failed" + e);
        }

    }

    public void btnBackClicked(ActionEvent actionEvent) {
        thisStage.close();
    }

    @Override
    public void disable(boolean disabled) {

    }
}
