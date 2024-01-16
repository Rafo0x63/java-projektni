package com.prod.hydraulicsystemsmaintenance.database;

import com.prod.hydraulicsystemsmaintenance.entities.Administrator;
import com.prod.hydraulicsystemsmaintenance.entities.Technician;
import com.prod.hydraulicsystemsmaintenance.entities.User;
import com.prod.hydraulicsystemsmaintenance.exceptions.UserAlreadyExistsException;
import com.prod.hydraulicsystemsmaintenance.exceptions.UserDoesntExistException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Database {
    public static Connection connect() {
        try {
            return DriverManager.getConnection(
                    "jdbc:mysql://localhost/hydraulicsystems", "antonio", ""
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

    public static boolean insertUser(User user) throws UserAlreadyExistsException {

        try {
            Connection connection = connect();

            if (!checkIfUserExists(user)) {
                PreparedStatement query = connection.prepareStatement("INSERT INTO users (name, username, password, administrator) VALUES (?, ?, ?, ?)");
                query.setString(1, user.getName());
                query.setString(2, user.getUsername());
                query.setString(3, user.getPassword());
                query.setInt(4, user.getAdministrator());
                if (query.executeUpdate() == 1) {
                    System.out.println("User created");
                    connection.close();
                    return true;

                } else {
                    System.out.println("Error creating user");
                    connection.close();
                    return false;
                }
            } else throw new UserAlreadyExistsException("User '" + user.getUsername() + "' already exists.");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static User loginUser(User user) throws UserDoesntExistException {
        try {
            Connection connection = connect();
            if (checkIfUserExists(user)) {
                PreparedStatement query = connection.prepareStatement("SELECT * FROM users WHERE username=? AND password=?");
                query.setString(1, user.getUsername());
                query.setString(2, user.getPassword());

                ResultSet rs = query.executeQuery();
                return getUserFromResultSet(rs);
            } else {
                connection.close();
                throw new UserDoesntExistException("User '" + user.getUsername() + "' doesn't exist.");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static User getUserByCriteria(User user) throws UserDoesntExistException {
        User resultUser = null;
        try {
            Connection connection = connect();

            StringBuilder str = new StringBuilder("SELECT * FROM users WHERE ");
            Integer conditionCounter = 0;
            if (user.getId() != null) {
                str.append("id=").append(user.getId());
                conditionCounter++;
            }
            if (user.getName() != null) {
                if (conditionCounter > 0) str.append(" AND ");
                str.append("name='").append(user.getName()).append("'");
                conditionCounter++;
            }
            if (user.getUsername() != null) {
                if (conditionCounter > 0) str.append(" AND ");
                str.append("username='").append(user.getUsername()).append("'");
                conditionCounter++;
            }
            if (user.getAdministrator() != null) {
                if (conditionCounter > 0) str.append(" AND ");
                str.append("administrator=").append(user.getAdministrator());
            }

            PreparedStatement query = connection.prepareStatement(str.toString());
            ResultSet rs = query.executeQuery();
            if (rs != null) {
                resultUser = getUserFromResultSet(rs);
            } else throw new UserDoesntExistException("User doesn't exist.");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return resultUser;
    }

    public static User getUserFromResultSet(ResultSet rs) {
        User user = null;
        try {
            while (rs.next()) {
                Integer id = rs.getInt("id");
                String name = rs.getString("name");
                String username = rs.getString("username");
                Integer administrator = rs.getInt("administrator");
                if (administrator.compareTo(0) == 0) {
                    user = new Technician(id, name, username, administrator);
                } else if (administrator.compareTo(1) == 0) {
                    user = new Administrator(id, name, username, administrator);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return user;
    }

    public static List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        try {
            Connection connection = connect();

            PreparedStatement query = connection.prepareStatement("SELECT * FROM users");
            query.executeUpdate();

            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return users;
    }
}
