package com.at.ac.tuwien.sepm.ss15.edulium.gui;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.statistics.MenuEntryRevenue;
import com.at.ac.tuwien.sepm.ss15.edulium.service.StatisticsService;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
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
            updateChart();
            menuEntries = observableArrayList(statisticsService.getPopularDishes(null, null));
            popularDishesTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
            popularDishesTable.setItems(menuEntries);
            popularDishesColMenuEntryId.setCellValueFactory(new PropertyValueFactory<>("MenuEntryId"));
            popularDishesColMenuEntryName.setCellValueFactory(new PropertyValueFactory<>("MenuEntryName"));
            popularDishesColMenuEntryPrice.setCellValueFactory(new PropertyValueFactory<>("MenuEntryPrice"));
            popularDishesColNumberSold.setCellValueFactory(new PropertyValueFactory<>("SoldNumber"));
            popularDishesColRevenue.setCellValueFactory(new PropertyValueFactory<>("Revenue"));
        } catch (Exception e) {
            LOGGER.error("Initialize Statistics View Failed due to" + e);
        }
    }


    @Override
    public void disable(boolean disabled) {

    }

    private void updateChart() {
        try {
            List<Pair<LocalDate, BigDecimal>> incomeDevelopment = statisticsService.getRevenueDevelopment(null, null);
            XYChart.Series series = new XYChart.Series();
            series.setName("Daily Revenue");
            for (Pair<LocalDate, BigDecimal> p : incomeDevelopment) {
                series.getData().add(new XYChart.Data(p.getKey().toString(), p.getValue().doubleValue()));
            }
            totalIncomeChart.getData().add(series);
        } catch (Exception e) {
            //TODO handle exception
            e.printStackTrace();
        }
    }

}
