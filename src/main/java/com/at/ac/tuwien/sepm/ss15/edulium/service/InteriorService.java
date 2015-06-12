package com.at.ac.tuwien.sepm.ss15.edulium.service;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.Section;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.Table;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ValidationException;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

/**
 * Section Service interface
 */
@PreAuthorize("isAuthenticated()")
public interface InteriorService extends Service {
    /**
     * writes the section to the underlying datasource;
     *
     * sets the identity parameter of section if the data was stored
     * successfully and a new identity has been generated by the
     * underlying datasource
     * @param section object to store
     * @throws ServiceException if the object couldn't be stored
     */
    void addSection(Section section) throws ServiceException, ValidationException;

    /**
     * updates data of the section in the underlying datasource
     * @param section object to update
     * @throws ServiceException if the section couldn't be updated
     */
    void updateSection(Section section) throws ServiceException, ValidationException;

    /**
     * removes the section from the underlying datasource
     * @param section object to remove
     * @throws ServiceException if the object couldn't be removed
     */
    void deleteSection(Section section) throws ServiceException, ValidationException;

    /**
     * returns all sections which parameters match the
     * parameters of the parameter object
     *
     * all parameters with value NULL will not be used for matching
     * @param section object used for matching
     * @return a list of matched section
     * @throws ServiceException if the data couldn't be retrieved
     */
    List<Section> findSections(Section section) throws ServiceException;

    /**
     * @return returns all stored sections
     * @throws ServiceException if the data couldn't be retrieved
     */
    List<Section> getAllSections() throws ServiceException;

    /**
     * writes the table to the underlying datasource;
     *
     * sets the identity parameter of table if the data was stored
     * successfully and a new identity has been generated by the
     * underlying datasource
     * @param table object to store
     * @throws com.at.ac.tuwien.sepm.ss15.edulium.dao.DAOException if the object couldn't be stored
     */
    void addTable(Table table) throws ServiceException, ValidationException;

    /**
     * updates data of the table in the underlying datasource
     * @param table object to update
     * @throws com.at.ac.tuwien.sepm.ss15.edulium.dao.DAOException if the table couldn't be updated
     */
    void updateTable(Table table) throws ServiceException, ValidationException;

    /**
     * removes the table from the underlying datasource
     * @param table object to remove
     * @throws com.at.ac.tuwien.sepm.ss15.edulium.dao.DAOException if the object couldn't be removed
     */
    void deleteTable(Table table) throws ServiceException, ValidationException;

    /**
     * returns all tables which parameters match the
     * parameters of the parameter object
     *
     * all parameters with value NULL will not be used for matching
     * @param table object used for matching
     * @return a list of matched table
     * @throws com.at.ac.tuwien.sepm.ss15.edulium.dao.DAOException if the data couldn't be retrieved
     */
    List<Table> findTables(Table table) throws ServiceException;

    /**
     * @return returns all stored tables
     * @throws com.at.ac.tuwien.sepm.ss15.edulium.dao.DAOException if the data couldn't be retrieved
     */
    List<Table> getAllTables() throws ServiceException;
}
