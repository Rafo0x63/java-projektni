package com.prod.hydraulicsystemsmaintenance.controllers;

import com.prod.hydraulicsystemsmaintenance.database.Database;
import com.prod.hydraulicsystemsmaintenance.entities.Actuator;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.time.LocalDate;
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
            filteredActuators = filteredActuators.stream().filter(r -> r.getForce().compareTo(Integer.valueOf(forceTextField.getText())) == 0).collect(Collectors.toList());
        }
        if (installationDatePicker.getValue() != null) {
            filteredActuators = filteredActuators.stream().filter(r -> r.getInstallationDate().isAfter(installationDatePicker.getValue())).collect(Collectors.toList());
        }

        tableView.setItems(FXCollections.observableArrayList(filteredActuators));
    }

    public void update() {
        Actuator actuator = tableView.getSelectionModel().getSelectedItem();
        if (actuator == null) {
            new Alert(Alert.AlertType.ERROR, "You must select an actuator to update!").show();
        } else {
            if (modelTextField.getText().isEmpty() && serialNumberTextField.getText().isEmpty() && forceTextField.getText().isEmpty() && installationDatePicker.getValue() == null) {
                new Alert(Alert.AlertType.ERROR, "You must fill at least one field to update an entity!").show();
            } else {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, STR."Are you sure you want to change actuator details?", ButtonType.YES, ButtonType.NO);
                alert.showAndWait();
                if (alert.getResult() == ButtonType.YES) {
                    String model = modelTextField.getText().isEmpty() ? actuator.getModel() : modelTextField.getText();
                    String serialNumber = serialNumberTextField.getText().isEmpty() ? actuator.getSerialNumber() : serialNumberTextField.getText();
                    Integer force = forceTextField.getText().isEmpty() ? actuator.getForce() : Integer.valueOf(forceTextField.getText());
                    LocalDate installationDate = installationDatePicker.getValue() == null ? actuator.getInstallationDate() : installationDatePicker.getValue();

                    Actuator newActuator = new Actuator(model, serialNumber, force, installationDate);
                    System.out.println(newActuator);
                    Database.updateActuator(actuator.getId(), newActuator);
                    System.out.println("actuator updated");

                    tableView.setItems(FXCollections.observableArrayList(Database.getAllActuators()));
                }
            }
        }

    }

    public void delete() {
        Actuator actuator = tableView.getSelectionModel().getSelectedItem();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, STR."Are you sure you want to delete Actuator\{actuator.toString()}?", ButtonType.YES, ButtonType.NO);
        alert.showAndWait();
        if (alert.getResult() == ButtonType.YES) {
            Database.deleteActuator(actuator.getId());
            alert = new Alert(Alert.AlertType.INFORMATION, "The actuator has been deleted.");
            alert.show();
        }
        tableView.setItems(FXCollections.observableArrayList(Database.getAllActuators()));
    }
}
