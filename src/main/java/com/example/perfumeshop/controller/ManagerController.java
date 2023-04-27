package com.example.perfumeshop.controller;

import com.example.perfumeshop.model.Language;
import com.example.perfumeshop.model.Product;
import com.example.perfumeshop.model.Shop;
import com.example.perfumeshop.model.persistence.ShopPersistence;
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
import java.util.List;
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

    @FXML
    private Button saveCSV;
    @FXML
    private Button saveJSON;
    @FXML
    private Button saveXML;
    @FXML
    private Button saveTXT;
    @FXML
    private Button brandAnalysisButton;

    @FXML
    private ChoiceBox<String> shopChoice;

    private final Language language;
    private final ProductController productController = new ProductController();

    public ManagerController(Language language) {
        this.language = language;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setFilterButton(language.getFilterButton());
        setSortNameButton(language.getSortNameButton());
        setSortPriceButton(language.getSortPriceButton());
        setBrandAnalysisButton(language.getBrandAnalysisButton());

        setAvailabilityColumn(language.getAvailabilityColumn());
        setPriceColumn(language.getPriceColumn());
        setBrandColumn(language.getBrandColumn());
        setNameColumn(language.getNameColumn());

        setSaveCSV(language.getSaveCSV());
        setSaveJSON(language.getSaveJSON());
        setSaveTXT(language.getSaveTXT());
        setSaveXML(language.getSaveXML());

        Controller.populateTableProducts(productTableView, productItems, nameColumn, brandColumn, availabilityColumn, priceColumn);
        initShopCheckBox(shopChoice);

        filterButton.setOnAction(e -> {
            var filteredItems = productController.filterProducts(nameFilter, brandFilter, availabilityFilter, priceFilter, shopChoice.getValue());
            Controller.populateTableProductsFiltered(productTableView, productItems, nameColumn, brandColumn, availabilityColumn, priceColumn, filteredItems);
        });
        sortNameButton.setOnAction(e -> {
            var sortedItems = productController.sortByName();
            Controller.populateTableProductsFiltered(productTableView, productItems, nameColumn, brandColumn, availabilityColumn, priceColumn, sortedItems);
        });
        sortPriceButton.setOnAction(e -> {
            var sortedItems = productController.sortByPrice();
            Controller.populateTableProductsFiltered(productTableView, productItems, nameColumn, brandColumn, availabilityColumn, priceColumn, sortedItems);
        });
        saveCSV.setOnAction(e -> {
            CsvPersistence saveSpCSVCommand = new CsvPersistence(productItems, "allProducts.csv");
            saveSpCSVCommand.save();
        });
        saveJSON.setOnAction(e -> {
            JsonPersistence saveSpJsonCommand = new JsonPersistence(productItems, "allProducts.json");
            saveSpJsonCommand.save();
        });
        saveXML.setOnAction(e -> {
            XmlPersistence saveSpXmlCommand = new XmlPersistence(productItems, "allProducts.xml");
            saveSpXmlCommand.save();
        });
        saveTXT.setOnAction(e -> {
            TxtPersistence saveSpTxtCommand = new TxtPersistence(productItems, "allProducts.txt");
            saveSpTxtCommand.save();
        });
        brandAnalysisButton.setOnAction(e -> {
            Callback<Class<?>, Object> controllerFactory = type -> {
                if (type == PieChartController.class) {
                    return new PieChartController(productItems);
                } else {
                    try {
                        return type.newInstance();
                    } catch (Exception exc) {
                        System.err.println("Could not load register controller " + type.getName());
                        throw new RuntimeException(exc);
                    }
                }
            };
            Controller.loadFXML("/com/example/perfumeshop/brand-pie-chart.fxml", controllerFactory);
        });
    }

    public void initShopCheckBox(ChoiceBox<String> shopChoiceBox) {
        ShopPersistence shopPersistence = new ShopPersistence();
        List<Shop> shops = shopPersistence.findAll();
        for(Shop shop: shops) {
            shopChoiceBox.getItems().add(shop.getName());
        }
        shopChoiceBox.setValue(shops.get(0).getName()); // suppose there is at least one shop
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

    public void setFilterButton(String filterButton) {
        this.filterButton.setText(filterButton);
    }

    public void setSortNameButton(String sortNameButton) {
        this.sortNameButton.setText(sortNameButton);
    }

    public void setSortPriceButton(String sortPriceButton) {
        this.sortPriceButton.setText(sortPriceButton);
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

    public void setBrandAnalysisButton(String brandAnalysisButton) {
        this.brandAnalysisButton.setText(brandAnalysisButton);
    }
}
