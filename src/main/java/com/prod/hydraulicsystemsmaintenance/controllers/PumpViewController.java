package com.prod.hydraulicsystemsmaintenance.controllers;

import com.prod.hydraulicsystemsmaintenance.Application;
import com.prod.hydraulicsystemsmaintenance.database.Database;
import com.prod.hydraulicsystemsmaintenance.entities.Pump;
import com.prod.hydraulicsystemsmaintenance.entities.User;
import com.prod.hydraulicsystemsmaintenance.generics.Change;
import com.prod.hydraulicsystemsmaintenance.utils.View;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.slf4j.Logger;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class PumpViewController implements Initializable {
    Logger logger = Application.logger;
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
    @FXML private Button updateButton;
    @FXML private Button deleteButton;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        View.serializeChanges();

        if (!Application.currentUser.isAdministrator()) {
            updateButton.setVisible(false);
            deleteButton.setVisible(false);
        }
        List<Pump> pumps = Database.getAllPumps();
        modelTC.setCellValueFactory(new PropertyValueFactory<Pump, String>("model"));
        serialNumberTC.setCellValueFactory(new PropertyValueFactory<Pump, String>("serialNumber"));
        flowRateTC.setCellValueFactory(new PropertyValueFactory<Pump, String>("flowRate"));
        pressureTC.setCellValueFactory(new PropertyValueFactory<Pump, String>("pressure"));
        installationDateTC.setCellValueFactory(p -> p.getValue().getInstallationDate().isBefore(LocalDate.now().minusMonths(6)) ? new SimpleStringProperty("Needs to be serviced!") : new SimpleStringProperty(p.getValue().getInstallationDate().toString()));

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
        logger.info("pump search");
    }

    public void update() {
        Pump pump = tableView.getSelectionModel().getSelectedItem();
        if (pump == null) {
            new Alert(Alert.AlertType.ERROR, "You must select an pump to update!").show();
            logger.error("unselected pump update attempt");
        } else {
            if (modelTextField.getText().isEmpty() && serialNumberTextField.getText().isEmpty() && flowRateTextField.getText().isEmpty() && pressureTextField.getText().isEmpty() && installationDatePicker.getValue() == null) {
                new Alert(Alert.AlertType.ERROR, "You must fill at least one field to update an entity!").show();
                logger.error("no new values to update");
            } else {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, STR."Are you sure you want to change pump details?", ButtonType.YES, ButtonType.NO);
                alert.showAndWait();
                if (alert.getResult() == ButtonType.YES) {
                    String model = modelTextField.getText().isEmpty() ? pump.getModel() : modelTextField.getText();
                    String serialNumber = serialNumberTextField.getText().isEmpty() ? pump.getSerialNumber() : serialNumberTextField.getText();
                    Integer flowRate = flowRateTextField.getText().isEmpty() ? pump.getFlowRate() : Integer.valueOf(flowRateTextField.getText());
                    Integer pressure = pressureTextField.getText().isEmpty() ? pump.getPressure() : Integer.valueOf(pressureTextField.getText());
                    LocalDate installationDate = installationDatePicker.getValue() == null ? pump.getInstallationDate() : installationDatePicker.getValue();

                    Pump newPump = new Pump(model, serialNumber, flowRate, pressure, installationDate);

                    Database.updatePump(pump.getId(), newPump);
                    System.out.println("pump updated");
                    logger.info(STR."\{pump} updated");
                    Application.changes.add(new Change<User, String>(Application.currentUser, STR."\{Application.currentUser.toChangeString()} updated \{pump.toChangeString()} to \{newPump.toChangeString()}").toString());

                    tableView.setItems(FXCollections.observableArrayList(Database.getAllPumps()));
                }
            }
        }
    }

    public void delete() {
        Pump pump = tableView.getSelectionModel().getSelectedItem();
        if (pump.isInstalledInSystem()) {
            new Alert(Alert.AlertType.ERROR, "The pump cannot be deleted from the database because it is installed in a system, remove it from the system to delete it from the database!").show();
            logger.error("attempt deleting pump installed in a system");
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, STR."Are you sure you want to delete Pump\{pump.toString()}?", ButtonType.YES, ButtonType.NO);
            alert.showAndWait();
            if (alert.getResult() == ButtonType.YES) {
                Database.deletePump(pump.getId());
                alert = new Alert(Alert.AlertType.INFORMATION, "The pump has been deleted.");
                alert.show();
                logger.info(STR."\{pump} deleted");
                Application.changes.add(new Change<User, String>(Application.currentUser, STR."\{Application.currentUser.toChangeString()} deleted \{pump.toChangeString()}").toString());
            }
            tableView.setItems(FXCollections.observableArrayList(Database.getAllPumps()));
        }
    }

    public void service() {
        Pump pump = tableView.getSelectionModel().getSelectedItem();
        if (pump == null) {
            new Alert(Alert.AlertType.ERROR, "You must select a pump to service!").show();
            logger.error("unselected pump service attempt");
        } else {
            pump.setInstallationDate(LocalDate.now());
            pump.service();
            new Alert(Alert.AlertType.INFORMATION, "Service record created.").show();
            System.out.println("service record created");
            logger.info(STR."\{pump} serviced, service record created");
            tableView.setItems(FXCollections.observableArrayList(Database.getAllPumps()));
            Application.changes.add(new Change<User, String>(Application.currentUser, STR."\{Application.currentUser.toChangeString()} serviced \{pump.toChangeString()}").toString());
        }
    }
}
