package com.at.ac.tuwien.sepm.ss15.edulium.gui;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.IntermittentSale;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.MenuEntry;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.OnetimeSale;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.Sale;
import com.at.ac.tuwien.sepm.ss15.edulium.gui.util.NumericTextField;
import com.at.ac.tuwien.sepm.ss15.edulium.service.MenuService;
import com.at.ac.tuwien.sepm.ss15.edulium.service.SaleService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.net.URL;
import java.time.*;
import java.util.*;

import static javafx.collections.FXCollections.observableArrayList;

/**
 * Controller for the TaxRate Dialog
 */
public class DialogSaleController implements Initializable{
    private static final Logger LOGGER = LogManager.getLogger(DialogSaleController.class);

    private static Stage thisStage;

    @Autowired
    private SaleService saleService;
    @Autowired
    private MenuService menuService;

    private static Sale sale;
    private static DialogEnumeration dialogEnumeration;

    public void showSale(){
        inMenuMenuEntries.setAll(sale.getEntries());
        textFieldName.setText(sale.getName());
        if (sale instanceof OnetimeSale) {
            radioButtonOnetimeSale.setSelected(true);
            selectOnetimeSaleRadioButton(true);
            datePickerFromTime.setValue(((OnetimeSale) sale).getFromTime().toLocalDate());
            datePickerToTime.setValue(((OnetimeSale) sale).getToTime().toLocalDate());
            textFieldFromTimeHr.setText(((OnetimeSale) sale).getFromTime().toLocalTime().getHour()+"");
            textFieldFromTimeMin.setText(((OnetimeSale) sale).getFromTime().toLocalTime().getMinute()+"");
            textFieldToTimeHr.setText(((OnetimeSale) sale).getToTime().toLocalTime().getHour()+"");
            textFieldToTimeMin.setText(((OnetimeSale) sale).getToTime().toLocalTime().getMinute()+"");
        } else if (sale instanceof IntermittentSale) {
            radioButtonIntermittentSale.setSelected(true);
            selectOnetimeSaleRadioButton(false);
            checkBoxEnabled.setSelected(((IntermittentSale) sale).getEnabled());
            checkBoxMonday.setSelected(((IntermittentSale) sale).getDaysOfSale().contains(DayOfWeek.MONDAY));
            checkBoxTuesday.setSelected(((IntermittentSale) sale).getDaysOfSale().contains(DayOfWeek.TUESDAY));
            checkBoxWednesday.setSelected(((IntermittentSale) sale).getDaysOfSale().contains(DayOfWeek.WEDNESDAY));
            checkBoxThursday.setSelected(((IntermittentSale) sale).getDaysOfSale().contains(DayOfWeek.THURSDAY));
            checkBoxFriday.setSelected(((IntermittentSale) sale).getDaysOfSale().contains(DayOfWeek.FRIDAY));
            checkBoxSaturday.setSelected(((IntermittentSale) sale).getDaysOfSale().contains(DayOfWeek.SATURDAY));
            checkBoxSunday.setSelected(((IntermittentSale) sale).getDaysOfSale().contains(DayOfWeek.SUNDAY));
            textFieldBeginningTimeHr.setText(((IntermittentSale) sale).getFromDayTime().getHour()+"");
            textFieldBeginningTimeMin.setText(((IntermittentSale) sale).getFromDayTime().getMinute()+"");
            long minutes = ((IntermittentSale) sale).getDuration().toMinutes();
            textFieldDuration.setText(minutes+"");
        }
    }

    public static void setThisStage(Stage thisStage) {
        DialogSaleController.thisStage = thisStage;
    }

    public static void setSale(Sale sale) {
        DialogSaleController.sale = sale; }

    public static void setDialogEnumeration(DialogEnumeration dialogEnumeration) {
        DialogSaleController.dialogEnumeration = dialogEnumeration;
    }
    
    public static Sale getSale() {
        return sale;
    }

    @FXML
    private TextField textFieldName;
    @FXML
    private TextField textFieldPrice;

    @FXML
    private TableView<MenuEntry> tableViewData;
    @FXML
    private TableColumn<MenuEntry, String> tableColNameData;
    @FXML
    private TableColumn<MenuEntry, String> tableColCategoryData;
    @FXML
    private TableColumn<MenuEntry, BigDecimal> tableColPriceData;


    @FXML
    private TableView<MenuEntry> tableViewInMenu;
    @FXML
    private TableColumn<MenuEntry, String> tableColNameInMenu;
    @FXML
    private TableColumn<MenuEntry, String> tableColCategoryInMen;
    @FXML
    private TableColumn<MenuEntry, BigDecimal> tableColPriceInMen;

