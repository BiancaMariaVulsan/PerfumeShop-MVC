package com.example.perfumeshop.controller;

import com.example.perfumeshop.model.Employee;
import com.example.perfumeshop.model.Person;
import com.example.perfumeshop.model.Role;
import com.example.perfumeshop.model.persistence.EmployeePersistence;
import com.example.perfumeshop.model.persistence.PersonPersistence;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.util.Callback;

public class LogInController {
    @FXML
    private TextField usernameTextField;
    @FXML
    private TextField passwordTextField;
    @FXML
    private Button signInButton;

    private static final PersonPersistence personPersistence = new PersonPersistence();
    private static final EmployeePersistence employeePersistence = new EmployeePersistence();

    public LogInController() {}

    @FXML
    public void initialize() {
        signInButton.setOnAction(actionEvent -> {
            signIn();
        });
    }

    public TextField getUsernameTextField() {
        return usernameTextField;
    }
    public TextField getPasswordTextField() {
        return passwordTextField;
    }

    private void signIn() {
        String username = getUsernameTextField().getText();
        String password = getPasswordTextField().getText();
        Person person = getPersonByUsername(username);
        if(person == null) {
            Controller.initAlarmBox("Error", "Invalid username and password!", Alert.AlertType.ERROR);
            return;
        }
        if(!person.getPassword().equals(password)){
            Controller.initAlarmBox("Error", "The password is not correct, please try again!", Alert.AlertType.ERROR);
            return;
        }
        if (person.getRole().equals(Role.ADMIN)) {
            Callback<Class<?>, Object> controllerFactory = type -> {
                if (type == AdminController.class) {
                    return new AdminController();
                } else {
                    try {
                        return type.newInstance();
                    } catch (Exception exc) {
                        Controller.initAlarmBox("Error", "Could not load admin controller...", Alert.AlertType.ERROR);
                        throw new RuntimeException(exc.getMessage());
                    }
                }
            };
            Controller.loadFXML("/com/example/perfumeshop/admin-view.fxml", controllerFactory);
        } else if (person.getRole().equals(Role.MANAGER)) {
            Callback<Class<?>, Object> controllerFactory = type -> {
                if (type == ManagerController.class) {
                    return new ManagerController();
                } else {
                    try {
                        return type.newInstance();
                    } catch (Exception exc) {
                        Controller.initAlarmBox("Error", "Could not load manager controller...", Alert.AlertType.ERROR);
                        throw new RuntimeException(exc.getMessage());
                    }
                }
            };
            Controller.loadFXML("/com/example/perfumeshop/manager-view.fxml", controllerFactory);
        } else {
            Callback<Class<?>, Object> controllerFactory = type -> {
                if (type == com.example.perfumeshop.controller.EmployeeController.class) {
                    return new com.example.perfumeshop.controller.EmployeeController(getShopId(username));
                } else {
                    try {
                        return type.newInstance();
                    } catch (Exception exc) {
                        Controller.initAlarmBox("Error", "Could not load employee controller...", Alert.AlertType.ERROR);
                        throw new RuntimeException(exc.getMessage());
                    }
                }
            };
            Controller.loadFXML("/com/example/perfumeshop/employee-view.fxml", controllerFactory);
        }
    }

    private static int getShopId(String username) {
        Employee employee = employeePersistence.findAll().stream()
                .filter(e -> e.getUsername().equals(username))
                .findFirst()
                .orElse(null);
        if(employee == null) {
            return -1;
        } else {
            return employee.getShopId();
        }
    }

    private static Person getPersonByUsername(String username) {
        return personPersistence.findAll().stream()
                .filter(p -> p.getUsername().equals(username))
                .findFirst()
                .orElse(null);
    }
}