package com.prod.hydraulicsystemsmaintenance.utils;

import com.prod.hydraulicsystemsmaintenance.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

import java.io.IOException;

public class View {
    public static void change(String view) {
        int w = 1200;
        int h = 720;
        if (view.matches("login") || view.matches("register")) {
            w = 600;
            h = 400;
        }
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource(view + "-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), w, h);
            Application.mainStage.setScene(scene);
            Application.mainStage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
