package com.prod.hydraulicsystemsmaintenance.controllers;

import com.prod.hydraulicsystemsmaintenance.Application;
import com.prod.hydraulicsystemsmaintenance.database.Database;
import com.prod.hydraulicsystemsmaintenance.entities.*;
import com.prod.hydraulicsystemsmaintenance.generics.Change;
import com.prod.hydraulicsystemsmaintenance.utils.View;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class SystemViewController implements Initializable {
    @FXML private TextField nameTextField;
    @FXML private ComboBox<Actuator> actuatorComboBox;
    @FXML private ComboBox<Pump> pumpComboBox;
    @FXML private ComboBox<Reservoir> reservoirComboBox;
    @FXML private ComboBox<Valve> valveComboBox;
    @FXML private ComboBox<Administrator> administratorComboBox;
    @FXML private TableColumn<HydraulicSystem, String> nameTableColumn;
    @FXML private TableColumn<HydraulicSystem, String> actuatorTableColumn;
    @FXML private TableColumn<HydraulicSystem, String> pumpTableColumn;
    @FXML private TableColumn<HydraulicSystem, String> reservoirTableColumn;
    @FXML private TableColumn<HydraulicSystem, String> valveTableColumn;
    @FXML private TableColumn<HydraulicSystem, String> administratorTableColumn;
    @FXML private TableView<HydraulicSystem> tableView;
    @FXML private Button updateButton;
    @FXML private Button deleteButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        View.serializeChanges();

        if (!Application.currentUser.isAdministrator()) {
            updateButton.setVisible(false);
            deleteButton.setVisible(false);
        }

        List<Actuator> actuators = Database.getAllActuators();
        List<Pump> pumps = Database.getAllPumps();
        List<Reservoir> reservoirs = Database.getAllReservoirs();
        List<Valve> valves = Database.getAllValves();
        List<User> users =  Database.getAllUsers().stream().filter(user -> user.getAdministrator() == 1).toList();
        List<Administrator> administrators = new ArrayList<>();
        for (User user : users) {
            administrators.add((Administrator) user);
        }
        actuatorComboBox.setItems(FXCollections.observableArrayList(actuators));
        pumpComboBox.setItems(FXCollections.observableArrayList(pumps));
        reservoirComboBox.setItems(FXCollections.observableArrayList(reservoirs));
        valveComboBox.setItems(FXCollections.observableArrayList(valves));
        administratorComboBox.setItems(FXCollections.observableArrayList(administrators));

        nameTableColumn.setCellValueFactory(new PropertyValueFactory<HydraulicSystem, String>("name"));
        actuatorTableColumn.setCellValueFactory(new PropertyValueFactory<HydraulicSystem, String>("actuator"));
        pumpTableColumn.setCellValueFactory(new PropertyValueFactory<HydraulicSystem, String>("pump"));
        reservoirTableColumn.setCellValueFactory(new PropertyValueFactory<HydraulicSystem, String>("reservoir"));
        valveTableColumn.setCellValueFactory(new PropertyValueFactory<HydraulicSystem, String>("valve"));
        administratorTableColumn.setCellValueFactory(new PropertyValueFactory<HydraulicSystem, String>("administrator"));

        tableView.setItems(FXCollections.observableArrayList(Database.getAllSystems()));
    }

    public void search(){
        List<HydraulicSystem> filteredSystems = Database.getAllSystems();

        if(!nameTextField.getText().isEmpty()) {
            filteredSystems = filteredSystems.stream().filter(system -> system.getName().contains(nameTextField.getText())).toList();
        }
        if(!actuatorComboBox.getSelectionModel().isEmpty()) {
            filteredSystems = filteredSystems.stream().filter(system -> system.getActuator().getId() == actuatorComboBox.getSelectionModel().getSelectedItem().getId()).toList();
        }
        if(!pumpComboBox.getSelectionModel().isEmpty()) {
            filteredSystems = filteredSystems.stream().filter(system -> system.getPump().getId() == pumpComboBox.getSelectionModel().getSelectedItem().getId()).toList();
        }
        if(!reservoirComboBox.getSelectionModel().isEmpty()) {
            filteredSystems = filteredSystems.stream().filter(system -> system.getReservoir().getId() == reservoirComboBox.getSelectionModel().getSelectedItem().getId()).toList();
        }
        if(!valveComboBox.getSelectionModel().isEmpty()) {
            filteredSystems = filteredSystems.stream().filter(system -> system.getValve().getId() == valveComboBox.getSelectionModel().getSelectedItem().getId()).toList();
        }
        if(!administratorComboBox.getSelectionModel().isEmpty()) {
            filteredSystems = filteredSystems.stream().filter(system -> system.getAdministrator().getId() == administratorComboBox.getSelectionModel().getSelectedItem().getId()).toList();
        }
        tableView.setItems(FXCollections.observableArrayList(filteredSystems));
    }

    public void update(){
        HydraulicSystem system = tableView.getSelectionModel().getSelectedItem();
        if (system == null) {
            new Alert(Alert.AlertType.ERROR, "You must select a system to update it!");
        } else {

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to update the system details?", ButtonType.YES, ButtonType.NO);
            alert.showAndWait();

            if (alert.getResult() == ButtonType.YES) {
                String name = nameTextField.getText().isEmpty() ? system.getName() : nameTextField.getText();
                Actuator actuator = actuatorComboBox.getSelectionModel().isEmpty() ? system.getActuator() : actuatorComboBox.getSelectionModel().getSelectedItem();
                Pump pump = pumpComboBox.getSelectionModel().isEmpty() ? system.getPump() : pumpComboBox.getSelectionModel().getSelectedItem();
                Reservoir reservoir = reservoirComboBox.getSelectionModel().isEmpty() ? system.getReservoir() : reservoirComboBox.getSelectionModel().getSelectedItem();
                Valve valve = valveComboBox.getSelectionModel().isEmpty() ? system.getValve() : valveComboBox.getSelectionModel().getSelectedItem();
                Administrator administrator = administratorComboBox.getSelectionModel().isEmpty() ? system.getAdministrator() : administratorComboBox.getSelectionModel().getSelectedItem();

                List<HydraulicSystem> systems = Database.getAllSystems();
                boolean componentAlreadyInSystem = false;
                List<HydraulicSystem> systemCheck = systems.stream().filter(s -> s.getActuator().getSerialNumber().matches(actuator.getSerialNumber())).toList();
                if (!systemCheck.isEmpty()) componentAlreadyInSystem = true;
                systemCheck = systems.stream().filter(s -> s.getPump().getSerialNumber().matches(pump.getSerialNumber())).toList();
                if (!systemCheck.isEmpty()) componentAlreadyInSystem = true;
                systemCheck = systems.stream().filter(s -> s.getValve().getSerialNumber().matches(valve.getSerialNumber())).toList();
                if (!systemCheck.isEmpty()) componentAlreadyInSystem = true;
                systemCheck = systems.stream().filter(s -> s.getReservoir().getSerialNumber().matches(reservoir.getSerialNumber())).toList();
                if (!systemCheck.isEmpty()) componentAlreadyInSystem = true;
                systemCheck = systems.stream().filter(s -> s.getAdministrator().getId() == administrator.getId()).toList();
                if (!systemCheck.isEmpty()) componentAlreadyInSystem = true;

                if (!componentAlreadyInSystem) {
                    HydraulicSystem newSystem = new HydraulicSystem(name, actuator, pump, reservoir, valve, administrator);

                    Database.updateSystem(system, newSystem);
                    tableView.setItems(FXCollections.observableArrayList(Database.getAllSystems()));
                    new Alert(Alert.AlertType.INFORMATION, "The system has been updated.").show();
                    System.out.println("system updated");
                    Application.changes.add(new Change<User, String>(Application.currentUser, STR."\{Application.currentUser.toChangeString()} updated \{system} to \{newSystem}").toString());
                    clearSelection();
                } else new Alert(Alert.AlertType.ERROR, "A component is already in a different system or the selected administrator is already administrating a different system!").show();
            }
        }


    }

    public void delete() {
        HydraulicSystem system = tableView.getSelectionModel().getSelectedItem();
        if (system != null) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this system?", ButtonType.YES, ButtonType.NO);
                alert.showAndWait();
            if (alert.getResult() == ButtonType.YES) {
                Database.deleteSystem(system);
                new Alert(Alert.AlertType.INFORMATION, "The system has been deleted from the database.").show();
                System.out.println("system deleted");
                Application.changes.add(new Change<User, String>(Application.currentUser, STR."\{Application.currentUser.toChangeString()} deleted \{system}").toString());
            }
            tableView.setItems(FXCollections.observableArrayList(Database.getAllSystems()));
        } else new Alert(Alert.AlertType.ERROR, "You must select a system to delete it!").show();
    }


    public void showAll() {
        tableView.setItems(FXCollections.observableArrayList(Database.getAllSystems()));
    }

    public void clearSelection() {
        actuatorComboBox.getSelectionModel().clearSelection();
        pumpComboBox.getSelectionModel().clearSelection();
        reservoirComboBox.getSelectionModel().clearSelection();
        valveComboBox.getSelectionModel().clearSelection();
        administratorComboBox.getSelectionModel().clearSelection();
    }
}