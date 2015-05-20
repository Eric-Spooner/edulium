package com.at.ac.tuwien.sepm.ss15.edulium.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller used for the Manager View
 */
public class ManagerController implements Initializable {
    private static final Logger LOGGER = LogManager.getLogger(ManagerController.class);


    @Override
    public void initialize(URL location, ResourceBundle resources){

    }

    public void buttonEmployeesAddClicked(ActionEvent actionEvent) {
    }

    public void buttonEmployeesUpdateClicked(ActionEvent actionEvent) {
    }

    public void buttonEmployeesSearchClicked(ActionEvent actionEvent) {
    }

    public void buttonEmployeesRemoveClicked(ActionEvent actionEvent) {
    }

    public void buttonMenuUpdateClicked(ActionEvent actionEvent) {
    }

    public void buttonMenuSearchClicked(ActionEvent actionEvent) {
    }

    public void buttonMenuRemoveClicked(ActionEvent actionEvent) {
    }

    public void buttonMenuAddClicked(ActionEvent actionEvent) {
    }

    public void buttonMenuEntryRemoveClicked(ActionEvent actionEvent) {
    }

    public void buttonMenuEntrySearchClicked(ActionEvent actionEvent) {
    }

    public void buttonMenuEntryUpdateClicked(ActionEvent actionEvent) {
    }

    public void buttonMenuEntryAddClicked(ActionEvent actionEvent) {
    }

    public void buttonMenuCategoryRemoveClicked(ActionEvent actionEvent) {
    }

    public void buttonMenuCategorySearchClicked(ActionEvent actionEvent) {
    }

    public void buttonMenuCategoryUpdateClicked(ActionEvent actionEvent) {
    }

    public void buttonMenuCategoryAddClicked(ActionEvent actionEvent) {
    }

    public void buttonTaxRateRemoveClicked(ActionEvent actionEvent) {
    }

    public void buttonTaxRateSearchClicked(ActionEvent actionEvent) {
    }

    public void buttonTaxRateUpdateClicked(ActionEvent actionEvent) {
    }

    public void buttonTaxRateAddClicked(ActionEvent actionEvent) throws IOException{
        LOGGER.info("Insert Tax Rate Button Click");
        Stage stage = new Stage();
        DialogTaxRateController.setThisStage(stage);
        stage.setTitle("Insert Tax Rate");
        Pane myPane = FXMLLoader.load(getClass().getResource("/gui/DialogTaxRate.fxml"));
        Scene scene = new Scene(myPane);
        stage.setScene(scene);
        stage.showAndWait();
    }
}
