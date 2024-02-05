package com.prod.hydraulicsystemsmaintenance.controllers;

import com.prod.hydraulicsystemsmaintenance.Application;
import com.prod.hydraulicsystemsmaintenance.database.Database;
import com.prod.hydraulicsystemsmaintenance.entities.Actuator;
import com.prod.hydraulicsystemsmaintenance.entities.ServiceRecord;
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

public class ActuatorViewController implements Initializable {
    Logger logger = Application.logger;
    @FXML private TextField modelTextField;
    @FXML private TextField serialNumberTextField;
    @FXML private TextField forceTextField;
    @FXML private DatePicker installationDatePicker;
    @FXML private TableView<Actuator> tableView;
    @FXML private TableColumn<Actuator, String> modelTableColumn;
    @FXML private TableColumn<Actuator, String> serialNumberTableColumn;
    @FXML private TableColumn<Actuator, String> forceTableColumn;
    @FXML private TableColumn<Actuator, String> installationDateTableColumn;
    @FXML private Button updateButton;
    @FXML private Button deleteButton;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (!Application.currentUser.isAdministrator()) {
            updateButton.setVisible(false);
            deleteButton.setVisible(false);
        }
        List<Actuator> actuators = Database.getAllActuators();
        modelTableColumn.setCellValueFactory(new PropertyValueFactory<Actuator, String>("model"));
        serialNumberTableColumn.setCellValueFactory(new PropertyValueFactory<Actuator, String>("serialNumber"));
        forceTableColumn.setCellValueFactory(new PropertyValueFactory<Actuator, String>("force"));
        installationDateTableColumn.setCellValueFactory(a -> a.getValue().getInstallationDate().isBefore(LocalDate.now().minusMonths(6)) ? new SimpleStringProperty("Needs to be serviced!") : new SimpleStringProperty(a.getValue().getInstallationDate().toString()));

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
        logger.info("actuator search");
    }

    public void update() {
        Actuator actuator = tableView.getSelectionModel().getSelectedItem();
        if (actuator == null) {
            new Alert(Alert.AlertType.ERROR, "You must select an actuator to update!").show();
            logger.error("actuator not selected while updating");
        } else {
            if (modelTextField.getText().isEmpty() && serialNumberTextField.getText().isEmpty() && forceTextField.getText().isEmpty() && installationDatePicker.getValue() == null) {
                new Alert(Alert.AlertType.ERROR, "You must fill at least one field to update an entity!").show();
                logger.error("actuator update attempt without changes");
            } else {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to change actuator details?", ButtonType.YES, ButtonType.NO);
                alert.showAndWait();
                if (alert.getResult() == ButtonType.YES) {
                    String model = modelTextField.getText().isEmpty() ? actuator.getModel() : modelTextField.getText();
                    String serialNumber = serialNumberTextField.getText().isEmpty() ? actuator.getSerialNumber() : serialNumberTextField.getText();
                    Integer force = forceTextField.getText().isEmpty() ? actuator.getForce() : Integer.valueOf(forceTextField.getText());
                    LocalDate installationDate = installationDatePicker.getValue() == null ? actuator.getInstallationDate() : installationDatePicker.getValue();

                    Actuator newActuator = new Actuator(model, serialNumber, force, installationDate);

                    Database.updateActuator(actuator.getId(), newActuator);
                    new Alert(Alert.AlertType.INFORMATION, "The actuator has been updated.").show();
                    System.out.println("actuator updated");
                    logger.info(STR."actuator \{actuator} updated");
                    Application.changes.add(new Change<User, String>(Application.currentUser, STR."\{Application.currentUser} updated \{actuator} to \{newActuator}").toString());

                    tableView.setItems(FXCollections.observableArrayList(Database.getAllActuators()));
                }
            }
        }
    }

    public void delete() {
        Actuator actuator = tableView.getSelectionModel().getSelectedItem();
        if (actuator.isInstalledInSystem()) {
            new Alert(Alert.AlertType.ERROR, "The actuator cannot be deleted from the database because it is installed in a system, remove it from the system to delete it from the database!").show();
            logger.error("delete attempt while actuator is installed in a system");
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, STR."Are you sure you want to delete Actuator\{actuator.toString()}?", ButtonType.YES, ButtonType.NO);
            alert.showAndWait();
            if (alert.getResult() == ButtonType.YES) {
                Database.deleteActuator(actuator.getId());
                alert = new Alert(Alert.AlertType.INFORMATION, "The actuator has been deleted.");
                alert.show();
                logger.info(STR."actuator \{actuator} deleted");
                Application.changes.add(new Change<User, String>(Application.currentUser, STR."\{Application.currentUser} deleted \{actuator}").toString());
            }
            tableView.setItems(FXCollections.observableArrayList(Database.getAllActuators()));
        }
    }

    public void service() {
        Actuator actuator = tableView.getSelectionModel().getSelectedItem();
        if (actuator == null) {
            new Alert(Alert.AlertType.ERROR, "You must select an actuator to service!").show();
            logger.error("unselected service attempt");
        } else {
            actuator.setInstallationDate(LocalDate.now());
            actuator.service();
            new Alert(Alert.AlertType.INFORMATION, "Service record created").show();
            System.out.println("service record created");
            logger.info(STR."\{actuator} serviced, service record created");
            Application.changes.add(new Change<User, String>(Application.currentUser, STR."\{Application.currentUser} serviced \{actuator}").toString());

            tableView.setItems(FXCollections.observableArrayList(Database.getAllActuators()));
        }
    }
}
