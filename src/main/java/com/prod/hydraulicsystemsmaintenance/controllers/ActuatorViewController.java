package com.prod.hydraulicsystemsmaintenance.controllers;

import com.prod.hydraulicsystemsmaintenance.database.Database;
import com.prod.hydraulicsystemsmaintenance.entities.Actuator;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ActuatorViewController implements Initializable {
    @FXML private TableView actuatorsTableView;
    @FXML private TableColumn<Actuator, String> modelTableColumn;
    @FXML private TableColumn<Actuator, String> serialNumberTableColumn;
    @FXML private TableColumn<Actuator, String> forceTableColumn;
    @FXML private TableColumn<Actuator, String> installationDateTableColumn;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        List<Actuator> actuators = Database.getAllActuators();
        modelTableColumn.setCellValueFactory(new PropertyValueFactory<Actuator, String>("name"));
        serialNumberTableColumn.setCellValueFactory(new PropertyValueFactory<Actuator, String>("serialNumber"));
        forceTableColumn.setCellValueFactory(new PropertyValueFactory<Actuator, String>("force"));
        installationDateTableColumn.setCellValueFactory(new PropertyValueFactory<Actuator, String>("installationDate"));

        actuatorsTableView.setItems(FXCollections.observableArrayList(actuators));
    }
}
