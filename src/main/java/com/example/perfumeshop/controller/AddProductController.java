package com.example.perfumeshop.controller;

import com.example.perfumeshop.model.ShopProduct;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class AddProductController implements Initializable {
    @FXML
    private TextField nameText;
    @FXML
    private TextField brandText;
    @FXML
    private TextField stockText;
    @FXML
    private TextField priceText;
    @FXML
    private Button saveButton;

    @FXML
    private final TableView<ShopProduct> productTableView;
    private final ObservableList<ShopProduct> productItems;
    @FXML
    private final TableColumn<ShopProduct, String> nameColumn;
    @FXML
    private final TableColumn<ShopProduct ,String> brandColumn;
    @FXML
    private final TableColumn<ShopProduct, Boolean> availabilityColumn;
    @FXML
    private final TableColumn<ShopProduct, Number> priceColumn;
    private final int idShop;
    private final boolean isEditing;
    private ShopProduct productToUpdate;

    ProductController productPresenter = new ProductController(); //todo: change similar to register if needed

    public AddProductController(TableView<ShopProduct> productTableView, ObservableList<ShopProduct> productItems,
                                TableColumn<ShopProduct, String> nameColumn, TableColumn<ShopProduct, String> brandColumn,
                                TableColumn<ShopProduct, Boolean> availabilityColumn, TableColumn<ShopProduct, Number> priceColumn,
                                int idShop) {
        isEditing = false;
        this.productTableView = productTableView;
        this.productItems = productItems;
        this.nameColumn = nameColumn;
        this.brandColumn = brandColumn;
        this.availabilityColumn = availabilityColumn;
        this.priceColumn = priceColumn;
        this.idShop = idShop;
    }

    public AddProductController(ShopProduct product, TableView<ShopProduct> productTableView, ObservableList<ShopProduct> productItems,
                                TableColumn<ShopProduct, String> nameColumn, TableColumn<ShopProduct, String> brandColumn,
                                TableColumn<ShopProduct, Boolean> availabilityColumn, TableColumn<ShopProduct, Number> priceColumn,
                                int idShop) {
        isEditing = true;
        this.productTableView = productTableView;
        this.productItems = productItems;
        this.nameColumn = nameColumn;
        this.brandColumn = brandColumn;
        this.availabilityColumn = availabilityColumn;
        this.priceColumn = priceColumn;
        this.idShop = idShop;
        this.productToUpdate = product;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if(isEditing) {
            nameText.setText(productToUpdate.getProduct().getName());
            brandText.setText(productToUpdate.getProduct().getBrand());
            stockText.setText(String.valueOf(productToUpdate.getStock()));
            priceText.setText(String.valueOf(productToUpdate.getProduct().getPrice()));

            nameText.setDisable(true);
            brandText.setDisable(true);
            priceText.setDisable(true);
        }

        saveButton.setOnAction(e -> {
            if(isEditing) {
//                productPresenter.updateProduct(productToUpdate.getId(),nameText, brandText, availabilityCheck, priceText, idShop);
                var products = productPresenter.updateProductInShop(productToUpdate.getProduct(), stockText, idShop);
                Controller.populateTableProducts(productTableView, productItems, nameColumn, brandColumn, availabilityColumn, priceColumn, products);
            } else {
                var products = productPresenter.addProduct(nameText, brandText, stockText, priceText, idShop);
                Controller.populateTableProducts(productTableView, productItems, nameColumn, brandColumn, availabilityColumn, priceColumn, products);
            }
        });
    }
}
