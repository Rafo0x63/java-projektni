package com.prod.hydraulicsystemsmaintenance.controllers;

import com.prod.hydraulicsystemsmaintenance.database.Database;
import com.prod.hydraulicsystemsmaintenance.entities.Valve;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import java.util.List;

public class ValveAddViewController {
    @FXML private TextField modelTextField;
    @FXML private TextField serialNumberTextField;
    @FXML private TextField flowRateTextField;
    @FXML private TextField pressureTextField;
    @FXML private DatePicker installationDatePicker;

    public void add() {
        if (modelTextField.getText().isEmpty() || serialNumberTextField.getText().isEmpty() || flowRateTextField.getText().isEmpty() || pressureTextField.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Model, serial number, flow rate and pressure fields cannot be empty!");
            alert.show();
        } else {
            List<Valve> valves = Database.getAllValves();
            boolean valveExists = false;
            for (Valve valve : valves) {
                if (serialNumberTextField.getText().compareTo(valve.getSerialNumber()) == 0) {
                    valveExists = true;
                }
            }

            if (!valveExists) {
                Valve valve = new Valve(modelTextField.getText(), serialNumberTextField.getText(), Long.parseLong(flowRateTextField.getText()), Long.parseLong(pressureTextField.getText()), installationDatePicker.getValue());
                Database.insertValve(valve);
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, STR."Valve \{valve.toString()} has been saved to the database");
                alert.show();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR, STR."A valve with the serial number \{serialNumberTextField.getText()} already exists!");
                alert.show();
            }
        }
    }
}
