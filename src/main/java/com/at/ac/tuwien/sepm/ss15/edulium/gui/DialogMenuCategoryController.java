package com.at.ac.tuwien.sepm.ss15.edulium.gui;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for the TaxRate Dialog
 */
public class DialogMenuCategoryController implements Initializable{
    private static final Logger LOGGER = LogManager.getLogger(DialogTaxRateController.class);

    private static Stage thisStage;

    public static void setThisStage(Stage thisStage) {
        DialogMenuCategoryController.thisStage = thisStage;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources){
        LOGGER.info("Initialize Dialog MenuCategory");

    }

    public void buttonOKClick(ActionEvent actionEvent) {
        LOGGER.info("Dialog MenuCategory OK Button clicked");
        thisStage.close();
    }

    public void buttonCancelClick(ActionEvent actionEvent) {
        LOGGER.info("Dialog MenuCategory Cancel Button clicked");
        thisStage.close();
    }
}
