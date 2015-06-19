package com.at.ac.tuwien.sepm.ss15.edulium.gui;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller used for the Statistics View
 */
public class StatisticViewController implements Initializable, Controller {
    private static final Logger LOGGER = LogManager.getLogger(StatisticViewController.class);

    @FXML
    public LineChart<Number, Number> totalIncomeChart;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        updateChart();
    }


    @Override
    public void disable(boolean disabled) {

    }

    private void updateChart() {
        XYChart.Series series = new XYChart.Series();
        series.setName("My portfolio");
        series.getData().add(new XYChart.Data("15.06",2));
        series.getData().add(new XYChart.Data("16.06",6));
        series.getData().add(new XYChart.Data("17.06",5));
        series.getData().add(new XYChart.Data("18.06",7));
        series.getData().add(new XYChart.Data("19.06",13));
        series.getData().add(new XYChart.Data("20.06",11));
        totalIncomeChart.getData().add(series);

    }

}
