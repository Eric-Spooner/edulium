package com.at.ac.tuwien.sepm.ss15.edulium.gui;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.MenuCategory;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import org.springframework.stereotype.Controller;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for the Menu Category Dialog
 */
@Controller
public class MenuCategoryDialogController implements Initializable, InputDialogController<MenuCategory> {

    @FXML
    private TextField textFieldName;

    private Long identity = null;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @Override
    public void prepareForCreate() {
        textFieldName.clear();
        identity = null;
    }

    @Override
    public void prepareForUpdate(MenuCategory menuCategory) {
        assert menuCategory != null;

        textFieldName.setText(menuCategory.getName());
        identity = menuCategory.getIdentity();
    }

    @Override
    public void prepareForSearch() {
        textFieldName.clear();
        identity = null;
    }

    @Override
    public MenuCategory toDomainObject() {
        MenuCategory menuCategory = new MenuCategory();
        menuCategory.setIdentity(identity);
        menuCategory.setName(textFieldName.getText().isEmpty() ? null : textFieldName.getText());
        return menuCategory;
    }
}
