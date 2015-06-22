package com.at.ac.tuwien.sepm.ss15.edulium.gui;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.MenuCategory;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.MenuEntry;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.TaxRate;
import com.at.ac.tuwien.sepm.ss15.edulium.service.MenuService;
import com.at.ac.tuwien.sepm.ss15.edulium.service.ServiceException;
import com.at.ac.tuwien.sepm.ss15.edulium.service.TaxRateService;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.StringConverter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.math.BigDecimal;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Controller for the Menu Entry Dialog
 */
@Controller
public class MenuEntryDialogController implements Initializable, InputDialogController<MenuEntry> {
    private static final Logger LOGGER = LogManager.getLogger(MenuEntryDialogController.class);

    @Autowired
    private MenuService menuService;
    @Autowired
    private TaxRateService taxRateService;

    @FXML
    private TextField textFieldName;
    @FXML
    private TextField textFieldPrice;
    @FXML
    private CheckBox checkAvailable;
    @FXML
    private TextArea textFieldDesription;
    @FXML
    private ChoiceBox<MenuCategory> dropMenuCategory;
    @FXML
    private ChoiceBox<TaxRate> dropTaxRate;

    private Long identity;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        dropTaxRate.setConverter(new StringConverter<TaxRate>() {
            @Override
            public String toString(TaxRate object) {
                return object.getValue().toString();
            }

            @Override
            public TaxRate fromString(String string) {
                TaxRate taxRate = new TaxRate();
                taxRate.setValue(BigDecimal.valueOf(Double.parseDouble(string)));
                return taxRate;
            }
        });

        // Used to handle the dropDown with the keyboard
        dropTaxRate.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.UP || event.getCode() == KeyCode.DOWN) {
                event.consume();
            }
        });

        dropMenuCategory.setConverter(new StringConverter<MenuCategory>() {
            @Override
            public String toString(MenuCategory object) {
                return object.getName();
            }

            @Override
            public MenuCategory fromString(String string) {
                MenuCategory menuCategory = new MenuCategory();
                menuCategory.setName(string);
                return menuCategory;
            }
        });

        // Used to handle the dropDown with the keyboard
        dropMenuCategory.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.UP || event.getCode() == KeyCode.DOWN) {
                event.consume();
            }
        });

        try {
            List<TaxRate> taxRates = taxRateService.getAllTaxRates();
            dropTaxRate.getItems().setAll(taxRates);
            dropTaxRate.getSelectionModel().selectFirst();
        } catch (ServiceException e) {
            e.printStackTrace();
        }

        try {
            List<MenuCategory> menuCategories = menuService.getAllMenuCategories();
            dropMenuCategory.getItems().setAll(menuCategories);
            dropMenuCategory.getSelectionModel().selectFirst();
        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void prepareForCreate() {
        textFieldName.clear();
        textFieldPrice.clear();
        textFieldDesription.clear();
        checkAvailable.setSelected(true);
        dropMenuCategory.getSelectionModel().selectFirst();
        dropTaxRate.getSelectionModel().selectFirst();
        identity = null;
    }

    @Override
    public void prepareForUpdate(MenuEntry menuEntry) {
        assert menuEntry != null;

        textFieldName.setText(menuEntry.getName());
        textFieldPrice.setText(menuEntry.getPrice().toString());
        checkAvailable.setSelected(menuEntry.getAvailable());
        textFieldDesription.setText(menuEntry.getDescription());
        dropMenuCategory.getSelectionModel().select(menuEntry.getCategory());
        dropTaxRate.getSelectionModel().select(menuEntry.getTaxRate());
        identity = menuEntry.getIdentity();
    }

    @Override
    public void prepareForSearch() {
        textFieldName.clear();
        textFieldPrice.clear();
        textFieldDesription.clear();
        checkAvailable.setSelected(true);
        dropMenuCategory.getSelectionModel().select(-1);
        dropTaxRate.getSelectionModel().select(-1);
        identity = null;
    }

    @Override
    public MenuEntry toDomainObject() {
        MenuEntry menuEntry = new MenuEntry();
        menuEntry.setIdentity(identity);
        menuEntry.setName(textFieldName.getText().isEmpty() ? null : textFieldName.getText());
        menuEntry.setPrice(textFieldPrice.getText().isEmpty() ? null : new BigDecimal(textFieldPrice.getText()));
        menuEntry.setAvailable(checkAvailable.isSelected());
        menuEntry.setDescription(textFieldDesription.getText().isEmpty() ? null : textFieldDesription.getText());
        menuEntry.setCategory(dropMenuCategory.getSelectionModel().getSelectedItem());
        menuEntry.setTaxRate(dropTaxRate.getSelectionModel().getSelectedItem());
        return menuEntry;
    }
}

