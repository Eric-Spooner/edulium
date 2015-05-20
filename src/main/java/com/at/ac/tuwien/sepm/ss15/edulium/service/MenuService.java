package com.at.ac.tuwien.sepm.ss15.edulium.service;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.Menu;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.MenuCategory;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.MenuEntry;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ValidationException;
import org.springframework.security.access.annotation.Secured;

import java.util.List;

/**
 * Service interface for the MenuCategory, MenuEntry and Menu domain objects
 */
public interface MenuService extends Service {
    /**
     * adds a MenuEntry to the underlying datasource
     * @param menuEntry MenuEntry to add
     * @throws ValidationException if the MenuEntry object is not valid
     * @throws ServiceException if an error in the service or persistence layer has occurred
     */
    //FIXME:
    //@Secured({"MANAGER"})
    void addMenuEntry(MenuEntry menuEntry) throws ValidationException, ServiceException;

    /**
     * updates a MenuEntry in the underlying datasource
     * @param menuEntry MenuEntry to udpate
     * @throws ValidationException if the MenuEntry object is not valid
     * @throws ServiceException if an error in the service or persistence layer has occurred
     */
    //FIXME:
    //@Secured({"MANAGER"})
    void updateMenuEntry(MenuEntry menuEntry) throws ValidationException, ServiceException;

    /**
     * removes a MenuEntry from the underlying datasource
     * @param menuEntry MenuEntry to remove
     * @throws ValidationException if the MenuEntry object is not valid
     * @throws ServiceException if an error in the service or persistence layer has occurred
     */
    //FIXME:
    //@Secured({"MANAGER"})
    void removeMenuEntry(MenuEntry menuEntry) throws ValidationException, ServiceException;

    /**
     * returns all menuEntries from the underlying datasource
     * which parameters match the parameters of the matcher
     * @param matcher matcher
     * @throws ServiceException if an error in the service or persistence layer has occurred
     */
    List<MenuEntry> findMenuEntry(MenuEntry matcher) throws ServiceException;

    /**
     * returns all menuEntries from the underlying datasource
     * @throws ValidationException if the MenuEntry object is not valid
     * @throws ServiceException if an error in the service or persistence layer has occurred
     */
    List<MenuEntry> getAllMenuEntries() throws ServiceException;

    /**
     * adds a MenuCategory to the underlying datasource
     * @param menuCategory MenuCategory to add
     * @throws ValidationException if the MenuCategory object is not valid
     * @throws ServiceException if an error in the service or persistence layer has occurred
     */
    //FIXME:
    //@Secured({"MANAGER"})
    void addMenuCategory(MenuCategory menuCategory) throws ValidationException, ServiceException;

    /**
     * updates a MenuCategory in the underlying datasource
     * @param menuCategory MenuCategory to udpate
     * @throws ValidationException if the MenuCategory object is not valid
     * @throws ServiceException if an error in the service or persistence layer has occurred
     */
    //FIXME:
    //@Secured({"MANAGER"})
    void updateMenuCategory(MenuCategory menuCategory) throws ValidationException, ServiceException;

    /**
     * removes a MenuCategory from the underlying datasource
     * @param menuCategory MenuCategory to remove
     * @throws ValidationException if the MenuCategory object is not valid
     * @throws ServiceException if an error in the service or persistence layer has occurred
     */
    //FIXME:
    //@Secured({"MANAGER"})
    void removeMenuCategory(MenuCategory menuCategory) throws ValidationException, ServiceException;

    /**
     * returns all menuCategories from the underlying datasource
     * which parameters match the parameters of the matcher
     * @param matcher matcher
     * @throws ServiceException if an error in the service or persistence layer has occurred
     */
    List<MenuCategory> findMenuCategory(MenuCategory matcher) throws ServiceException;

    /**
     * returns all menuCategories from the underlying datasource
     * @throws ValidationException if the MenuEntry object is not valid
     * @throws ServiceException if an error in the service or persistence layer has occurred
     */
    List<MenuCategory> getAllMenuCategories() throws ServiceException;

    /**
     * adds a menu to the underlying datasource
     * @param menu menu to add
     * @throws ValidationException if the menu object is not valid
     * @throws ServiceException if an error in the service or persistence layer has occurred
     */
    //FIXME:
    //@Secured({"MANAGER"})
    void addMenu(Menu menu) throws ValidationException, ServiceException;

    /**
     * updates a Menu in the underlying datasource
     * @param menu Menu to udpate
     * @throws ValidationException if the Menu object is not valid
     * @throws ServiceException if an error in the service or persistence layer has occurred
     */
    //FIXME:
    //@Secured({"MANAGER"})
    void updateMenu(Menu menu) throws ValidationException, ServiceException;

    /**
     * removes a menu from the underlying datasource
     * @param menu MenuCategory to remove
     * @throws ValidationException if the menu object is not valid
     * @throws ServiceException if an error in the service or persistence layer has occurred
     */
    //FIXME:
    //@Secured({"MANAGER"})
    void removeMenu(Menu menu) throws ValidationException, ServiceException;

    /**
     * returns all menus from the underlying datasource
     * which parameters match the parameters of the matcher
     * @param matcher matcher
     * @throws ServiceException if an error in the service or persistence layer has occurred
     */
    List<Menu> findMenu(Menu matcher) throws ServiceException;

    /**
     * returns all menus from the underlying datasource
     * @throws ValidationException if the Menu object is not valid
     * @throws ServiceException if an error in the service or persistence layer has occurred
     */
    List<Menu> getAllMenus() throws ServiceException;

}
