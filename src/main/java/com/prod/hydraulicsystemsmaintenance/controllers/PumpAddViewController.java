package com.prod.hydraulicsystemsmaintenance.controllers;

import com.prod.hydraulicsystemsmaintenance.database.Database;
import com.prod.hydraulicsystemsmaintenance.entities.Pump;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import java.util.List;

public class PumpAddViewController {
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
            List<Pump> pumps = Database.getAllPumps();
            boolean pumpExists = false;
            for (Pump pump : pumps) {
                if (serialNumberTextField.getText().compareTo(pump.getSerialNumber()) == 0) {
                    pumpExists = true;
                }
            }

            if (!pumpExists) {
                Database.insertPump(new Pump(modelTextField.getText(), serialNumberTextField.getText(), Integer.parseInt(flowRateTextField.getText()), Integer.parseInt(pressureTextField.getText()), installationDatePicker.getValue()));
                new Alert(Alert.AlertType.INFORMATION, "The pump has been saved to the database.").show();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR, STR."A pump with the serial number \{serialNumberTextField.getText()} already exists!");
                alert.show();
            }
        }
    }
}
