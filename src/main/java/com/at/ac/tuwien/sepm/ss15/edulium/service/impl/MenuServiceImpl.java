package com.at.ac.tuwien.sepm.ss15.edulium.service.impl;

import com.at.ac.tuwien.sepm.ss15.edulium.dao.DAO;
import com.at.ac.tuwien.sepm.ss15.edulium.dao.DAOException;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.Menu;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.MenuCategory;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.MenuEntry;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ValidationException;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.Validator;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.impl.MenuCategoryValidator;
import com.at.ac.tuwien.sepm.ss15.edulium.service.MenuService;
import com.at.ac.tuwien.sepm.ss15.edulium.service.ServiceException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Implementation of the MenuService
 */
class MenuServiceImpl implements MenuService {
    private static final Logger LOGGER = LogManager.getLogger(MenuServiceImpl.class);

    @Autowired
    private DAO<MenuEntry> menuEntryDAO;
    @Autowired
    private DAO<MenuCategory> menuCategoryDAO;
    @Autowired
    private DAO<Menu> menuDAO;
    @Autowired
    private Validator<MenuEntry> menuEntryValidator;
    @Autowired
    private Validator<MenuCategory> menuCategoryValidator;
    @Autowired
    private Validator<Menu> menuValidator;

    @Override
    public void addMenuEntry(MenuEntry menuEntry) throws ValidationException, ServiceException {
        LOGGER.debug("Entering addMenuEntry with parameters: " + menuEntry);

        menuEntryValidator.validateForCreate(menuEntry);

        try {
            menuEntryDAO.create(menuEntry);
        } catch (DAOException e) {
            LOGGER.error("An Error has occurred in the data access object", e);
            throw new ServiceException("An Error has occurred in the data access object", e);
        }
    }

    @Override
    public void updateMenuEntry(MenuEntry menuEntry) throws ValidationException, ServiceException {
        LOGGER.debug("Entering updateMenuEntry with parameters: " + menuEntry);

        menuEntryValidator.validateForUpdate(menuEntry);

        try {
            menuEntryDAO.update(menuEntry);
        } catch (DAOException e) {
            LOGGER.error("An Error has occurred in the data access object", e);
            throw new ServiceException("An Error has occurred in the data access object", e);
        }
    }

    @Override
    public void removeMenuEntry(MenuEntry menuEntry) throws ValidationException, ServiceException {
        LOGGER.debug("Entering removeMenuEntry with parameters: " + menuEntry);

        menuEntryValidator.validateForDelete(menuEntry);

        try {
            menuEntryDAO.delete(menuEntry);
        } catch (DAOException e) {
            LOGGER.error("An Error has occurred in the data access object", e);
            throw new ServiceException("An Error has occurred in the data access object", e);
        }
    }

    @Override
    public List<MenuEntry> findMenuEntry(MenuEntry matcher) throws ServiceException {
        LOGGER.debug("Entering findMenuEntry with parameters: " + matcher);

        try {
            return menuEntryDAO.find(matcher);
        } catch (DAOException e) {
            LOGGER.error("An Error has occurred in the data access object", e);
            throw new ServiceException("An Error has occurred in the data access object", e);
        }
    }

    @Override
    public List<MenuEntry> getAllMenuEntries() throws ServiceException {
        LOGGER.debug("Entering getAllMenuEntries");

        try {
            return menuEntryDAO.getAll();
        } catch (DAOException e) {
            LOGGER.error("An Error has occurred in the data access object", e);
            throw new ServiceException("An Error has occurred in the data access object",e);
        }
    }

    @Override
    public void addMenuCategory(MenuCategory menuCategory) throws ValidationException, ServiceException {
        LOGGER.debug("Entering addMenuCategory with parameters: " + menuCategory);

        menuCategoryValidator.validateForCreate(menuCategory);

        try {
            menuCategoryDAO.create(menuCategory);
        } catch (DAOException e) {
            LOGGER.error("An Error has occurred in the data access object", e);
            throw new ServiceException("An Error has occurred in the data access object", e);
        }
    }

