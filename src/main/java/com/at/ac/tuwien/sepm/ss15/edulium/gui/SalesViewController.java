package com.at.ac.tuwien.sepm.ss15.edulium.gui;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.IntermittentSale;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.OnetimeSale;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.Sale;
import com.at.ac.tuwien.sepm.ss15.edulium.service.SaleService;
import com.at.ac.tuwien.sepm.ss15.edulium.service.ServiceException;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

import static javafx.collections.FXCollections.observableArrayList;

/**
 * Created by - on 12.06.2015.
 */
public class SalesViewController implements Initializable {
    private static final Logger LOGGER = LogManager.getLogger(SalesViewController.class);

    @FXML
    private TableView<Sale> tableViewSale;
    @FXML
    private TableColumn<Sale,Long> tableColSaleId;
    @FXML
    private TableColumn<Sale,String> tableColSaleName;
    @FXML
    private TableColumn<Sale,String> tableColSaleEntries;

    @Resource(name = "salesDialogPane")
    private FXMLPane salesDialogPane;

    private DialogSaleController saleDialogController;
    private Dialog<Sale> saleDialog;

    @Autowired
    private SaleService saleService;

    private ObservableList<Sale> sales;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // queued
        try {
            sales = observableArrayList(saleService.getAllSales());
            tableViewSale.setItems(sales);
            tableColSaleId.setCellValueFactory(new PropertyValueFactory<Sale, Long>("identity"));
            tableColSaleName.setCellValueFactory(new PropertyValueFactory<Sale, String>("name"));
            tableColSaleEntries.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Sale, String>, ObservableValue<String>>() {
                public ObservableValue<String> call(TableColumn.CellDataFeatures<Sale, String> p) {
                    // p.getValue() returns the Person instance for a particular TableView row
                    List<String> list = new LinkedList<String>();
                    p.getValue().getEntries().forEach(entry -> list.add(entry.getName()));
                    return new SimpleStringProperty(list.toString());
                }
            });

            saleDialogController = salesDialogPane.getController(DialogSaleController.class);
            saleDialog = new Dialog<>();
            saleDialog.getDialogPane().setContent(salesDialogPane);
            ButtonType cancelButtonType = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
            saleDialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, cancelButtonType);
            final Button btOk = (Button) saleDialog.getDialogPane().lookupButton(ButtonType.OK);
            btOk.addEventFilter(ActionEvent.ACTION, event -> {
                if (!saleDialogController.validateData()) {
                    event.consume();
                }
            });
            final Button cancelButton = (Button) saleDialog.getDialogPane().lookupButton(cancelButtonType);
            cancelButton.addEventFilter(ActionEvent.ACTION, event -> {
                saleDialogController.resetDialog();
            });
        }catch (ServiceException e){
            LOGGER.error("Initialize Menu View Failed due to" + e);
        }

    }

    public void buttonSaleUpdateClicked(ActionEvent actionEvent) {
        try {
            LOGGER.info("Update Sale Button Click");
            Stage stage = new Stage();
            if(tableViewSale.getSelectionModel().getSelectedItem() == null){
                showErrorDialog
                        ("Error", "Input Validation Error", "You have to select a Sale to Update");
                return;
            }
            saleDialogController.resetDialog();
            saleDialogController.setDialogEnumeration(DialogEnumeration.UPDATE);
            saleDialogController.setSale(tableViewSale.getSelectionModel().getSelectedItem());
            saleDialogController.showSale();
            saleDialog.showAndWait();
            sales.setAll(saleService.getAllSales());
            saleDialogController.resetDialog();
        }catch (Exception e){
            LOGGER.error("Loading the Menus failed" + e);
        }
    }

    public void buttonSaleRemoveClicked(ActionEvent actionEvent) {
        try {
            LOGGER.info("Delete Sale Button Click");
            if(tableViewSale.getSelectionModel().getSelectedItem() == null){
                showErrorDialog
                        ("Error", "Input Validation Error", "You have to select a Sale to Delete");
                return;
            }
            Sale s = tableViewSale.getSelectionModel().getSelectedItem();
            if (s instanceof IntermittentSale) {
                saleService.removeIntermittentSale((IntermittentSale)s);
            } else if (s instanceof OnetimeSale) {
                saleService.removeIntermittentSale((IntermittentSale)s);
            } else {
                LOGGER.error("Sale is neither IntermittentSale nor OnetimeSale!");
            }
            sales.setAll(saleService.getAllSales());
        }catch (Exception e){
            LOGGER.error("Loading the Sales failed" + e);
        }
    }

    public void buttonSaleAddClicked(ActionEvent actionEvent){
        try {
            LOGGER.info("Add Sale Button Click");
            saleDialogController.resetDialog();
            saleDialogController.setDialogEnumeration(DialogEnumeration.ADD);
            saleDialog.showAndWait();
            sales.setAll(saleService.getAllSales());
        }catch (Exception e){
            LOGGER.error("Loading the Sales failed" + e);
        }
    }

    /*public void buttonShowAllSaleClicked(ActionEvent actionEvent) {
        try {
            sales.setAll(saleService.getAllSales());
        } catch (Exception e){
            LOGGER.error("Loading All Sale failed" + e);
        }
    }*/

    public void showErrorDialog(String title, String head, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(head);
        alert.setContentText(content);

        alert.showAndWait();
    }
}
