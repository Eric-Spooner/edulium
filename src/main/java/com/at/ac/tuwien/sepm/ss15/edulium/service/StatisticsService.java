package com.at.ac.tuwien.sepm.ss15.edulium.service;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.MenuEntry;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ValidationException;
import javafx.util.Pair;
import org.springframework.security.access.prepost.PreAuthorize;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.List;

/**
 * Service interface for statistics
 */
@PreAuthorize("isAuthenticated()")
public interface StatisticsService extends Service {
    /**
     * Get a list of popular dishes and drinks, ordered descending by total income, over the given period.
     * @param fromTime the beginning of the period. A null value means unbounded.
     * @param untilTime the end of the period. A null value means unbounded.
     * @return the list of popular dishes/drinks along with the number of sold items.
     * @throws ValidationException if the dates are not valid
     * @throws ServiceException if an error in the service or persistence layer has occurred
     */
    @PreAuthorize("hasRole('MANAGER')")
    List<Pair<MenuEntry, Long>> getPopularDishes(LocalTime fromTime, LocalTime untilTime) throws ValidationException, ServiceException;

    /**
     * Get a list of categories in which the customers can be put in, according to the invoice sizes, over the given period.
     * @param fromTime the beginning of the period. A null value means unbounded.
     * @param untilTime the end of the period. A null value means unbounded.
     * @return the list of the categories, i.e. the category name and the number of the occurrences.
     * @throws ValidationException if the dates are not valid
     * @throws ServiceException if an error in the service or persistence layer has occurred
     */
    @PreAuthorize("hasRole('MANAGER')")
    List<Pair<String, Long>> getCustomerBehaviour(LocalTime fromTime, LocalTime untilTime) throws ValidationException, ServiceException;

    /**
     * Get the chart data of the total income over the given time period.
     * @param fromTime the beginning of the period. A null value means unbounded.
     * @param untilTime the end of the period. A null value means unbounded.
     * @return the chart data (Date, Income)
     * @throws ValidationException if the dates are not valid
     * @throws ServiceException if an error in the service or persistence layer has occurred
     */
    @PreAuthorize("hasRole('MANAGER')")
    List<Pair<LocalTime, BigDecimal>> getIncomeDevelopment(LocalTime fromTime, LocalTime untilTime) throws ValidationException, ServiceException;
}
