package com.example.jacques.testapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.google.cloud.translate.*;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.Translate.TranslateOption;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;


public class MainActivity extends AppCompatActivity {

    private static final String API_KEY = "AIzaSyCVktAWTsOabuB_YAZdLznuEtyiZnbUlss";

    public static void main(String... args) throws Exception {
        // Instantiates a client
        TranslateOptions options = TranslateOptions.newBuilder()
                .setApiKey(API_KEY)
                .build();

        Translate translate = options.getService();

        // The text to translate
        String text = "Hello, world!";


        // Translates some text into Russian
        Translation translation =
                translate.translate(
                        text,
                        Translate.TranslateOption.sourceLanguage("en"),
                        Translate.TranslateOption.targetLanguage("ru"));


        System.out.printf("Text: %s%n", text);
        System.out.printf("Translation: %s%n", translation.getTranslatedText());
    }
}

