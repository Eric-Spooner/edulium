package com.at.ac.tuwien.sepm.ss15.edulium.gui;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.TaxRate;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.User;
import com.at.ac.tuwien.sepm.ss15.edulium.service.ServiceException;
import com.at.ac.tuwien.sepm.ss15.edulium.service.TaxRateService;
import com.at.ac.tuwien.sepm.ss15.edulium.service.UserService;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ResourceBundle;

import static javafx.collections.FXCollections.observableArrayList;

@Controller
public class TaxRateViewController implements Initializable {
    private static final Logger LOGGER = LogManager.getLogger(TaxRateViewController.class);

    @FXML
    private TableView<TaxRate> tableViewTaxRate;
    @FXML
    private TableColumn<TaxRate,Long> tableColTaxRateID;
    @FXML
    private TableColumn<TaxRate,BigDecimal> tableColTaxRateValue;
    @FXML
    private TextField txtTaxRateValue;

    @Autowired
    private TaxRateService taxRateService;
    @Autowired
    private TaskScheduler taskScheduler;

    private ObservableList<TaxRate> taxRates;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // queued
        try {
            taxRates = observableArrayList(taxRateService.getAllTaxRates());
            tableViewTaxRate.setItems(taxRates);
            tableColTaxRateID.setCellValueFactory(new PropertyValueFactory<TaxRate, Long>("identity"));
            tableColTaxRateValue.setCellValueFactory(new PropertyValueFactory<TaxRate, BigDecimal>("value"));
        }catch (ServiceException e){
            LOGGER.error("Initialize taxRate View Failed due to" + e);
        }

    }

    public void buttonTaxRateRemoveClicked(ActionEvent actionEvent) {
        try {
            LOGGER.info("Delete TaxRate Button Click");
            if(tableViewTaxRate.getSelectionModel().getSelectedItem() == null){
                ManagerViewController.showErrorDialog
                        ("Error", "Input Validation Error", "You have to select a Tax Rate to Delete");
                return;
            }
            taxRateService.removeTaxRate(tableViewTaxRate.getSelectionModel().getSelectedItem());
            taxRates.setAll(taxRateService.getAllTaxRates());
        }catch (Exception e){
            LOGGER.error("Loading the taxRates failed" + e);
        }
    }


    public void buttonTaxRateUpdateClicked(ActionEvent actionEvent) {
        try {
            LOGGER.info("Delete TaxRate Button Click");
            if(tableViewTaxRate.getSelectionModel().getSelectedItem() == null){
                ManagerViewController.showErrorDialog
                        ("Error", "Input Validation Error", "You have to select a Tax Rate to Delete");
                return;
            }
            BigDecimal value = BigDecimal.valueOf(0.0);
            try {
                value = BigDecimal.valueOf(Double.parseDouble(txtTaxRateValue.getText()));
            } catch (NumberFormatException e) {
                ManagerViewController.showErrorDialog("Error", "Input Validation Error", "Value must be a number");
                LOGGER.info("Tax Rate Value must be a number" + e);
            }
            TaxRate taxRate = tableViewTaxRate.getSelectionModel().getSelectedItem();
            taxRate.setValue(value);
            taxRateService.updateTaxRate(taxRate);
            taxRates.setAll(taxRateService.getAllTaxRates());
        }catch (Exception e){
            LOGGER.error("Update the taxRates failed" + e);
        }
    }

    public void buttonTaxRateAddClicked(ActionEvent actionEvent){
        try {
            LOGGER.info("Add TaxRate Button Click");
            try {
                BigDecimal value = BigDecimal.valueOf(Double.parseDouble(txtTaxRateValue.getText()));
                if(value.compareTo(BigDecimal.valueOf(1)) == 1 || value.compareTo(BigDecimal.valueOf(0))== 2 ){
                    ManagerViewController.showErrorDialog("Error", "Input Validation Error", "Value must be between 0 and 1");
                    LOGGER.debug("Tax Rate Value must be between 0 and 1");
                }
                TaxRate taxRate = new TaxRate();
                taxRate.setValue(value);
                taxRateService.addTaxRate(taxRate);
                taxRates.setAll(taxRateService.getAllTaxRates());
            } catch (NumberFormatException e) {
                ManagerViewController.showErrorDialog("Error", "Input Validation Error", "Value must be a number");
                LOGGER.info("Tax Rate Value must be a number" + e);
            }
        }catch (Exception e){
            LOGGER.error("Update the taxRates failed" + e);
        }
    }
}
