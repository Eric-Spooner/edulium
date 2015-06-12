package com.at.ac.tuwien.sepm.ss15.edulium.gui;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.*;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.Menu;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.Table;
import com.at.ac.tuwien.sepm.ss15.edulium.service.*;
import javafx.beans.property.SimpleStringProperty;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ValidationException;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.util.*;

import static javafx.collections.FXCollections.observableArrayList;
import static javafx.collections.FXCollections.observableList;

/**
 * Controller used for the Manager View
 */
@Component
public class ManagerViewController implements Initializable, Controller {
    private static final Logger LOGGER = LogManager.getLogger(ManagerViewController.class);

    @FXML TabPane tabPaneManager;

    private ObservableList<Tab> tabs;

    @Override
    public void initialize(URL location, ResourceBundle resources){
        ApplicationContext context = EduliumApplicationContext.getContext();
        FXMLPane employeeViewPane = context.getBean("emplyoeeViewPane", FXMLPane.class);
        tabPaneManager.getTabs().add(new Tab("Employees", employeeViewPane));
        FXMLPane taxRateViewPane = context.getBean("taxRateViewPane", FXMLPane.class);
        tabPaneManager.getTabs().add(new Tab("TaxRates", taxRateViewPane));
        FXMLPane menuCategoryViewPane = context.getBean("menuCategoryViewPane", FXMLPane.class);
        tabPaneManager.getTabs().add(new Tab("MenuCategories", menuCategoryViewPane));
        FXMLPane menuEntryViewPane = context.getBean("menuEntryViewPane", FXMLPane.class);
        tabPaneManager.getTabs().add(new Tab("MenuEntries", menuEntryViewPane));
        FXMLPane menuViewPane = context.getBean("menuViewPane", FXMLPane.class);
        tabPaneManager.getTabs().add(new Tab("Menus", menuViewPane));
        FXMLPane statisticViewPane = context.getBean("statisticViewPane", FXMLPane.class);
        tabPaneManager.getTabs().add(new Tab("Statistics", statisticViewPane));
        FXMLPane roomViewPane = context.getBean("roomViewPane", FXMLPane.class);
        tabPaneManager.getTabs().add(new Tab("Rooms", roomViewPane));
    }

    public static void showErrorDialog(String title, String head, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(head);
        alert.setContentText(content);

        alert.showAndWait();
    }

    @Override
    public void disable(boolean disabled) {

    }
}
