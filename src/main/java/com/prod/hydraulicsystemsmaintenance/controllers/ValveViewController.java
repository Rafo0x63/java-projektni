package com.prod.hydraulicsystemsmaintenance.controllers;

import com.prod.hydraulicsystemsmaintenance.database.Database;
import com.prod.hydraulicsystemsmaintenance.entities.Valve;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class ValveViewController implements Initializable {

    @FXML private TextField modelTextField;
    @FXML private TextField serialNumberTextField;
    @FXML private TextField flowRateTextField;
    @FXML private TextField pressureTextField;
    @FXML private DatePicker installationDatePicker;
    @FXML private TableColumn<Valve, String> modelTC;
    @FXML private TableColumn<Valve, String> serialNumberTC;
    @FXML private TableColumn<Valve, String> flowRateTC;
    @FXML private TableColumn<Valve, String> pressureTC;
    @FXML private TableColumn<Valve, String> installationDateTC;
    @FXML private TableView<Valve> tableView;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        List<Valve> valves = Database.getAllValves();
        modelTC.setCellValueFactory(new PropertyValueFactory<Valve, String>("model"));
        serialNumberTC.setCellValueFactory(new PropertyValueFactory<Valve, String>("serialNumber"));
        flowRateTC.setCellValueFactory(new PropertyValueFactory<Valve, String>("flowRate"));
        pressureTC.setCellValueFactory(new PropertyValueFactory<Valve, String>("pressure"));
        installationDateTC.setCellValueFactory(new PropertyValueFactory<Valve, String>("installationDate"));

        tableView.setItems(FXCollections.observableArrayList(valves));
    }

    public void search() {
        List<Valve> filteredValves = Database.getAllValves();

        if (!modelTextField.getText().isEmpty()) {
            filteredValves = filteredValves.stream().filter(r -> r.getModel().toLowerCase().contains(modelTextField.getText().toLowerCase())).collect(Collectors.toList());
        }
        if (!serialNumberTextField.getText().isEmpty()) {
            filteredValves = filteredValves.stream().filter(r -> r.getSerialNumber().toLowerCase().contains(serialNumberTextField.getText().toLowerCase())).collect(Collectors.toList());
        }
        if (!flowRateTextField.getText().isEmpty()) {
            filteredValves = filteredValves.stream().filter(r -> r.getFlowRate().compareTo(Integer.valueOf(flowRateTextField.getText())) == 0).collect(Collectors.toList());
        }
        if (!pressureTextField.getText().isEmpty()) {
            filteredValves = filteredValves.stream().filter(r -> r.getPressure().compareTo(Integer.valueOf(pressureTextField.getText())) == 0).collect(Collectors.toList());
        }
        if (installationDatePicker.getValue() != null) {
            filteredValves = filteredValves.stream().filter(r -> r.getInstallationDate().isAfter(installationDatePicker.getValue())).collect(Collectors.toList());
        }

        tableView.setItems(FXCollections.observableArrayList(filteredValves));
    }

    public void update() {
        Valve valve = tableView.getSelectionModel().getSelectedItem();
        if (valve == null) {
            new Alert(Alert.AlertType.ERROR, "You must select an actuator to update!").show();
        } else {
            if (modelTextField.getText().isEmpty() && serialNumberTextField.getText().isEmpty() && flowRateTextField.getText().isEmpty() && pressureTextField.getText().isEmpty() && installationDatePicker.getValue() == null) {
                new Alert(Alert.AlertType.ERROR, "You must fill at least one field to update an entity!").show();
            } else {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, STR."Are you sure you want to change valve details?", ButtonType.YES, ButtonType.NO);
                alert.showAndWait();
                if (alert.getResult() == ButtonType.YES) {
                    String model = modelTextField.getText().isEmpty() ? valve.getModel() : modelTextField.getText();
                    String serialNumber = serialNumberTextField.getText().isEmpty() ? valve.getSerialNumber() : serialNumberTextField.getText();
                    Integer flowRate = flowRateTextField.getText().isEmpty() ? valve.getFlowRate() : Integer.valueOf(flowRateTextField.getText());
                    Integer pressure = pressureTextField.getText().isEmpty() ? valve.getPressure() : Integer.valueOf(pressureTextField.getText());
                    LocalDate installationDate = installationDatePicker.getValue() == null ? valve.getInstallationDate() : installationDatePicker.getValue();

                    Valve newValve = new Valve(model, serialNumber, flowRate, pressure, installationDate);

                    Database.updateValve(valve.getId(), newValve);
                    new Alert(Alert.AlertType.INFORMATION, "The valve has been updated successfully").show();
                    System.out.println("valve updated");

                    tableView.setItems(FXCollections.observableArrayList(Database.getAllValves()));
                }
            }
        }
    }

    public void delete() {
        Valve valve = tableView.getSelectionModel().getSelectedItem();
        if(valve.isInstalledInSystem()) {
            new Alert(Alert.AlertType.ERROR, "The actuator cannot be deleted from the database because it is installed in a system, remove it from the system to delete it from the database!").show();
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, STR."Are you sure you want to delete Valve\{valve.toString()}?", ButtonType.YES, ButtonType.NO);
            alert.showAndWait();
            if (alert.getResult() == ButtonType.YES) {
                Database.deleteValve(valve.getId());
                alert = new Alert(Alert.AlertType.INFORMATION, "The valve has been deleted.");
                alert.show();
            }
            tableView.setItems(FXCollections.observableArrayList(Database.getAllValves()));
        }
    }
}
