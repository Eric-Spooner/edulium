package com.at.ac.tuwien.sepm.ss15.edulium.gui;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.User;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ValidationException;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.Validator;
import com.at.ac.tuwien.sepm.ss15.edulium.service.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ResourceBundle;

@Controller
public class EmployeeViewController implements Initializable {
    private static final Logger LOGGER = LogManager.getLogger(EmployeeViewController.class);

    @FXML
    private TableView<User> tableViewEmployee;
    @FXML
    private TableColumn<User, String> employeeId;
    @FXML
    private TableColumn<User, String> employeeName;
    @FXML
    private TableColumn<User, String> employeeRole;
    @FXML
    private TableColumn<User, BigDecimal> employeeTip;
    @FXML
    private Button buttonUpdate;
    @FXML
    private Button buttonRemove;
    @FXML
    private Button clearTipButton;

    @Autowired
    private UserService userService;
    @Autowired
    private Validator<User> userValidator;

    @Resource(name = "userDialogPane")
    private FXMLPane userDialogPane;
    private UserDialogController userDialogController;

    private ObservableList<User> users = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        userDialogController = userDialogPane.getController(UserDialogController.class);

        tableViewEmployee.setItems(users);

        employeeId.setCellValueFactory(new PropertyValueFactory<>("identity"));
        employeeName.setCellValueFactory(new PropertyValueFactory<>("name"));
        employeeRole.setCellValueFactory(new PropertyValueFactory<>("role"));
        employeeTip.setCellValueFactory(new PropertyValueFactory<>("tip"));

        tableViewEmployee.getSelectionModel().selectedIndexProperty().addListener(event -> {
            final boolean hasSelection = tableViewEmployee.getSelectionModel().getSelectedIndex() >= 0;

            buttonUpdate.setDisable(!hasSelection);
            buttonRemove.setDisable(!hasSelection);
            clearTipButton.setDisable(!hasSelection);
        });

        loadAllUsers();
    }

    @FXML
    public void buttonEmployeesAddClicked(ActionEvent actionEvent) {
        InputDialog<User> userInputDialog = new CreateInputDialog<>("employee");
        userInputDialog.setValidator(userValidator);
        userInputDialog.setContent(userDialogPane);
        userInputDialog.setController(userDialogController);
        userInputDialog.showAndWait().ifPresent(user -> {
            try {
                userService.addUser(user);
                users.add(user);
            } catch (ValidationException | ServiceException e) {
                LOGGER.error("Could not add user " + user, e);

                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error while adding employee");
                alert.setHeaderText("Could not add employee '" + user.getIdentity() + "'");
                alert.setContentText(e.getMessage());
                alert.showAndWait();
            }
        });
    }

    @FXML
    public void buttonEmployeesUpdateClicked(ActionEvent actionEvent) {
        User selectedUser = tableViewEmployee.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            InputDialog<User> userInputDialog = new UpdateInputDialog<>("employee", selectedUser);
            userInputDialog.setValidator(userValidator);
            userInputDialog.setContent(userDialogPane);
            userInputDialog.setController(userDialogController);
            userInputDialog.showAndWait().ifPresent(editedUser -> {
                try {
                    userService.updateUser(editedUser);
                    users.remove(selectedUser);
                    users.add(editedUser);
                } catch (ValidationException | ServiceException e) {
                    LOGGER.error("Could not change user " + editedUser, e);

                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error while updating employee");
                    alert.setHeaderText("Could not update employee '" + selectedUser.getIdentity() + "'");
                    alert.setContentText(e.getMessage());
                    alert.showAndWait();
                }
            });
        }
    }

    @FXML
    public void buttonEmployeesSearchClicked(ActionEvent actionEvent) {
        InputDialog<User> userSearchDialog = new SearchInputDialog<>("employees");
        userSearchDialog.setContent(userDialogPane);
        userSearchDialog.setController(userDialogController);
        userSearchDialog.showAndWait().ifPresent(userMatcher -> {
            try {
                users.setAll(userService.findUsers(userMatcher));
            } catch (ServiceException e) {
                LOGGER.error("Could not search for users with matcher " + userMatcher, e);

                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error while searching for employees");
                alert.setHeaderText("Could not search for employees");
                alert.setContentText(e.getMessage());
                alert.showAndWait();
            }
        });
    }

    @FXML
    public void buttonEmployeesRemoveClicked(ActionEvent actionEvent) {
        User selectedUser = tableViewEmployee.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            try {
                userService.deleteUser(selectedUser);
                users.remove(selectedUser);
            } catch (ValidationException | ServiceException e) {
                LOGGER.error("Could not delete user " + selectedUser, e);

                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error while deleting employee");
                alert.setHeaderText("Could not delete employee '" + selectedUser.getIdentity() + "'");
                alert.setContentText(e.getMessage());
                alert.showAndWait();
            }
        }
    }

    @FXML
    public void buttonClearTipClicked(ActionEvent actionEvent) {
        User selectedUser = tableViewEmployee.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            try {
                User editedUser = selectedUser.clone();
                editedUser.setTip(BigDecimal.ZERO);
                userService.updateUser(editedUser);

                users.remove(selectedUser);
                users.add(editedUser);
            } catch (ValidationException | ServiceException e) {
                LOGGER.error("Could not clear tip of user " + selectedUser, e);

                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error while clearing the tip of a employee");
                alert.setHeaderText("Could not clear the tip of the employee '" + selectedUser.getIdentity() + "'");
                alert.setContentText(e.getMessage());
                alert.showAndWait();
            }
        }
    }

    @FXML
    public void buttonEmployeesShowAll(ActionEvent actionEvent) {
        loadAllUsers();
    }

    private void loadAllUsers() {
        try {
            users.setAll(userService.getAllUsers());
        } catch (ServiceException e){
            LOGGER.error("Could not load all users", e);
        }
    }
}
