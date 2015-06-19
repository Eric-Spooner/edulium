package com.at.ac.tuwien.sepm.ss15.edulium.gui;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ValidationException;
import com.at.ac.tuwien.sepm.ss15.edulium.service.ServiceException;
import com.at.ac.tuwien.sepm.ss15.edulium.service.StatisticsService;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalTime;
import java.util.HashMap;
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
            HashMap<LocalTime, BigDecimal> incomeDevelopment = statisticsService.getIncomeDevelopment(null,null);
            XYChart.Series series = new XYChart.Series();
            series.setName("My portfolio");
            for (LocalTime t : incomeDevelopment.keySet()) {
                series.getData().add(new XYChart.Data(t.toString(),incomeDevelopment.get(t)));
            }
            totalIncomeChart.getData().add(series);
        } catch (Exception e) {
            //TODO handle exception
            e.printStackTrace();
        }
    }

}
