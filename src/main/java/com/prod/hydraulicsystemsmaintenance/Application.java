package com.prod.hydraulicsystemsmaintenance;

import com.prod.hydraulicsystemsmaintenance.database.Database;
import com.prod.hydraulicsystemsmaintenance.entities.CurrentUser;
import com.prod.hydraulicsystemsmaintenance.entities.User;
import com.prod.hydraulicsystemsmaintenance.exceptions.UserDoesntExistException;
import com.prod.hydraulicsystemsmaintenance.utils.View;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.stage.Stage;

import java.io.IOException;

public class Application extends javafx.application.Application {

    public static Stage mainStage;
    public static CurrentUser currentUser;

    private boolean autoLogin = true;


    @Override
    public void start(Stage stage) throws IOException {
        mainStage = stage;
        if (autoLogin) {
            try {
                currentUser = new CurrentUser(Database.getUsersByCriteria(new User("rafo")).getFirst());
                View.change("main");
            } catch (UserDoesntExistException e) {
                throw new RuntimeException(e);
            }
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

/*
* Aplikacija za praćenje stanja i održavanje hidrauličkih sustava
* Napravite sustav koji omogućuje tvrtkama koje se bave hidrauličkim sustavima praćenje stanja opreme, planiranje servisa i praćenje financijske učinkovitosti sustava.
*/