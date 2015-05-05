package com.at.ac.tuwien.sepm.ss15.edulium.dao;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.MenuCategory;

import java.util.List;

/**
 * data access object for the menu category domain
 */
public interface MenuCategoryDAO {

    /**
     * writes the object menuCategory to the underlying datasource;
     *
     * sets the identity parameter of menuCategory, if the data was stored
     * successfully
     * @param menuCategory object to store (must not be null)
     * @throws DAOException if the object couldn't be stored
     */
    void create(MenuCategory menuCategory) throws DAOException;

    /**
     * updates data of object menuCategory
     * @param menuCategory object to update (must not be null)
     * @throws DAOException if the object couldn't be updated
     */
    void update(MenuCategory menuCategory) throws DAOException;

    /**
     * removes the object menuCategory
     * @param menuCategory object to remove (must not be null)
     * @throws DAOException if the object couldn't be removed
     */
    void delete(MenuCategory menuCategory) throws DAOException;

    /**
     * returns all objects which parameters match the
     * parameters of the object menuCategory
     *
     * all parameters with value NULL will not be used for matching
     * @param menuCategory object used for matching
     * @return a list of matched objects
     * @throws DAOException if the data couldn't be retrieved
     */
    List<MenuCategory> find(MenuCategory menuCategory) throws DAOException;

    /**
     * @return returns all stored objects
     * @throws DAOException if the data couldn't be retrieved
     */
    List<MenuCategory> getAll() throws DAOException;
}
