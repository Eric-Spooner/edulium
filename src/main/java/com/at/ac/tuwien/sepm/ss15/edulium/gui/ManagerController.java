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
        try {
            LOGGER.info("Update Menu Button Click");
            Stage stage = new Stage();
            DialogMenuController.setThisStage(stage);
            stage.setTitle("Update Menu");
            Pane myPane = FXMLLoader.load(getClass().getResource("/gui/DialogMenu.fxml"));
            Scene scene = new Scene(myPane);
            stage.setScene(scene);
            stage.showAndWait();
        }catch (IOException e){
            LOGGER.error("Add Menu Button Click did not work");
        }
    }

    public void buttonMenuSearchClicked(ActionEvent actionEvent) {
        try {
            LOGGER.info("Search Menu Button Click");
            Stage stage = new Stage();
            DialogMenuController.setThisStage(stage);
            stage.setTitle("Search Menu");
            Pane myPane = FXMLLoader.load(getClass().getResource("/gui/DialogMenu.fxml"));
            Scene scene = new Scene(myPane);
            stage.setScene(scene);
            stage.showAndWait();
        }catch (IOException e){
            LOGGER.error("Search Menu Button Click did not work");
        }
    }

    public void buttonMenuRemoveClicked(ActionEvent actionEvent) {
    }

    public void buttonMenuAddClicked(ActionEvent actionEvent){
        try {
            LOGGER.info("Add Menu Button Click");
            Stage stage = new Stage();
            DialogMenuController.setThisStage(stage);
            stage.setTitle("Add Menu");
            Pane myPane = FXMLLoader.load(getClass().getResource("/gui/DialogMenu.fxml"));
            Scene scene = new Scene(myPane);
            stage.setScene(scene);
            stage.showAndWait();
        }catch (IOException e){
            LOGGER.error("Add Menu Button Click did not work");
        }
    }

    public void buttonMenuEntryRemoveClicked(ActionEvent actionEvent) {
    }

    public void buttonMenuEntrySearchClicked(ActionEvent actionEvent) {
        try {
            LOGGER.info("Search MenuEntry Button Click");
            Stage stage = new Stage();
            DialogMenuEntryController.setThisStage(stage);
            stage.setTitle("Search MenuEntry");
            Pane myPane = FXMLLoader.load(getClass().getResource("/gui/DialogMenuEntry.fxml"));
            Scene scene = new Scene(myPane);
            stage.setScene(scene);
            stage.showAndWait();
        }catch (IOException e){
            LOGGER.error("Search MenuEntry Button Click did not work");
        }
    }

    public void buttonMenuEntryUpdateClicked(ActionEvent actionEvent) {
        try {
            LOGGER.info("Update MenuEntry Button Click");
            Stage stage = new Stage();
            DialogMenuEntryController.setThisStage(stage);
            stage.setTitle("Update MenuEntry");
            Pane myPane = FXMLLoader.load(getClass().getResource("/gui/DialogMenuEntry.fxml"));
            Scene scene = new Scene(myPane);
            stage.setScene(scene);
            stage.showAndWait();
        }catch (IOException e){
            LOGGER.error("Update MenuEntry Button Click did not work");
        }
    }

    public void buttonMenuEntryAddClicked(ActionEvent actionEvent) {
        try {
            LOGGER.info("Add MenuEntry Button Click");
            Stage stage = new Stage();
            DialogMenuEntryController.setThisStage(stage);
            stage.setTitle("Insert MenuEntry");
            Pane myPane = FXMLLoader.load(getClass().getResource("/gui/DialogMenuEntry.fxml"));
            Scene scene = new Scene(myPane);
            stage.setScene(scene);
            stage.showAndWait();
        }catch (IOException e){
            LOGGER.error("Add MenuEntry Button Click did not work");
        }
    }

    public void buttonMenuCategoryRemoveClicked(ActionEvent actionEvent) {
    }

    public void buttonMenuCategorySearchClicked(ActionEvent actionEvent) {
        try {
            LOGGER.info("Search MenuCategory Button Click");
            Stage stage = new Stage();
            DialogMenuCategoryController.setThisStage(stage);
            stage.setTitle("Search Menu Category");
            Pane myPane = FXMLLoader.load(getClass().getResource("/gui/DialogMenuCategory.fxml"));
            Scene scene = new Scene(myPane);
            stage.setScene(scene);
            stage.showAndWait();
        }catch (IOException e){
            LOGGER.error("Search MenuCategory Button Click did not work");
        }
    }

    public void buttonMenuCategoryUpdateClicked(ActionEvent actionEvent) {
        try {
            LOGGER.info("Update MenuCategory Button Click");
            Stage stage = new Stage();
            DialogMenuCategoryController.setThisStage(stage);
            stage.setTitle("Update Menu Category");
            Pane myPane = FXMLLoader.load(getClass().getResource("/gui/DialogMenuCategory.fxml"));
            Scene scene = new Scene(myPane);
            stage.setScene(scene);
            stage.showAndWait();
        }catch (IOException e){
            LOGGER.error("Update MenuCategory Button Click did not work");
        }
    }

    public void buttonMenuCategoryAddClicked(ActionEvent actionEvent) {
        try {
            LOGGER.info("Add MenuCategory Button Click");
            Stage stage = new Stage();
            DialogMenuCategoryController.setThisStage(stage);
            stage.setTitle("Insert Menu Category");
            Pane myPane = FXMLLoader.load(getClass().getResource("/gui/DialogMenuCategory.fxml"));
            Scene scene = new Scene(myPane);
            stage.setScene(scene);
            stage.showAndWait();
        }catch (IOException e){
            LOGGER.error("Add MenuCategory Button Click did not work");
        }
    }

    public void buttonTaxRateRemoveClicked(ActionEvent actionEvent) {
    }

    public void buttonTaxRateSearchClicked(ActionEvent actionEvent) {
        try {
            LOGGER.info("Search TaxRate Button Click");
            Stage stage = new Stage();
            DialogTaxRateController.setThisStage(stage);
            stage.setTitle("Search Tax Rate");
            Pane myPane = FXMLLoader.load(getClass().getResource("/gui/DialogTaxRate.fxml"));
            Scene scene = new Scene(myPane);
            stage.setScene(scene);
            stage.showAndWait();
        }catch (IOException e){
            LOGGER.error("Search TaxRate Button Click did not work");
        }
    }

    public void buttonTaxRateUpdateClicked(ActionEvent actionEvent) {
        try {
            LOGGER.info("Update TaxRate Button Click");
            Stage stage = new Stage();
            DialogTaxRateController.setThisStage(stage);
            stage.setTitle("Update Tax Rate");
            Pane myPane = FXMLLoader.load(getClass().getResource("/gui/DialogTaxRate.fxml"));
            Scene scene = new Scene(myPane);
            stage.setScene(scene);
            stage.showAndWait();
        }catch (IOException e){
            LOGGER.error("Update TaxRate Button Click did not work");
        }
    }

    public void buttonTaxRateAddClicked(ActionEvent actionEvent){
        try {
            LOGGER.info("Add TaxRate Button Click");
            Stage stage = new Stage();
            DialogTaxRateController.setThisStage(stage);
            stage.setTitle("Insert Tax Rate");
            Pane myPane = FXMLLoader.load(getClass().getResource("/gui/DialogTaxRate.fxml"));
            Scene scene = new Scene(myPane);
            stage.setScene(scene);
            stage.showAndWait();
        }catch (IOException e){
            LOGGER.error("Add TaxRate Button Click did not work");
        }
    }
}
