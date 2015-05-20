package com.at.ac.tuwien.sepm.ss15.edulium.service.impl;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.Menu;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.MenuCategory;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.MenuEntry;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ValidationException;
import com.at.ac.tuwien.sepm.ss15.edulium.service.MenuService;
import com.at.ac.tuwien.sepm.ss15.edulium.service.ServiceException;

import java.util.List;

/**
 * Implementation of the MenuService
 */
class MenuServiceImpl implements MenuService {

    @Override
    public void addMenuEntry(MenuEntry menuEntry) throws ValidationException, ServiceException {

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
    public void removeMenuCategory(MenuCategory menuCategory) throws ValidationException, ServiceException {

    }

    @Override
    public List<MenuEntry> findMenuCategory(MenuCategory matcher) throws ServiceException {
        return null;
    }

    @Override
    public List<MenuEntry> getAllMenuCategories() throws ServiceException {
        return null;
    }

    @Override
    public void addMenu(Menu menu) throws ValidationException, ServiceException {

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
