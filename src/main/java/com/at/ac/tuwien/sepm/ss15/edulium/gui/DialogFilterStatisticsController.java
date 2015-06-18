package com.at.ac.tuwien.sepm.ss15.edulium.gui;

import javafx.fxml.Initializable;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller used for the invoice statistics View
 */
public class DialogFilterStatisticsController implements Initializable {
    private static final Logger LOGGER = LogManager.getLogger(UpdateTableController.class);

    private static Stage thisStage;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        LOGGER.info("Initializing Dialog Filter Statistics Controller");
    }

    public static void setThisStage(Stage thisStage) {
        DialogFilterStatisticsController.thisStage = thisStage;
    }

    public static void resetDialog() {

    }
}