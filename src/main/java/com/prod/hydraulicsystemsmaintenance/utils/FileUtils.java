package com.prod.hydraulicsystemsmaintenance.utils;

import com.prod.hydraulicsystemsmaintenance.Application;
import com.prod.hydraulicsystemsmaintenance.database.Database;
import com.prod.hydraulicsystemsmaintenance.entities.User;

import java.io.*;
import java.util.List;

public class FileUtils {
    public static void saveUser(User user) {
        try (FileWriter fw = new FileWriter("dat/users.txt", true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw)) {

            out.println(user.getUsername());
            out.println(Database.hashPassword(user.getPassword()));
            out.println(user.getAdministrator());
        } catch (IOException e) {
            Application.logger.error("error while writing to file", e);
        }
    }

    public static List<User> readAllUsers() {
        try (FileReader fr = new FileReader("dat/users.txt");
            BufferedReader br = new BufferedReader(fr)){

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}