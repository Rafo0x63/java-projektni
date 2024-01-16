package com.prod.hydraulicsystemsmaintenance;

import com.prod.hydraulicsystemsmaintenance.entities.CurrentUser;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Application extends javafx.application.Application {

    public static Stage mainStage;
    public static CurrentUser currentUser;


    @Override
    public void start(Stage stage) throws IOException {
        mainStage = stage;
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("login-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        stage.setTitle("Hydraulics Systems maintenance");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}

/*
* Aplikacija za praćenje stanja i održavanje hidrauličkih sustava
* Napravite sustav koji omogućuje tvrtkama koje se bave hidrauličkim sustavima praćenje stanja opreme, planiranje servisa i praćenje financijske učinkovitosti sustava.

 * */