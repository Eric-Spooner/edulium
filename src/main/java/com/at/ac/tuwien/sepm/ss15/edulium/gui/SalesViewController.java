package com.at.ac.tuwien.sepm.ss15.edulium.gui;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.Sale;
import com.at.ac.tuwien.sepm.ss15.edulium.service.SaleService;
import com.at.ac.tuwien.sepm.ss15.edulium.service.ServiceException;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

import static javafx.collections.FXCollections.observableArrayList;

/**
 * Created by - on 12.06.2015.
 */
public class SalesViewController implements Initializable, Controller {
    private static final Logger LOGGER = LogManager.getLogger(SalesViewController.class);

    @FXML
    private TableView<Sale> tableViewSale;
    @FXML
    private TableColumn<Sale,Long> tableColSaleId;
    @FXML
    private TableColumn<Sale,String> tableColSaleName;
    @FXML
    private TableColumn<Sale,String> tableColSaleEntries;

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
        }catch (ServiceException e){
            LOGGER.error("Initialize Menu View Failed due to" + e);
        }

    }

    public void buttonSaleUpdateClicked(ActionEvent actionEvent) {
        /*try {
            LOGGER.info("Update Menu Button Click");
            Stage stage = new Stage();
            if(tableViewSale.getSelectionModel().getSelectedItem() == null){
                ManagerViewController.showErrorDialog
                        ("Error", "Input Validation Error", "You have to select a Menu to Update");
                return;
            }
            DialogMenuController.resetDialog();
            DialogMenuController.setThisStage(stage);
            DialogMenuController.setDialogEnumeration(DialogEnumeration.UPDATE);
            DialogMenuController.setMenu(tableViewSale.getSelectionModel().getSelectedItem());
            stage.setTitle("Update Menu");
            ApplicationContext context = EduliumApplicationContext.getContext();
            FXMLPane myPane = context.getBean("menuDialogPane", FXMLPane.class);
            Scene scene = new Scene(myPane);
            stage.setScene(scene);
            stage.showAndWait();
            sales.setAll(saleService.getAllMenus());
            DialogMenuController.resetDialog();
        }catch (Exception e){
            LOGGER.error("Loading the Menus failed" + e);
        }*/
    }

    public void buttonSaleRemoveClicked(ActionEvent actionEvent) {
        /*try {
            LOGGER.info("Delete Menu Button Click");
            if(tableViewSale.getSelectionModel().getSelectedItem() == null){
                ManagerViewController.showErrorDialog
                        ("Error", "Input Validation Error", "You have to select a Menu to Delete");
                return;
            }
            saleService.removeMenu(tableViewSale.getSelectionModel().getSelectedItem());
            sales.setAll(saleService.getAllMenus());
        }catch (Exception e){
            LOGGER.error("Loading the Menus failed" + e);
        }*/
    }

    public void buttonSaleAddClicked(ActionEvent actionEvent){
        /*try {
            LOGGER.info("Add Menu Button Click");
            Stage stage = new Stage();
            DialogMenuController.resetDialog();
            DialogMenuController.setThisStage(stage);
            DialogMenuController.setDialogEnumeration(DialogEnumeration.ADD);
            stage.setTitle("Add Menu");
            AnchorPane myPane = FXMLLoader.load(getClass().getResource("/gui/DialogMenu.fxml"));
            Scene scene = new Scene(myPane);
            stage.setScene(scene);
            stage.showAndWait();
            sales.setAll(saleService.getAllMenus());
        }catch (IOException e){
            LOGGER.error("Add Menu Button Click did not work");
        }catch (Exception e){
            LOGGER.error("Loading the Menus failed" + e);
        }*/
    }

    public void buttonShowAllSaleClicked(ActionEvent actionEvent) {
        try {
            sales.setAll(saleService.getAllSales());
        } catch (Exception e){
            LOGGER.error("Loading All Sale failed" + e);
        }
    }

    @Override
    public void disable(boolean disabled) {

    }
}
