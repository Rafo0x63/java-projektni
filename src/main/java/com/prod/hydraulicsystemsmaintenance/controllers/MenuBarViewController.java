package com.prod.hydraulicsystemsmaintenance.controllers;

import com.prod.hydraulicsystemsmaintenance.Application;
import com.prod.hydraulicsystemsmaintenance.utils.View;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;

import java.util.Currency;

public class MenuBarViewController {
    @FXML
    public void viewUsers() {
        View.change("users");
    }

    @FXML
    public void addUsers() {
        if (Application.currentUser.isAdministrator()) View.change("users-add");
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Only administrators can add new users!");
            alert.show();
        }
    }

    @FXML
    public void viewEquipment() {
        View.change("equipment");
    }

    @FXML
    public void addEquipment() {
        if (Application.currentUser.isAdministrator()) View.change("equipment-add");
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Only administrators can add new equipment!");
            alert.show();
        }
    }

    @FXML
    public void home() {
        View.change("main");
    }
}
