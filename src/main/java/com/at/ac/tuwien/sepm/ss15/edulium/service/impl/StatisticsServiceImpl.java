package com.at.ac.tuwien.sepm.ss15.edulium.service.impl;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.Invoice;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.MenuCategory;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.MenuEntry;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.TaxRate;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.statistics.MenuEntryRevenue;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ValidationException;
import com.at.ac.tuwien.sepm.ss15.edulium.service.InvoiceService;
import com.at.ac.tuwien.sepm.ss15.edulium.service.ServiceException;
import com.at.ac.tuwien.sepm.ss15.edulium.service.StatisticsService;
import javafx.util.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * implementation of the StatisticsService
 */
public class StatisticsServiceImpl implements StatisticsService {
    private static final Logger LOGGER = LogManager.getLogger(StatisticsServiceImpl.class);

    @Autowired
    private InvoiceService invoiceService;

    @Override
    public List<MenuEntryRevenue> getPopularDishes(LocalDate fromDate, LocalDate toDate) throws ValidationException, ServiceException {
        LOGGER.debug("Entering getPopularDishes with parameters: " + fromDate + ", " + toDate);

        //Get invoices and count popular dishes/drinks
        List<MenuEntryRevenue> popularDishes = new ArrayList<>();
        List<Invoice> invoices = invoiceService.getAllInvoices();

        return popularDishes;
    }

    @Override
    public List<Pair<LocalDate, BigDecimal>> getRevenueDevelopment(LocalDate fromDate, LocalDate toDate) throws ValidationException, ServiceException {
        LOGGER.debug("Entering getRevenueDevelopment with parameters: " + fromDate + ", " + toDate);

        //Get invoices and count revenue
        List<Invoice> invoices = invoiceService.getAllInvoices();
        HashMap<LocalDate, BigDecimal> revenueDevelopment = new HashMap<>();
        for (Invoice invoice : invoices) {
            LocalDate date = invoice.getTime().toLocalDate();
            if (revenueDevelopment.containsKey(date)) {
                BigDecimal oldValue = revenueDevelopment.get(date);
                revenueDevelopment.put(date, oldValue.add(invoice.getGross()));
            } else {
                revenueDevelopment.put(date, invoice.getGross());
            }
        }

        //Create chart
        ArrayList<Pair<LocalDate, BigDecimal>> incomeChart = new ArrayList<>();
        for (LocalDate date : revenueDevelopment.keySet()) {
            incomeChart.add(new Pair<>(date, revenueDevelopment.get(date)));
        }

        return incomeChart;
    }
}
