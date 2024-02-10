package com.prod.hydraulicsystemsmaintenance.controllers;

import com.prod.hydraulicsystemsmaintenance.Application;
import com.prod.hydraulicsystemsmaintenance.database.Database;
import com.prod.hydraulicsystemsmaintenance.entities.*;
import com.prod.hydraulicsystemsmaintenance.generics.Change;
import com.prod.hydraulicsystemsmaintenance.utils.View;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class AssignEquipmentController implements Initializable {
    @FXML private ComboBox<User> technicianComboBox;
    @FXML private ListView<Component> componentListView;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        View.serializeChanges();

        componentListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        List<User> technicians = Database.getAllUsers().stream().filter(u -> u.getAdministrator() == 0).toList();
        technicianComboBox.setItems(FXCollections.observableArrayList(technicians));

        List<Actuator> actuators = Database.getAllActuators().stream().filter(a -> a.getUserId() == 0).toList();
        List<Pump> pumps = Database.getAllPumps().stream().filter(p -> p.getUserId() == 0).toList();
        List<Reservoir> reservoirs = Database.getAllReservoirs().stream().filter(r -> r.getUserId() == 0).toList();
        List<Valve> valves = Database.getAllValves().stream().filter(v -> v.getUserId() == 0).toList();

        List<Component> components = new ArrayList<>(actuators);
        components.addAll(pumps);
        components.addAll(reservoirs);
        components.addAll(valves);
        componentListView.setItems(FXCollections.observableArrayList(components));
    }

    public void assign() {
        if (technicianComboBox.getSelectionModel().isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "You must select a technician to assign equipment!").show();
        } else if (componentListView.getSelectionModel().isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "You must select at least one component to assign it!").show();
        } else {
            List<Component> components = componentListView.getSelectionModel().getSelectedItems();
            User technician = technicianComboBox.getSelectionModel().getSelectedItem();
            for (Component component : components) {
                Database.assignTechnician(technician.getId(), component);
            }
            new Alert(Alert.AlertType.INFORMATION, "Equipment assigned to the selected technician.").show();
            Application.changes.add(new Change<User, String>(Application.currentUser, STR."\{Application.currentUser} assigned \{components} to \{technician.toChangeString()}").toString());
            View.change("assign-equipment");
        }
    }
}
