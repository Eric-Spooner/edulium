package com.at.ac.tuwien.sepm.ss15.edulium.service;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.*;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.history.History;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ValidationException;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

/**
 * Service interface for sales.
 */
public interface SaleService extends Service {
    /**
     * returns all sales from the underlying datasource
     * @throws ServiceException if an error in the service or persistence layer has occurred
     */
    List<Sale> getAllSales() throws ServiceException;

    /**
     * adds a intermittentSale to the underlying datasource
     * @param intermittentSale intermittentSale to add
     * @throws ValidationException if the intermittentSale object is not valid
     * @throws ServiceException if an error in the service or persistence layer has occurred
     */
    @PreAuthorize("hasRole('MANAGER')")
    void addIntermittentSale(IntermittentSale intermittentSale) throws ValidationException, ServiceException;

    /**
     * updates a IntermittentSale in the underlying datasource
     * @param intermittentSale IntermittentSale to udpate
     * @throws ValidationException if the IntermittentSale object is not valid
     * @throws ServiceException if an error in the service or persistence layer has occurred
     */
    @PreAuthorize("hasRole('MANAGER')")
    void updateIntermittentSale(IntermittentSale intermittentSale) throws ValidationException, ServiceException;

    /**
     * removes a intermittentSale from the underlying datasource
     * @param intermittentSale IntermittentSaleCategory to remove
     * @throws ValidationException if the intermittentSale object is not valid
     * @throws ServiceException if an error in the service or persistence layer has occurred
     */
    @PreAuthorize("hasRole('MANAGER')")
    void removeIntermittentSale(IntermittentSale intermittentSale) throws ValidationException, ServiceException;

    /**
     * returns all intermittentSales from the underlying datasource
     * which parameters match the parameters of the matcher
     * @param matcher matcher
     * @throws ServiceException if an error in the service or persistence layer has occurred
     */
    List<IntermittentSale> findIntermittentSale(IntermittentSale matcher) throws ServiceException;

    /**
     * returns all intermittentSales from the underlying datasource
     * @throws ValidationException if the IntermittentSale object is not valid
     * @throws ServiceException if an error in the service or persistence layer has occurred
     */
    List<IntermittentSale> getAllIntermittentSales() throws ServiceException;

    /**
     * @param intermittentSale object to get the history for
     * @return returns the history of changes for the object
     * @throws ValidationException if the IntermittentSale object parameters are
     *         not valid for this action
     * @throws ServiceException if an error in the service or persistence layer has occurred
     */
    @PreAuthorize("hasRole('MANAGER')")
    List<History<IntermittentSale>> getIntermittentSaleHistory(IntermittentSale intermittentSale) throws ValidationException, ServiceException;

    /**
     * adds a onetimeSale to the underlying datasource
     * @param onetimeSale onetimeSale to add
     * @throws ValidationException if the onetimeSale object is not valid
     * @throws ServiceException if an error in the service or persistence layer has occurred
     */
    @PreAuthorize("hasRole('MANAGER')")
    void addOnetimeSale(OnetimeSale onetimeSale) throws ValidationException, ServiceException;

    /**
     * updates a OnetimeSale in the underlying datasource
     * @param onetimeSale OnetimeSale to udpate
     * @throws ValidationException if the OnetimeSale object is not valid
     * @throws ServiceException if an error in the service or persistence layer has occurred
     */
    @PreAuthorize("hasRole('MANAGER')")
    void updateOnetimeSale(OnetimeSale onetimeSale) throws ValidationException, ServiceException;

    /**
     * removes a onetimeSale from the underlying datasource
     * @param onetimeSale OnetimeSaleCategory to remove
     * @throws ValidationException if the onetimeSale object is not valid
     * @throws ServiceException if an error in the service or persistence layer has occurred
     */
    @PreAuthorize("hasRole('MANAGER')")
    void removeOnetimeSale(OnetimeSale onetimeSale) throws ValidationException, ServiceException;

    /**
     * returns all onetimeSales from the underlying datasource
     * which parameters match the parameters of the matcher
     * @param matcher matcher
     * @throws ServiceException if an error in the service or persistence layer has occurred
     */
    List<OnetimeSale> findOnetimeSale(OnetimeSale matcher) throws ServiceException;

    /**
     * returns all onetimeSales from the underlying datasource
     * @throws ValidationException if the OnetimeSale object is not valid
     * @throws ServiceException if an error in the service or persistence layer has occurred
     */
    List<OnetimeSale> getAllOnetimeSales() throws ServiceException;


    /**
     * @param onetimeSale object to get the history for
     * @return returns the history of changes for the object
     * @throws ValidationException if the OnetimeSale object parameters are
     *         not valid for this action
     * @throws ServiceException if an error in the service or persistence layer has occurred
     */
    @PreAuthorize("hasRole('MANAGER')")
    List<History<OnetimeSale>> getOnetimeSaleHistory(OnetimeSale onetimeSale) throws ValidationException, ServiceException;

    /**
     * Tries to find a sale for the given menuEntry and changes the price if possible.
     * @param order the order that might be sold cheaper with the sale
     * @throws ValidationException if the menuEntry object parameters are
     *         not valid for this action
     * @throws ServiceException if an error in the service or persistence layer has occurred
     */
    void applySales(Order order) throws ValidationException, ServiceException;
}