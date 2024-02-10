package com.prod.hydraulicsystemsmaintenance.utils;

import com.prod.hydraulicsystemsmaintenance.Application;
import com.prod.hydraulicsystemsmaintenance.threads.SerializeChange;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class View {
    public static void change(String view) {
        int w = 1200;
        int h = 720;
        if (view.matches("login") || view.matches("register")) {
            w = 600;
            h = 400;
        }
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource(STR."\{view}-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), w, h);
            Application.mainStage.setScene(scene);
            scene.getStylesheets().add(Application.class.getResource("style.css").toExternalForm());
            Application.mainStage.setTitle("Hydraulic systems maintenance");
            Application.mainStage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Application.logger.info(STR."view changed to \{view}-view.fxml");
    }

    public static void serializeChanges() {
        SerializeChange sc = new SerializeChange();
        ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.execute(sc);
        executorService.close();
    }
}
