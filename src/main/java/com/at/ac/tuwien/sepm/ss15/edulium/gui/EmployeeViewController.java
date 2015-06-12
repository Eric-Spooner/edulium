package com.at.ac.tuwien.sepm.ss15.edulium.gui;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.User;
import com.at.ac.tuwien.sepm.ss15.edulium.service.*;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static javafx.collections.FXCollections.observableArrayList;

/**
 * Created by - on 12.06.2015.
 */
public class EmployeeViewController implements Initializable, Controller {
    private static final Logger LOGGER = LogManager.getLogger(ManagerViewController.class);

    @FXML
    private TableView<User> tableViewEmployee;
    @FXML
    private TableColumn<User,String> employeeId;
    @FXML
    private TableColumn<User,String> employeeName;
    @FXML
    private TableColumn<User,String> employeeRole;

    @Autowired
    private UserService userService;
    @Autowired
    private TaskScheduler taskScheduler;

    private ObservableList<User> users;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // queued
        try {
            users = observableArrayList(userService.getAllUsers());
            tableViewEmployee.setItems(users);
            employeeId.setCellValueFactory(new PropertyValueFactory<User, String>("identity"));
            employeeName.setCellValueFactory(new PropertyValueFactory<User, String>("name"));
            employeeRole.setCellValueFactory(new PropertyValueFactory<User, String>("role"));
        }catch (ServiceException e){
            LOGGER.error("Initialize User View Failed due to" + e);
        }

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

    public void buttonEmployeesShowAll(ActionEvent actionEvent) {
        try {
            users.setAll(userService.getAllUsers());
        } catch (Exception e){
            LOGGER.error("Loading All Users failed" + e);
        }
    }

    @Override
    public void disable(boolean disabled) {

    }
}
