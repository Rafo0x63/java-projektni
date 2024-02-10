package com.prod.hydraulicsystemsmaintenance.controllers;

import com.prod.hydraulicsystemsmaintenance.Application;
import com.prod.hydraulicsystemsmaintenance.entities.CurrentUser;
import com.prod.hydraulicsystemsmaintenance.threads.DeserializeChange;
import com.prod.hydraulicsystemsmaintenance.utils.View;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainViewController implements Initializable {

    CurrentUser cu = Application.currentUser;
    @FXML private Label userInfo;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        StringBuilder currentUserInfo = new StringBuilder("Logged in as ").append(cu.getUsername());
        if (cu.getAdministrator() == 1) currentUserInfo.append(" (Administrator).");
        else currentUserInfo.append(" (Technician).");
        userInfo.setText(currentUserInfo.toString());

        DeserializeChange dc = new DeserializeChange();
        ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.execute(dc);
        executorService.close();

    }

    @FXML
    public void logout() {
        Application.logger.info(STR."\{Application.currentUser} logged out");
        Application.currentUser = null;
        View.change("login");
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Logged out.");
        alert.show();

    }
}