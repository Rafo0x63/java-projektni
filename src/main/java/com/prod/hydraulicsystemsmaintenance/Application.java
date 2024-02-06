package com.prod.hydraulicsystemsmaintenance;

import com.prod.hydraulicsystemsmaintenance.controllers.ChangesViewController;
import com.prod.hydraulicsystemsmaintenance.database.Database;
import com.prod.hydraulicsystemsmaintenance.entities.CurrentUser;
import com.prod.hydraulicsystemsmaintenance.entities.User;
import com.prod.hydraulicsystemsmaintenance.exceptions.UserDoesntExistException;
import com.prod.hydraulicsystemsmaintenance.utils.FileUtils;
import com.prod.hydraulicsystemsmaintenance.utils.View;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Application extends javafx.application.Application {

    public static Stage mainStage;
    public static CurrentUser currentUser;
    public static List<String> changes = new ArrayList<>();
    public static Logger logger = LoggerFactory.getLogger(Application.class);
    public boolean autoLogin = true;
    public static boolean dbLogin = false;


    @Override
    public void start(Stage stage) throws IOException {
        logger.info("application started");
        FileUtils.readAllUsers();
        mainStage = stage;
        if (!Database.getAllUsers().isEmpty() && autoLogin) {
            currentUser = new CurrentUser(Database.getAllUsers().stream().filter(u -> u.getUsername().compareTo("rafo") == 0).toList().getFirst());
            View.change("main");
        } else {


        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("login-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        stage.setTitle("Hydraulic Systems maintenance");
        stage.setScene(scene);
        stage.show();
        }
    }

    public static void main(String[] args) {
        launch();
    }
}