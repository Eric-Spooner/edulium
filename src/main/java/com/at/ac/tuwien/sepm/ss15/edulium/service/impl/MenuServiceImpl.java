package com.at.ac.tuwien.sepm.ss15.edulium.service.impl;

import com.at.ac.tuwien.sepm.ss15.edulium.dao.DAO;
import com.at.ac.tuwien.sepm.ss15.edulium.dao.DAOException;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.Menu;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.MenuCategory;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.MenuEntry;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ValidationException;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.Validator;
import com.at.ac.tuwien.sepm.ss15.edulium.service.MenuService;
import com.at.ac.tuwien.sepm.ss15.edulium.service.ServiceException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Implementation of the MenuService
 */
public class MenuServiceImpl implements MenuService {
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

    @Autowired
    public MenuServiceImpl(DAO<MenuEntry> menuEntryDAO, DAO<MenuCategory> menuCategoryDAO, DAO<Menu> menuDAO) {
        this.menuEntryDAO = menuEntryDAO;
        this.menuCategoryDAO = menuCategoryDAO;
        this.menuDAO = menuDAO;
    }

    @Autowired
    public void setValidators(Validator<MenuEntry> menuEntryValidator,
                              Validator<MenuCategory> menuCategoryValidator,
                              Validator<Menu> menuValidator) {
        this.menuEntryValidator = menuEntryValidator;
        this.menuCategoryValidator = menuCategoryValidator;
        this.menuValidator = menuValidator;
    }

    @Override
    public void addMenuEntry(MenuEntry menuEntry) throws ValidationException, ServiceException {

    }

    @Override
    public void updateMenuEntry(MenuEntry menuEntry) throws ValidationException, ServiceException {

    }

    @Override
    public void removeMenuEntry(MenuEntry menuEntry) throws ValidationException, ServiceException {

    }

    @Override
    public List<MenuEntry> findMenuEntry(MenuEntry matcher) throws ServiceException {
        return null;
    }

    @Override
    public List<MenuEntry> getAllMenuEntries() throws ServiceException {
        return null;
    }

    @Override
    public void addMenuCategory(MenuCategory menuCategory) throws ValidationException, ServiceException {

    }

    @Override
    public void updateMenuCategory(MenuCategory menuCategory) throws ValidationException, ServiceException {

    }

    @Override
    public void removeMenuCategory(MenuCategory menuCategory) throws ValidationException, ServiceException {

    }

    @Override
    public List<MenuCategory> findMenuCategory(MenuCategory matcher) throws ServiceException {
        return null;
    }

    @Override
    public List<MenuCategory> getAllMenuCategories() throws ServiceException {
        return null;
    }

    @Override
    public void addMenu(Menu menu) throws ValidationException, ServiceException {

    }

    @Override
    public void updateMenu(Menu menu) throws ValidationException, ServiceException {

    }

    @Override
    public void removeMenu(Menu menu) throws ValidationException, ServiceException {

    }

    @Override
    public List<Menu> findMenu(Menu matcher) throws ServiceException {
        return null;
    }

    @Override
    public List<Menu> getAllMenus() throws ServiceException {
        return null;
    }
}
