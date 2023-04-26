package com.example.perfumeshop.controller;

import com.example.perfumeshop.model.Language;
import com.example.perfumeshop.model.persistence.files.LanguagePersistence;

import java.util.Arrays;
import java.util.Observable;
import java.util.Observer;

public class LanguageController implements Observer {
    private final LanguagePersistence languagePersistence = new LanguagePersistence();
    private Language language;

    @Override
    public void update(Observable o, Object arg) {
        try {
            if(arg.equals("English")) {
                language = languagePersistence.read("D:\\FACULTATE\\ANIII\\SEM2\\SD\\lab\\Project_MVC\\PerfumeShop-MVC\\PerfumeShop\\EnglishLanguage.json");
            } else if(arg.equals("Romanian")) {
                language = languagePersistence.read("D:\\FACULTATE\\ANIII\\SEM2\\SD\\lab\\Project_MVC\\PerfumeShop-MVC\\PerfumeShop\\RomanianLanguage.json");
            } else {
                language = languagePersistence.read("D:\\FACULTATE\\ANIII\\SEM2\\SD\\lab\\Project_MVC\\PerfumeShop-MVC\\PerfumeShopGermanLanguage.json");
            }
        } catch (Exception e) {
            System.out.println("Exception while reading: " + e.getMessage() + " at " + Arrays.toString(e.getStackTrace()));
        }
    }

    public Language getLanguage() {
        return language;
    }
}
