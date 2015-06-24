package com.at.ac.tuwien.sepm.ss15.edulium.gui.service;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;

import java.net.URL;
import java.util.ResourceBundle;

@Controller
public class PaymentSelectionController implements Initializable {
    private static final Logger LOGGER = LogManager.getLogger(PaymentSelectionController.class);

    @FXML
    private Button cashButton;
    @FXML
    private Button creditButton;
    @FXML
    private Button debitButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    public void onCashButtonClicked() {

    }

    @FXML
    public void onCreditButtonClicked() {

    }

    @FXML
    public void onDebitButtonClicked() {

    }
}
