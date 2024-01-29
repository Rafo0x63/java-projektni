package com.prod.hydraulicsystemsmaintenance.controllers;

import com.prod.hydraulicsystemsmaintenance.database.Database;
import com.prod.hydraulicsystemsmaintenance.entities.Reservoir;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import java.sql.Date;
import java.util.List;

public class ReservoirAddViewController {
    @FXML private TextField modelTextField;
    @FXML private TextField serialNumberTextField;
    @FXML private TextField capacityTextField;
    @FXML private DatePicker installationDatePicker;

    public void add() {
        List<Reservoir> reservoirs = Database.getAllReservoirs();

        if (modelTextField.getText().isEmpty() || serialNumberTextField.getText().isEmpty() || capacityTextField.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Model, serial number and capacity fields cannot be empty!");
            alert.show();
        } else {
            boolean reservoirExists = false;
            for (Reservoir reservoir : reservoirs) {
                if (reservoir.getSerialNumber().compareTo(serialNumberTextField.getText()) == 0) {
                    reservoirExists = true;
                }
            }

            if (!reservoirExists) {
                Reservoir reservoir = new Reservoir(modelTextField.getText(), serialNumberTextField.getText(), Integer.parseInt(capacityTextField.getText()), Date.valueOf(installationDatePicker.getValue()).toLocalDate());
                Database.insertReservoir(reservoir);

                Alert alert = new Alert(Alert.AlertType.INFORMATION, STR."Reservoir \{reservoir.toString()} has been saved to the database.");
                alert.show();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR, STR."A reservoir already exits with the serial number \{serialNumberTextField.getText()}!");
                alert.show();
            }
        }
    }
}
