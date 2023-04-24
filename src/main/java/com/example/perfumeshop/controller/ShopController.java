package com.example.perfumeshop.controller;

import com.example.perfumeshop.model.Shop;
import com.example.perfumeshop.model.persistence.ShopPersistence;

import java.util.List;

public class ShopController {
    static ShopPersistence shopPersistence = new ShopPersistence();

    public static List<Shop> getShops() {
        return shopPersistence.findAll();
    }

    public static String getShopNameById(int id) {
        return shopPersistence.findById(id).getName();
    }
}
