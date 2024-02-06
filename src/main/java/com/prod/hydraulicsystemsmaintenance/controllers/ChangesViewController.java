package com.prod.hydraulicsystemsmaintenance.controllers;

import com.prod.hydraulicsystemsmaintenance.Application;
import com.prod.hydraulicsystemsmaintenance.threads.DeserializeChange;
import com.prod.hydraulicsystemsmaintenance.utils.View;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.io.*;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ChangesViewController implements Initializable {
    @FXML private TableView<String> tableView;
    @FXML private TableColumn<String, String> changesTableColumn;
    public static final Object lock = new Object();
    private static boolean flag = false;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        View.serializeChanges();

        DeserializeChange dc = new DeserializeChange();
        ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.execute(dc);
        executorService.close();

        List<String> changes = Application.changes;
        changesTableColumn.setCellValueFactory(c -> new SimpleStringProperty(c.getValue()));

        tableView.setItems(FXCollections.observableArrayList(changes));
    }

    public static synchronized void saveChanges() {
        try {
            flag = true;

            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("dat/changes.dat"));
            oos.writeObject(Application.changes);
            oos.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        flag = false;
        synchronized (lock) {
            lock.notifyAll();
        }
    }

    public static synchronized void getChanges() {
        if (flag) {
            synchronized (lock) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream("dat/changes.dat"));
            Application.changes = (List<String>) ois.readObject();

            ois.close();
        } catch (ClassNotFoundException | IOException e) {
            System.out.println("File empty");
        }
    }
}
