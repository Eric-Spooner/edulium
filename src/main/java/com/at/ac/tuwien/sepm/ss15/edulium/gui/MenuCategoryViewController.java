package com.at.ac.tuwien.sepm.ss15.edulium.gui;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.MenuCategory;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ValidationException;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.Validator;
import com.at.ac.tuwien.sepm.ss15.edulium.service.MenuService;
import com.at.ac.tuwien.sepm.ss15.edulium.service.ServiceException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import java.net.URL;
import java.util.ResourceBundle;

@Controller
public class MenuCategoryViewController implements Initializable {
    private static final Logger LOGGER = LogManager.getLogger(MenuCategoryViewController.class);

    @FXML
    private TableView<MenuCategory> tableViewMenuCategory;
    @FXML
    private TableColumn<MenuCategory,Long> tableColMenuCategoryID;
    @FXML
    private TableColumn<MenuCategory,String> tableColMenuCategoryName;
    @FXML
    private Button buttonUpdate;
    @FXML
    private Button buttonRemove;

    @Autowired
    private MenuService menuService;
    @Autowired
    private Validator<MenuCategory> menuCategoryValidator;

    @Resource(name = "menuCategoryDialogPane")
    private FXMLPane menuCategoryDialogPane;
    private MenuCategoryDialogController menuCategoryDialogController;

    private final ObservableList<MenuCategory> menuCategories = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        menuCategoryDialogController = menuCategoryDialogPane.getController();

        tableViewMenuCategory.setItems(menuCategories);

        tableColMenuCategoryID.setCellValueFactory(new PropertyValueFactory<>("identity"));
        tableColMenuCategoryName.setCellValueFactory(new PropertyValueFactory<>("name"));

        tableViewMenuCategory.getSelectionModel().selectedIndexProperty().addListener(event -> {
            final boolean hasSelection = tableViewMenuCategory.getSelectionModel().getSelectedIndex() >= 0;

            buttonUpdate.setDisable(!hasSelection);
            buttonRemove.setDisable(!hasSelection);
        });

        loadAllMenuCategories();
    }

    @FXML
    public void buttonMenuCategorySearchClicked() {
        SearchInputDialog<MenuCategory> menuCategorySearchDialog = new SearchInputDialog<>("menu categories");
        menuCategorySearchDialog.setContent(menuCategoryDialogPane);
        menuCategorySearchDialog.setController(menuCategoryDialogController);
        menuCategorySearchDialog.showAndWait().ifPresent(menuCategoryMatcher -> {
            try {
                menuCategories.setAll(menuService.findMenuCategory(menuCategoryMatcher));
            } catch (ServiceException e) {
                LOGGER.error("Could not search for menu categories with matcher " + menuCategoryMatcher, e);

                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error while searching for menu categories");
                alert.setHeaderText("Could not search for menu categories");
                alert.setContentText(e.getMessage());
                alert.showAndWait();
            }
        });
    }

    @FXML
    public void buttonMenuCategoryUpdateClicked() {
        MenuCategory selectedMenuCategory = tableViewMenuCategory.getSelectionModel().getSelectedItem();
        if (selectedMenuCategory != null) {
            UpdateInputDialog<MenuCategory> menuCategoryInputDialog = new UpdateInputDialog<>("menu category", selectedMenuCategory);
            menuCategoryInputDialog.setValidator(menuCategoryValidator);
            menuCategoryInputDialog.setContent(menuCategoryDialogPane);
            menuCategoryInputDialog.setController(menuCategoryDialogController);
            menuCategoryInputDialog.showAndWait().ifPresent(editedMenuCategory -> {
                try {
                    menuService.updateMenuCategory(editedMenuCategory);
                    menuCategories.remove(selectedMenuCategory);
                    menuCategories.add(editedMenuCategory);
                } catch (ValidationException | ServiceException e) {
                    LOGGER.error("Could not change menu category " + editedMenuCategory, e);

                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error while updating menu category");
                    alert.setHeaderText("Could not update menu category '" + selectedMenuCategory.getName() + "'");
                    alert.setContentText(e.getMessage());
                    alert.showAndWait();
                }
            });
        }
    }

    @FXML
    public void buttonMenuCategoryAddClicked() {
        CreateInputDialog<MenuCategory> menuCategoryInputDialog = new CreateInputDialog<>("menu category");
        menuCategoryInputDialog.setValidator(menuCategoryValidator);
        menuCategoryInputDialog.setContent(menuCategoryDialogPane);
        menuCategoryInputDialog.setController(menuCategoryDialogController);
        menuCategoryInputDialog.showAndWait().ifPresent(menuCategory -> {
            try {
                menuService.addMenuCategory(menuCategory);
                menuCategories.add(menuCategory);
            } catch (ValidationException | ServiceException e) {
                LOGGER.error("Could not add menu category " + menuCategory, e);

                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error while adding menu category");
                alert.setHeaderText("Could not add menu category '" + menuCategory.getName() + "'");
                alert.setContentText(e.getMessage());
                alert.showAndWait();
            }
        });
    }

    @FXML
    public void buttonMenuCategoryRemoveClicked() {
        MenuCategory selectedMenuCategory = tableViewMenuCategory.getSelectionModel().getSelectedItem();
        if (selectedMenuCategory != null) {
            try {
                menuService.removeMenuCategory(selectedMenuCategory);
                menuCategories.remove(selectedMenuCategory);
            } catch (ValidationException | ServiceException e) {
                LOGGER.error("Could not delete menu category " + selectedMenuCategory, e);

                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error while deleting menu category");
                alert.setHeaderText("Could not delete menu category '" + selectedMenuCategory.getName() + "'");
                alert.setContentText(e.getMessage());
                alert.showAndWait();
            }
        }
    }

    @FXML
    public void buttonShowAllMenuCategoryClicked() {
        loadAllMenuCategories();
    }

    private void loadAllMenuCategories() {
        try {
            menuCategories.setAll(menuService.getAllMenuCategories());
        } catch (ServiceException e){
            LOGGER.error("Could not load all menu categories", e);
        }
    }
}
