package com.prod.hydraulicsystemsmaintenance.controllers;

import com.prod.hydraulicsystemsmaintenance.database.Database;
import com.prod.hydraulicsystemsmaintenance.entities.Actuator;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

public class ActuatorViewController implements Initializable {
    @FXML private TextField modelTextField;
    @FXML private TextField serialNumberTextField;
    @FXML private TextField forceTextField;
    @FXML private DatePicker installationDatePicker;
    @FXML private TableView<Actuator> tableView;
    @FXML private TableColumn<Actuator, String> modelTableColumn;
    @FXML private TableColumn<Actuator, String> serialNumberTableColumn;
    @FXML private TableColumn<Actuator, String> forceTableColumn;
    @FXML private TableColumn<Actuator, String> installationDateTableColumn;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        List<Actuator> actuators = Database.getAllActuators();
        modelTableColumn.setCellValueFactory(new PropertyValueFactory<Actuator, String>("model"));
        serialNumberTableColumn.setCellValueFactory(new PropertyValueFactory<Actuator, String>("serialNumber"));
        forceTableColumn.setCellValueFactory(new PropertyValueFactory<Actuator, String>("force"));
        installationDateTableColumn.setCellValueFactory(new PropertyValueFactory<Actuator, String>("installationDate"));

        tableView.setItems(FXCollections.observableArrayList(actuators));
    }

    public void search() {
        List<Actuator> filteredActuators = Database.getAllActuators();

        if (!modelTextField.getText().isEmpty()) {
            filteredActuators = filteredActuators.stream().filter(r -> r.getModel().toLowerCase().contains(modelTextField.getText().toLowerCase())).collect(Collectors.toList());
        }
        if (!serialNumberTextField.getText().isEmpty()) {
            filteredActuators = filteredActuators.stream().filter(r -> r.getSerialNumber().toLowerCase().contains(serialNumberTextField.getText().toLowerCase())).collect(Collectors.toList());
        }
        if (!forceTextField.getText().isEmpty()) {
            filteredActuators = filteredActuators.stream().filter(r -> r.getForce().compareTo(Long.valueOf(forceTextField.getText())) == 0).collect(Collectors.toList());
        }
        if (installationDatePicker.getValue() != null) {
            filteredActuators = filteredActuators.stream().filter(r -> r.getInstallationDate().isAfter(installationDatePicker.getValue())).collect(Collectors.toList());
        }

        tableView.setItems(FXCollections.observableArrayList(filteredActuators));
    }
}
