package com.prod.hydraulicsystemsmaintenance.controllers;

import com.prod.hydraulicsystemsmaintenance.Application;
import com.prod.hydraulicsystemsmaintenance.database.Database;
import com.prod.hydraulicsystemsmaintenance.entities.Reservoir;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class ReservoirViewController implements Initializable {
    @FXML private TextField modelTextField;
    @FXML private TextField serialNumberTextField;
    @FXML private TextField capacityTextField;
    @FXML private DatePicker installationDatePicker;
    @FXML private TableColumn<Reservoir, String> modelTC;
    @FXML private TableColumn<Reservoir, String> serialNumberTC;
    @FXML private TableColumn<Reservoir, String> capacityTC;
    @FXML private TableColumn<Reservoir, String> installationDateTC;
    @FXML private TableView<Reservoir> tableView;
    @FXML private Button updateButton;
    @FXML private Button deleteButton;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (!Application.currentUser.isAdministrator()) {
            updateButton.setVisible(false);
            deleteButton.setVisible(false);
        }
        List<Reservoir> reservoirs = Database.getAllReservoirs();

        modelTC.setCellValueFactory(new PropertyValueFactory<Reservoir, String>("model"));
        serialNumberTC.setCellValueFactory(new PropertyValueFactory<Reservoir, String>("serialNumber"));
        capacityTC.setCellValueFactory(new PropertyValueFactory<Reservoir, String>("capacity"));
        installationDateTC.setCellValueFactory(new PropertyValueFactory<Reservoir, String>("installationDate"));

        tableView.setItems(FXCollections.observableArrayList(reservoirs));
    }

    public void search() {
        List<Reservoir> filteredReservoirs = Database.getAllReservoirs();

        if (!modelTextField.getText().isEmpty()) {
            filteredReservoirs = filteredReservoirs.stream().filter(r -> r.getModel().toLowerCase().contains(modelTextField.getText().toLowerCase())).collect(Collectors.toList());
        }
        if (!serialNumberTextField.getText().isEmpty()) {
            filteredReservoirs = filteredReservoirs.stream().filter(r -> r.getSerialNumber().toLowerCase().contains(serialNumberTextField.getText().toLowerCase())).collect(Collectors.toList());
        }
        if (!capacityTextField.getText().isEmpty()) {
            filteredReservoirs = filteredReservoirs.stream().filter(r -> r.getCapacity().compareTo(Integer.valueOf(capacityTextField.getText())) == 0).collect(Collectors.toList());
        }
        if (installationDatePicker.getValue() != null) {
            filteredReservoirs = filteredReservoirs.stream().filter(r -> r.getInstallationDate().isAfter(installationDatePicker.getValue())).collect(Collectors.toList());
        }

        tableView.setItems(FXCollections.observableArrayList(filteredReservoirs));
    }

    public void update() {
        Reservoir reservoir = tableView.getSelectionModel().getSelectedItem();
        if (reservoir == null) {
            new Alert(Alert.AlertType.ERROR, "You must select an reservoir to update!").show();
        } else {
            if (modelTextField.getText().isEmpty() && serialNumberTextField.getText().isEmpty() && capacityTextField.getText().isEmpty() && installationDatePicker.getValue() == null) {
                new Alert(Alert.AlertType.ERROR, "You must fill at least one field to update an entity!").show();
            } else {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to change reservoir details?", ButtonType.YES, ButtonType.NO);
                alert.showAndWait();
                if (alert.getResult() == ButtonType.YES) {
                    String model = modelTextField.getText().isEmpty() ? reservoir.getModel() : modelTextField.getText();
                    String serialNumber = serialNumberTextField.getText().isEmpty() ? reservoir.getSerialNumber() : serialNumberTextField.getText();
                    Integer capacity = capacityTextField.getText().isEmpty() ? reservoir.getCapacity() : Integer.valueOf(capacityTextField.getText());
                    LocalDate installationDate = installationDatePicker.getValue() == null ? reservoir.getInstallationDate() : installationDatePicker.getValue();

                    Reservoir newReservoir = new Reservoir(model, serialNumber, capacity, installationDate);

                    Database.updateReservoir(reservoir.getId(), newReservoir);
                    new Alert(Alert.AlertType.INFORMATION, "The reservoir has been updated.").show();
                    System.out.println("reservoir updated");

                    tableView.setItems(FXCollections.observableArrayList(Database.getAllReservoirs()));
                }
            }
        }

    }

    public void delete() {
        Reservoir reservoir = tableView.getSelectionModel().getSelectedItem();
        if (reservoir.isInstalledInSystem()) {
            new Alert(Alert.AlertType.ERROR, "The reservoir cannot be deleted from the database because it is installed in a system, remove it from the system to delete it from the database!").show();
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, STR."Are you sure you want to delete Reservoir\{reservoir.toString()}?", ButtonType.YES, ButtonType.NO);
            alert.showAndWait();
            if (alert.getResult() == ButtonType.YES) {
                Database.deleteReservoir(reservoir.getId());
                alert = new Alert(Alert.AlertType.INFORMATION, "The reservoir has been deleted.");
                alert.show();
            }
            tableView.setItems(FXCollections.observableArrayList(Database.getAllReservoirs()));
        }
    }
}
