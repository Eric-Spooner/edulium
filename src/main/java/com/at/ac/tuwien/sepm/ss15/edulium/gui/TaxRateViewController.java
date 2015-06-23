package com.at.ac.tuwien.sepm.ss15.edulium.gui;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.TaxRate;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ValidationException;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.Validator;
import com.at.ac.tuwien.sepm.ss15.edulium.service.ServiceException;
import com.at.ac.tuwien.sepm.ss15.edulium.service.TaxRateService;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
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
    private Button buttonTaxRateUpdate;
    @FXML
    private Button buttonTaxRateRemove;

    @Autowired
    private TaxRateService taxRateService;
    @Autowired
    private Validator<TaxRate> taxRateValidator;

    @Resource(name = "taxRateDialogPane")
    private FXMLPane taxRateDialogPane;

    private TaxRateDialogController taxRateDialogController;
    private ObservableList<TaxRate> taxRates;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        taxRateDialogController = taxRateDialogPane.getController(TaxRateDialogController.class);

        // queued
        try {
            taxRates = observableArrayList(taxRateService.getAllTaxRates());
            tableViewTaxRate.setItems(taxRates);
            tableColTaxRateID.setCellValueFactory(new PropertyValueFactory<>("identity"));
            tableColTaxRateValue.setCellValueFactory(new PropertyValueFactory<>("value"));
        }catch (ServiceException e){
            LOGGER.error("Initialize taxRate View Failed due to" + e);
        }
    }

    public void buttonTaxRateRemoveClicked(ActionEvent actionEvent) {
        TaxRate selectedTaxRate = tableViewTaxRate.getSelectionModel().getSelectedItem();
        if(selectedTaxRate == null) {
            return;
        }

        try {
            taxRateService.removeTaxRate(selectedTaxRate);
            taxRates.remove(selectedTaxRate);
        }catch (Exception e){
            LOGGER.error("Could not remove tax rate " + selectedTaxRate, e);

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error while removing tax rate");
            alert.setHeaderText("Could not remove tax rate");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    public void buttonTaxRateUpdateClicked(ActionEvent actionEvent) {
        TaxRate selectedTaxRate = tableViewTaxRate.getSelectionModel().getSelectedItem();
        if (selectedTaxRate != null) {
            UpdateInputDialog<TaxRate> taxRateInputDialog = new UpdateInputDialog<>("tax rate", selectedTaxRate);
            taxRateInputDialog.setValidator(taxRateValidator);
            taxRateInputDialog.setContent(taxRateDialogPane);
            taxRateInputDialog.setController(taxRateDialogController);
            taxRateInputDialog.showAndWait().ifPresent(editedTaxRate -> {
                try {
                    taxRateService.updateTaxRate(editedTaxRate);
                    taxRates.remove(selectedTaxRate);
                    taxRates.add(editedTaxRate);
                } catch (ValidationException | ServiceException e) {
                    LOGGER.error("Could not change tax rate " + editedTaxRate, e);

                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error while updating tax rate");
                    alert.setHeaderText("Could not update tax rate");
                    alert.setContentText(e.getMessage());
                    alert.showAndWait();
                }
            });
        }
    }

    @FXML
    public void buttonTaxRateAddClicked(ActionEvent actionEvent) {
        CreateInputDialog<TaxRate> taxRateInputDialog = new CreateInputDialog<>("tax rate");
        taxRateInputDialog.setValidator(taxRateValidator);
        taxRateInputDialog.setContent(taxRateDialogPane);
        taxRateInputDialog.setController(taxRateDialogController);
        taxRateInputDialog.showAndWait().ifPresent(taxRate -> {
            try {
                taxRateService.addTaxRate(taxRate);
                taxRates.add(taxRate);
            } catch (ValidationException | ServiceException e) {
                LOGGER.error("Could not add tax rate " + taxRate, e);

                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error while adding tax rate");
                alert.setHeaderText("Could not add tax rate");
                alert.setContentText(e.getMessage());
                alert.showAndWait();
            }
        });
    }


    @FXML
    public void on_tableView_clicked() {
        boolean disable = tableViewTaxRate.getSelectionModel().isEmpty();
        buttonTaxRateUpdate.setDisable(disable);
        buttonTaxRateRemove.setDisable(disable);
    }
}