    @Override
    public void updateMenuCategory(MenuCategory menuCategory) throws ValidationException, ServiceException {
        LOGGER.debug("Entering updateMenuCategory with parameters: " + menuCategory);

        menuCategoryValidator.validateForUpdate(menuCategory);

        try {
            menuCategoryDAO.update(menuCategory);
        } catch (DAOException e) {
            LOGGER.error("An Error has occurred in the data access object", e);
            throw new ServiceException("An Error has occurred in the data access object", e);
        }
    }

    @Override
    public void removeMenuCategory(MenuCategory menuCategory) throws ValidationException, ServiceException {
        LOGGER.debug("Entering removeMenuCategory with parameters: " + menuCategory);

        menuCategoryValidator.validateForDelete(menuCategory);

        try {
            menuCategoryDAO.delete(menuCategory);
        } catch (DAOException e) {
            LOGGER.error("An Error has occurred in the data access object", e);
            throw new ServiceException("An Error has occurred in the data access object", e);
        }
    }

    @Override
    public List<MenuCategory> findMenuCategory(MenuCategory matcher) throws ServiceException {
        LOGGER.debug("Entering findMenuCategory with parameters: " + matcher);

        try {
            return menuCategoryDAO.find(matcher);
        } catch (DAOException e) {
            LOGGER.error("An Error has occurred in the data access object", e);
            throw new ServiceException("An Error has occurred in the data access object", e);
        }
    }

    @Override
    public List<MenuCategory> getAllMenuCategories() throws ServiceException {
        LOGGER.debug("Entering getAllMenuCategories ");

        try {
            return menuCategoryDAO.getAll();
        } catch (DAOException e) {
            LOGGER.error("An Error has occurred in the data access object", e);
            throw new ServiceException("An Error has occurred in the data access object", e);
        }
    }

    @Override
    public void addMenu(Menu menu) throws ValidationException, ServiceException {
        LOGGER.debug("Entering addMenu with parameters: " + menu);

        menuValidator.validateForCreate(menu);

        try {
            menuDAO.create(menu);
        } catch (DAOException e) {
            LOGGER.error("An Error has occurred in the data access object", e);
            throw new ServiceException("An Error has occurred in the data access object", e);
        }
    }

    @Override
    public void updateMenu(Menu menu) throws ValidationException, ServiceException {
        LOGGER.debug("Entering updateMenu with parameters: " + menu);

        menuValidator.validateForUpdate(menu);

        try {
            menuDAO.update(menu);
        } catch (DAOException e) {
            LOGGER.error("An Error has occurred in the data access object", e);
            throw new ServiceException("An Error has occurred in the data access object", e);
        }
    }

    @Override
    public void removeMenu(Menu menu) throws ValidationException, ServiceException {
        LOGGER.debug("Entering removeMenu with parameters: " + menu);

        menuValidator.validateForDelete(menu);

        try {
            menuDAO.delete(menu);
        } catch (DAOException e) {
            LOGGER.error("An Error has occurred in the data access object", e);
            throw new ServiceException("An Error has occurred in the data access object", e);
        }
    }

    @Override
    public List<Menu> findMenu(Menu matcher) throws ServiceException {
        LOGGER.debug("Entering removeMenu with parameters: " + matcher);

        try {
            return menuDAO.find(matcher);
        } catch (DAOException e) {
            LOGGER.error("An Error has occurred in the data access object", e);
            throw new ServiceException("An Error has occurred in the data access object", e);
        }
    }

    @Override
    public List<Menu> getAllMenus() throws ServiceException {
        LOGGER.debug("Entering getAllMenus");

        try {
            return menuDAO.getAll();
        } catch (DAOException e) {
            LOGGER.error("An Error has occurred in the data access object", e);
            throw new ServiceException("An Error has occurred in the data access object", e);
        }
    }
}
