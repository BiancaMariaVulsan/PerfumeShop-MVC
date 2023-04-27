package com.example.perfumeshop.controller;

import com.example.perfumeshop.model.Language;
import com.example.perfumeshop.model.ShopProduct;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;

public class AddProductController implements Initializable {
    @FXML
    public Label nameLabel;
    @FXML
    public Label brandLabel;
    @FXML
    public Label priceLabel;
    @FXML
    public Label productLabel;
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

    private final ProductController productController = new ProductController();
    private Language language;

    public AddProductController(Language language, TableView<ShopProduct> productTableView, ObservableList<ShopProduct> productItems,
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
        this.language = language;
    }

    public AddProductController(Language language, ShopProduct product, TableView<ShopProduct> productTableView, ObservableList<ShopProduct> productItems,
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
        this.language = language;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setBrandLabel(language.getBrandColumn());
        setPriceLabel(language.getPriceColumn());
        setNameLabel(language.getNameColumn());
        setProductLabel(language.getProduct());
        setSaveButton(language.getSave());

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
                var products = productController.updateProductInShop(productToUpdate.getProduct(), stockText, idShop);
                Controller.populateTableProducts(productTableView, productItems, nameColumn, brandColumn, availabilityColumn, priceColumn, products);
            } else {
                var products = productController.addProduct(nameText, brandText, stockText, priceText, idShop);
                Controller.populateTableProducts(productTableView, productItems, nameColumn, brandColumn, availabilityColumn, priceColumn, products);
            }
        });
    }

    public void setNameLabel(String nameLabel) {
        this.nameLabel.setText(nameLabel);
    }

    public void setBrandLabel(String brandLabel) {
        this.brandLabel.setText(brandLabel);
    }

    public void setPriceLabel(String priceLabel) {
        this.priceLabel.setText(priceLabel);
    }

    public void setProductLabel(String productLabel) {
        this.productLabel.setText(productLabel);
    }

    public void setSaveButton(String saveButton) {
        this.saveButton.setText(saveButton);
    }
}
