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

    @Autowired
    private MenuService menuService;
    @Autowired
    private TaxRateService taxRateService;
    @Autowired
    private InteriorService interiorService;
    @Autowired
    private UserService userService;
    @Autowired
    private OrderService orderService;


    @FXML TabPane tabPaneManager;

    @FXML
    private TableView<User> tableViewEmployee;
    @FXML
    private TableColumn<User,String> employeeId;
    @FXML
    private TableColumn<User,String> employeeName;
    @FXML
    private TableColumn<User,String> employeeRole;

    @FXML
    private TableView<TaxRate> tableViewTaxRate;
    @FXML
    private TableColumn<TaxRate,Long> tableColTaxRateID;
    @FXML
    private TableColumn<TaxRate,BigDecimal> tableColTaxRateValue;
    @FXML
    private TextField txtTaxRateValue;

    @FXML
    private TableView<MenuCategory> tableViewMenuCategory;
    @FXML
    private TableColumn<MenuCategory,Long> tableColMenuCategoryID;
    @FXML
    private TableColumn<MenuCategory,String> tableColMenuCategoryName;

    @FXML
    private TableView<MenuEntry> tableViewMenuEntry;
    @FXML
    private TableColumn<MenuEntry,Long> tableColMenuEntryId;
    @FXML
    private TableColumn<MenuEntry,String> tableColMenuEntryName;
    @FXML
    private TableColumn<MenuEntry,BigDecimal> tableColMenuEntryPrice;
    @FXML
    private TableColumn<MenuEntry, String> tableColMenuEntryCategory;
    @FXML
    private TableColumn<MenuEntry, Boolean> tableColMenuEntryAvailable;

    @FXML
    private TableView<Menu> tableViewMenu;
    @FXML
    private TableColumn<Menu,Long> tableColMenuId;
    @FXML
    private TableColumn<Menu,String> tableColMenuName;
    @FXML
    private TableColumn<Menu,String> tableColMenuEntries;

    @FXML
    private Canvas tablesCanvas;
    @FXML
    private ScrollPane scrollPaneLeft;
    @FXML
    private AnchorPane tableAnchor;
    private ArrayList<Rect> rects = new ArrayList<Rect>();
    private double scaleX = 1.0;
    private double scaleY = 1.0;
    private long clickedSectionId = -1;

    private final int FACT = 10;
    private final int CANVAS_PADDING = 20;
    private final int TABLE_SIZE = 40;
    private final int SECTION_OFFSET = 40;
    private final int SECTION_PADDING = 10;
    private final int TEXT_BORDER_BOTTOM = 2;

    private ObservableList<TaxRate> taxRates;
    private ObservableList<Menu> menus;
    private ObservableList<MenuEntry> menuEntries;
    private ObservableList<MenuCategory> menuCategories;
    private ObservableList<User> users;

    @Override
    public void initialize(URL location, ResourceBundle resources){
        try {
            ApplicationContext context = EduliumApplicationContext.getContext();

            users = observableArrayList(userService.getAllUsers());
            tableViewEmployee.setItems(users);
            employeeId.setCellValueFactory(new PropertyValueFactory<User, String>("identity"));
            employeeName.setCellValueFactory(new PropertyValueFactory<User, String>("name"));
            employeeRole.setCellValueFactory(new PropertyValueFactory<User, String>("role"));

            Tab tab = new Tab();
            tab.setText("User");
            tab.setContent(context.getBean("employeeViewPane", FXMLPane.class));
            tabPaneManager.getTabs().set(1,tab);

            taxRates = observableArrayList(taxRateService.getAllTaxRates());
            tableViewTaxRate.setItems(taxRates);
            tableColTaxRateID.setCellValueFactory(new PropertyValueFactory<TaxRate, Long>("identity"));
            tableColTaxRateValue.setCellValueFactory(new PropertyValueFactory<TaxRate, BigDecimal>("value"));

            menuEntries = observableArrayList(menuService.getAllMenuEntries());
            tableViewMenuEntry.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
            tableViewMenuEntry.setItems(menuEntries);
            tableColMenuEntryId.setCellValueFactory(new PropertyValueFactory<MenuEntry, Long>("identity"));
            tableColMenuEntryName.setCellValueFactory(new PropertyValueFactory<MenuEntry, String>("name"));
            tableColMenuEntryPrice.setCellValueFactory(new PropertyValueFactory<MenuEntry, BigDecimal>("price"));
            tableColMenuEntryCategory.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<MenuEntry, String>, ObservableValue<String>>() {
                public ObservableValue<String> call(CellDataFeatures<MenuEntry, String> p) {
                    // p.getValue() returns the Person instance for a particular TableView row
                    return new SimpleStringProperty(p.getValue().getCategory().getName());
                }
            });

            tableColMenuEntryAvailable.setCellValueFactory(new PropertyValueFactory<MenuEntry, Boolean>("available"));

            menuCategories = observableArrayList(menuService.getAllMenuCategories());
            tableViewMenuCategory.setItems(menuCategories);
            tableColMenuCategoryID.setCellValueFactory(new PropertyValueFactory<MenuCategory, Long>("identity"));
            tableColMenuCategoryName.setCellValueFactory(new PropertyValueFactory<MenuCategory, String>("name"));

            menus = observableArrayList(menuService.getAllMenus());
            tableViewMenu.setItems(menus);
            tableColMenuId.setCellValueFactory(new PropertyValueFactory<Menu, Long>("identity"));
            tableColMenuName.setCellValueFactory(new PropertyValueFactory<Menu, String>("name"));
            tableColMenuEntries.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Menu, String>, ObservableValue<String>>() {
                public ObservableValue<String> call(CellDataFeatures<Menu, String>p) {
                    // p.getValue() returns the Person instance for a particular TableView row
                    List<String> list = new LinkedList<String>();
                    p.getValue().getEntries().forEach(entry->list.add(entry.getName()));
                    return new SimpleStringProperty(list.toString());
                }
            });

            // Example data for Table View
            Section section1 = new Section();
            section1.setIdentity((long)1);
            section1.setName("Garten");
            Table table1 = new Table();
            table1.setSection(section1);
            table1.setRow(0);
            table1.setColumn(0);
            table1.setNumber((long)1);
            table1.setSeats(4);
            Table table2 = new Table();
            table2.setSection(section1);
            table2.setRow(5);
            table2.setColumn(0);
            table2.setNumber((long)2);
            table2.setSeats(4);
            Table table3 = new Table();
            table3.setSection(section1);
            table3.setRow(5);
            table3.setColumn(5);
            table3.setNumber((long)3);
            table3.setSeats(4);

            Section section2 = new Section();
            section2.setIdentity((long)2);
            section2.setName("Bar");
            Table table4 = new Table();
            table4.setSection(section2);
            table4.setRow(0);
            table4.setColumn(0);
            table4.setNumber((long)4);
            table4.setSeats(4);
            Table table5 = new Table();
            table5.setSection(section2);
            table5.setRow(5);
            table5.setColumn(5);
            table5.setNumber((long)5);
            table5.setSeats(4);
            Table table6 = new Table();
            table6.setSection(section2);
            table6.setRow(0);
            table6.setColumn(10);
            table6.setNumber((long)6);
            table6.setSeats(4);

            Section section3 = new Section();
            section3.setIdentity((long)3);
            section3.setName("Saal");
            Table table7 = new Table();
            table7.setSection(section3);
            table7.setRow(0);
            table7.setColumn(0);
            table7.setNumber((long)4);
            table7.setSeats(4);
            Table table8 = new Table();
            table8.setSection(section3);
            table8.setRow(5);
            table8.setColumn(5);
            table8.setNumber((long)5);
            table8.setSeats(4);
            Table table9 = new Table();
            table9.setSection(section3);
            table9.setRow(5);
            table9.setColumn(10);
            table9.setNumber((long)6);
            table9.setSeats(4);

            Section section4 = new Section();
            section4.setIdentity((long) 4);
            section4.setName("Gang");
            Table table10 = new Table();
            table10.setSection(section4);
            table10.setRow(0);
            table10.setColumn(0);
            table10.setNumber((long)4);
            table10.setSeats(4);
            Table table11 = new Table();
            table11.setSection(section4);
            table11.setRow(5);
            table11.setColumn(5);
            table11.setNumber((long)5);
            table11.setSeats(4);

            interiorService.addSection(section1);
            interiorService.addTable(table1);
            interiorService.addTable(table2);
            interiorService.addTable(table3);
            interiorService.addSection(section2);
            interiorService.addTable(table4);
            interiorService.addTable(table5);
            interiorService.addTable(table6);
            interiorService.addSection(section3);
            interiorService.addTable(table7);
            interiorService.addTable(table8);
            interiorService.addTable(table9);
            interiorService.addSection(section4);
            interiorService.addTable(table10);
            interiorService.addTable(table11);
        }catch (Exception e){
            LOGGER.error("Initialize Manager View Fail" + e);
        }

        drawCanvas();

        tablesCanvas.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                boolean noSectionClicked = true;
                for(Rect rect : rects) {
                    try {
                        Table clickedTable = rect.getTable(t.getX(), t.getY());
                        if(clickedTable != null) {
                            System.out.println((String.valueOf(clickedTable.getNumber()) + " clicked"));
                        }
                        Section clickedSection = rect.getSection(t.getX(), t.getY());
                        if(clickedSection != null) {
                            noSectionClicked = false;
                            clickedSectionId = clickedSection.getIdentity();
                            System.out.println((String.valueOf(clickedSection.getName()) + " clicked" + clickedSection.getIdentity()));
                        }
                    } catch(ServiceException e) {
                        showErrorDialog("Error", "Error", e.getMessage());
                    }
                }
                if(noSectionClicked)
                    clickedSectionId = -1;
                drawCanvas();
            }
        });

        scrollPaneLeft.setPrefSize(120, 120);
        scrollPaneLeft.setContent(tablesCanvas);
        //scrollPaneLeft.setStyle("-fx-font-size: 40px;");

        setupListeners();
    }



    public void buttonEmployeesAddClicked(ActionEvent actionEvent) {
        try {
            LOGGER.info("Add User Button Click");
            Stage stage = new Stage();
            DialogUserController.resetDialog();
            DialogUserController.setThisStage(stage);
            DialogUserController.setUserService(userService);
            DialogUserController.setDialogEnumeration(DialogEnumeration.ADD);
            stage.setTitle("Add Employee");
            AnchorPane myPane = FXMLLoader.load(getClass().getResource("/gui/DialogUser.fxml"));
            Scene scene = new Scene(myPane);
            stage.setScene(scene);
            stage.showAndWait();
            users.setAll(userService.getAllUsers());
        }catch (IOException e){
            LOGGER.error("Add User Button Click did not work");
        }catch (Exception e){
            LOGGER.error("Loading the Users failed" + e);
        }
    }

    public void buttonEmployeesUpdateClicked(ActionEvent actionEvent) {
        try {
            LOGGER.info("Update User Button Click");
            Stage stage = new Stage();
            if(tableViewEmployee.getSelectionModel().getSelectedItem() == null){
                ManagerViewController.showErrorDialog
                        ("Error", "Input Validation Error", "You have to select a User to Update");
                return;
            }
            DialogUserController.resetDialog();
            DialogUserController.setThisStage(stage);
            DialogUserController.setUserService(userService);
            DialogUserController.setDialogEnumeration(DialogEnumeration.UPDATE);
            DialogUserController.setUser(tableViewEmployee.getSelectionModel().getSelectedItem());
            stage.setTitle("Update User");
            AnchorPane myPane = FXMLLoader.load(getClass().getResource("/gui/DialogUser.fxml"));
            Scene scene = new Scene(myPane);
            stage.setScene(scene);
            stage.showAndWait();
            users.setAll(userService.getAllUsers());
            DialogMenuController.resetDialog();
        }catch (IOException e){
            LOGGER.error("Add User Button Click did not work");
        }catch (Exception e){
            LOGGER.error("Loading the User failed" + e);
        }
    }

    public void buttonEmployeesSearchClicked(ActionEvent actionEvent) {
        try {
            LOGGER.info("Search User Button Click");
            Stage stage = new Stage();
            DialogUserController.resetDialog();
            DialogUserController.setThisStage(stage);
            DialogUserController.setUserService(userService);
            DialogUserController.setDialogEnumeration(DialogEnumeration.SEARCH);
            stage.setTitle("Search User");
            AnchorPane myPane = FXMLLoader.load(getClass().getResource("/gui/DialogUser.fxml"));
            Scene scene = new Scene(myPane);
            stage.setScene(scene);
            stage.showAndWait();
            if(DialogUserController.getUser() != null){
                users.setAll(userService.findUsers(DialogUserController.getUser()));
            }else {
                users.setAll(userService.getAllUsers());
            }
            DialogMenuEntryController.resetDialog();
        }catch (IOException e){
            LOGGER.error("Search User Button Click did not work");
        }catch (Exception e){
            LOGGER.error("Loading the User Entries failed" + e);
        }
    }

    public void buttonEmployeesRemoveClicked(ActionEvent actionEvent) {
        try {
            LOGGER.info("Delete User Button Click");
            if(tableViewEmployee.getSelectionModel().getSelectedItem() == null){
                ManagerViewController.showErrorDialog
                        ("Error", "Input Validation Error", "You have to select a User to Delete");
                return;
            }
            userService.deleteUser(tableViewEmployee.getSelectionModel().getSelectedItem());
            users.setAll(userService.getAllUsers());
        }catch (Exception e){
            LOGGER.error("Loading the User Entries failed" + e);
        }
    }

    public void buttonMenuUpdateClicked(ActionEvent actionEvent) {
        try {
            LOGGER.info("Update Menu Button Click");
            Stage stage = new Stage();
            if(tableViewMenu.getSelectionModel().getSelectedItem() == null){
                ManagerViewController.showErrorDialog
                        ("Error", "Input Validation Error", "You have to select a Menu to Update");
                return;
            }
            DialogMenuController.resetDialog();
            DialogMenuController.setThisStage(stage);
            DialogMenuController.setMenuService(menuService);
            DialogMenuController.setDialogEnumeration(DialogEnumeration.UPDATE);
            DialogMenuController.setMenu(tableViewMenu.getSelectionModel().getSelectedItem());
            stage.setTitle("Update Menu");
            AnchorPane myPane = FXMLLoader.load(getClass().getResource("/gui/DialogMenu.fxml"));
            Scene scene = new Scene(myPane);
            stage.setScene(scene);
            stage.showAndWait();
            menus.setAll(menuService.getAllMenus());
            DialogMenuController.resetDialog();
        }catch (IOException e){
            LOGGER.error("Add Menu Button Click did not work");
        }catch (Exception e){
            LOGGER.error("Loading the Menus failed" + e);
        }
    }

    public void buttonMenuSearchClicked(ActionEvent actionEvent) {
        try {
            LOGGER.info("Search Menu Button Click");
            Stage stage = new Stage();
            DialogMenuController.resetDialog();
            DialogMenuController.setThisStage(stage);
            DialogMenuController.setMenuService(menuService);
            DialogMenuController.setDialogEnumeration(DialogEnumeration.SEARCH);
            stage.setTitle("Search Menu");
            AnchorPane myPane = FXMLLoader.load(getClass().getResource("/gui/DialogMenu.fxml"));
            Scene scene = new Scene(myPane);
            stage.setScene(scene);
            stage.showAndWait();
            if(DialogMenuController.getMenu() != null){
                menus.setAll(menuService.findMenu(DialogMenuController.getMenu()));
            }else {
                menus.setAll(menuService.getAllMenus());
            }
            DialogMenuController.resetDialog();
        }catch (IOException e){
            LOGGER.error("Search Menu Button Click did not work" + e);
        }catch (ServiceException e){
            LOGGER.error("Menu Service finding Menus did not work" + e);
        }
    }

    public void buttonMenuRemoveClicked(ActionEvent actionEvent) {
        try {
            LOGGER.info("Delete Menu Button Click");
            if(tableViewMenu.getSelectionModel().getSelectedItem() == null){
                ManagerViewController.showErrorDialog
                        ("Error", "Input Validation Error", "You have to select a Menu to Delete");
                return;
            }
            menuService.removeMenu(tableViewMenu.getSelectionModel().getSelectedItem());
            menus.setAll(menuService.getAllMenus());
        }catch (Exception e){
            LOGGER.error("Loading the Menus failed" + e);
        }
    }

    public void buttonMenuAddClicked(ActionEvent actionEvent){
        try {
            LOGGER.info("Add Menu Button Click");
            Stage stage = new Stage();
            DialogMenuController.resetDialog();
            DialogMenuController.setThisStage(stage);
            DialogMenuController.setMenuService(menuService);
            DialogMenuController.setDialogEnumeration(DialogEnumeration.ADD);
            stage.setTitle("Add Menu");
            AnchorPane myPane = FXMLLoader.load(getClass().getResource("/gui/DialogMenu.fxml"));
            Scene scene = new Scene(myPane);
            stage.setScene(scene);
            stage.showAndWait();
            menus.setAll(menuService.getAllMenus());
        }catch (IOException e){
            LOGGER.error("Add Menu Button Click did not work");
        }catch (Exception e){
            LOGGER.error("Loading the Menus failed" + e);
        }
    }

    public void buttonMenuEntryRemoveClicked(ActionEvent actionEvent) {
        try {
            LOGGER.info("Delete MenuEntry Button Click");
            if(tableViewMenuEntry.getSelectionModel().getSelectedItem() == null){
                ManagerViewController.showErrorDialog
                        ("Error", "Input Validation Error", "You have to select a MenuEntry to Delete");
                return;
            }
            menuService.removeMenuEntry(tableViewMenuEntry.getSelectionModel().getSelectedItem());
            menuEntries.setAll(menuService.getAllMenuEntries());
        }catch (Exception e){
            LOGGER.error("Loading the Menus Entries failed" + e);
        }
    }

    public void buttonMenuEntrySearchClicked(ActionEvent actionEvent) {
        try {
            LOGGER.info("Search MenuEntry Button Click");
            Stage stage = new Stage();
            DialogMenuEntryController.resetDialog();
            DialogMenuEntryController.setThisStage(stage);
            DialogMenuEntryController.setMenuService(menuService);
            DialogMenuEntryController.setTaxRateService(taxRateService);
            DialogMenuEntryController.setDialogEnumeration(DialogEnumeration.SEARCH);
            stage.setTitle("Search MenuEntry");
            AnchorPane myPane = FXMLLoader.load(getClass().getResource("/gui/DialogMenuEntry.fxml"));
            Scene scene = new Scene(myPane);
            stage.setScene(scene);
            stage.showAndWait();
            if(DialogMenuEntryController.getMenuEntry() != null){
                menuEntries.setAll(menuService.findMenuEntry(DialogMenuEntryController.getMenuEntry()));
            }else {
                menuEntries.setAll(menuService.getAllMenuEntries());
            }
            DialogMenuEntryController.resetDialog();
        }catch (IOException e){
            LOGGER.error("Search MenuEntry Button Click did not work");
        }catch (Exception e){
            LOGGER.error("Loading the Menus Entries failed" + e);
        }
    }

    public void buttonMenuEntryUpdateClicked(ActionEvent actionEvent) {
        try {
            LOGGER.info("Update MenuEntry Button Click");
            Stage stage = new Stage();
            if(tableViewMenuEntry.getSelectionModel().getSelectedItem() == null){
                ManagerViewController.showErrorDialog
                        ("Error", "Input Validation Error", "You have to select a MenuEntry to Update");
                return;
            }
            if(tableViewMenuEntry.getSelectionModel().getSelectedItems().size() >1){
                ManagerViewController.showErrorDialog
                        ("Error", "Input Validation Error", "You have to select only one MenuEntry to Update");
                return;
            }
            DialogMenuEntryController.resetDialog();
            DialogMenuEntryController.setThisStage(stage);
            DialogMenuEntryController.setMenuService(menuService);
            DialogMenuEntryController.setTaxRateService(taxRateService);
            DialogMenuEntryController.setDialogEnumeration(DialogEnumeration.UPDATE);
            DialogMenuEntryController.setMenuEntry(tableViewMenuEntry.getSelectionModel().getSelectedItem());
            stage.setTitle("Update MenuEntry");
            AnchorPane myPane = FXMLLoader.load(getClass().getResource("/gui/DialogMenuEntry.fxml"));
            Scene scene = new Scene(myPane);
            stage.setScene(scene);
            stage.showAndWait();
            menuEntries.setAll(menuService.getAllMenuEntries());
            DialogMenuEntryController.resetDialog();
        }catch (Exception e){
            LOGGER.error("Loading the Menus Entries failed" + e);
        }
    }

    public void buttonMenuEntryAddClicked(ActionEvent actionEvent) {
        try {
            LOGGER.info("Add MenuEntry Button Click");
            Stage stage = new Stage();
            DialogMenuEntryController.resetDialog();
            DialogMenuEntryController.setThisStage(stage);
            DialogMenuEntryController.setMenuService(menuService);
            DialogMenuEntryController.setTaxRateService(taxRateService);
            DialogMenuEntryController.setDialogEnumeration(DialogEnumeration.ADD);
            stage.setTitle("Insert MenuEntry");
            AnchorPane myPane = FXMLLoader.load(getClass().getResource("/gui/DialogMenuEntry.fxml"));
            Scene scene = new Scene(myPane);
            stage.setScene(scene);
            stage.showAndWait();
            menuEntries.setAll(menuService.getAllMenuEntries());
            DialogMenuEntryController.resetDialog();
        }catch (Exception e){
            LOGGER.error("Loading the Menus Entries failed" + e);
        }
    }

    public void buttonMenuEntryAvailableClicked(ActionEvent actionEvent) {
        try {
            LOGGER.info("Update MenuEntry Button Click");
            if (tableViewMenuEntry.getSelectionModel().getSelectedItem() == null) {
                ManagerViewController.showErrorDialog
                        ("Error", "Input Validation Error", "You have to select a MenuEntry to Update");
                return;
            }
            for(MenuEntry menuEntry: tableViewMenuEntry.getSelectionModel().getSelectedItems()){
                menuEntry.setAvailable(true);
                menuService.updateMenuEntry(menuEntry);
            }
            menuEntries.setAll(menuService.getAllMenuEntries());
        } catch (Exception e){
            LOGGER.error("Loading the Menus Entries failed" + e);
        }
    }

    public void buttonMenuCategoryRemoveClicked(ActionEvent actionEvent) {
        try {
            LOGGER.info("Delete MenuCategory Button Click");
            if(tableViewMenuCategory.getSelectionModel().getSelectedItem() == null){
                ManagerViewController.showErrorDialog
                        ("Error", "Input Validation Error", "You have to select a Menu Category to Delete");
                return;
            }
            menuService.removeMenuCategory(tableViewMenuCategory.getSelectionModel().getSelectedItem());
            menuCategories.setAll(menuService.getAllMenuCategories());
        }catch (Exception e){
            LOGGER.error("Loading the Menus Categories failed" + e);
        }
    }

    public void buttonMenuCategorySearchClicked(ActionEvent actionEvent) {
        try {
            LOGGER.info("Search MenuCategory Button Click");
            Stage stage = new Stage();
            DialogMenuCategoryController.setThisStage(stage);
            DialogMenuCategoryController.setMenuService(menuService);
            DialogMenuCategoryController.setDialogEnumeration(DialogEnumeration.SEARCH);
            stage.setTitle("Search Menu Category");
            AnchorPane myPane = FXMLLoader.load(getClass().getResource("/gui/DialogMenuCategory.fxml"));
            Scene scene = new Scene(myPane);
            stage.setScene(scene);
            stage.showAndWait();
            if (DialogMenuCategoryController.getMenuCategory() != null) {
                menuCategories.setAll(menuService.findMenuCategory(DialogMenuCategoryController.getMenuCategory()));
            } else {
                menuCategories.setAll(menuService.getAllMenuCategories());
            }
            DialogMenuCategoryController.resetDialog();
        }catch (Exception e){
            LOGGER.error("Search MenuCategory Button Click did not work");
        }
    }

    public void buttonMenuCategoryUpdateClicked(ActionEvent actionEvent) {
        try {
            LOGGER.info("Update MenuCategory Button Click");
            Stage stage = new Stage();
            if(tableViewMenuCategory.getSelectionModel().getSelectedItem() == null){
                ManagerViewController.showErrorDialog
                        ("Error", "Input Validation Error", "You have to select a MenuCategory to Update");
                return;
            }
            DialogMenuCategoryController.setThisStage(stage);
            DialogMenuCategoryController.setMenuService(menuService);
            DialogMenuCategoryController.setDialogEnumeration(DialogEnumeration.UPDATE);
            DialogMenuCategoryController.setMenuCategory(tableViewMenuCategory.getSelectionModel().getSelectedItem());
            stage.setTitle("Update Menu Category");
            AnchorPane myPane = FXMLLoader.load(getClass().getResource("/gui/DialogMenuCategory.fxml"));
            Scene scene = new Scene(myPane);
            stage.setScene(scene);
            stage.showAndWait();
            menuCategories.setAll(menuService.getAllMenuCategories());
            DialogMenuCategoryController.resetDialog();
        }catch (Exception e){
            LOGGER.error("Update MenuCategory Button Click did not work" + e);
        }
    }

    public void buttonMenuCategoryAddClicked(ActionEvent actionEvent) {
        try {
            LOGGER.info("Add MenuCategory Button Click");
            Stage stage = new Stage();
            DialogMenuCategoryController.setThisStage(stage);
            DialogMenuCategoryController.setMenuService(menuService);
            DialogMenuCategoryController.setDialogEnumeration(DialogEnumeration.ADD);
            stage.setTitle("Insert Menu Category");
            AnchorPane myPane = FXMLLoader.load(getClass().getResource("/gui/DialogMenuCategory.fxml"));
            Scene scene = new Scene(myPane);
            stage.setScene(scene);
            stage.showAndWait();
            menuCategories.setAll(menuService.getAllMenuCategories());
            DialogMenuCategoryController.resetDialog();
        }catch (Exception e){
            LOGGER.error("Add MenuCategory Button Click did not work" + e);
        }
    }

    public void buttonTaxRateRemoveClicked(ActionEvent actionEvent) {
        try {
            LOGGER.info("Delete TaxRate Button Click");
            if(tableViewTaxRate.getSelectionModel().getSelectedItem() == null){
                ManagerViewController.showErrorDialog
                        ("Error", "Input Validation Error", "You have to select a Tax Rate to Delete");
                return;
            }
            taxRateService.removeTaxRate(tableViewTaxRate.getSelectionModel().getSelectedItem());
            taxRates.setAll(taxRateService.getAllTaxRates());
        }catch (Exception e){
            LOGGER.error("Loading the taxRates failed" + e);
        }
    }


    public void buttonTaxRateUpdateClicked(ActionEvent actionEvent) {
        try {
            LOGGER.info("Delete TaxRate Button Click");
            if(tableViewTaxRate.getSelectionModel().getSelectedItem() == null){
                ManagerViewController.showErrorDialog
                        ("Error", "Input Validation Error", "You have to select a Tax Rate to Delete");
                return;
            }
            BigDecimal value = BigDecimal.valueOf(0.0);
            try {
                value = BigDecimal.valueOf(Double.parseDouble(txtTaxRateValue.getText()));
            } catch (NumberFormatException e) {
                ManagerViewController.showErrorDialog("Error", "Input Validation Error", "Value must be a number");
                LOGGER.info("Tax Rate Value must be a number" + e);
            }
            TaxRate taxRate = tableViewTaxRate.getSelectionModel().getSelectedItem();
            taxRate.setValue(value);
            taxRateService.updateTaxRate(taxRate);
            taxRates.setAll(taxRateService.getAllTaxRates());
        }catch (Exception e){
            LOGGER.error("Update the taxRates failed" + e);
        }
    }

    public void buttonTaxRateAddClicked(ActionEvent actionEvent){
        try {
            LOGGER.info("Add TaxRate Button Click");
            try {
                BigDecimal value = BigDecimal.valueOf(Double.parseDouble(txtTaxRateValue.getText()));
                if(value.compareTo(BigDecimal.valueOf(1)) == 1 || value.compareTo(BigDecimal.valueOf(0))== 2 ){
                    ManagerViewController.showErrorDialog("Error", "Input Validation Error", "Value must be between 0 and 1");
                    LOGGER.debug("Tax Rate Value must be between 0 and 1");
                }
                TaxRate taxRate = new TaxRate();
                taxRate.setValue(value);
                taxRateService.addTaxRate(taxRate);
                taxRates.setAll(taxRateService.getAllTaxRates());
            } catch (NumberFormatException e) {
                ManagerViewController.showErrorDialog("Error", "Input Validation Error", "Value must be a number");
                LOGGER.info("Tax Rate Value must be a number" + e);
            }
        }catch (Exception e){
            LOGGER.error("Update the taxRates failed" + e);
        }
    }

    public static void showErrorDialog(String title, String head, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(head);
        alert.setContentText(content);

        alert.showAndWait();
    }


    public void buttonShowAllMenuCategoryClicked(ActionEvent actionEvent) {
        try {
            menuCategories.setAll(menuService.getAllMenuCategories());
        } catch (Exception e){
            LOGGER.error("Loading All Menu Categories failed" + e);
        }
    }

    public void buttonShowAllMenuEntryClicked(ActionEvent actionEvent) {
        try {
            menuEntries.setAll(menuService.getAllMenuEntries());
        } catch (Exception e){
            LOGGER.error("Loading All Menu Entries failed" + e);
        }
    }

    public void buttonShowAllMenuClicked(ActionEvent actionEvent) {
        try {
            menus.setAll(menuService.getAllMenus());
        } catch (Exception e){
            LOGGER.error("Loading All Menu failed" + e);
        }
    }

    public void buttonEmployeesShowAll(ActionEvent actionEvent) {
        try {
            users.setAll(userService.getAllUsers());
        } catch (Exception e){
            LOGGER.error("Loading All Users failed" + e);
        }
    }

    public void buttonAddSectionClicked(ActionEvent actionEvent) {
        LOGGER.info("Add Section Button Click");
        drawCanvas();
        try {
            Stage stage = new Stage();
            AddSectionController.setInteriorService(interiorService);
            AddSectionController.setThisStage(stage);
            //AddSectionController.setUpdateCanvas(new UpdateCanvas());
            stage.setTitle("Add Section");
            AnchorPane myPane = FXMLLoader.load(getClass().getResource("/gui/AddSection.fxml"));
            Scene scene = new Scene(myPane);
            stage.setScene(scene);
            stage.showAndWait();
        } catch (IOException e) {
            LOGGER.error("Unable to Load Add Section" + e);
        }
    }

    public void buttonEditSectionClicked(ActionEvent actionEvent) {
        LOGGER.info("Edit Section Button Click");
        if(clickedSectionId != -1) {
            drawCanvas();
            try {
                Stage stage = new Stage();
                EditSectionController.setInteriorService(interiorService);
                EditSectionController.setThisStage(stage);
                //EditSectionController.setUpdateCanvas(new UpdateCanvas());
                EditSectionController.initTables(clickedSectionId);
                stage.setTitle("Edit Section");
                AnchorPane myPane = FXMLLoader.load(getClass().getResource("/gui/EditSection.fxml"));
                Scene scene = new Scene(myPane);
                stage.setScene(scene);
                stage.showAndWait();
            } catch (IOException e) {
                LOGGER.error("Unable to Load Edit Section" + e);
            }
        }
    }

    public void buttonRemoveSectionClicked(ActionEvent event) {
        LOGGER.info("Remove Section Button Click");
        if(clickedSectionId != -1) {
            try {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Remove Section?");
                alert.setHeaderText("Remove Section?");
                alert.setContentText("Do you really want to remove the selected Section and all the tables inside?");

                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK) {
                    Section sectionMatcher = new Section();
                    sectionMatcher.setIdentity(clickedSectionId);
                    Section section = interiorService.findSections(sectionMatcher).get(0);
                    Table tableMatcher = new Table();
                    tableMatcher.setSection(section);
                    ArrayList<Table> deleteTables = new ArrayList<>();
                    for(Table table : interiorService.findTables(tableMatcher)) {
                        deleteTables.add(table);    // Avoid deleting while iterating
                    }
                    for(Table table : deleteTables) {
                        interiorService.deleteTable(table);
                    }
                    interiorService.deleteSection(section);
                    clickedSectionId = -1;
                }
            } catch (ServiceException e) {
                LOGGER.error("Unable to remove section");
            } catch (ValidationException e) {
                LOGGER.error("Validation error");
            }
        }
        drawCanvas();
    }

    public void setupListeners() {
        scrollPaneLeft.widthProperty().addListener(new ChangeListener<Number>() {
            @Override public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneWidth, Number newSceneWidth) {
                tablesCanvas.setWidth(tablesCanvas.getWidth() - (oldSceneWidth.intValue() - newSceneWidth.intValue()));
                scaleX = newSceneWidth.doubleValue()/550.0;
                scaleX = Math.min(scaleX, 2.0);
                scaleX = Math.max(scaleX, 0.5);
                drawCanvas();
            }
        });
        scrollPaneLeft.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneHeight, Number newSceneHeight) {
                tablesCanvas.setHeight(tablesCanvas.getHeight() - (oldSceneHeight.intValue() - newSceneHeight.intValue()));
                scaleY = newSceneHeight.doubleValue()/598.0;
                scaleY = Math.min(scaleY, 2.0);
                scaleY = Math.max(scaleY, 0.5);
                drawCanvas();
            }
        });
    }

    private int calculateWidth(Section section) throws ServiceException {
        int max = -1;
        Table matcher = new Table();
        matcher.setSection(section);
        for(Table table : interiorService.findTables(matcher)) {
            if(table.getColumn() > max) {
                max = table.getColumn();
            }
        }
        return max*FACT + TABLE_SIZE + 2*SECTION_PADDING;
    }

    private int calculateHeight(Section section) throws ServiceException {
        int max = -1;
        Table matcher = new Table();
        matcher.setSection(section);
        for(Table table : interiorService.findTables(matcher)) {
            if(table.getRow() > max) {
                max = table.getRow();
            }
        }
        return max*FACT + TABLE_SIZE + 2*SECTION_PADDING;
    }

    @Override
    public void disable(boolean disabled) {

    }
    private void drawCanvas() {
        GraphicsContext gc = tablesCanvas.getGraphicsContext2D();
        double scaleText = scaleY;
        boolean firstSection = true;
        Section prevSection = null;
        int rowHeight = 0;
        int x = CANVAS_PADDING;
        int y = CANVAS_PADDING;
        tablesCanvas.setWidth(scrollPaneLeft.getWidth()-20);

        gc.clearRect(0, 0, tablesCanvas.getWidth(), tablesCanvas.getHeight());

        try {
            for (Section section : interiorService.getAllSections()) {
                if(firstSection) {
                    firstSection = false;
                } else {
                    if(x*scaleX+calculateWidth(prevSection)*scaleX+SECTION_PADDING*scaleX+CANVAS_PADDING*scaleX+calculateWidth(section)*scaleX < tablesCanvas.getWidth()) {
                        x += calculateWidth(prevSection)+SECTION_PADDING*scaleX;
                    } else {
                        x = CANVAS_PADDING;
                        y += rowHeight + SECTION_OFFSET * scaleY;
                        rowHeight = 0;
                    }
                }

                if(tablesCanvas.getHeight() < y*scaleY + calculateHeight(section)*scaleY+CANVAS_PADDING*scaleY)
                    tablesCanvas.setHeight(y*scaleY + calculateHeight(section)*scaleY+CANVAS_PADDING*scaleY);
                if(section.getIdentity().equals(Long.valueOf(clickedSectionId)))
                    gc.setStroke(Color.RED);
                gc.strokeRoundRect(x*scaleX, y*scaleY, calculateWidth(section)*scaleX, calculateHeight(section)*scaleY, 10, 10);
                gc.setStroke(Color.BLACK);
                Rect rectSection = new Rect(x*scaleX, y*scaleY, calculateWidth(section)*scaleX, calculateHeight(section)*scaleY, interiorService);
                rectSection.setIdentity(section.getIdentity());
                rects.add(rectSection);
                gc.setFont(new Font(gc.getFont().getName(), 20 * scaleText));
                gc.fillText(section.getName() + ":", x*scaleX, (y-TEXT_BORDER_BOTTOM)*scaleY);
                Table matcher = new Table();
                matcher.setSection(section);
                for (Table table : interiorService.findTables(matcher)) {
                    Rect rectTable = new Rect(((x+SECTION_PADDING)+(table.getColumn()*FACT))*scaleX, ((y+SECTION_PADDING)+(table.getRow()*FACT))*scaleY, TABLE_SIZE*scaleX, TABLE_SIZE*scaleY, interiorService);
                    rectTable.setNumber(table.getNumber());
                    rectTable.setSection(section);
                    gc.strokeRoundRect(rectTable.getX(), rectTable.getY(), rectTable.getW(), rectTable.getH(), 2, 2);
                    rects.add(rectTable);
                    gc.fillText(String.valueOf(table.getNumber()), ((x+SECTION_PADDING)+(table.getColumn()*FACT)+TABLE_SIZE/4)*scaleX, ((y+SECTION_PADDING)+(table.getRow()*FACT)+TABLE_SIZE/1.5)*scaleY);
                }
                rowHeight = Math.max(rowHeight, calculateHeight(section));
                prevSection = section;
            }
        } catch(ServiceException e) {
            showErrorDialog("Error", "Error", e.getMessage());
        }
    }

}
