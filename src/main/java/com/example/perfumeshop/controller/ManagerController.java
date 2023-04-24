package com.example.perfumeshop.controller;

import com.example.perfumeshop.model.Product;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;

public class ManagerController implements Initializable {
    @FXML
    private TableView<Product> productTableView;
    private final ObservableList<Product> productItems = FXCollections.observableArrayList();
    @FXML
    private TableColumn<Product, String> nameColumn;
    @FXML
    private TableColumn<Product ,String> brandColumn;
    @FXML
    private TableColumn<Product, Boolean> availabilityColumn;
    @FXML
    private TableColumn<Product, Number> priceColumn;

    @FXML
    private TextField nameFilter;
    @FXML
    private TextField brandFilter;
    @FXML
    private CheckBox availabilityFilter;
    @FXML
    private TextField priceFilter;

    @FXML
    private Button filterButton;
    @FXML
    private Button sortNameButton;
    @FXML
    private Button sortPriceButton;

    private ProductController productPresenter = new ProductController();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Controller.populateTableProducts(productTableView, productItems, nameColumn, brandColumn, availabilityColumn, priceColumn);

        filterButton.setOnAction(e -> {
            var filteredItems = productPresenter.filterProducts(nameFilter, brandFilter, availabilityFilter, priceFilter);
            Controller.populateTableProductsFiltered(productTableView, productItems, nameColumn, brandColumn, availabilityColumn, priceColumn, filteredItems);
        });
        sortNameButton.setOnAction(e -> {
            var sortedItems = productPresenter.sortByName();
            Controller.populateTableProductsFiltered(productTableView, productItems, nameColumn, brandColumn, availabilityColumn, priceColumn, sortedItems);
        });
        sortPriceButton.setOnAction(e -> {
            var sortedItems = productPresenter.sortByPrice();
            Controller.populateTableProductsFiltered(productTableView, productItems, nameColumn, brandColumn, availabilityColumn, priceColumn, sortedItems);
        });
    }
}
