package com.example.perfumeshop.controller;

import com.example.perfumeshop.model.Language;
import com.example.perfumeshop.model.Person;
import com.example.perfumeshop.model.persistence.PersonPersistence;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.Callback;

import java.net.URL;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;

public class AdminController implements Initializable, Observer {
    @FXML
    private TableView<Person> personTableView;
    private final ObservableList<Person> personItems = FXCollections.observableArrayList();
    private static final PersonPersistence personPersistence = new PersonPersistence();
    @FXML
    private TableColumn<Person, String> firstNameColumn;
    @FXML
    private TableColumn<Person, String> lastNameColumn;
    @FXML
    private TableColumn<Person, String> roleColumn;
    @FXML
    private Button addButton;
    @FXML
    private Button deleteButton;
    @FXML
    private Button editButton;
    @FXML
    private Button filterButton;
    @FXML
    private ChoiceBox<String> roleChoice;

    public AdminController() {}

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Controller.populateTablePersons(personTableView, personItems, firstNameColumn, lastNameColumn, roleColumn);
        addButton.setOnAction(e -> {
            Callback<Class<?>, Object> controllerFactory = type -> {
                if (type == RegisterController.class) {
                    return new AdminController();//RegisterController(personTableView, personItems, firstNameColumn, lastNameColumn, roleColumn);
                } else {
                    try {
                        return type.newInstance();
                    } catch (Exception exc) {
                        System.err.println("Could not load register controller " + type.getName());
                        throw new RuntimeException(exc);
                    }
                }
            };
            Controller.loadFXML("/com/example/perfumeshop/register-view.fxml", controllerFactory);
        });
        deleteButton.setOnAction(e -> {
            var person = personTableView.getSelectionModel().getSelectedItem();
            if(person == null) {
                Controller.initAlarmBox("Warning", "Please select the product to be deleted!", Alert.AlertType.WARNING);
                return;
            }
            if(deletePersons(person)) {
                Controller.populateTablePersons(personTableView, personItems, firstNameColumn, lastNameColumn, roleColumn);
            } else {
                Controller.initAlarmBox("Warning", "Delete operation failed, please try again!", Alert.AlertType.WARNING);
            }
        });
        editButton.setOnAction(e -> {
            Person item = personTableView.getSelectionModel().getSelectedItem();
            if(item == null) {
                Controller.initAlarmBox("Warning", "Please select the product to be edited!", Alert.AlertType.WARNING);
                return;
            }
            Callback<Class<?>, Object> controllerFactory = type -> {
                if (type == RegisterController.class) {
                    return new AdminController();// RegisterController(item, personTableView, personItems, firstNameColumn, lastNameColumn, roleColumn);
                } else {
                    try {
                        return type.newInstance();
                    } catch (Exception exc) {
                        System.err.println("Could not load register controller " + type.getName());
                        throw new RuntimeException(exc);
                    }
                }
            };
            Controller.loadFXML("/com/example/perfumeshop/register-view.fxml", controllerFactory);
        });
        filterButton.setOnAction(e -> {
            filter();
        });
    }

    private void filter() {
        List<Person> persons = personPersistence.findAll();
        String role = roleChoice.getValue();
        if(role.equals("ADMINISTRATOR")) {
            role = "ADMIN";
        } else if(role.equals("ANGAJAT") || role.equals("MITERBEITER")) {
            role = "EMPLOYEE";
        } else if (role.equals("MANAGER")){
            role = "MANAGER";
        }
        String finalRole = role;
        personItems.setAll(persons.stream().filter(p -> p.getRole().name().equals(finalRole)).toList());
    }

    private static boolean deletePersons(Person person) {
        try {
            personPersistence.delete(person);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public void setFirstNameColumn(String firstNameColumn) {
        this.firstNameColumn.setText(firstNameColumn);
    }

    public void setLastNameColumn(String lastNameColumn) {
        this.lastNameColumn.setText(lastNameColumn);
    }

    public void setRoleColumn(String roleColumn) {
        this.roleColumn.setText(roleColumn);
    }

    public void setAddButton(String textButton) {
        this.addButton.setText(textButton);
    }

    public void setDeleteButton(String deleteButton) {
        this.deleteButton.setText(deleteButton);
    }

    public void setEditButton(String editButton) {
        this.editButton.setText(editButton);
    }

    public void setFilterButton(String filterButton) {
        this.filterButton.setText(filterButton);
    }

    public void setRoleChoice(List<String> roleChoice) {
        ObservableList<String> items = FXCollections.observableArrayList(roleChoice);
        this.roleChoice.setItems(items);
    }

    @Override
    public void update(Observable o, Object arg) {
        setAddButton(((Language) arg).getAddButton());
        setDeleteButton(((Language) arg).getDeleteButton());
        setEditButton(((Language) arg).getEditButton());
        setFilterButton(((Language) arg).getFilterButton());
        setFirstNameColumn(((Language) arg).getFirstNameColumn());
        setLastNameColumn(((Language) arg).getLastNameColumn());
        setRoleChoice(((Language) arg).getRoleChoice());
    }
}
