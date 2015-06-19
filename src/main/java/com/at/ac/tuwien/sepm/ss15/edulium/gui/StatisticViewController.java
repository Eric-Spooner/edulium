package com.at.ac.tuwien.sepm.ss15.edulium.gui;

import com.at.ac.tuwien.sepm.ss15.edulium.service.StatisticsService;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.util.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Controller used for the Statistics View
 */
public class StatisticViewController implements Initializable, Controller {
    private static final Logger LOGGER = LogManager.getLogger(StatisticViewController.class);

    @FXML
    public LineChart<Number, Number> totalIncomeChart;

    @Autowired
    private StatisticsService statisticsService;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        updateChart();
    }


    @Override
    public void disable(boolean disabled) {

    }

    private void updateChart() {
        try {
            List<Pair<LocalDate, BigDecimal>> incomeDevelopment = statisticsService.getIncomeDevelopment(null,null);
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
