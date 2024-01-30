package com.prod.hydraulicsystemsmaintenance.controllers;

import com.prod.hydraulicsystemsmaintenance.Application;
import com.prod.hydraulicsystemsmaintenance.utils.View;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;

public class MenuBarViewController {
    @FXML
    public void viewUsers() {
        View.change("users");
    }

    @FXML
    public void assignEquipment() {
        if (Application.currentUser.isAdministrator()) View.change("assign-equipment");
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Only administrators can assign equipment to users!");
            alert.show();
        }
    }

    public void showPermissionError() {
        Alert alert = new Alert(Alert.AlertType.ERROR, "Only administrators can add new equipment");
        alert.show();
    }

    @FXML
    public void addActuators() {
        if (Application.currentUser.isAdministrator()) View.change("actuator-add");
        else showPermissionError();
    }

    @FXML
    public void viewActuators() {
        View.change("actuator");
    }

    @FXML
    public void addPumps() {
        if (Application.currentUser.isAdministrator()) View.change("pump-add");
        else showPermissionError();
    }

    @FXML
    public void viewPumps() {
        View.change("pump");
    }

    @FXML
    public void addReservoirs() {
        if (Application.currentUser.isAdministrator()) View.change("reservoir-add");
        else showPermissionError();
    }

    @FXML
    public void viewReservoirs() {
        View.change("reservoir");
    }

    @FXML
    public void addValves() {
        if (Application.currentUser.isAdministrator()) View.change("valve-add");
        else showPermissionError();
    }

    @FXML
    public void viewValves() {
        View.change("valve");
    }

    @FXML
    public void viewSystems() {View.change("system");}

    @FXML
    public void addSystems() { View.change("system-add"); }

    @FXML
    public void viewRecords() { View.change("records"); }

    @FXML
    public void home() {
        View.change("main");
    }
}
