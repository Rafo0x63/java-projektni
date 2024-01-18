package com.prod.hydraulicsystemsmaintenance.controllers;

import com.prod.hydraulicsystemsmaintenance.Application;
import com.prod.hydraulicsystemsmaintenance.database.Database;
import com.prod.hydraulicsystemsmaintenance.entities.CurrentUser;
import com.prod.hydraulicsystemsmaintenance.entities.User;
import com.prod.hydraulicsystemsmaintenance.exceptions.UserDoesntExistException;
import com.prod.hydraulicsystemsmaintenance.exceptions.WrongPasswordException;
import com.prod.hydraulicsystemsmaintenance.utils.View;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginViewController {
    @FXML private TextField usernameTextField;
    @FXML private PasswordField passwordField;

    @FXML
    public void login() {
        try {
            Application.currentUser = new CurrentUser(Database.loginUser(usernameTextField.getText(), passwordField.getText()));
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Login successful.");
            alert.show();
            System.out.println(STR."Currently logged in as \{Application.currentUser.getUsername()}");
            View.change("main");
        } catch (UserDoesntExistException | WrongPasswordException e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    public void goToRegister() {
        View.change("register");
    }
}
