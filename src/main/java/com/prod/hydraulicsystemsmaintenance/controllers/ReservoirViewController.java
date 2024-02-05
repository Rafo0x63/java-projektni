package com.prod.hydraulicsystemsmaintenance.controllers;

import com.prod.hydraulicsystemsmaintenance.Application;
import com.prod.hydraulicsystemsmaintenance.database.Database;
import com.prod.hydraulicsystemsmaintenance.entities.Reservoir;
import com.prod.hydraulicsystemsmaintenance.entities.User;
import com.prod.hydraulicsystemsmaintenance.generics.Change;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.slf4j.Logger;

import java.net.URL;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class ReservoirViewController implements Initializable {
    Logger logger = Application.logger;
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
        System.out.println(Application.changes);
        if (!Application.currentUser.isAdministrator()) {
            updateButton.setVisible(false);
            deleteButton.setVisible(false);
        }
        List<Reservoir> reservoirs = Database.getAllReservoirs();

        modelTC.setCellValueFactory(new PropertyValueFactory<Reservoir, String>("model"));
        serialNumberTC.setCellValueFactory(new PropertyValueFactory<Reservoir, String>("serialNumber"));
        capacityTC.setCellValueFactory(new PropertyValueFactory<Reservoir, String>("capacity"));
        installationDateTC.setCellValueFactory(r -> r.getValue().getInstallationDate().isBefore(LocalDate.now().minusMonths(12)) ? new SimpleStringProperty("Needs to be replaced!") : new SimpleStringProperty(r.getValue().getInstallationDate().toString()));

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
                    Application.changes.add(new Change<User, String>(Application.currentUser, STR."\{Application.currentUser} updated \{reservoir} to \{newReservoir}").toString());

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
                Application.changes.add(new Change<User, String>(Application.currentUser, STR."\{Application.currentUser} deleted \{reservoir}").toString());
            }
            tableView.setItems(FXCollections.observableArrayList(Database.getAllReservoirs()));
        }
    }

    public void replace() {
        Reservoir reservoir = tableView.getSelectionModel().getSelectedItem();
        if (reservoir == null) {
            logger.error("unselected reservoir replace attempt");
            new Alert(Alert.AlertType.ERROR, "You must select the reservoir you want to replace").show();
        } else {
            if (serialNumberTextField.getText().isEmpty() || serialNumberTextField.getText().compareTo(reservoir.getSerialNumber()) == 0) {
                logger.error("serial number conflict or missing");
                new Alert(Alert.AlertType.ERROR, "Serial number cannot be empty or the same as the previous model!").show();
            } else {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to replace this reservoir?", ButtonType.YES, ButtonType.NO);
                alert.showAndWait();

                if (alert.getResult() == ButtonType.YES) {
                    String model = modelTextField.getText().isEmpty() ? reservoir.getModel() : modelTextField.getText();
                    String serialNumber = serialNumberTextField.getText();
                    LocalDate installationDate = LocalDate.now();
                    Integer capacity = capacityTextField.getText().isEmpty() ? reservoir.getCapacity() : Integer.valueOf(capacityTextField.getText());
                    Reservoir newReservoir = new Reservoir(model, serialNumber, capacity, installationDate);
                    reservoir.replace(reservoir.getId(), newReservoir);

                    new Alert(Alert.AlertType.INFORMATION, "Reservoir has been replaced.").show();
                    logger.info("reservoir replaced");
                    Application.changes.add(new Change<User, String>(Application.currentUser, STR."\{Application.currentUser} replaced \{reservoir} with \{newReservoir}").toString());

                    tableView.setItems(FXCollections.observableArrayList(Database.getAllReservoirs()));
                }
            }
        }
    }
}
