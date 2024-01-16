package com.prod.hydraulicsystemsmaintenance.controllers;

import com.prod.hydraulicsystemsmaintenance.Application;
import com.prod.hydraulicsystemsmaintenance.database.Database;
import com.prod.hydraulicsystemsmaintenance.entities.CurrentUser;
import com.prod.hydraulicsystemsmaintenance.entities.User;
import com.prod.hydraulicsystemsmaintenance.exceptions.UserDoesntExistException;
import com.prod.hydraulicsystemsmaintenance.utils.View;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginViewController {
    @FXML private TextField usernameTextField;
    @FXML private PasswordField passwordField;

    @FXML
    public void login() {
        try {
            User user = Database.getUserByCriteria(new User(usernameTextField.getText()));
            Application.currentUser = new CurrentUser(Database.loginUser(user));
            System.out.println("Currently logged in as " + Application.currentUser.getUser().getName());
        } catch (UserDoesntExistException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void goToRegister() {
        View.change("register");
    }
}
