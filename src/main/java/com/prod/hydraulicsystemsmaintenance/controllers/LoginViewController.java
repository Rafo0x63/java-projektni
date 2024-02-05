package com.prod.hydraulicsystemsmaintenance.controllers;

import com.prod.hydraulicsystemsmaintenance.Application;
import com.prod.hydraulicsystemsmaintenance.database.Database;
import com.prod.hydraulicsystemsmaintenance.entities.CurrentUser;
import com.prod.hydraulicsystemsmaintenance.entities.User;
import com.prod.hydraulicsystemsmaintenance.exceptions.UserDoesntExistException;
import com.prod.hydraulicsystemsmaintenance.exceptions.WrongPasswordException;
import com.prod.hydraulicsystemsmaintenance.utils.FileUtils;
import com.prod.hydraulicsystemsmaintenance.utils.View;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.util.List;

public class LoginViewController {
    @FXML private TextField usernameTextField;
    @FXML private PasswordField passwordField;

    @FXML
    public void login() {
        if (Application.dbLogin) {
            try {
                Application.currentUser = new CurrentUser(Database.loginUser(usernameTextField.getText(), passwordField.getText()));
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Login successful.");
                alert.show();
                System.out.println(STR."Currently logged in as \{Application.currentUser.getUsername()}");
                Application.logger.info(STR."successful login as \{Application.currentUser}");
                View.change("main");
            } catch (UserDoesntExistException | WrongPasswordException e) {
                System.out.println(e.getMessage());
                Application.logger.error(e.getMessage());
            }
        } else {
            List<User> users = FileUtils.readAllUsers();
            users = users.stream().filter(u -> u.getUsername().compareTo(usernameTextField.getText()) == 0).toList();
            if (!users.isEmpty()) {
                users = users.stream().filter(u -> u.getPassword().compareTo(Database.hashPassword(passwordField.getText())) == 0).toList();
                if (!users.isEmpty()) {
                    Application.currentUser = new CurrentUser(users.getFirst());
                    new Alert(Alert.AlertType.INFORMATION, "Logged in").show();
                    View.change("main");
                    Application.logger.info(STR."Logged in as \{Application.currentUser}");
                } else {
                    new Alert(Alert.AlertType.ERROR, "Wrong password").show();
                    Application.logger.error("wrong password");
                }
            } else {
                new Alert(Alert.AlertType.ERROR, "User doesn't exist").show();
                Application.logger.error("user doesn't exist");
            }
        }
    }

    @FXML
    public void goToRegister() {
        View.change("register");
    }
}
