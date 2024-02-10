package com.prod.hydraulicsystemsmaintenance.controllers;

import com.prod.hydraulicsystemsmaintenance.Application;
import com.prod.hydraulicsystemsmaintenance.database.Database;
import com.prod.hydraulicsystemsmaintenance.entities.User;
import com.prod.hydraulicsystemsmaintenance.entities.Valve;
import com.prod.hydraulicsystemsmaintenance.exceptions.SerialNumberConflictException;
import com.prod.hydraulicsystemsmaintenance.generics.Change;
import com.prod.hydraulicsystemsmaintenance.generics.ComponentCheck;
import com.prod.hydraulicsystemsmaintenance.utils.SerialNumber;
import com.prod.hydraulicsystemsmaintenance.utils.View;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ValveAddViewController implements Initializable {
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
            Valve valve = new Valve(modelTextField.getText(), serialNumberTextField.getText(), Integer.parseInt(flowRateTextField.getText()), Integer.parseInt(pressureTextField.getText()), installationDatePicker.getValue());
            boolean valveExists = true;
            ComponentCheck cc = new ComponentCheck(valve);
            try {
                valveExists = cc.check();
            } catch (SerialNumberConflictException e) {
                new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
                Application.logger.error(e.getMessage());
            }
            if (!valveExists) {
                Database.insertValve(valve);
                Alert alert = new Alert(Alert.AlertType.INFORMATION, STR."Valve \{valve.toString()} has been saved to the database");
                alert.show();
                Application.changes.add(new Change<User, String>(Application.currentUser, STR."\{Application.currentUser.toChangeString()} added \{valve.toChangeString()} to the database").toString());
            }
        }
    }

    public void generateSerialNumber() {
        serialNumberTextField.setText(SerialNumber.generate());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        View.serializeChanges();
    }
}
