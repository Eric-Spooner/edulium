package com.at.ac.tuwien.sepm.ss15.edulium.gui;

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

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for the TaxRate Dialog
 */
public class DialogTaxRateController implements Initializable{
    private static final Logger LOGGER = LogManager.getLogger(DialogTaxRateController.class);

    private static Stage thisStage;

    public static void setThisStage(Stage thisStage) {
        DialogTaxRateController.thisStage = thisStage;
    }

    @FXML
    private Slider sldValue;
    @FXML
    private Text lblValue;

    @Override
    public void initialize(URL location, ResourceBundle resources){
        LOGGER.info("Initialize Dialog Tax Rate");

        sldValue.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov,
                                Number old_val, Number new_val) {
                lblValue.setText(String.format("%.2f", new_val));
            }
        });
    }

    public void buttonOKClick(ActionEvent actionEvent) {
        LOGGER.info("Dialog Tax Rate OK Button clicked");
        thisStage.close();
    }

    public void buttonCancleClick(ActionEvent actionEvent) {
        LOGGER.info("Dialog Tax Rate Cancle Button clicked");
        thisStage.close();
    }
}
