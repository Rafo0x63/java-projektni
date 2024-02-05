package com.prod.hydraulicsystemsmaintenance.controllers;

import com.prod.hydraulicsystemsmaintenance.Application;
import com.prod.hydraulicsystemsmaintenance.database.Database;
import com.prod.hydraulicsystemsmaintenance.entities.Actuator;
import com.prod.hydraulicsystemsmaintenance.entities.User;
import com.prod.hydraulicsystemsmaintenance.exceptions.SerialNumberConflictException;
import com.prod.hydraulicsystemsmaintenance.generics.Change;
import com.prod.hydraulicsystemsmaintenance.generics.ComponentCheck;
import com.prod.hydraulicsystemsmaintenance.utils.SerialNumber;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import org.slf4j.Logger;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

public class ActuatorAddViewController {
    Logger logger = Application.logger;
    @FXML private TextField modelTextField;
    @FXML private TextField serialNumberTextField;
    @FXML private TextField forceTextField;
    @FXML private DatePicker installationDatePicker;

    public void add() {
        if (modelTextField.getText().isEmpty() || serialNumberTextField.getText().isEmpty() || forceTextField.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Model, serial number and force fields cannot be empty!");
            alert.show();
            logger.error("not null fields empty while adding actuator");
        } else {
            Actuator actuator = new Actuator(modelTextField.getText(), serialNumberTextField.getText(), Integer.parseInt(forceTextField.getText()), Date.valueOf(installationDatePicker.getValue()).toLocalDate());
            boolean actuatorExists = true;
            ComponentCheck cc = new ComponentCheck(actuator);
            try {
                actuatorExists = cc.check();
            } catch (SerialNumberConflictException e) {
                new Alert(Alert.AlertType.ERROR, e.getMessage());
                Application.logger.error(e.getMessage());
            }


            if (!actuatorExists) {
                Database.insertActuator(actuator);
                new Alert(Alert.AlertType.INFORMATION, STR."Actuator \{actuator} has been saved to the database.").show();
                logger.info(STR."added actuator \{actuator} to database");
                Application.changes.add(new Change<User, String>(Application.currentUser, STR."\{Application.currentUser.toChangeString()} added \{actuator.toChangeString()} to the database").toString());
                System.out.println(Application.changes.getFirst());
            }
        }
    }

    public void generateSerialNumber() {
        serialNumberTextField.setText(SerialNumber.generate());
    }
}
