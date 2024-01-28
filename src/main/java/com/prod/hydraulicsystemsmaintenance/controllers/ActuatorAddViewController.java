package com.prod.hydraulicsystemsmaintenance.controllers;

import com.prod.hydraulicsystemsmaintenance.database.Database;
import com.prod.hydraulicsystemsmaintenance.entities.Actuator;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import java.sql.Date;
import java.time.LocalDate;

public class ActuatorAddViewController {
    @FXML private TextField modelTextField;
    @FXML private TextField serialNumberTextField;
    @FXML private TextField forceTextField;
    @FXML private DatePicker installationDatePicker;

    public void add() {
        Database.insertActuator(new Actuator(modelTextField.getText(), serialNumberTextField.getText(), Long.parseLong(forceTextField.getText()), Date.valueOf(installationDatePicker.getValue()).toLocalDate()));
    }
}
