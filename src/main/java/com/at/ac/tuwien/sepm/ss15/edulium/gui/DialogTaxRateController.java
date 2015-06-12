package com.at.ac.tuwien.sepm.ss15.edulium.gui;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.TaxRate;
import com.at.ac.tuwien.sepm.ss15.edulium.service.TaxRateService;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for the TaxRate Dialog
 */
public class DialogTaxRateController implements Initializable{
    private static final Logger LOGGER = LogManager.getLogger(DialogTaxRateController.class);

    private static Stage thisStage;
    @Autowired
    private TaxRateService taxRateService;
    private static TaxRate taxRate;
    private static DialogEnumeration dialogEnumeration;

    public static void setThisStage(Stage thisStage) {
        DialogTaxRateController.thisStage = thisStage;
    }
    public static void setDialogEnumeration(DialogEnumeration dialogEnumeration) {
        DialogTaxRateController.dialogEnumeration = dialogEnumeration;
    }
    public static TaxRate getTaxRate() {
        return taxRate;
    }
    public static void setTaxRate(TaxRate taxRate) {
        DialogTaxRateController.taxRate = taxRate;
    }

    @FXML
    private Slider sldValue;
    @FXML
    private Text lblValue;

    @Override
    public void initialize(URL location, ResourceBundle resources){
        LOGGER.info("Initialize Dialog Tax Rate");
        if(taxRate == null){
            taxRate = new TaxRate();
        }
        if (taxRate.getValue() == null){
            taxRate.setValue(BigDecimal.valueOf(0));
        }
        sldValue.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov,
                                Number old_val, Number new_val) {
                lblValue.setText(String.format("%.2f", new_val));
            }
        });
        sldValue.setValue(taxRate.getValue().doubleValue());

    }

    public void buttonOKClick(ActionEvent actionEvent) {
        LOGGER.info("Dialog Tax Rate OK Button clicked");
        taxRate.setValue(BigDecimal.valueOf(sldValue.getValue()));
        try {
            switch (DialogTaxRateController.dialogEnumeration){
                case ADD:
                    taxRateService.addTaxRate(taxRate);
                    break;
                case UPDATE:
                    taxRateService.updateTaxRate(taxRate);
                    break;
            }
            thisStage.close();
        }
        catch (Exception e){
            LOGGER.error("Updating the TaxRate in The Database Failed " + e);
            return;
        }
        thisStage.close();
    }

    public void buttonCancelClick(ActionEvent actionEvent) {
        LOGGER.info("Dialog Tax Rate Cancel Button clicked");
        resetDialog();
        thisStage.close();
    }

    public static void resetDialog(){
        taxRate = null;
    }
}
