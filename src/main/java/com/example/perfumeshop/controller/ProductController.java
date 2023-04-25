package com.example.perfumeshop.controller;

import com.example.perfumeshop.model.Product;
import com.example.perfumeshop.model.Shop;
import com.example.perfumeshop.model.ShopProduct;
import com.example.perfumeshop.model.persistence.ProductPersistence;
import com.example.perfumeshop.model.persistence.ShopPersistence;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;

import java.util.*;
import java.util.stream.Collectors;

public class ProductController {
    ProductPersistence productPersistence = new ProductPersistence();
    Map<Integer, List<ShopProduct>> productsMap;

    public ProductController() {
        if (this.productsMap == null) {
            this.productsMap = getProductsMap();
        }
    }

    public List<Product> getProducts() {
        return productPersistence.findAll();
    }

    public List<ShopProduct> getProducts(int idShop) {
        return productsMap.get(idShop);
    }

    public boolean isAvailableInTheChain(int productId) {
        for(Map.Entry<Integer, List<ShopProduct>> entry : productsMap.entrySet()) {
            for(ShopProduct shopProduct : entry.getValue()) {
                if(shopProduct.getProduct().getId() == productId && shopProduct.getStock() > 0)
                    return true;
            }
        }
        return false;
    }

    public List<Product> filterProducts(TextField nameFilter, TextField brandFilter, CheckBox availabilityFilter, TextField priceFilter, String shopName) {
        String name = nameFilter.getText();
        String brand = brandFilter.getText();
        boolean availability = availabilityFilter.isSelected();
        double price;
        if(name.isEmpty()) {
            name = "";
        }
        if(brand.isEmpty()) {
            brand = "";
        }
        try {
            price = Double.parseDouble(priceFilter.getText());
        } catch (NumberFormatException exception) {
            price = -1;
        }
        List<Product> products = getProducts();
        String finalName = name;
        String finalBrand = brand;
        double finalPrice = price;
        return products.stream()
                .filter(it -> finalName.equals("") || it.getName().toLowerCase().contains(finalName.toLowerCase()))
                .filter(it -> finalBrand.equals("") || it.getBrand().toLowerCase().contains(finalBrand.toLowerCase()))
                .filter(it -> !availability || isAvailableInTheChain(it.getId()))
                .filter(it -> finalPrice == -1 || it.getPrice() <= finalPrice)
                .filter(it -> isProductInShop(it, shopName))
                .collect(Collectors.toList());
    }

    private boolean isProductInShop(Product product, String shopName) {
        ShopPersistence shopPersistence = new ShopPersistence();
        int shopId = shopPersistence.findAll().stream().filter(s -> s.getName().equals(shopName)).map(Shop::getId).toList().get(0);
        return productsMap.get(shopId).stream().filter(p -> p.getProduct().getId()==product.getId()).toList().size() > 0;
    }

    public List<ShopProduct> filterProducts(TextField brandFilter, CheckBox availabilityFilter, TextField priceFilter, int shopId) {
        String brand = brandFilter.getText();
        boolean availability = availabilityFilter.isSelected();
        double price;
        if(brand.isEmpty()) {
            brand = "";
        }
        try {
            price = Double.parseDouble(priceFilter.getText());
        } catch (NumberFormatException exception) {
            price = -1;
        }

        List<ShopProduct> products = productsMap.get(shopId);
        String finalBrand = brand;
        double finalPrice = price;
        return products.stream()
                .filter(it -> finalBrand.equals("") || it.getProduct().getBrand().toLowerCase().contains(finalBrand.toLowerCase()))
                .filter(it -> !availability || (it.getStock() > 0))
                .filter(it -> finalPrice == -1 || it.getProduct().getPrice() <= finalPrice)
                .collect(Collectors.toList());
    }

    public List<ShopProduct> addProduct(TextField nameText, TextField brandText, TextField stockText, TextField priceText, int shopId) {
        try {
            int stock = Integer.parseInt(stockText.getText());
            boolean availability = stock > 0;
            Product product = new Product(nameText.getText(), brandText.getText(), Double.parseDouble(priceText.getText()));
            productPersistence.insert(product);
            Product insertedProduct = productPersistence.findAll()
                    .stream().filter(p -> p.getName().equals(product.getName()) && p.getBrand().equals(product.getBrand())
                    && p.getPrice() == product.getPrice())
                    .findFirst()
                    .orElse(null);
            productPersistence.insertProductInShop(shopId, insertedProduct.getId(), stock);
            productsMap = getProductsMap();
        } catch (Exception e) {
            Controller.initAlarmBox("Error", "Something went wrong when trying to add the product. Please make sure you insert valid properties!", Alert.AlertType.ERROR);
        }
        return productsMap.get(shopId);
    }

    public List<ShopProduct> deleteProduct(Product product, int shopId) {
        if(product == null) {
            Controller.initAlarmBox("Warning", "Please select the product to be deleted!", Alert.AlertType.WARNING);
        } else {
            try {
//            productPersistence.delete(product);
                productPersistence.deleteProductFromShop(shopId, product.getId());
                productsMap = getProductsMap();
            } catch (Exception e) {
                Controller.initAlarmBox("Warning", "Something went wrong!", Alert.AlertType.WARNING);
            }
        }
        return  productsMap.get(shopId);
    }

    public List<ShopProduct> updateProductInShop(Product productToUpdate, TextField stock, int shopId) {
        try {
            int newStock = Integer.parseInt(stock.getText());
            productPersistence.updateStockOfProduct(shopId, productToUpdate.getId(), newStock);
//            productsMap = getProductsMap();
            for(ShopProduct shopP: productsMap.get(shopId)) {
                if(shopP.getProduct().getId() == productToUpdate.getId()) {
                    shopP.setStock(Integer.parseInt(stock.getText()));
                }
            }
        } catch (Exception e) {
            Controller.initAlarmBox("Error", "Something went wrong when trying to update the stock of the the product. Please make sure you insert valid properties!", Alert.AlertType.ERROR);
        }
        return productsMap.get(shopId);
    }

    public List<Product> sortByName() {
        return this.getProducts().stream().sorted(Comparator.comparing(Product::getName)).toList();
    }

    public List<Product> sortByPrice() {
        return this.getProducts().stream().sorted(Comparator.comparing(Product::getPrice)).toList();
    }

    private Map<Integer, List<ShopProduct>> getProductsMap() {
        return productPersistence.getShopProducts();
    }
}
