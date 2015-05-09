package com.at.ac.tuwien.sepm.ss15.edulium.dao;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.Section;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ValidationException;

import java.util.List;

/**
 * data access object for the section domain
 */
public interface SectionDAO {

    /**
     * writes the object section to the underlying datasource;
     *
     * sets the identity parameter of section, if the data was stored
     * successfully
     * @param section object to store
     * @throws DAOException if the object couldn't be stored
     */
    void create(Section section) throws DAOException, ValidationException;

    /**
     * updates data of object section in the underlying datasource
     * @param section object to update
     * @throws DAOException if the object couldn't be updated
     */
    void update(Section section) throws DAOException, ValidationException;

    /**
     * removes the object section from the underlying datasource
     * @param section object to remove
     * @throws DAOException if the object couldn't be removed
     */
    void delete(Section section) throws DAOException, ValidationException;

    /**
     * returns all objects which parameters match the
     * parameters of the object section
     *
     * all parameters with value NULL will not be used for matching
     * @param section object used for matching
     * @return a list of matched objects
     * @throws DAOException if the data couldn't be retrieved
     */
    List<Section> find(Section section) throws DAOException;

    /**
     * @return returns all stored objects
     * @throws DAOException if the data couldn't be retrieved
     */
    List<Section> getAll() throws DAOException;
}
