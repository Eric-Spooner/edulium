package com.at.ac.tuwien.sepm.ss15.edulium.service.impl;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.*;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.statistics.MenuEntryRevenue;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ValidationException;
import com.at.ac.tuwien.sepm.ss15.edulium.service.InvoiceService;
import com.at.ac.tuwien.sepm.ss15.edulium.service.OrderService;
import com.at.ac.tuwien.sepm.ss15.edulium.service.ServiceException;
import com.at.ac.tuwien.sepm.ss15.edulium.service.StatisticsService;
import javafx.util.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * implementation of the StatisticsService
 */
public class StatisticsServiceImpl implements StatisticsService {
    private static final Logger LOGGER = LogManager.getLogger(StatisticsServiceImpl.class);

    @Autowired
    private InvoiceService invoiceService;
    @Autowired
    private OrderService orderService;

    @Override
    public List<MenuEntryRevenue> getPopularDishes(LocalDate fromDate, LocalDate toDate) throws ValidationException, ServiceException {
        LOGGER.debug("Entering getPopularDishes with parameters: " + fromDate + ", " + toDate);

        //Get orders and count popular dishes/drinks
        List<Order> orders = orderService.findOrderBetween(fromDate==null ? null : fromDate.atStartOfDay(), toDate==null ? null : toDate.atTime(23,59));
        HashMap<MenuEntry, Long> occurrences = new HashMap<>();
        for (Order order : orders) {
            MenuEntry menuEntry = order.getMenuEntry();
            if (occurrences.containsKey(menuEntry)) {
                Long oldValue = occurrences.get(menuEntry);
                occurrences.put(menuEntry, new Long(oldValue+1));
            } else {
                occurrences.put(menuEntry, new Long(1));
            }
        }

        //Create list
        List<MenuEntryRevenue> popularDishes = new ArrayList<>();
        for (MenuEntry entry : occurrences.keySet()) {
            MenuEntryRevenue menuEntryRevenue = new MenuEntryRevenue();
            menuEntryRevenue.setMenuEntry(entry);
            menuEntryRevenue.setSoldNumber(occurrences.get(entry));
            popularDishes.add(menuEntryRevenue);
        }

        Collections.sort(popularDishes, Collections.reverseOrder());

        return popularDishes;
    }

    @Override
    public List<Pair<LocalDate, BigDecimal>> getRevenueDevelopment(LocalDate fromDate, LocalDate toDate) throws ValidationException, ServiceException {
        LOGGER.debug("Entering getRevenueDevelopment with parameters: " + fromDate + ", " + toDate);

        //Get invoices and count revenue
        List<Invoice> invoices = invoiceService.findInvoiceBetween(fromDate==null ? null : fromDate.atStartOfDay(), toDate==null ? null : toDate.atTime(23, 59));
        TreeMap<LocalDate, BigDecimal> revenueDevelopment = new TreeMap<>();
        for (Invoice invoice : invoices) {
            LocalDate date = invoice.getTime().toLocalDate();
            if (revenueDevelopment.containsKey(date)) {
                BigDecimal oldValue = revenueDevelopment.get(date);
                revenueDevelopment.put(date, oldValue.add(invoice.getGross()));
            } else {
                revenueDevelopment.put(date, invoice.getGross());
            }
        }

        //Fill dates with 0 values to create consistent chart
        LocalDate firstDate = revenueDevelopment.firstKey();
        LocalDate lastDate = revenueDevelopment.lastKey();
        for (LocalDate localDate = firstDate.plus(1, ChronoUnit.DAYS); localDate.isBefore(lastDate) ;localDate = localDate.plus(1, ChronoUnit.DAYS)) {
            if (! revenueDevelopment.containsKey(localDate)) {
                revenueDevelopment.put(localDate, new BigDecimal(0));
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
