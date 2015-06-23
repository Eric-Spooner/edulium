package com.at.ac.tuwien.sepm.ss15.edulium.service.impl;

import com.at.ac.tuwien.sepm.ss15.edulium.dao.DAO;
import com.at.ac.tuwien.sepm.ss15.edulium.dao.DAOException;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.Menu;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.MenuCategory;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.MenuEntry;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.history.History;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ValidationException;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.Validator;
import com.at.ac.tuwien.sepm.ss15.edulium.service.MenuService;
import com.at.ac.tuwien.sepm.ss15.edulium.service.ServiceException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Resource;
import java.util.List;

/**
 * Implementation of the MenuService
 */
class MenuServiceImpl implements MenuService {
    private static final Logger LOGGER = LogManager.getLogger(MenuServiceImpl.class);

    @Resource(name = "menuEntryDAO")
    private DAO<MenuEntry> menuEntryDAO;
    @Resource(name = "menuCategoryDAO")
    private DAO<MenuCategory> menuCategoryDAO;
    @Resource(name = "menuDAO")
    private DAO<Menu> menuDAO;
    @Resource(name = "menuEntryValidator")
    private Validator<MenuEntry> menuEntryValidator;
    @Resource(name = "menuCategoryValidator")
    private Validator<MenuCategory> menuCategoryValidator;
    @Resource(name = "menuValidator")
    private Validator<Menu> menuValidator;

    @Override
    public void addMenuEntry(MenuEntry menuEntry) throws ValidationException, ServiceException {
        LOGGER.debug("Entering addMenuEntry with parameter: " + menuEntry);

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
        LOGGER.debug("Entering updateMenuEntry with parameter: " + menuEntry);

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
        LOGGER.debug("Entering removeMenuEntry with parameter: " + menuEntry);

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
        LOGGER.debug("Entering findMenuEntry with parameter: " + matcher);

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
    public List<History<MenuEntry>> getMenuEntryHistory(MenuEntry entry) throws ValidationException, ServiceException {
        LOGGER.debug("Entering getMenuEntryHistory with parameter: " + entry);

        menuEntryValidator.validateIdentity(entry);

        try {
            return menuEntryDAO.getHistory(entry);
        } catch (DAOException e) {
            LOGGER.error("An Error has occurred in the data access object", e);
            throw new ServiceException("An Error has occurred in the data access object", e);
        }
    }

    @Override
    public void addMenuCategory(MenuCategory menuCategory) throws ValidationException, ServiceException {
        LOGGER.debug("Entering addMenuCategory with parameter: " + menuCategory);

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
        LOGGER.debug("Entering updateMenuCategory with parameter: " + menuCategory);

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
        LOGGER.debug("Entering removeMenuCategory with parameter: " + menuCategory);

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
        LOGGER.debug("Entering findMenuCategory with parameter: " + matcher);

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
    public List<History<MenuCategory>> getMenuCategoryHistory(MenuCategory category) throws ValidationException, ServiceException {
        LOGGER.debug("Entering getMenuCategoryHistory with parameter: " + category);

        menuCategoryValidator.validateIdentity(category);

        try {
            return menuCategoryDAO.getHistory(category);
        } catch (DAOException e) {
            LOGGER.error("An Error has occurred in the data access object", e);
            throw new ServiceException("An Error has occurred in the data access object", e);
        }
    }

    @Override
    public void addMenu(Menu menu) throws ValidationException, ServiceException {
        LOGGER.debug("Entering addMenu with parameter: " + menu);

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
        LOGGER.debug("Entering updateMenu with parameter: " + menu);

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
        LOGGER.debug("Entering removeMenu with parameter: " + menu);

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
        LOGGER.debug("Entering removeMenu with parameter: " + matcher);

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

    @Override
    public List<History<Menu>> getMenuHistory(Menu menu) throws ValidationException, ServiceException {
        LOGGER.debug("Entering getMenuHistory with parameter: " + menu);

        menuValidator.validateIdentity(menu);

        try {
            return menuDAO.getHistory(menu);
        } catch (DAOException e) {
            LOGGER.error("An Error has occurred in the data access object", e);
            throw new ServiceException("An Error has occurred in the data access object", e);
        }
    }
}
