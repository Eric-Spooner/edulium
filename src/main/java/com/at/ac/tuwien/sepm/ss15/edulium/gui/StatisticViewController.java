package com.at.ac.tuwien.sepm.ss15.edulium.gui;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.statistics.MenuEntryRevenue;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ValidationException;
import com.at.ac.tuwien.sepm.ss15.edulium.service.ServiceException;
import com.at.ac.tuwien.sepm.ss15.edulium.service.StatisticsService;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

import static javafx.collections.FXCollections.observableArrayList;

/**
 * Controller used for the Statistics View
 */
public class StatisticViewController implements Initializable, Controller {
    private static final Logger LOGGER = LogManager.getLogger(StatisticViewController.class);

    @FXML
    private CheckBox fromCheckBox;
    @FXML
    private CheckBox toCheckBox;
    @FXML
    private DatePicker fromDatePicker;
    @FXML
    private DatePicker toDatePicker;
    @FXML
    public TableView<MenuEntryRevenue> popularDishesTable;
    @FXML
    private TableColumn<MenuEntryRevenue,Long> popularDishesColMenuEntryId;
    @FXML
    private TableColumn<MenuEntryRevenue,String> popularDishesColMenuEntryName;
    @FXML
    private TableColumn<MenuEntryRevenue,BigDecimal> popularDishesColMenuEntryPrice;
    @FXML
    private TableColumn<MenuEntryRevenue,Long> popularDishesColNumberSold;
    @FXML
    private TableColumn<MenuEntryRevenue,BigDecimal> popularDishesColRevenue;
    @FXML
    public LineChart<Number, Number> totalIncomeChart;

    @Autowired
    private StatisticsService statisticsService;

    private ObservableList<MenuEntryRevenue> menuEntries;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            menuEntries = observableArrayList(statisticsService.getPopularDishes(null, null));
            popularDishesTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
            popularDishesTable.setItems(menuEntries);
            popularDishesColMenuEntryId.setCellValueFactory(new PropertyValueFactory<>("MenuEntryId"));
            popularDishesColMenuEntryName.setCellValueFactory(new PropertyValueFactory<>("MenuEntryName"));
            popularDishesColMenuEntryPrice.setCellValueFactory(new PropertyValueFactory<>("MenuEntryPrice"));
            popularDishesColNumberSold.setCellValueFactory(new PropertyValueFactory<>("SoldNumber"));
            popularDishesColRevenue.setCellValueFactory(new PropertyValueFactory<>("Revenue"));
            updateChart();
        } catch (Exception e) {
            LOGGER.error("Initialize Statistics View Failed due to" + e);
        }
    }


    @Override
    public void disable(boolean disabled) {

    }

    /**
     * gets called when button is clicked
     */
    public void showStatistics() {
        try {
            menuEntries.clear();
            menuEntries.addAll(statisticsService.getPopularDishes(getFromDate(), getToDate()));
        } catch (Exception e) {
            LOGGER.error("Show statistics failed due to" + e);
        }
        updateChart();
    }

    private void updateChart() {
        try {
            List<Pair<LocalDate, BigDecimal>> incomeDevelopment = statisticsService.getRevenueDevelopment(getFromDate(), getToDate());
            XYChart.Series series = new XYChart.Series();
            series.setName("Daily Revenue");
            for (Pair<LocalDate, BigDecimal> p : incomeDevelopment) {
                series.getData().add(new XYChart.Data(p.getKey().toString(), p.getValue().doubleValue()));
            }
            totalIncomeChart.getData().clear();
            totalIncomeChart.getData().add(series);
        } catch (Exception e) {
            LOGGER.error("Could not update chart due to" + e);
        }
    }

    private LocalDate getFromDate() {
        return fromCheckBox.isSelected() ? fromDatePicker.getValue() : null;
    }

    private LocalDate getToDate() {
        return toCheckBox.isSelected() ? toDatePicker.getValue() : null;
    }

}