    @FXML
    private RadioButton radioButtonOnetimeSale;
    @FXML
    private RadioButton radioButtonIntermittentSale;
    @FXML
    private DatePicker datePickerFromTime;
    @FXML
    private DatePicker datePickerToTime;
    @FXML
    private NumericTextField textFieldFromTimeHr;
    @FXML
    private NumericTextField textFieldFromTimeMin;
    @FXML
    private NumericTextField textFieldToTimeHr;
    @FXML
    private NumericTextField textFieldToTimeMin;
    @FXML
    private CheckBox checkBoxEnabled;
    @FXML
    private CheckBox checkBoxMonday;
    @FXML
    private CheckBox checkBoxTuesday;
    @FXML
    private CheckBox checkBoxWednesday;
    @FXML
    private CheckBox checkBoxThursday;
    @FXML
    private CheckBox checkBoxFriday;
    @FXML
    private CheckBox checkBoxSaturday;
    @FXML
    private CheckBox checkBoxSunday;
    @FXML
    private NumericTextField textFieldBeginningTimeHr;
    @FXML
    private NumericTextField textFieldBeginningTimeMin;
    @FXML
    private NumericTextField textFieldDuration;

    private final ToggleGroup group = new ToggleGroup();

    private ObservableList<MenuEntry> allMenuEntries;
    private ObservableList<MenuEntry> inMenuMenuEntries;



