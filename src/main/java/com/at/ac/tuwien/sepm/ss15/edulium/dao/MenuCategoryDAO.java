package com.at.ac.tuwien.sepm.ss15.edulium.dao;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.MenuCategory;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ValidationException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * data access object for the menu category domain
 */
@Repository
@Transactional(propagation = Propagation.MANDATORY)
public interface MenuCategoryDAO {

    /**
     * writes the object menuCategory to the underlying datasource;
     *
     * sets the identity parameter of menuCategory, if the data was stored
     * successfully
     * @param menuCategory object to store
     * @throws DAOException if the object couldn't be stored
     */
    void create(MenuCategory menuCategory) throws DAOException, ValidationException;

    /**
     * updates data of object menuCategory in the underlying datasource
     * @param menuCategory object to update
     * @throws DAOException if the object couldn't be updated
     */
    void update(MenuCategory menuCategory) throws DAOException, ValidationException;

    /**
     * removes the object menuCategory from the underlying datasource
     * @param menuCategory object to remove
     * @throws DAOException if the object couldn't be removed
     */
    void delete(MenuCategory menuCategory) throws DAOException, ValidationException;

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
