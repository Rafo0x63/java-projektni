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
        private List<User> users = Database.getAllUsers();
        @FXML private TableView<User> usersTableView;
        @FXML private TableColumn<User, String> nameTableColumn;
        @FXML private TableColumn<User, String> usernameTableColumn;
        @FXML private TableColumn<User, String> administratorTableColumn;
        @FXML private TableColumn<User, String> equipmentTableColumn;
        @FXML private TextField nameTextField;
        @FXML private TextField usernameTextField;
        @FXML private CheckBox administratorCheckBox;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        View.serializeChanges();

        nameTableColumn.setCellValueFactory(new PropertyValueFactory<User, String>("name"));
        usernameTableColumn.setCellValueFactory(new PropertyValueFactory<User, String>("username"));
        administratorTableColumn.setCellValueFactory(new PropertyValueFactory<User, String>("isAdmin"));
        equipmentTableColumn.setCellValueFactory(u -> {
            if (u.getValue().getAdministrator() == 0) {
                Technician t = (Technician) u.getValue();
                return new SimpleStringProperty(t.getResponsibleFor().isEmpty() ? "" : t.getResponsibleFor().toString());
            } else {
                Administrator a = (Administrator) u.getValue();
                return new SimpleStringProperty("");
            }
        });
        usersTableView.getItems().setAll(FXCollections.observableArrayList(users));
    }

    @FXML
    public void search() {
        try {
            User user = new User.Builder(usernameTextField.getText()).name(nameTextField.getText()).administrator( administratorCheckBox.isSelected() ? 1 : 0).build();
            usersTableView.getItems().setAll(FXCollections.observableArrayList(Database.getUsersByCriteria(user)));
        } catch (UserDoesntExistException e) {
            System.out.println("No user found");
        }
    }

    @FXML
    public void showAll() {
        usersTableView.getItems().setAll(FXCollections.observableArrayList(users));
    }
}
