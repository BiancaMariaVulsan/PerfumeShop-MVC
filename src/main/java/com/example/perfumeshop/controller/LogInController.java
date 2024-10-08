package com.example.perfumeshop.controller;

import com.example.perfumeshop.model.Employee;
import com.example.perfumeshop.model.Language;
import com.example.perfumeshop.model.Person;
import com.example.perfumeshop.model.Role;
import com.example.perfumeshop.model.persistence.EmployeePersistence;
import com.example.perfumeshop.model.persistence.PersonPersistence;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.Callback;

import java.net.URL;
import java.util.*;

public class LogInController extends Observable implements Initializable {
    @FXML
    public Label usernameLabel;
    @FXML
    public Label passwordLabel;
    @FXML
    private TextField usernameTextField;
    @FXML
    private TextField passwordTextField;
    @FXML
    private Button signInButton;
    @FXML
    private ChoiceBox<String> languageChoice;

    private static final PersonPersistence personPersistence = new PersonPersistence();
    private static final EmployeePersistence employeePersistence = new EmployeePersistence();
    private final LanguageController languageController = new LanguageController();
    private Language language;

    public LogInController() {}

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initLanguageCheckBox();

        languageChoice.showingProperty().addListener((obs, wasShowing, isNowShowing) -> {
            languageController.read(languageChoice.getValue());
            language = languageController.getLanguage();
            setChanged();
            this.notifyObservers(language);
            setPasswordLabel(language.getPassword());
            setUsernameLabel(language.getUsername());
        });

        signInButton.setOnAction(actionEvent -> {
            languageController.read(languageChoice.getValue());
            language = languageController.getLanguage();
            setPasswordLabel(language.getPassword());
            setUsernameLabel(language.getUsername());
            signIn();
            setChanged();
            this.notifyObservers(language);
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
            AdminController adminController = new AdminController();
            this.addObserver(adminController);
            Callback<Class<?>, Object> controllerFactory = type -> {
                if (type == AdminController.class) {
                    return adminController;
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
            ManagerController managerController = new ManagerController(language);
            this.addObserver(managerController);
            Callback<Class<?>, Object> controllerFactory = type -> {
                if (type == ManagerController.class) {
                    return managerController;
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
            EmployeeController employeeController = new EmployeeController(language, getShopId(username));
            this.addObserver(employeeController);
            Callback<Class<?>, Object> controllerFactory = type -> {
                if (type == com.example.perfumeshop.controller.EmployeeController.class) {
                    return employeeController;
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

    private void initLanguageCheckBox() {
        List<String> languages = new ArrayList<>() {
            {
                add("English");
                add("Romanian");
                add("German");
            }
        };
        for(String language: languages) {
            languageChoice.getItems().add(language);
        }
        languageChoice.setValue(languages.get(0));
    }

    public void setUsernameLabel(String usernameLabel) {
        this.usernameLabel.setText(usernameLabel);
    }

    public void setPasswordLabel(String passwordLabel) {
        this.passwordLabel.setText(passwordLabel);
    }
}