    /**
     * Function is used to init the Sale Dialog
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        LOGGER.info("Initialize Dialog Sale");

        radioButtonOnetimeSale.setToggleGroup(group);
        radioButtonIntermittentSale.setToggleGroup(group);
        radioButtonOnetimeSale.setSelected(true);
        selectOnetimeSaleRadioButton(true);

        tableColNameData.setCellValueFactory(new PropertyValueFactory<>("name"));
        tableColCategoryData.setCellValueFactory(p -> {
            // p.getValue() returns the Person instance for a particular TableView row
            return new SimpleStringProperty(p.getValue().getCategory().getName());
        });
        tableColPriceData.setCellValueFactory(new PropertyValueFactory<>("price"));

        if(sale == null){
            Sale saleForInit = new OnetimeSale();
            saleForInit.setEntries(new LinkedList<>());
            DialogSaleController.setSale(saleForInit);
        }
        if(sale.getEntries() == null) {
            sale.setEntries(new LinkedList<>());
        }
        try {
            allMenuEntries = observableArrayList(menuService.getAllMenuEntries());
            inMenuMenuEntries = observableArrayList(sale.getEntries());
        }catch (Exception e){
            showErrorDialog
                    ("Refreshing View", "An Error occured during initializing the View /n" + e.toString());
        }
        if(sale.getName() != null) textFieldName.setText(sale.getName());
        tableViewData.setItems(allMenuEntries);
        tableViewInMenu.setItems(inMenuMenuEntries);
        tableColNameInMenu.setCellValueFactory(new PropertyValueFactory<>("name"));
        tableColCategoryInMen.setCellValueFactory(p -> {
            // p.getValue() returns the Person instance for a particular TableView row
            return new SimpleStringProperty(p.getValue().getCategory().getName());
        });
        tableColPriceInMen.setCellValueFactory(new PropertyValueFactory<>("price"));

    }

    public boolean validateData() {
        LOGGER.info("Dialog Sale OK Button clicked");
        if ((textFieldName.getText() == null || textFieldName.getText().equals(""))) {
            showErrorDialog("Input Validation Error", "Name must have a value");
            return false;
        }
        sale.setName(textFieldName.getText());
        if (sale.getEntries().size() == 0) {
            showErrorDialog
                    ("Input Validation Error", "There hast to be at least one Menu Entry");
            return false;
        }
        try {
            switch (DialogSaleController.dialogEnumeration) {
                case ADD:
                    if (radioButtonOnetimeSale.isSelected()) {
                        OnetimeSale onetimeSale = new OnetimeSale();
                        onetimeSale.setIdentity((long) 1);
                        onetimeSale.setName(sale.getName());
                        onetimeSale.setEntries(sale.getEntries());
                        LocalDate fromDate = datePickerFromTime.getValue();
                        if (fromDate == null) {
                            showErrorDialog
                                    ("Input Validation Error", "Please specify a date");
                            return false;
                        }
                        Integer hr;
                        Integer min;
                        try {
                            hr = new Integer(textFieldFromTimeHr.getText());
                            min = new Integer(textFieldFromTimeMin.getText());
                        } catch (NumberFormatException e) {
                            showErrorDialog
                                    ("Input Validation Error", "Please specify valid integer numbers");
                            return false;
                        }
                        if (hr<0 || hr>=24 || min < 0 || min >= 60) {
                            showErrorDialog
                                    ("Input Validation Error", "Please specify valid numbers for hour/minutes");
                            return false;
                        }
                        LocalTime fromTimeT = LocalTime.of(hr, min);
                        LocalDateTime fromTime = LocalDateTime.of(fromDate, fromTimeT);
                        onetimeSale.setFromTime(fromTime);
                        LocalDate toDate = datePickerFromTime.getValue();
                        if (toDate == null) {
                            showErrorDialog
                                    ("Input Validation Error", "Please specify a date");
                            return false;
                        }
                        try {
                            hr = new Integer(textFieldToTimeHr.getText());
                            min = new Integer(textFieldToTimeMin.getText());
                        } catch (NumberFormatException e) {
                            showErrorDialog
                                    ("Input Validation Error", "Please specify valid integer numbers");
                            return false;
                        }
                        if (hr<0 || hr>=24 || min < 0 || min >= 60) {
                            showErrorDialog
                                    ("Input Validation Error", "Please specify valid numbers for hour/minutes");
                            return false;
                        }
                        LocalTime toTimeT = LocalTime.of(hr, min);
                        LocalDateTime toTime = LocalDateTime.of(toDate, toTimeT);
                        onetimeSale.setToTime(toTime);
                        saleService.addOnetimeSale(onetimeSale);
                        sale = onetimeSale;
                    } else {
                        IntermittentSale intermittentSale = new IntermittentSale();
                        intermittentSale.setIdentity((long) 1);
                        intermittentSale.setName(sale.getName());
                        intermittentSale.setEntries(sale.getEntries());
                        intermittentSale.setEnabled(checkBoxEnabled.isSelected());
                        Set<DayOfWeek> weekDays = new HashSet<>();
                        if (checkBoxMonday.isSelected()) {
                            weekDays.add(DayOfWeek.MONDAY);
                        }
                        if (checkBoxTuesday.isSelected()) {
                            weekDays.add(DayOfWeek.TUESDAY);
                        }
                        if (checkBoxWednesday.isSelected()) {
                            weekDays.add(DayOfWeek.WEDNESDAY);
                        }
                        if (checkBoxThursday.isSelected()) {
                            weekDays.add(DayOfWeek.THURSDAY);
                        }
                        if (checkBoxFriday.isSelected()) {
                            weekDays.add(DayOfWeek.FRIDAY);
                        }
                        if (checkBoxSaturday.isSelected()) {
                            weekDays.add(DayOfWeek.SATURDAY);
                        }
                        if (checkBoxSunday.isSelected()) {
                            weekDays.add(DayOfWeek.SUNDAY);
                        }

                        if(weekDays.isEmpty()) {
                            showErrorDialog
                                    ("Input Validation Error", "There should be at least one day of sale");
                            return false;
                        }

                        intermittentSale.setDaysOfSale(weekDays);
                        Integer hr;
                        Integer min;
                        try {
                            hr = new Integer(textFieldBeginningTimeHr.getText());
                            min = new Integer(textFieldBeginningTimeMin.getText());
                        } catch (NumberFormatException e) {
                            showErrorDialog
                                    ("Input Validation Error", "Please specify valid integer numbers");
                            return false;
                        }
                        if (hr<0 || hr>=24 || min < 0 || min >= 60) {
                            showErrorDialog
                                    ("Input Validation Error", "Please specify valid numbers for hour/minutes");
                            return false;
                        }
                        LocalTime fromDayTime = LocalTime.of(hr,min);
                        intermittentSale.setFromDayTime(fromDayTime);
                        Duration duration = Duration.ofMinutes(new Long(textFieldDuration.getText()));
                        intermittentSale.setDuration(duration);
                        saleService.addIntermittentSale(intermittentSale);
                        sale = intermittentSale;
                    }
                    break;
                case UPDATE:
                    if (radioButtonOnetimeSale.isSelected()) {
                        if (! (sale instanceof OnetimeSale)) {
                            showErrorDialog
                                    ("Sale Service Error", "An intermittent sale cannot be converted to a onetime sale.");
                            return false;
                        }
                        LocalDate fromDate = datePickerFromTime.getValue();
                        if (fromDate == null) {
                            showErrorDialog
                                    ("Input Validation Error", "Please specify a date");
                            return false;
                        }
                        Integer hr;
                        Integer min;
                        try {
                            hr = new Integer(textFieldFromTimeHr.getText());
                            min = new Integer(textFieldFromTimeMin.getText());
                        } catch (NumberFormatException e) {
                            showErrorDialog
                                    ("Input Validation Error", "Please specify valid integer numbers");
                            return false;
                        }
                        if (hr<0 || hr>=24 || min < 0 || min >= 60) {
                            showErrorDialog
                                    ("Input Validation Error", "Please specify valid numbers for hour/minutes");
                            return false;
                        }
                        LocalTime fromTimeT = LocalTime.of(hr, min);
                        LocalDateTime fromTime = LocalDateTime.of(fromDate, fromTimeT);
                        ((OnetimeSale)sale).setFromTime(fromTime);
                        LocalDate toDate = datePickerFromTime.getValue();
                        if (toDate == null) {
                            showErrorDialog
                                    ("Input Validation Error", "Please specify a date");
                            return false;
                        }
                        try {
                            hr = new Integer(textFieldToTimeHr.getText());
                            min = new Integer(textFieldToTimeMin.getText());
                        } catch (NumberFormatException e) {
                            showErrorDialog
                                    ("Input Validation Error", "Please specify valid integer numbers");
                            return false;
                        }
                        if (hr<0 || hr>=24 || min < 0 || min >= 60) {
                            showErrorDialog
                                    ("Input Validation Error", "Please specify valid numbers for hour/minutes");
                            return false;
                        }
                        LocalTime toTimeT = LocalTime.of(hr, min);
                        LocalDateTime toTime = LocalDateTime.of(toDate, toTimeT);
                        ((OnetimeSale)sale).setToTime(toTime);
                        saleService.updateOnetimeSale((OnetimeSale) sale);
                    } else {
                        if (! (sale instanceof IntermittentSale)) {
                            showErrorDialog
                                    ("Sale Service Error", "A onetime sale cannot be converted to an intermittent sale.");
                            return false;
                        }
                        ((IntermittentSale)sale).setEnabled(checkBoxEnabled.isSelected());
                        Set<DayOfWeek> weekDays = new HashSet<>();
                        if (checkBoxMonday.isSelected()) {
                            weekDays.add(DayOfWeek.MONDAY);
                        }
                        if (checkBoxTuesday.isSelected()) {
                            weekDays.add(DayOfWeek.TUESDAY);
                        }
                        if (checkBoxWednesday.isSelected()) {
                            weekDays.add(DayOfWeek.WEDNESDAY);
                        }
                        if (checkBoxThursday.isSelected()) {
                            weekDays.add(DayOfWeek.THURSDAY);
                        }
                        if (checkBoxFriday.isSelected()) {
                            weekDays.add(DayOfWeek.FRIDAY);
                        }
                        if (checkBoxSaturday.isSelected()) {
                            weekDays.add(DayOfWeek.SATURDAY);
                        }
                        if (checkBoxSunday.isSelected()) {
                            weekDays.add(DayOfWeek.SUNDAY);
                        }

                        if(weekDays.isEmpty()) {
                            showErrorDialog
                                    ("Input Validation Error", "There should be at least one day of sale");
                            return false;
                        }

                        ((IntermittentSale)sale).setDaysOfSale(weekDays);
                        Integer hr;
                        Integer min;
                        try {
                            hr = new Integer(textFieldBeginningTimeHr.getText());
                            min = new Integer(textFieldBeginningTimeMin.getText());
                        } catch (NumberFormatException e) {
                            showErrorDialog
                                    ("Input Validation Error", "Please specify valid integer numbers");
                            return false;
                        }
                        if (hr<0 || hr>=24 || min < 0 || min >= 60) {
                            showErrorDialog
                                    ("Input Validation Error", "Please specify valid numbers for hour/minutes");
                            return false;
                        }
                        LocalTime fromDayTime = LocalTime.of(hr,min);
                        ((IntermittentSale)sale).setFromDayTime(fromDayTime);
                        Duration duration = Duration.ofMinutes(new Long(textFieldDuration.getText()));
                        ((IntermittentSale)sale).setDuration(duration);
                        saleService.updateIntermittentSale((IntermittentSale)sale);
                    }
                    break;
            }
        }catch (Exception e){
            e.printStackTrace();
            showErrorDialog
                    ("Sale Service Error", "The Service was unable to handle the required Sale action/n" + e.toString());
            LOGGER.error("The Service was unable to handle the required Sale action " + e);
            return false;
        }
        return true;
    }

    public void buttonAddClick() {
        if((textFieldPrice.getText() == null || textFieldPrice.getText().equals("")) &&
                dialogEnumeration != DialogEnumeration.SEARCH){
            switch (dialogEnumeration) {
                case UPDATE:
                case ADD: //There has to be a Price, if the User wants to ADD or UPDATE
                    showErrorDialog("Input Validation Error", "Price must have a value");
                    return;
            }
        }
        if(tableViewData.getSelectionModel().getSelectedItem() == null){
            showErrorDialog
                    ("Input Validation Error", "You have to select a Menu Entry from the left side");
            return;
        }

        try {
            MenuEntry menuEntry = tableViewData.getSelectionModel().getSelectedItem();
            BigDecimal price;
            switch (dialogEnumeration) {
                case UPDATE:
                case ADD:
                    price = BigDecimal.valueOf(Double.parseDouble(textFieldPrice.getText()));
                    if (price.compareTo(new BigDecimal(0)) < 0) {
                        showErrorDialog("Input Validation Error", "Price must not be negative");
                        return;
                    }
                    menuEntry.setPrice(price);
                    break;
            }
            List<MenuEntry> list = sale.getEntries();
            boolean alreadyThere = false;
            for (MenuEntry e : list) {
                if (e.getIdentity() == menuEntry.getIdentity()) {
                    alreadyThere = true;
                }
            }
            if (alreadyThere) {
                showErrorDialog("Input Validation Error", "The same Menu entry should not be in the list twice.");
            } else {
                list.add(menuEntry);
            }
            sale.setEntries(list);
            inMenuMenuEntries.setAll(sale.getEntries());
        } catch (NumberFormatException e) {
            showErrorDialog("Input Validation Error", "Price must be a\npositive number");
            LOGGER.info("Dialog Sale Add Button Clicked Price must be number " + e);
        } catch (Exception e) {
            showErrorDialog
                    ("Data Validation", "An Error occured during adding MenuEntry/n" + e.toString());
            LOGGER.info("Dialog Sale Add Button Menu Entry handling Error" + e);
        }
    }

    public void buttonRemoveClick() {
        if(tableViewInMenu.getSelectionModel().getSelectedItem() == null){
            showErrorDialog
                    ("Input Validation Error", "You have to select a Menu Entry from the right side");
            return;
        }
        MenuEntry menuEntry = tableViewInMenu.getSelectionModel().getSelectedItem();
        List<MenuEntry> list = sale.getEntries();
        list.remove(menuEntry);
        sale.setEntries(list);
        inMenuMenuEntries.setAll(sale.getEntries());
    }

    /**
     * this function is used to rest the static members of the class
     */
    public void resetDialog(){
        this.textFieldName.setText("");
        this.textFieldPrice.setText("");
        Sale saleForInit = new OnetimeSale();
        saleForInit.setEntries(new LinkedList<>());
        inMenuMenuEntries.clear();
        try {
            allMenuEntries.setAll(menuService.getAllMenuEntries());
        }catch (Exception e){
            showErrorDialog
                    ("Refreshing View", "An Error occured during resetting the View /n" + e.toString());
        }
        setSale(saleForInit);
        if(sale.getEntries() == null) {
            sale.setEntries(new LinkedList<>());
        }
    }

