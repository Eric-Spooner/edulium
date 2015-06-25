package com.at.ac.tuwien.sepm.ss15.edulium.gui;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.TaxRate;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import jfxtras.labs.scene.control.BigDecimalField;
import org.springframework.stereotype.Controller;

import java.math.BigDecimal;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for the Menu Category Dialog
 */
@Controller
public class TaxRateDialogController implements Initializable, InputDialogController<TaxRate> {

    @FXML
    private BigDecimalField textFieldValue;

    private Long identity = null;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        textFieldValue.setMinValue(BigDecimal.valueOf(0.0));
        textFieldValue.setMaxValue(BigDecimal.valueOf(1.0));
        textFieldValue.setStepwidth(BigDecimal.valueOf(0.05));
    }

    @Override
    public void prepareForCreate() {
        textFieldValue.setNumber(textFieldValue.getMinValue());
        identity = null;
    }

    @Override
    public void prepareForUpdate(TaxRate taxRate) {
        assert taxRate != null;

        textFieldValue.setText(taxRate.getValue().toString());
        identity = taxRate.getIdentity();
    }

    @Override
    public void prepareForSearch() {
        textFieldValue.setNumber(textFieldValue.getMinValue());
        identity = null;
    }

    @Override
    public TaxRate toDomainObject() {
        TaxRate taxRate = new TaxRate();
        taxRate.setIdentity(identity);
        try {
            taxRate.setValue(textFieldValue.getNumber() == null ? null : textFieldValue.getNumber());
        } catch (NumberFormatException e) {
            taxRate.setValue(null);
        }
        return taxRate;
    }
}
