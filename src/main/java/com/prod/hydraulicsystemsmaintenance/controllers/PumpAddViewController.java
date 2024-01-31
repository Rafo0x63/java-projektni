package com.prod.hydraulicsystemsmaintenance.controllers;

import com.prod.hydraulicsystemsmaintenance.Application;
import com.prod.hydraulicsystemsmaintenance.database.Database;
import com.prod.hydraulicsystemsmaintenance.entities.Pump;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import org.slf4j.Logger;
import java.util.List;

public class PumpAddViewController {
    Logger logger = Application.logger;
    @FXML private TextField modelTextField;
    @FXML private TextField serialNumberTextField;
    @FXML private TextField flowRateTextField;
    @FXML private TextField pressureTextField;
    @FXML private DatePicker installationDatePicker;

    public void add() {
        if (modelTextField.getText().isEmpty() || serialNumberTextField.getText().isEmpty() || flowRateTextField.getText().isEmpty() || pressureTextField.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Model, serial number, flow rate and pressure fields cannot be empty!");
            alert.show();
            logger.error("not null fields empty in pump add attempt");
        } else {
            List<Pump> pumps = Database.getAllPumps();
            boolean pumpExists = false;
            for (Pump pump : pumps) {
                if (serialNumberTextField.getText().compareTo(pump.getSerialNumber()) == 0) {
                    pumpExists = true;
                }
            }

            if (!pumpExists) {
                Pump pump = new Pump(modelTextField.getText(), serialNumberTextField.getText(), Integer.parseInt(flowRateTextField.getText()), Integer.parseInt(pressureTextField.getText()), installationDatePicker.getValue());
                Database.insertPump(pump);
                new Alert(Alert.AlertType.INFORMATION, "The pump has been saved to the database.").show();
                logger.info(STR."\{pump} added");
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR, STR."A pump with the serial number \{serialNumberTextField.getText()} already exists!");
                alert.show();
                logger.error("pump serial number conflict");
            }
        }
    }
}
