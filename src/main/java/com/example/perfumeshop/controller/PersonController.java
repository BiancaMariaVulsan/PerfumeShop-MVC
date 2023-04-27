package com.example.perfumeshop.controller;

import com.example.perfumeshop.model.Employee;
import com.example.perfumeshop.model.Person;
import com.example.perfumeshop.model.Role;
import com.example.perfumeshop.model.persistence.EmployeePersistence;
import com.example.perfumeshop.model.persistence.PersonPersistence;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.ArrayList;

public class PersonController {
    private final RegisterController registerView;
    private static final PersonPersistence personPersistence = new PersonPersistence();
    private static final EmployeePersistence employeePersistence = new EmployeePersistence();

    public PersonController(RegisterController registerView) {
        this.registerView = registerView;
    }
    public void setProgressIndicator() {
        ArrayList<TextField> textFields = new ArrayList<>();
        textFields.add(registerView.getUsernameTextField());
        textFields.add(registerView.getPasswordTextField());
        textFields.add(registerView.getFirstNameTextField());
        textFields.add(registerView.getLastNameTextField());

        for (TextField textField : textFields) {
            textField.textProperty().addListener((observable, oldValue, newValue) -> {

                int numberOfNonEmpty = 0;
                for (TextField textField1 : textFields)
                    if (!textField1.getText().isEmpty()) {
                        numberOfNonEmpty++;
                    }
                registerView.getProgressIndicator().setProgress((double) numberOfNonEmpty / textFields.size());
            });
        }
    }
    public void register() {
        String role = registerView.getRoleChoiceBox().getValue();
        Person person;
        if(role.equals("EMPLOYEE")) {
            person = new Employee(registerView.getFirstNameTextField().getText(),
                    registerView.getLastNameTextField().getText(), registerView.getUsernameTextField().getText(),
                    registerView.getPasswordTextField().getText(), registerView.getShopChoiceBox().getValue().getId());
        } else {
            person = new Person(registerView.getFirstNameTextField().getText(),
                    registerView.getLastNameTextField().getText(), Role.valueOf(role), registerView.getUsernameTextField().getText(),
                    registerView.getPasswordTextField().getText());
        }
        if(addPerson(person)) {
            Controller.initAlarmBox("Successful registration", "You are successfully registered!", Alert.AlertType.INFORMATION);
            Stage stage = (Stage) registerView.getRegisterButton().getScene().getWindow();
            stage.close();
        } else {
            Controller.initAlarmBox("Error", "An error occurred during the registration, please try again!", Alert.AlertType.ERROR);
        }
    }

    public void updatePerson(Person personToUpdate) {
        String role = registerView.getRoleChoiceBox().getValue();
        PersonPersistence personPersistence = new PersonPersistence();
        EmployeePersistence employeePersistence = new EmployeePersistence();
        boolean successUpdate = false;
        if(role.equals("EMPLOYEE")) {
            personToUpdate = new Employee(personToUpdate.getId(), registerView.getFirstNameTextField().getText(),
                    registerView.getLastNameTextField().getText(), registerView.getUsernameTextField().getText(),
                    registerView.getPasswordTextField().getText(), registerView.getShopChoiceBox().getValue().getId());
            successUpdate = employeePersistence.update((Employee) personToUpdate);
        } else {
            personToUpdate = new Person(personToUpdate.getId(), registerView.getFirstNameTextField().getText(),
                    registerView.getLastNameTextField().getText(), Role.valueOf(registerView.getRoleChoiceBox().getValue()),
                    registerView.getUsernameTextField().getText(), registerView.getPasswordTextField().getText());
            successUpdate = personPersistence.update(personToUpdate);
        }
        if(successUpdate) {
            Controller.initAlarmBox("Successful registration", "Person successfully updated!", Alert.AlertType.INFORMATION);
            Stage stage = (Stage) registerView.getRegisterButton().getScene().getWindow();
            stage.close();
        } else {
            Controller.initAlarmBox("Error", "An error occurred during the registration, please try again!", Alert.AlertType.ERROR);
        }
    }

    public static boolean addEmloyee(Employee employee) {
        try {
            employeePersistence.insert(employee);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    private boolean addPerson(Person person) {
        try {
            if(person.getRole()!=Role.EMPLOYEE) {
                personPersistence.insert(person);
            } else {
                addEmloyee((Employee) person);
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public void enableShopChoiceBox(String role) {
        if(role.equals("EMPLOYEE")) {
            registerView.getShopChoiceBox().setDisable(false);
        } else {
            registerView.getShopChoiceBox().setDisable(true);
        }
    }
}
