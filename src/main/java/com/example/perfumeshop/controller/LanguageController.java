package com.example.perfumeshop.controller;

import com.example.perfumeshop.model.Language;
import com.example.perfumeshop.model.persistence.files.LanguagePersistence;

import java.util.Arrays;
import java.util.Observable;
import java.util.Observer;

public class LanguageController {
    private final LanguagePersistence languagePersistence = new LanguagePersistence();
    private Language language;

    public void read(String arg) {
        try {
            if(arg.equals("English")) {
                language = languagePersistence.read("EnglishLanguage.json");
            } else if(arg.equals("Romanian")) {
                language = languagePersistence.read("RomanianLanguage.json");
            } else {
                language = languagePersistence.read("GermanLanguage.json");
            }
        } catch (Exception e) {
            System.out.println("Exception while reading: " + e.getMessage() + " at " + Arrays.toString(e.getStackTrace()));
        }
    }

    public Language getLanguage() {
        return language;
    }
}
