package com.at.ac.tuwien.sepm.ss15.edulium.service;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.MenuEntry;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ValidationException;
import org.springframework.security.access.prepost.PreAuthorize;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.HashMap;

/**
 * Service interface for statistics
 */
@PreAuthorize("isAuthenticated()")
public interface StatisticsService extends Service {
    /**
     * Get a list of popular dishes and drinks, ordered descending by total income, over the given period.
     * @param fromDate the beginning of the period. A null value means unbounded.
     * @param toDate the end of the period. A null value means unbounded.
     * @return the list of popular dishes/drinks along with the number of sold items.
     * @throws ValidationException if the dates are not valid
     * @throws ServiceException if an error in the service or persistence layer has occurred
     */
    @PreAuthorize("hasRole('MANAGER')")
    HashMap<MenuEntry, Long> getPopularDishes(LocalTime fromDate, LocalTime toDate) throws ValidationException, ServiceException;

    /**
     * Get the chart data of the total income over the given time period.
     * @param fromDate the beginning of the period. A null value means unbounded.
     * @param toDate the end of the period. A null value means unbounded.
     * @return the chart data (Date, Income)
     * @throws ValidationException if the dates are not valid
     * @throws ServiceException if an error in the service or persistence layer has occurred
     */
    @PreAuthorize("hasRole('MANAGER')")
    HashMap<LocalTime, BigDecimal> getIncomeDevelopment(LocalTime fromDate, LocalTime toDate) throws ValidationException, ServiceException;
}
