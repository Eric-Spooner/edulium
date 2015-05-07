package com.at.ac.tuwien.sepm.ss15.edulium.dao;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.Table;

import java.util.List;

/**
 * data access object for the table domain
 */
public interface TableDAO {
    /**
     * writes the object table to the underlying datasource
     *
     * sets the number parameter of table, if the data was stored
     * successfully
     * @param table object to store
     * @throws DAOException if the object couldn't be stored
     */
    void create(Table table) throws DAOException;

    /**
     * updates data of object table in the underlying datasource
     * @param table object to update
     * @throws DAOException if the object couldn't be updated
     */
    void update(Table table) throws DAOException;

    /**
     * removes the object table from the underlying datasource
     * @param table object to remove
     * @throws DAOException if the object couldn't be removed
     */
    void delete(Table table) throws DAOException;

    /**
     * returns all objects which parameters match the
     * parameters of the object table
     *
     * all parameters with value NULL will not be used for matching
     * @param table object used for matching
     * @return a list of matched objects
     * @throws DAOException if the data couldn't be retrieved
     */
    List<Table> find(Table table) throws DAOException;

    /**
     * @return returns all stored objects
     * @throws DAOException if the data couldn't be retrieved
     */
    List<Table> getAll() throws DAOException;
}
