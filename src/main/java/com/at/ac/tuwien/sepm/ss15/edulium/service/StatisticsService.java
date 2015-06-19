package com.at.ac.tuwien.sepm.ss15.edulium.service;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.statistics.MenuEntryRevenue;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ValidationException;
import javafx.util.Pair;
import org.springframework.security.access.prepost.PreAuthorize;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

/**
 * Service interface for statistics
 */
@PreAuthorize("isAuthenticated()")
public interface StatisticsService extends Service {
    /**
     * Get a list of popular dishes and drinks, ordered descending by total revenue, over the given period.
     * @param fromDate the beginning of the period. A null value means unbounded.
     * @param toDate the end of the period. A null value means unbounded.
     * @return the list of popular dishes/drinks along with the number of sold items.
     * @throws ValidationException if the dates are not valid
     * @throws ServiceException if an error in the service or persistence layer has occurred
     */
    @PreAuthorize("hasRole('MANAGER')")
    List<MenuEntryRevenue> getPopularDishes(LocalDate fromDate, LocalDate toDate) throws ValidationException, ServiceException;

    /**
     * Get the chart data of the total daily income over the given time period.
     * @param fromDate the beginning of the period. A null value means unbounded.
     * @param toDate the end of the period. A null value means unbounded.
     * @return the chart data (Date, Income)
     * @throws ValidationException if the dates are not valid
     * @throws ServiceException if an error in the service or persistence layer has occurred
     */
    @PreAuthorize("hasRole('MANAGER')")
    List<Pair<LocalDate, BigDecimal>> getIncomeDevelopment(LocalDate fromDate, LocalDate toDate) throws ValidationException, ServiceException;
}
