package com.prod.hydraulicsystemsmaintenance.utils;

import com.prod.hydraulicsystemsmaintenance.Application;
import com.prod.hydraulicsystemsmaintenance.database.Database;
import com.prod.hydraulicsystemsmaintenance.entities.Administrator;
import com.prod.hydraulicsystemsmaintenance.entities.Technician;
import com.prod.hydraulicsystemsmaintenance.entities.User;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
        List<User> users = new ArrayList<>();

        try {
            List<String> lines = Files.lines(Paths.get("dat/users.txt")).toList();
            int c = 0;
            for (int i = 0; i < lines.size()/3; i++) {
                String username = lines.get(c++);
                String passwordHash = lines.get(c++);
                Integer administrator = Integer.parseInt(lines.get(c++));

                users.add(new User.Builder(username).passwordHash(passwordHash).administrator(administrator).build());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return users;
    }
}