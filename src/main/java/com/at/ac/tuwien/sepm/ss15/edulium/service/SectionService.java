package com.at.ac.tuwien.sepm.ss15.edulium.service;

import com.at.ac.tuwien.sepm.ss15.edulium.dao.DAOException;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.Section;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.Table;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.history.History;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ValidationException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Section Service interface
 */
@Component
@PreAuthorize("isAuthenticated()")
@Transactional(propagation = Propagation.REQUIRED)
public interface SectionService {
    /**
     * writes the section to the underlying datasource;
     *
     * sets the identity parameter of section if the data was stored
     * successfully and a new identity has been generated by the
     * underlying datasource
     * @param section object to store
     * @throws ServiceException if the object couldn't be stored
     */
    void addSection(Section section) throws ServiceException;

    /**
     * updates data of the section in the underlying datasource
     * @param section object to update
     * @throws ServiceException if the section couldn't be updated
     */
    void updateSection(Section section) throws ServiceException;

    /**
     * removes the section from the underlying datasource
     * @param section object to remove
     * @throws ServiceException if the object couldn't be removed
     */
    void deleteSection(Section section) throws ServiceException;

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
     * adds one table to the section
     *
     * @param section the section that the table should be added
     * @param table the table to be added
     * @throws ServiceException if table couldn't be added
     */
    void addTable(Section section, Table table) throws ServiceException;

    /**
     * returns all tables that are located in the section
     *
     * @param section the section that contains the retrieved tables
     * @return the tables in the section
     * @throws ServiceException if tables couldn't be retrieved
     */
    List<Table> getAllTables(Section section) throws ServiceException;
}
