package com.at.ac.tuwien.sepm.ss15.edulium.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static javafx.collections.FXCollections.observableArrayList;

/**
 * Created by - on 12.06.2015.
 */
public class StatisticViewController implements Initializable, Controller {
    private static final Logger LOGGER = LogManager.getLogger(StatisticViewController.class);

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }


    @Override
    public void disable(boolean disabled) {

    }

    public void buttonShowInvoiceStatisticsClicked(ActionEvent actionEvent) {
        try {
            LOGGER.info("Invoice Statistics Button Click");
            Stage stage = new Stage();
            InvoiceStatisticsController.resetDialog();
            InvoiceStatisticsController.setThisStage(stage);
            //TODO InvoiceStatisticsController.setInvoiceService(invoiceService);
            stage.setTitle("Invoice statistics");
            AnchorPane myPane = FXMLLoader.load(getClass().getResource("/gui/InvoiceStatistics.fxml"));
            Scene scene = new Scene(myPane);
            stage.setScene(scene);
            stage.showAndWait();
            /* TODO: get return object
            if(InvoiceStatisticsController.getUser() != null){
                users.setAll(userService.findUsers(DialogUserController.getUser()));
            }else {
                users.setAll(userService.getAllUsers());
            }*/
            InvoiceStatisticsController.resetDialog();
        }catch (IOException e){
            LOGGER.error("Invoice statistics Button Click did not work");
        }catch (Exception e){
            LOGGER.error("Loading the Invoice statistics failed" + e);
        }
    }

    public void buttonFilterStatisticsClicked(ActionEvent actionEvent) {
        try {
            LOGGER.info("Filter Statistics Button Click");
            Stage stage = new Stage();
            DialogFilterStatisticsController.resetDialog();
            DialogFilterStatisticsController.setThisStage(stage);
            //TODO DialogFilterStatisticsController.setInvoiceService(invoiceService);
            stage.setTitle("Filter statistics");
            AnchorPane myPane = FXMLLoader.load(getClass().getResource("/gui/DialogFilterStatistics.fxml"));
            Scene scene = new Scene(myPane);
            stage.setScene(scene);
            stage.showAndWait();
            /* TODO: get return object
            if(InvoiceStatisticsController.getUser() != null){
                users.setAll(userService.findUsers(DialogUserController.getUser()));
            }else {
                users.setAll(userService.getAllUsers());
            }*/
            DialogFilterStatisticsController.resetDialog();
        }catch (IOException e){
            LOGGER.error("Invoice statistics Button Click did not work");
        }catch (Exception e){
            LOGGER.error("Loading the Invoice statistics failed" + e);
        }
    }
}
