package com.at.ac.tuwien.sepm.ss15.edulium.gui;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.User;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ValidationException;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.Validator;
import com.at.ac.tuwien.sepm.ss15.edulium.service.ServiceException;
import com.at.ac.tuwien.sepm.ss15.edulium.service.UserService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
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

    private final ObservableList<User> users = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        userDialogController = userDialogPane.getController();

        tableViewEmployee.setItems(users);
        tableViewEmployee.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

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
    public void buttonEmployeesAddClicked() {
        CreateInputDialog<User> userInputDialog = new CreateInputDialog<>("employee");
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
    public void buttonEmployeesUpdateClicked() {
        User selectedUser = tableViewEmployee.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            UpdateInputDialog<User> userInputDialog = new UpdateInputDialog<>("employee", selectedUser);
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
    public void buttonEmployeesSearchClicked() {
        SearchInputDialog<User> userSearchDialog = new SearchInputDialog<>("employees");
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
    public void buttonEmployeesRemoveClicked() {
        List<User> selectedUsers = tableViewEmployee.getSelectionModel().getSelectedItems();
        List<User> removedUsers = new ArrayList<>();
        for (User selectedUser : selectedUsers) {
            try {
                userService.deleteUser(selectedUser);
                removedUsers.add(selectedUser);
            } catch (ValidationException | ServiceException e) {
                LOGGER.error("Could not delete user " + selectedUser, e);

                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error while deleting employee");
                alert.setHeaderText("Could not delete employee '" + selectedUser.getIdentity() + "'");
                alert.setContentText(e.getMessage());
                alert.showAndWait();
            }
        }
        users.removeAll(removedUsers);
    }

    @FXML
    public void buttonClearTipClicked() {
        List<User> selectedUsers = tableViewEmployee.getSelectionModel().getSelectedItems();
        List<User> addUsers = new ArrayList<>();
        for (User selectedUser : selectedUsers) {
            try {
                User editedUser = selectedUser.clone();
                editedUser.setTip(BigDecimal.ZERO);
                userService.updateUser(editedUser);

                addUsers.add(editedUser);
            } catch (ValidationException | ServiceException e) {
                LOGGER.error("Could not clear tip of user " + selectedUser, e);

                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error while clearing the tip of a employee");
                alert.setHeaderText("Could not clear the tip of the employee '" + selectedUser.getIdentity() + "'");
                alert.setContentText(e.getMessage());
                alert.showAndWait();
            }
        }
        users.removeAll(selectedUsers);
        users.addAll(addUsers);
    }

    @FXML
    public void buttonEmployeesShowAll() {
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
