package com.prod.hydraulicsystemsmaintenance.controllers;

import com.prod.hydraulicsystemsmaintenance.database.Database;
import com.prod.hydraulicsystemsmaintenance.entities.Administrator;
import com.prod.hydraulicsystemsmaintenance.entities.Technician;
import com.prod.hydraulicsystemsmaintenance.exceptions.UserAlreadyExistsException;
import com.prod.hydraulicsystemsmaintenance.utils.View;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class RegisterViewController {
    @FXML private TextField nameTextField;
    @FXML private TextField usernameTextField;
    @FXML private PasswordField passwordField;
    @FXML private CheckBox adminCheckBox;

    @FXML
    public void register() {
        try {
            Database.insertUser(adminCheckBox.isSelected() ? new Administrator(nameTextField.getText(), usernameTextField.getText(), passwordField.getText(), 1) : new Technician(nameTextField.getText(), usernameTextField.getText(), passwordField.getText(), 0));

        } catch (UserAlreadyExistsException e) {
            System.out.println(e.getMessage());
        }
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Registration successful");
        alert.show();
        goToLogin();
    }

    @FXML
    public void goToLogin() {
        View.change("login");
    }
}
