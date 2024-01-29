package com.prod.hydraulicsystemsmaintenance.controllers;

import com.prod.hydraulicsystemsmaintenance.database.Database;
import com.prod.hydraulicsystemsmaintenance.entities.Pump;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class PumpViewController implements Initializable {
    @FXML private TextField modelTextField;
    @FXML private TextField serialNumberTextField;
    @FXML private TextField flowRateTextField;
    @FXML private TextField pressureTextField;
    @FXML private DatePicker installationDatePicker;
    @FXML private TableColumn<Pump, String> modelTC;
    @FXML private TableColumn<Pump, String> serialNumberTC;
    @FXML private TableColumn<Pump, String> flowRateTC;
    @FXML private TableColumn<Pump, String> pressureTC;
    @FXML private TableColumn<Pump, String> installationDateTC;
    @FXML private TableView<Pump> tableView;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        List<Pump> pumps = Database.getAllPumps();
        modelTC.setCellValueFactory(new PropertyValueFactory<Pump, String>("model"));
        serialNumberTC.setCellValueFactory(new PropertyValueFactory<Pump, String>("serialNumber"));
        flowRateTC.setCellValueFactory(new PropertyValueFactory<Pump, String>("flowRate"));
        pressureTC.setCellValueFactory(new PropertyValueFactory<Pump, String>("pressure"));
        installationDateTC.setCellValueFactory(new PropertyValueFactory<Pump, String>("installationDate"));

        tableView.setItems(FXCollections.observableArrayList(pumps));
    }

    public void search() {
        List<Pump> filteredPumps = Database.getAllPumps();

        if (!modelTextField.getText().isEmpty()) {
            filteredPumps = filteredPumps.stream().filter(r -> r.getModel().toLowerCase().contains(modelTextField.getText().toLowerCase())).collect(Collectors.toList());
        }
        if (!serialNumberTextField.getText().isEmpty()) {
            filteredPumps = filteredPumps.stream().filter(r -> r.getSerialNumber().toLowerCase().contains(serialNumberTextField.getText().toLowerCase())).collect(Collectors.toList());
        }
        if (!flowRateTextField.getText().isEmpty()) {
            filteredPumps = filteredPumps.stream().filter(r -> r.getFlowRate().compareTo(Integer.valueOf(flowRateTextField.getText())) == 0).collect(Collectors.toList());
        }
        if (!pressureTextField.getText().isEmpty()) {
            filteredPumps = filteredPumps.stream().filter(r -> r.getPressure().compareTo(Integer.valueOf(pressureTextField.getText())) == 0).collect(Collectors.toList());
        }
        if (installationDatePicker.getValue() != null) {
            filteredPumps = filteredPumps.stream().filter(r -> r.getInstallationDate().isAfter(installationDatePicker.getValue())).collect(Collectors.toList());
        }

        tableView.setItems(FXCollections.observableArrayList(filteredPumps));
    }
    public void edit() {}
    public void delete() {
        Pump pump = tableView.getSelectionModel().getSelectedItem();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, STR."Are you sure you want to delete Pump\{pump.toString()}?", ButtonType.YES, ButtonType.NO);
        alert.showAndWait();
        if (alert.getResult() == ButtonType.YES) {
            Database.deletePump(pump.getId());
            alert = new Alert(Alert.AlertType.INFORMATION, "The pump has been deleted.");
            alert.show();
        }
        tableView.setItems(FXCollections.observableArrayList(Database.getAllPumps()));
    }
}
