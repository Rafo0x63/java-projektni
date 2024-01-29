package com.prod.hydraulicsystemsmaintenance.controllers;

import com.prod.hydraulicsystemsmaintenance.database.Database;
import com.prod.hydraulicsystemsmaintenance.entities.Actuator;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

public class ActuatorAddViewController {
    @FXML private TextField modelTextField;
    @FXML private TextField serialNumberTextField;
    @FXML private TextField forceTextField;
    @FXML private DatePicker installationDatePicker;

    public void add() {
        List<Actuator> actuators = Database.getAllActuators();

        if (modelTextField.getText().isEmpty() || serialNumberTextField.getText().isEmpty() || forceTextField.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Model, serial number and force fields cannot be empty!");
            alert.show();
        } else {
            boolean actuatorExists = false;
            for (Actuator actuator : actuators) {
                if (actuator.getSerialNumber().compareTo(serialNumberTextField.getText()) == 0) {
                    actuatorExists = true;
                }
            }

            if (!actuatorExists) {
                Actuator actuator = new Actuator(modelTextField.getText(), serialNumberTextField.getText(), Integer.parseInt(forceTextField.getText()), Date.valueOf(installationDatePicker.getValue()).toLocalDate());
                Database.insertActuator(actuator);
                new Alert(Alert.AlertType.INFORMATION, STR."Actuator \{actuator} has been saved to the database.").show();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR, STR."An actuator already exits with the serial number \{serialNumberTextField.getText()}!");
                alert.show();
            }
        }
    }
}
