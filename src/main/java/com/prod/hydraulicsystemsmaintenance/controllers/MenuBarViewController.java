package com.prod.hydraulicsystemsmaintenance.controllers;

import com.prod.hydraulicsystemsmaintenance.Application;
import com.prod.hydraulicsystemsmaintenance.utils.View;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;

public class MenuBarViewController {
    public void viewUsers() {
        View.change("users");
    }

    public void assignEquipment() {
        if (Application.currentUser.isAdministrator()) View.change("assign-equipment");
        else showPermissionError();
    }
    public void showPermissionError() {
        Alert alert = new Alert(Alert.AlertType.ERROR, "Only administrators can add new equipment");
        alert.show();
    }

    public void addActuators() {
        if (Application.currentUser.isAdministrator()) View.change("actuator-add");
        else showPermissionError();
    }

    public void viewActuators() {
        View.change("actuator");
    }

    public void addPumps() {
        if (Application.currentUser.isAdministrator()) View.change("pump-add");
        else showPermissionError();
    }

    public void viewPumps() {
        View.change("pump");
    }

    public void addReservoirs() {
        if (Application.currentUser.isAdministrator()) View.change("reservoir-add");
        else showPermissionError();
    }

    public void viewReservoirs() {
        View.change("reservoir");
    }

    public void addValves() {
        if (Application.currentUser.isAdministrator()) View.change("valve-add");
        else showPermissionError();
    }

    public void viewValves() {
        View.change("valve");
    }

    public void viewSystems() {View.change("system");}

    public void addSystems() {
        if (Application.currentUser.isAdministrator()) View.change("system-add");
        else showPermissionError();
    }

    public void viewRecords() { View.change("records"); }

    public void home() {
        View.change("main");
    }
}
