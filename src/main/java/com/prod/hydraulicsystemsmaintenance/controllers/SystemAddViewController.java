package com.prod.hydraulicsystemsmaintenance.controllers;

import com.prod.hydraulicsystemsmaintenance.Application;
import com.prod.hydraulicsystemsmaintenance.database.Database;
import com.prod.hydraulicsystemsmaintenance.entities.*;
import com.prod.hydraulicsystemsmaintenance.generics.Change;
import com.prod.hydraulicsystemsmaintenance.utils.View;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class SystemAddViewController implements Initializable {
    @FXML private TextField nameTextField;
    @FXML private ComboBox<Actuator> actuatorComboBox;
    @FXML private ComboBox<Pump> pumpComboBox;
    @FXML private ComboBox<Reservoir> reservoirComboBox;
    @FXML private ComboBox<Valve> valveComboBox;
    @FXML private ComboBox<Administrator> administratorComboBox;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        List<Actuator> actuators = Database.getAllActuators().stream().filter(actuator -> !actuator.isInstalledInSystem()).toList();
        List<Pump> pumps = Database.getAllPumps().stream().filter(pump -> !pump.isInstalledInSystem()).toList();
        List<Reservoir> reservoirs = Database.getAllReservoirs().stream().filter(reservoir -> !reservoir.isInstalledInSystem()).toList();
        List<Valve> valves = Database.getAllValves().stream().filter(valve -> !valve.isInstalledInSystem()).toList();
        List<User> users =  Database.getAllUsers().stream().filter(user -> user.getAdministrator() == 1).toList();
        List<Administrator> administrators = new ArrayList<>();
        for (User user : users) {
            administrators.add((Administrator) user);
        }
        administrators = administrators.stream().filter(administrator -> !administrator.isAdministratingASystem()).toList();
        actuatorComboBox.setItems(FXCollections.observableArrayList(actuators));
        pumpComboBox.setItems(FXCollections.observableArrayList(pumps));
        reservoirComboBox.setItems(FXCollections.observableArrayList(reservoirs));
        valveComboBox.setItems(FXCollections.observableArrayList(valves));
        administratorComboBox.setItems(FXCollections.observableArrayList(administrators));
    }

    public void add() {
        if (nameTextField.getText().isEmpty() || actuatorComboBox.getSelectionModel().isEmpty() || pumpComboBox.getSelectionModel().isEmpty() || reservoirComboBox.getSelectionModel().isEmpty() || valveComboBox.getSelectionModel().isEmpty() || administratorComboBox.getSelectionModel().isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "All fields must be filled or selected!").show();
        } else {
            String name = nameTextField.getText();
            Actuator actuator = actuatorComboBox.getSelectionModel().getSelectedItem();
            Pump pump = pumpComboBox.getSelectionModel().getSelectedItem();
            Reservoir reservoir = reservoirComboBox.getSelectionModel().getSelectedItem();
            Valve valve = valveComboBox.getSelectionModel().getSelectedItem();
            Administrator administrator = administratorComboBox.getSelectionModel().getSelectedItem();
            HydraulicSystem system = new HydraulicSystem(name, actuator, pump, reservoir, valve, administrator);

            Database.insertSystem(system);

            new Alert(Alert.AlertType.INFORMATION, "The system has been saved to the database").show();
            System.out.println("system saved");
            Application.changes.add(new Change<User, String>(Application.currentUser, STR."\{Application.currentUser.toChangeString()} added \{system} to the database").toString());

            View.change("system-add");

        }
    }
}
