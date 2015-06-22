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
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.util.*;

import static javafx.collections.FXCollections.observableArrayList;
import static javafx.collections.FXCollections.observableList;

/**
 * Controller used for the Manager View
 */
@Controller
public class ManagerViewController implements Initializable {
    private static final Logger LOGGER = LogManager.getLogger(ManagerViewController.class);

    @FXML
    private TabPane tabPaneManager;

    @Resource(name = "employeeViewPane")
    FXMLPane employeeViewPane;
    @Resource(name = "taxRateViewPane")
    FXMLPane taxRateViewPane;
    @Resource(name = "menuCategoryViewPane")
    FXMLPane menuCategoryViewPane;
    @Resource(name = "menuEntryViewPane")
    FXMLPane menuEntryViewPane;
    @Resource(name = "menuViewPane")
    FXMLPane menuViewPane;
    @Resource(name = "statisticViewPane")
    FXMLPane statisticViewPane;
    @Resource(name = "roomViewPane")
    FXMLPane roomViewPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tabPaneManager.getTabs().add(new Tab("Employees", employeeViewPane));
        tabPaneManager.getTabs().add(new Tab("TaxRates", taxRateViewPane));
        tabPaneManager.getTabs().add(new Tab("MenuCategories", menuCategoryViewPane));
        tabPaneManager.getTabs().add(new Tab("MenuEntries", menuEntryViewPane));
        tabPaneManager.getTabs().add(new Tab("Menus", menuViewPane));
        tabPaneManager.getTabs().add(new Tab("Statistics", statisticViewPane));
        tabPaneManager.getTabs().add(new Tab("Rooms", roomViewPane));
    }

    public static void showErrorDialog(String title, String head, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(head);
        alert.setContentText(content);

        alert.showAndWait();
    }
}
