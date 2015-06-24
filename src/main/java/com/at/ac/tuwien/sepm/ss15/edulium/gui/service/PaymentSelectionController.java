package com.at.ac.tuwien.sepm.ss15.edulium.gui.service;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import org.springframework.stereotype.Controller;

import java.net.URL;
import java.util.ResourceBundle;

@Controller
public class PaymentSelectionController implements Initializable {
    @FXML
    private Button cashButton;
    @FXML
    private Button creditButton;
    @FXML
    private Button debitButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public Button getCashButton() {
        return cashButton;
    }

    public Button getCreditButton() {
        return creditButton;
    }

    public Button getDebitButton() {
        return debitButton;
    }
}
