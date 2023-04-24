package com.example.perfumeshop.controller;

import com.example.perfumeshop.model.Person;
import com.example.perfumeshop.model.Product;
import com.example.perfumeshop.model.ShopProduct;
import com.example.perfumeshop.model.persistence.PersonPersistence;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.property.ReadOnlyDoubleWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class Controller {
    static ProductController productPresenter = new ProductController();

    private static final PersonPersistence personPersistence = new PersonPersistence();
    private static List<Person> getPersons() {
        return personPersistence.findAll();
    }

    public static void loadFXML(String fxmlFile, Callback<Class<?>, Object> controllerFactory) {
        Stage programStage = new Stage();
        Parent programRoot;

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Controller.class.getResource(fxmlFile));
            fxmlLoader.setControllerFactory(controllerFactory);
            var path = Controller.class.getResource(fxmlFile);
            fxmlLoader.setLocation(path);
            programRoot = fxmlLoader.load();
            Scene programScene = new Scene(programRoot);
            String css = Controller.class.getResource("/com/example/perfumeshop/start.css").toExternalForm();
            programScene.getStylesheets().add(css);
            programStage.setTitle("Running Program");
            programStage.setScene(programScene);
            programStage.show();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    public static Optional<ButtonType> initAlarmBox(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        Button confirm = (Button) alert.getDialogPane().lookupButton(ButtonType.OK);
        confirm.setDefaultButton(false);
        confirm.setStyle("-fx-focus-color: transparent; -fx-faint-focus-color: transparent;");
        return alert.showAndWait();
    }

    public static void populateTableProductsFiltered(TableView<Product> productTableView, ObservableList<Product> productItems, TableColumn<Product, String> nameColumn,
                                             TableColumn<Product ,String> brandColumn, TableColumn<Product, Boolean> availabilityColumn, TableColumn<Product, Number> priceColumn,
                                             List<Product> filteredProducts){
        productItems.clear();
        productTableView.getItems().clear();
        nameColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getName()));
        brandColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getBrand()));
        availabilityColumn.setCellValueFactory(cellData -> new ReadOnlyBooleanWrapper(productPresenter.isAvailableInTheChain(cellData.getValue().getId())));
        priceColumn.setCellValueFactory(cellData -> new ReadOnlyDoubleWrapper(cellData.getValue().getPrice()));
        productItems.addAll(filteredProducts);
        productTableView.setItems(productItems);
    }

    public static void populateTableProducts(TableView<ShopProduct> productTableView, ObservableList<ShopProduct> productItems, TableColumn<ShopProduct, String> nameColumn,
                                             TableColumn<ShopProduct ,String> brandColumn, TableColumn<ShopProduct, Boolean> availabilityColumn, TableColumn<ShopProduct, Number> priceColumn,
                                             List<ShopProduct> filteredProducts){
        productItems.clear();
        productTableView.getItems().clear();
        nameColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getProduct().getName()));
        brandColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getProduct().getBrand()));
        availabilityColumn.setCellValueFactory(cellData -> new ReadOnlyBooleanWrapper(cellData.getValue().getStock() > 0));
        priceColumn.setCellValueFactory(cellData -> new ReadOnlyDoubleWrapper(cellData.getValue().getProduct().getPrice()));
        productItems.addAll(filteredProducts);
        productTableView.setItems(productItems);
    }

    public static void populateTableProducts(TableView<Product> productTableView, ObservableList<Product> productItems, TableColumn<Product, String> nameColumn,
                                             TableColumn<Product ,String> brandColumn, TableColumn<Product, Boolean> availabilityColumn, TableColumn<Product, Number> priceColumn){
        productItems.clear();
        productTableView.getItems().clear();
        nameColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getName()));
        brandColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getBrand()));
        availabilityColumn.setCellValueFactory(cellData -> new ReadOnlyBooleanWrapper(productPresenter.isAvailableInTheChain(cellData.getValue().getId())));
        priceColumn.setCellValueFactory(cellData -> new ReadOnlyDoubleWrapper(cellData.getValue().getPrice()));
        productItems.addAll(productPresenter.getProducts());
        productTableView.setItems(productItems);
    }

    public static void populateTableProducts(TableView<ShopProduct> productTableView, ObservableList<ShopProduct> productItems, TableColumn<ShopProduct, String> nameColumn,
                                             TableColumn<ShopProduct ,String> brandColumn, TableColumn<ShopProduct, Boolean> availabilityColumn, TableColumn<ShopProduct, Number> priceColumn,
                                             int idShop){
        productItems.clear();
        productTableView.getItems().clear();
        nameColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getProduct().getName()));
        brandColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getProduct().getBrand()));
        availabilityColumn.setCellValueFactory(cellData -> new ReadOnlyBooleanWrapper(cellData.getValue().getStock() > 0));
        priceColumn.setCellValueFactory(cellData -> new ReadOnlyDoubleWrapper(cellData.getValue().getProduct().getPrice()));
        productItems.addAll(productPresenter.getProducts(idShop));
        productTableView.setItems(productItems);
    }

    public static void populateTablePersons(TableView<Person> personTableView, ObservableList<Person> personItems, TableColumn<Person, String> firstNameColumn,
                                            TableColumn<Person ,String> lastNameColumn, TableColumn<Person, String> roleColumn){
        personItems.clear();
        personTableView.getItems().clear();
        firstNameColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getFirstName()));
        lastNameColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getLastName()));
        roleColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getRole().toString()));
        personItems.addAll(getPersons());
        personTableView.setItems(personItems);
    }
}
