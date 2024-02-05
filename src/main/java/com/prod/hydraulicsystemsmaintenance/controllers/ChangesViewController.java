package com.prod.hydraulicsystemsmaintenance.controllers;

import com.prod.hydraulicsystemsmaintenance.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ChangesViewController implements Initializable {
    @FXML private TableView<String> tableView;
    @FXML private TableColumn<String, String> changesTableColumn;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        List<String> changes = Application.changes;
        changesTableColumn.setCellValueFactory(c -> new SimpleStringProperty(c.getValue()));

        tableView.setItems(FXCollections.observableArrayList(changes));
    }
}
