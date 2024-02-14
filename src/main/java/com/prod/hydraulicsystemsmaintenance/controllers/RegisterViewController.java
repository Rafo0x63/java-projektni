package com.prod.hydraulicsystemsmaintenance.controllers;

import com.prod.hydraulicsystemsmaintenance.Application;
import com.prod.hydraulicsystemsmaintenance.database.Database;
import com.prod.hydraulicsystemsmaintenance.entities.Administrator;
import com.prod.hydraulicsystemsmaintenance.entities.Technician;
import com.prod.hydraulicsystemsmaintenance.entities.User;
import com.prod.hydraulicsystemsmaintenance.exceptions.UserAlreadyExistsException;
import com.prod.hydraulicsystemsmaintenance.generics.Change;
import com.prod.hydraulicsystemsmaintenance.utils.FileUtils;
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
    @FXML private PasswordField repeatPasswordField;
    @FXML private CheckBox adminCheckBox;

    @FXML
    public void register() {
        try {
            if (nameTextField.getText().isEmpty() || usernameTextField.getText().isEmpty() || passwordField.getText().isEmpty() || repeatPasswordField.getText().isEmpty()) {
                new Alert(Alert.AlertType.ERROR, "All fields must be filled to register a user!").show();
                Application.logger.error("incomplete user details registration attempt");
            } else {
                if (passwordField.getText().compareTo(repeatPasswordField.getText()) != 0) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Passwords must match!");
                    alert.show();
                    System.out.println("password mismatch");
                } else {
                    if (adminCheckBox.isSelected()) {
                        Administrator administrator = new Administrator(nameTextField.getText(), usernameTextField.getText(), passwordField.getText(), 1);
                        Database.insertUser(administrator);
                        FileUtils.saveUser(administrator);
                    } else if (!adminCheckBox.isSelected()) {
                        Technician technician = new Technician(nameTextField.getText(), usernameTextField.getText(), passwordField.getText(), 0);
                        Database.insertUser(technician);
                        FileUtils.saveUser(technician);
                    }
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Registration successful.");
                    alert.show();
                    System.out.println("registered user");
                    goToLogin();
                }
            }
        } catch (UserAlreadyExistsException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
            System.out.println(e.getMessage());
            Application.logger.error(e.getMessage());
        }

    }

    @FXML
    public void goToLogin() {
        View.change("login");
    }
}
