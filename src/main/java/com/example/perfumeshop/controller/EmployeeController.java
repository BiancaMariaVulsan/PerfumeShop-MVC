package com.example.perfumeshop.controller;

import com.example.perfumeshop.model.Language;
import com.example.perfumeshop.model.ShopProduct;
import com.example.perfumeshop.model.persistence.files.CsvPersistence;
import com.example.perfumeshop.model.persistence.files.JsonPersistence;
import com.example.perfumeshop.model.persistence.files.TxtPersistence;
import com.example.perfumeshop.model.persistence.files.XmlPersistence;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.Callback;

import java.net.URL;
import java.util.Comparator;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class EmployeeController implements Initializable {

    @FXML
    private TableView<ShopProduct> productTableView;
    private final ObservableList<ShopProduct> productItems = FXCollections.observableArrayList();
    @FXML
    private TableColumn<ShopProduct, String> nameColumn;
    @FXML
    private TableColumn<ShopProduct ,String> brandColumn;
    @FXML
    private TableColumn<ShopProduct, Boolean> availabilityColumn;
    @FXML
    private TableColumn<ShopProduct, Number> priceColumn;

    @FXML
    private Button addButton;
    @FXML
    private Button deleteButton;
    @FXML
    private Button editButton;
    @FXML
    private Button filterButton;

    @FXML
    private TextField brandFilter;
    @FXML
    private CheckBox availabilityFilter;
    @FXML
    private TextField priceFilter;
    @FXML
    public TextField nameFilter;

    @FXML
    private Button saveCSV;
    @FXML
    private Button saveJSON;
    @FXML
    private Button saveXML;
    @FXML
    private Button saveTXT;

    @FXML
    public Button sortByNameButton;
    @FXML
    public Button sortByPriceButton;

    private final int idShop;
    private final ProductController productPresenter = new ProductController();
    private final Language language;

    public EmployeeController(int isShop, Language language) {
        this.idShop = isShop;
        this.language = language;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setAddButton(language.getAddButton());
        setDeleteButton(language.getDeleteButton());
        setEditButton(language.getEditButton());
        setFilterButton(language.getFilterButton());
        setSortByPriceButton(language.getSortPriceButton());
        setSortByNameButton(language.getSortNameButton());
        setAvailabilityColumn(language.getAvailabilityColumn());
        setBrandColumn(language.getBrandColumn());
        setNameColumn(language.getNameColumn());
        setPriceColumn(language.getPriceColumn());
        setSaveCSV(language.getSaveCSV());
        setSaveJSON(language.getSaveJSON());
        setSaveTXT(language.getSaveTXT());
        setSaveXML(language.getSaveXML());

        Controller.populateTableProducts(productTableView, productItems, nameColumn, brandColumn, availabilityColumn, priceColumn, idShop);
        addButton.setOnAction(e -> {
            Callback<Class<?>, Object> controllerFactory = type -> {
                if (type == AddProductController.class) {
                    return new AddProductController(language, productTableView, productItems, nameColumn, brandColumn, availabilityColumn, priceColumn, idShop);
                } else {
                    try {
                        return type.newInstance();
                    } catch (Exception exc) {
                        System.err.println("Could not load register controller " + type.getName());
                        throw new RuntimeException(exc);
                    }
                }
            };
            Controller.loadFXML("/com/example/perfumeshop/add-product-view.fxml", controllerFactory);
        });
        deleteButton.setOnAction(e -> {
            var products = productPresenter.deleteProduct(productTableView.getSelectionModel().getSelectedItem().getProduct(), idShop);
            Controller.populateTableProducts(productTableView, productItems, nameColumn, brandColumn, availabilityColumn, priceColumn, products);
        });
        filterButton.setOnAction(e -> {
            var filteredItems = productPresenter.filterProducts(nameFilter, brandFilter, availabilityFilter, priceFilter, idShop);
            Controller.populateTableProducts(productTableView, productItems, nameColumn, brandColumn, availabilityColumn, priceColumn, filteredItems);
        });
        editButton.setOnAction(e -> {
            ShopProduct product = productTableView.getSelectionModel().getSelectedItem();
            if(product == null) {
                Controller.initAlarmBox("Warning", "Please select the product to be updated!", Alert.AlertType.WARNING);
                return;
            }
            Callback<Class<?>, Object> controllerFactory = type -> {
                if (type == AddProductController.class) {
                    return new AddProductController(language, product, productTableView, productItems, nameColumn, brandColumn, availabilityColumn, priceColumn, idShop);
                } else {
                    try {
                        return type.newInstance();
                    } catch (Exception exc) {
                        System.err.println("Could not load register controller " + type.getName());
                        throw new RuntimeException(exc);
                    }
                }
            };
            Controller.loadFXML("/com/example/perfumeshop/add-product-view.fxml", controllerFactory);
        });
        saveCSV.setOnAction(e -> {
            CsvPersistence saveSpCSVCommand = new CsvPersistence(productItems.stream().map(ShopProduct::getProduct).toList(), "shopProducts.csv");
            saveSpCSVCommand.save();
        });
        saveJSON.setOnAction(e -> {
            JsonPersistence saveSpJsonCommand = new JsonPersistence(productItems.stream().map(ShopProduct::getProduct).toList(), "shopProducts.json");
            saveSpJsonCommand.save();
        });
        saveXML.setOnAction(e -> {
            XmlPersistence saveSpXmlCommand = new XmlPersistence(productItems.stream().map(ShopProduct::getProduct).toList(), "shopProducts.xml");
            saveSpXmlCommand.save();
        });
        saveTXT.setOnAction(e -> {
            TxtPersistence saveSpTxtCommand = new TxtPersistence(productItems.stream().map(ShopProduct::getProduct).toList(), "shopProducts.txt");
            saveSpTxtCommand.save();
        });
        sortByNameButton.setOnAction(e -> {
            productItems.setAll(productItems.stream()
                    .sorted(Comparator.comparing(sp -> sp.getProduct().getName()))
                    .collect(Collectors.toList()));

        });
        sortByPriceButton.setOnAction(e -> {
            productItems.setAll(productItems.stream()
                    .sorted(Comparator.comparing(sp -> sp.getProduct().getPrice()))
                    .collect(Collectors.toList()));
        });
    }

    public void setNameColumn(String nameColumn) {
        this.nameColumn.setText(nameColumn);
    }

    public void setBrandColumn(String brandColumn) {
        this.brandColumn.setText(brandColumn);
    }

    public void setAvailabilityColumn(String availabilityColumn) {
        this.availabilityColumn.setText(availabilityColumn);
    }

    public void setPriceColumn(String priceColumn) {
        this.priceColumn.setText(priceColumn);
    }

    public void setAddButton(String addButton) {
        this.addButton.setText(addButton);
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

    public void setSaveCSV(String saveCSV) {
        this.saveCSV.setText(saveCSV);
    }

    public void setSaveJSON(String saveJSON) {
        this.saveJSON.setText(saveJSON);
    }

    public void setSaveXML(String saveXML) {
        this.saveXML.setText(saveXML);
    }

    public void setSaveTXT(String saveTXT) {
        this.saveTXT.setText(saveTXT);
    }

    public void setSortByNameButton(String sortByNameButton) {
        this.sortByNameButton.setText(sortByNameButton);
    }

    public void setSortByPriceButton(String sortByPriceButton) {
        this.sortByPriceButton.setText(sortByPriceButton);
    }
}
