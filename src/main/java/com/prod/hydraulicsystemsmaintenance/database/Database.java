package com.prod.hydraulicsystemsmaintenance.database;

import com.prod.hydraulicsystemsmaintenance.entities.*;
import com.prod.hydraulicsystemsmaintenance.exceptions.UserAlreadyExistsException;
import com.prod.hydraulicsystemsmaintenance.exceptions.UserDoesntExistException;
import com.prod.hydraulicsystemsmaintenance.exceptions.WrongPasswordException;
import javafx.scene.control.Alert;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class Database {
    public static Connection connect() {
        try {
            return DriverManager.getConnection(
                    "jdbc:mysql://localhost/hydraulicsystems", "root", "rootpass"
            );
        } catch (SQLException e) {
            System.out.println("DB connection was unsuccessful.");
            throw new RuntimeException(e);
        }
    }

    public static boolean checkIfUserExists(User user) {
        try {
            Connection connection = connect();

            PreparedStatement query = connection.prepareStatement("SELECT * FROM users WHERE username=?");
            query.setString(1, user.getUsername());
            ResultSet rs = query.executeQuery();

            return rs.next();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static String hashPassword(String password) {
        try {
            var digest = MessageDigest.getInstance("SHA-256").digest(password.getBytes());

            return new String(Base64.getEncoder().encode(digest));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static void insertUser(User user) throws UserAlreadyExistsException {

        try {
            Connection connection = connect();

            if (!checkIfUserExists(user)) {
                PreparedStatement query = connection.prepareStatement("INSERT INTO users (name, username, password, administrator) VALUES (?, ?, ?, ?)");
                query.setString(1, user.getName());
                query.setString(2, user.getUsername());
                query.setString(3, hashPassword(user.getPassword()));
                query.setInt(4, user.getAdministrator());
                if (query.executeUpdate() == 1) {
                    System.out.println("User created");
                    connection.close();

                } else {
                    System.out.println("Error creating user");
                    connection.close();
                }
            } else throw new UserAlreadyExistsException(STR."User '\{user.getUsername()}' already exists.");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static User loginUser(String username, String password) throws UserDoesntExistException, WrongPasswordException {
        try {
            Connection connection = connect();
            if (checkIfUserExists(new User(username))) {
                PreparedStatement query = connection.prepareStatement("SELECT * FROM users WHERE username=? AND password=?");
                query.setString(1, username);
                query.setString(2, hashPassword(password));

                ResultSet rs = query.executeQuery();

                if (!rs.isBeforeFirst()) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Password incorrect. Try again.");
                    alert.show();
                    throw new WrongPasswordException("Password incorrect. Try again.");
                } else {
                    return getUsersFromResultSet(rs).getFirst();
                }

            } else {
}

                connection.close();
                Alert alert = new Alert(Alert.AlertType.ERROR, "User doesn't exist.");
                alert.show();
                throw new UserDoesntExistException("User '" + username + "' doesn't exist.");
            } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static List<User> getUsersByCriteria(User user) throws UserDoesntExistException {
        try {
            Connection connection = connect();

            StringBuilder str = new StringBuilder("SELECT * FROM users WHERE ");
            Integer conditionCounter = 0;
            if (user.getId() != null) {
                str.append("id LIKE '%").append(user.getId()).append("%'");
                conditionCounter++;
            }
            if (user.getName() != null) {
                if (conditionCounter > 0) str.append(" AND ");
                str.append("name LIKE '%").append(user.getName()).append("%'");
                conditionCounter++;
            }
            if (user.getUsername() != null) {
                if (conditionCounter > 0) str.append(" AND ");
                str.append("username LIKE '%").append(user.getUsername()).append("%'");
                conditionCounter++;
            }
            if (user.getAdministrator() != null) {
                if (conditionCounter > 0) str.append(" AND ");
                str.append("administrator=").append(user.getAdministrator());
            }

            PreparedStatement query = connection.prepareStatement(str.toString());
            ResultSet rs = query.executeQuery();
            if (rs != null) {
                return getUsersFromResultSet(rs);
            } else throw new UserDoesntExistException("User doesn't exist.");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public static List<User> getUsersFromResultSet(ResultSet rs) {
        try {
            List<User> users = new ArrayList<>();
            while (rs.next()) {
                Long id = rs.getLong("id");
                String name = rs.getString("name");
                String username = rs.getString("username");
                Integer administrator = rs.getInt("administrator");
                if (administrator.compareTo(0) == 0) {
                    users.add(new Technician(id, name, username, administrator));
                } else if (administrator.compareTo(1) == 0) {
                    users.add(new Administrator(id, name, username, administrator));
                }
            }
            return users;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<User> getAllUsers() {
        try {
            List<User> users = new ArrayList<>();
            Connection connection = connect();

            PreparedStatement query = connection.prepareStatement("SELECT * FROM users");
            ResultSet rs = query.executeQuery();

            users = getUsersFromResultSet(rs);

            connection.close();
            return users;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void insertActuator(Actuator actuator) {
        try {
            Connection connection = connect();

            PreparedStatement query = connection.prepareStatement("INSERT INTO actuators(model, serialNumber, `force`, installationDate) VALUES (?, ?, ?, ?)");
            query.setString(1, actuator.getName());
            query.setString(2, actuator.getSerialNumber());
            query.setLong(3, actuator.getForce());
            query.setDate(4, java.sql.Date.valueOf(actuator.getInstallationDate()));

            query.executeUpdate();

            System.out.println("Actuator saved into database");

            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<Actuator> getAllActuators() {
        try {
            Connection connection = connect();
            List<Actuator> actuators = new ArrayList<>();

            PreparedStatement query = connection.prepareStatement("SELECT * FROM actuators");
            ResultSet rs = query.executeQuery();

            return getActuatorsFromResultSet(rs);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<Actuator> getActuatorsFromResultSet(ResultSet rs) {
        try {
            Connection connection = connect();
            List<Actuator> actuators = new ArrayList<>();

            while(rs.next()) {
                Long id = rs.getLong("id");
                String model = rs.getString("model");
                String serialNumber = rs.getString("serialNumber");
                Long force = rs.getLong("force");
                LocalDate installationDate = rs.getDate("installationDate").toLocalDate();

                actuators.add(new Actuator(id, model, serialNumber, installationDate, force));
            }
            connection.close();
            return actuators;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void insertPump(Pump pump) {
        try {
            Connection connection = connect();

            PreparedStatement query = connection.prepareStatement("INSERT INTO pumps(model, serialNumber, flowRate, pressure, installationDate) VALUES (?, ?, ?, ?, ?)");
            query.setString(1, pump.getName());
            query.setString(2, pump.getSerialNumber());
            query.setLong(3, pump.getFlowRate());
            query.setLong(4, pump.getPressure());
            query.setDate(5, java.sql.Date.valueOf(pump.getInstallationDate()));

            query.executeUpdate();

            System.out.println("Pump saved into database");

            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
