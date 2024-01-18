package com.prod.hydraulicsystemsmaintenance.controllers;

import com.prod.hydraulicsystemsmaintenance.database.Database;
import com.prod.hydraulicsystemsmaintenance.entities.User;
import com.prod.hydraulicsystemsmaintenance.exceptions.UserDoesntExistException;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.w3c.dom.Text;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class UsersViewController implements Initializable {
        private List<User> users = Database.getAllUsers();
        @FXML private TableView<User> usersTableView;
        @FXML private TableColumn<User, String> nameTableColumn;
        @FXML private TableColumn<User, String> usernameTableColumn;
        @FXML private TableColumn<User, String> administratorTableColumn;
        @FXML private TextField nameTextField;
        @FXML private TextField usernameTextField;
        @FXML private CheckBox administratorCheckBox;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        nameTableColumn.setCellValueFactory(new PropertyValueFactory<User, String>("name"));
        usernameTableColumn.setCellValueFactory(new PropertyValueFactory<User, String>("username"));
        administratorTableColumn.setCellValueFactory(new PropertyValueFactory<User, String>("isAdmin"));
        usersTableView.getItems().setAll(FXCollections.observableArrayList(users));
    }

    @FXML
    public void search() {
        try {
            User user = new User(nameTextField.getText(), usernameTextField.getText(), administratorCheckBox.isSelected() ? 1 : 0);
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