    public void changeRadio() {
        if (DialogSaleController.dialogEnumeration.equals(DialogEnumeration.UPDATE)) {
            showErrorDialog
                    ("Sale Information", "The type of a sale cannot be changed after its creation.");
            if (sale instanceof OnetimeSale) {
                radioButtonOnetimeSale.setSelected(true);
            } else {
                radioButtonIntermittentSale.setSelected(true);
            }
        }
        selectOnetimeSaleRadioButton(radioButtonOnetimeSale.isSelected());
    }

    private void selectOnetimeSaleRadioButton(boolean b) {
        datePickerFromTime.setDisable(!b);
        datePickerToTime.setDisable(!b);
        textFieldFromTimeHr.setDisable(!b);
        textFieldToTimeHr.setDisable(!b);
        textFieldFromTimeMin.setDisable(!b);
        textFieldToTimeMin.setDisable(!b);
        checkBoxEnabled.setDisable(b);
        checkBoxMonday.setDisable(b);
        checkBoxTuesday.setDisable(b);
        checkBoxWednesday.setDisable(b);
        checkBoxThursday.setDisable(b);
        checkBoxFriday.setDisable(b);
        checkBoxSaturday.setDisable(b);
        checkBoxSunday.setDisable(b);
        textFieldBeginningTimeHr.setDisable(b);
        textFieldBeginningTimeMin.setDisable(b);
        textFieldDuration.setDisable(b);
    }

    private void showErrorDialog(String head, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(head);
        alert.setContentText(content);

        alert.showAndWait();
    }
}
