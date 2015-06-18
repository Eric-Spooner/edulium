package com.at.ac.tuwien.sepm.ss15.edulium.service.impl;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.MenuCategory;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.MenuEntry;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.TaxRate;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ValidationException;
import com.at.ac.tuwien.sepm.ss15.edulium.service.ServiceException;
import com.at.ac.tuwien.sepm.ss15.edulium.service.StatisticsService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.HashMap;

/**
 * implementation of the StatisticsService
 */
public class StatisticsServiceImpl implements StatisticsService {
    private static final Logger LOGGER = LogManager.getLogger(StatisticsServiceImpl.class);

    @Override
    public HashMap<MenuEntry, Long> getPopularDishes(LocalTime fromDate, LocalTime toDate) throws ValidationException, ServiceException {
        LOGGER.debug("Entering getPopularDishes with parameters: " + fromDate + ", " + toDate);

        HashMap<MenuEntry, Long> popularDishes = new HashMap<>();

        //dummy data for GUI testing, TODO: replace by real service
        MenuCategory category = new MenuCategory();
        TaxRate taxRate = new TaxRate();
        MenuEntry entry1 = new MenuEntry();
        entry1.setIdentity(new Long(1));
        entry1.setPrice(new BigDecimal(6.50));
        entry1.setAvailable(new Boolean(true));
        entry1.setCategory(category);
        entry1.setDescription("blabla");
        entry1.setName("Linsensuppe");
        entry1.setTaxRate(taxRate);
        popularDishes.put(entry1, new Long(54));
        MenuEntry entry2 = new MenuEntry();
        entry2.setIdentity(new Long(2));
        entry2.setPrice(new BigDecimal(1.00));
        entry2.setAvailable(new Boolean(true));
        entry2.setCategory(category);
        entry2.setDescription("blabla");
        entry2.setName("Mineralwasser");
        entry2.setTaxRate(taxRate);
        popularDishes.put(entry2, new Long(80));

        return popularDishes;
    }

    @Override
    public HashMap<LocalTime, BigDecimal> getIncomeDevelopment(LocalTime fromDate, LocalTime toDate) throws ValidationException, ServiceException {
        LOGGER.debug("Entering getIncomeDevelopment with parameters: " + fromDate + ", " + toDate);

        HashMap<LocalTime, BigDecimal> incomeChart = new HashMap<>();

        //dummy data for GUI testing, TODO: replace by real service
        incomeChart.put(fromDate, new BigDecimal(56.4));
        incomeChart.put(toDate, new BigDecimal(56.4));

        return incomeChart;
    }
}
