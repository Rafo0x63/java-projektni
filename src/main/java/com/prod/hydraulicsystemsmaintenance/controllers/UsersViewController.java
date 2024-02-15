package com.prod.hydraulicsystemsmaintenance.controllers;

import com.prod.hydraulicsystemsmaintenance.database.Database;
import com.prod.hydraulicsystemsmaintenance.entities.*;
import com.prod.hydraulicsystemsmaintenance.exceptions.UserDoesntExistException;
import com.prod.hydraulicsystemsmaintenance.utils.View;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class UsersViewController implements Initializable {
        @FXML private TableView<User> usersTableView;
        @FXML private TableColumn<User, String> nameTableColumn;
        @FXML private TableColumn<User, String> usernameTableColumn;
        @FXML private TableColumn<User, String> administratorTableColumn;
        @FXML private TableColumn<User, String> equipmentTableColumn;
        @FXML private TextField nameTextField;
        @FXML private TextField usernameTextField;
        @FXML private ToggleGroup admin;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        View.serializeChanges();

        nameTableColumn.setCellValueFactory(new PropertyValueFactory<User, String>("name"));
        usernameTableColumn.setCellValueFactory(new PropertyValueFactory<User, String>("username"));
        administratorTableColumn.setCellValueFactory(new PropertyValueFactory<User, String>("isAdmin"));
        equipmentTableColumn.setCellValueFactory(u -> {
            if (u.getValue().getAdministrator() == 0) {
                Technician t = (Technician) u.getValue();
                return new SimpleStringProperty(t.getResponsibleFor().isEmpty() ? "" : t.equipmentString());
            } else {
                Administrator a = (Administrator) u.getValue();
                return new SimpleStringProperty("");
            }
        });
        usersTableView.getItems().setAll(FXCollections.observableArrayList(Database.getAllUsers()));
    }

    @FXML
    public void search() {
        List<User> users = Database.getAllUsers();
        if (!nameTextField.getText().isEmpty()) users = users.stream().filter(user -> user.getName().toLowerCase().contains(nameTextField.getText().toLowerCase())).toList();
        if (!usernameTextField.getText().isEmpty()) users = users.stream().filter(user -> user.getUsername().toLowerCase().contains(usernameTextField.getText().toLowerCase())).toList();
        RadioButton selected = (RadioButton) admin.getSelectedToggle();
        String selectedValue = selected.getText();
        Integer administrator = selectedValue.matches("Yes") ? 1 : selectedValue.matches("No") ? 0 : -1;
        if (administrator != -1) users = users.stream().filter(user -> user.getAdministrator() == administrator).toList();

        usersTableView.setItems(FXCollections.observableArrayList(users));
    }

    @FXML
    public void showAll() {
        usersTableView.getItems().setAll(FXCollections.observableArrayList(Database.getAllUsers()));
    }
}
