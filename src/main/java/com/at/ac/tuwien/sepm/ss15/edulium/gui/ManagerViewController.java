package com.at.ac.tuwien.sepm.ss15.edulium.gui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller used for the Manager View
 */
@Controller
public class ManagerViewController implements Initializable {

    @FXML
    private TabPane tabPaneManager;

    @Resource(name = "employeeViewPane")
    private FXMLPane employeeViewPane;
    @Resource(name = "taxRateViewPane")
    private FXMLPane taxRateViewPane;
    @Resource(name = "menuCategoryViewPane")
    private FXMLPane menuCategoryViewPane;
    @Resource(name = "menuEntryViewPane")
    private FXMLPane menuEntryViewPane;
    @Resource(name = "menuViewPane")
    private FXMLPane menuViewPane;
    @Resource(name = "salesViewPane")
    private FXMLPane salesViewPane;
    @Resource(name = "statisticViewPane")
    private FXMLPane statisticViewPane;
    @Resource(name = "roomViewPane")
    private FXMLPane roomViewPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tabPaneManager.getTabs().add(new Tab("Employees", employeeViewPane));
        tabPaneManager.getTabs().add(new Tab("TaxRates", taxRateViewPane));
        tabPaneManager.getTabs().add(new Tab("MenuCategories", menuCategoryViewPane));
        tabPaneManager.getTabs().add(new Tab("MenuEntries", menuEntryViewPane));
        tabPaneManager.getTabs().add(new Tab("Menus", menuViewPane));
        tabPaneManager.getTabs().add(new Tab("Sales", salesViewPane));
        tabPaneManager.getTabs().add(new Tab("Statistics", statisticViewPane));
        tabPaneManager.getTabs().add(new Tab("Rooms", roomViewPane));
    }
}
