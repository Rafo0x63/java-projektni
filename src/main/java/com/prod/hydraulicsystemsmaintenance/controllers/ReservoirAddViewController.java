package com.prod.hydraulicsystemsmaintenance.controllers;

import com.prod.hydraulicsystemsmaintenance.Application;
import com.prod.hydraulicsystemsmaintenance.database.Database;
import com.prod.hydraulicsystemsmaintenance.entities.Reservoir;
import com.prod.hydraulicsystemsmaintenance.entities.User;
import com.prod.hydraulicsystemsmaintenance.exceptions.SerialNumberConflictException;
import com.prod.hydraulicsystemsmaintenance.generics.Change;
import com.prod.hydraulicsystemsmaintenance.generics.ComponentCheck;
import com.prod.hydraulicsystemsmaintenance.utils.SerialNumber;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import org.slf4j.Logger;

import java.sql.Date;
import java.util.List;

public class ReservoirAddViewController {
    @FXML private TextField modelTextField;
    @FXML private TextField serialNumberTextField;
    @FXML private TextField capacityTextField;
    @FXML private DatePicker installationDatePicker;
    Logger logger = Application.logger;

    public void add() {
        List<Reservoir> reservoirs = Database.getAllReservoirs();

        if (modelTextField.getText().isEmpty() || serialNumberTextField.getText().isEmpty() || capacityTextField.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Model, serial number and capacity fields cannot be empty!");
            alert.show();
        } else {
            Reservoir reservoir = new Reservoir(modelTextField.getText(), serialNumberTextField.getText(), Integer.parseInt(capacityTextField.getText()), Date.valueOf(installationDatePicker.getValue()).toLocalDate());
            boolean reservoirExists = true;
            ComponentCheck cc = new ComponentCheck(reservoir);
            try {
                reservoirExists = cc.check();
            } catch (SerialNumberConflictException e) {
                new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
                Application.logger.error(e.getMessage());
            }

            if (!reservoirExists) {
                Database.insertReservoir(reservoir);

                Alert alert = new Alert(Alert.AlertType.INFORMATION, STR."Reservoir \{reservoir.toString()} has been saved to the database.");
                alert.show();
                logger.info("reservoir added");
                Application.changes.add(new Change<User, String>(Application.currentUser, STR."\{Application.currentUser} added \{reservoirs} to the database").toString());
            }
        }
    }

    public void generateSerialNumber() {
        serialNumberTextField.setText(SerialNumber.generate());
    }
}
