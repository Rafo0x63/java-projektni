package com.prod.hydraulicsystemsmaintenance.database;

import com.prod.hydraulicsystemsmaintenance.Application;
import com.prod.hydraulicsystemsmaintenance.entities.*;
import com.prod.hydraulicsystemsmaintenance.exceptions.UserAlreadyExistsException;
import com.prod.hydraulicsystemsmaintenance.exceptions.UserDoesntExistException;
import com.prod.hydraulicsystemsmaintenance.exceptions.WrongPasswordException;
import javafx.scene.control.Alert;
import org.slf4j.Logger;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

public class Database {
    private static Logger logger = Application.logger;
    public static Connection connect() {
        try {
            Properties properties = new Properties();
            properties.load(new FileReader("dat/database.properties"));

            return DriverManager.getConnection(
                    properties.getProperty("databaseURL"),
                    properties.getProperty("databaseUsername"),
                    properties.getProperty("databasePassword")
            );
        } catch (SQLException | FileNotFoundException e) {
            logger.error("There was a problem with the application", e);
            System.out.println("DB connection was unsuccessful.");
            throw new RuntimeException(e);
        } catch (IOException e) {
            logger.error("There was a problem with the application", e);
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
            logger.error("There was a problem with the application", e);
            throw new RuntimeException(e);
        }
    }

    public static String hashPassword(String password) {
        try {
            var digest = MessageDigest.getInstance("SHA-256").digest(password.getBytes());
            return new String(Base64.getEncoder().encode(digest));
        } catch (NoSuchAlgorithmException e) {
            logger.error("There was a problem with the application", e);
            throw new RuntimeException(e);
        }
    }

    public static void insertUser(User user) throws UserAlreadyExistsException {

        try {
            Connection connection = connect();

            if (!checkIfUserExists(user)) {
                PreparedStatement query = connection.prepareStatement("INSERT INTO users (name, username, password, administrator, isAdministratingASystem) VALUES (?, ?, ?, ?, ?)");
                query.setString(1, user.getName());
                query.setString(2, user.getUsername());
                query.setString(3, hashPassword(user.getPassword()));
                query.setInt(4, user.getAdministrator());
                query.setBoolean(5, false);
                if (query.executeUpdate() == 1) {
                    System.out.println("User created");
                    connection.close();

                } else {
                    System.out.println("Error creating user");
                    connection.close();
                }
            } else throw new UserAlreadyExistsException(STR."User '\{user.getUsername()}' already exists.");

        } catch (SQLException e) {
            logger.error("There was a problem with the application", e);
            throw new RuntimeException(e);
        }
    }

    public static User loginUser(String username, String password) throws UserDoesntExistException, WrongPasswordException {
        try {
            Connection connection = connect();
            if (checkIfUserExists(new User.Builder(username).build())) {
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
                connection.close();
                Alert alert = new Alert(Alert.AlertType.ERROR, "User doesn't exist.");
                alert.show();
                throw new UserDoesntExistException("User '" + username + "' doesn't exist.");
            }
            } catch (SQLException e) {
                logger.error("There was a problem with the application", e);
                throw new RuntimeException(e);
        }
    }

    public static List<User> getUsersByCriteria(User user) throws UserDoesntExistException {
        try {
            Connection connection = connect();

            StringBuilder str = new StringBuilder("SELECT * FROM users WHERE ");
            int conditionCounter = 0;
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
            logger.error("There was a problem with the application", e);
            throw new RuntimeException(e);
        }

    }

    public static List<User> getUsersFromResultSet(ResultSet rs) {
        try {
            List<User> users = new ArrayList<>();
            while (rs.next()) {
                Integer id = rs.getInt("id");
                String name = rs.getString("name");
                String username = rs.getString("username");
                Integer administrator = rs.getInt("administrator");
                if (administrator.compareTo(0) == 0) {
                    List<Actuator> actuators = Database.getAllActuators();
                    List<Pump> pumps = Database.getAllPumps();
                    List<Reservoir> reservoirs = Database.getAllReservoirs();
                    List<Valve> valves = Database.getAllValves();

                    List<Component> components = new ArrayList<>(actuators);
                    components.addAll(pumps);
                    components.addAll(reservoirs);
                    components.addAll(valves);

                    Set<Component> responsibleFor = new HashSet<>();
                    for (Component c : components) {
                        if (c.getUserId() == id) {
                            responsibleFor.add(c);
                        }
                    }
                    users.add(new Technician(id, name, username, administrator, responsibleFor));
                } else if (administrator.compareTo(1) == 0) {
                    boolean isAdministratingASystem = rs.getBoolean("isAdministratingASystem");
                    users.add(new Administrator(id, name, username, administrator, isAdministratingASystem));
                }
            }
            return users;
        } catch (SQLException e) {
            logger.error("There was a problem with the application", e);
            throw new RuntimeException(e);
        }
    }

    public static List<User> getAllUsers() {
        try {
            Connection connection = connect();

            PreparedStatement query = connection.prepareStatement("SELECT * FROM users");
            ResultSet rs = query.executeQuery();

            return getUsersFromResultSet(rs);

        } catch (SQLException e) {
            logger.error("There was a problem with the application", e);
            throw new RuntimeException(e);
        }
    }

    public static void insertActuator(Actuator actuator) {
        try {
            Connection connection = connect();

            PreparedStatement query = connection.prepareStatement("INSERT INTO actuators(model, serialNumber, `force`, installationDate, isInstalledInSystem) VALUES (?, ?, ?, ?, ?)");
            query.setString(1, actuator.getModel());
            query.setString(2, actuator.getSerialNumber());
            query.setInt(3, actuator.getForce());
            query.setDate(4, java.sql.Date.valueOf(actuator.getInstallationDate()));
            query.setBoolean(5, false);

            query.executeUpdate();

            System.out.println("Actuator saved into database");

            connection.close();
        } catch (SQLException e) {
            logger.error("There was a problem with the application", e);
            throw new RuntimeException(e);
        }
    }

    public static List<Actuator> getAllActuators() {
        try {
            Connection connection = connect();

            PreparedStatement query = connection.prepareStatement("SELECT * FROM actuators");
            ResultSet rs = query.executeQuery();

            return getActuatorsFromResultSet(rs);

        } catch (SQLException e) {
            logger.error("There was a problem with the application", e);
            throw new RuntimeException(e);
        }
    }

    public static List<Actuator> getActuatorsFromResultSet(ResultSet rs) {
        try {
            Connection connection = connect();
            List<Actuator> actuators = new ArrayList<>();

            while(rs.next()) {
                Integer id = rs.getInt("id");
                String model = rs.getString("model");
                String serialNumber = rs.getString("serialNumber");
                Integer force = rs.getInt("force");
                LocalDate installationDate = rs.getDate("installationDate").toLocalDate();
                boolean isInstalledInSystem = rs.getBoolean("isInstalledInSystem");
                Integer userId = rs.getInt("userId");

                actuators.add(new Actuator(id, model, serialNumber, installationDate, force, isInstalledInSystem, userId));
            }
            connection.close();
            return actuators;

        } catch (SQLException e) {
            logger.error("There was a problem with the application", e);
            throw new RuntimeException(e);
        }
    }

    public static void deleteActuator(Integer id) {
        try {
            Connection connection = connect();
            PreparedStatement query = connection.prepareStatement("DELETE FROM actuators WHERE id=?");
            query.setInt(1, id);

            query.executeUpdate();
            connection.close();
        } catch (SQLException e) {
            logger.error("There was a problem with the application", e);
            throw new RuntimeException(e);
        }
    }

    public static void updateActuator(Integer id, Actuator actuator) {
        try {
            Connection connection = connect();

            PreparedStatement query = connection.prepareStatement("UPDATE actuators SET model=?, serialNumber=?, `force`=?, installationDate=? WHERE id=?");
            query.setString(1, actuator.getModel());
            query.setString(2, actuator.getSerialNumber());
            query.setInt(3, actuator.getForce());
            query.setDate(4, Date.valueOf(actuator.getInstallationDate()));
            query.setInt(5, id);
            query.executeUpdate();

            connection.close();
        } catch (SQLException e) {
            logger.error("There was a problem with the application", e);
            throw new RuntimeException(e);
        }
    }

    public static void insertPump(Pump pump) {
        try {
            Connection connection = connect();

            PreparedStatement query = connection.prepareStatement("INSERT INTO pumps(model, serialNumber, flowRate, pressure, installationDate, isInstalledInSystem) VALUES (?, ?, ?, ?, ?, ?)");
            query.setString(1, pump.getModel());
            query.setString(2, pump.getSerialNumber());
            query.setInt(3, pump.getFlowRate());
            query.setInt(4, pump.getPressure());
            query.setDate(5, java.sql.Date.valueOf(pump.getInstallationDate()));
            query.setBoolean(6, false);

            query.executeUpdate();

            System.out.println("Pump saved into database");

            connection.close();
        } catch (SQLException e) {
            logger.error("There was a problem with the application", e);
            throw new RuntimeException(e);
        }
    }

    public static List<Pump> getAllPumps() {
        try {
            Connection connection = connect();

            PreparedStatement query = connection.prepareStatement("SELECT * FROM pumps");
            ResultSet rs = query.executeQuery();

            return getPumpsFromResultSet(rs);
        } catch (SQLException e) {
            logger.error("There was a problem with the application", e);
            throw new RuntimeException(e);
        }
    }

    public static List<Pump> getPumpsFromResultSet(ResultSet rs) {
        try {
            Connection connection = connect();
            List<Pump> pumps = new ArrayList<>();

            while (rs.next()) {
                Integer id = rs.getInt("id");
                String model = rs.getString("model");
                String serialNumber = rs.getString("serialNumber");
                LocalDate installationDate = rs.getDate("installationDate").toLocalDate();
                Integer flowRate = rs.getInt("flowRate");
                Integer pressure = rs.getInt("pressure");
                boolean isInstalledInSystem = rs.getBoolean("isInstalledInSystem");
                Integer userId = rs.getInt("userId");

                pumps.add(new Pump(id, model, serialNumber, installationDate, flowRate, pressure, isInstalledInSystem, userId));
            }

            connection.close();
            return pumps;
        } catch (SQLException e) {
            logger.error("There was a problem with the application", e);
            throw new RuntimeException(e);
        }
    }

    public static void deletePump(Integer id) {
        try {
            Connection connection = connect();
            PreparedStatement query = connection.prepareStatement("DELETE FROM pumps WHERE id=?");
            query.setInt(1, id);
            query.executeUpdate();

            connection.close();
        } catch (SQLException e) {
            logger.error("There was a problem with the application", e);
            throw new RuntimeException(e);
        }
    }

    public static void updatePump(Integer id, Pump pump) {
        try {
            Connection connection = connect();

            PreparedStatement query = connection.prepareStatement("UPDATE pumps SET model=?, serialNumber=?, flowRate=?, pressure=?, installationDate=? WHERE id=?");
            query.setString(1, pump.getModel());
            query.setString(2, pump.getSerialNumber());
            query.setInt(3, pump.getFlowRate());
            query.setInt(4, pump.getPressure());
            query.setDate(5, Date.valueOf(pump.getInstallationDate()));
            query.setInt(6, id);
            query.executeUpdate();

            connection.close();
        } catch (SQLException e) {
            logger.error("There was a problem with the application", e);
            throw new RuntimeException(e);
        }
    }

    public static void insertValve(Valve valve) {
        try {
            Connection connection = connect();
            PreparedStatement query = connection.prepareStatement("INSERT INTO valves(model, serialNumber, flowRate, pressure, installationDate, isInstalledInSystem) VALUES (?, ?, ?, ?, ?, ?)");
            query.setString(1, valve.getModel());
            query.setString(2, valve.getSerialNumber());
            query.setInt(3, valve.getFlowRate());
            query.setInt(4, valve.getPressure());
            query.setDate(5, Date.valueOf(valve.getInstallationDate()));
            query.setBoolean(6, false);

            query.executeUpdate();
            System.out.println("valve saved");
            connection.close();
        } catch (SQLException e) {
            logger.error("There was a problem with the application", e);
            throw new RuntimeException(e);
        }
    }

    public static List<Valve> getAllValves() {
        try {
            Connection connection = connect();

            PreparedStatement query = connection.prepareStatement("SELECT * FROM valves");
            ResultSet rs = query.executeQuery();

            return getValvesFromResultSet(rs);
        } catch (SQLException e) {
            logger.error("There was a problem with the application", e);
            throw new RuntimeException(e);
        }
    }

    public static List<Valve> getValvesFromResultSet(ResultSet rs) {
        try {
            Connection connection = connect();
            List<Valve> valves = new ArrayList<>();

            while (rs.next()) {
                Integer id = rs.getInt("id");
                String model = rs.getString("model");
                String serialNumber = rs.getString("serialNumber");
                LocalDate installationDate = rs.getDate("installationDate").toLocalDate();
                Integer flowRate = rs.getInt("flowRate");
                Integer pressure = rs.getInt("pressure");
                boolean isInstalledInSystem = rs.getBoolean("isInstalledInSystem");
                Integer userId = rs.getInt("userId");

                valves.add(new Valve(id, model, serialNumber, installationDate, flowRate, pressure, isInstalledInSystem, userId));
            }

            connection.close();
            return valves;
        } catch (SQLException e) {
            logger.error("There was a problem with the application", e);
            throw new RuntimeException(e);
        }
    }

    public static void deleteValve(Integer id) {
        try {
            Connection connection = connect();
            PreparedStatement query = connection.prepareStatement("DELETE FROM valves WHERE id=?");
            query.setInt(1, id);
            query.executeUpdate();

            connection.close();
        } catch (SQLException e) {
            logger.error("There was a problem with the application", e);
            throw new RuntimeException(e);
        }
    }

    public static void updateValve(Integer id, Valve valve) {
        try {
            Connection connection = connect();

            PreparedStatement query = connection.prepareStatement("UPDATE valves SET model=?, serialNumber=?, flowRate=?, pressure=?, installationDate=? WHERE id=?");
            query.setString(1, valve.getModel());
            query.setString(2, valve.getSerialNumber());
            query.setInt(3, valve.getFlowRate());
            query.setInt(4, valve.getPressure());
            query.setDate(5, Date.valueOf(valve.getInstallationDate()));
            query.setInt(6, id);
            query.executeUpdate();

            connection.close();
        } catch (SQLException e) {
            logger.error("There was a problem with the application", e);
            throw new RuntimeException(e);
        }
    }

    public static void insertReservoir(Reservoir reservoir) {
        try {
            Connection connection = connect();

            PreparedStatement query = connection.prepareStatement("INSERT INTO reservoirs(model, serialNumber, capacity, installationDate, isInstalledInSystem) VALUES (?, ?, ?, ?, ?)");
            query.setString(1, reservoir.getModel());
            query.setString(2, reservoir.getSerialNumber());
            query.setInt(3, reservoir.getCapacity());
            query.setDate(4, java.sql.Date.valueOf(reservoir.getInstallationDate()));
            query.setBoolean(5, false);

            query.executeUpdate();

            System.out.println("Reservoir saved into database");

            connection.close();
        } catch (SQLException e) {
            logger.error("There was a problem with the application", e);
            throw new RuntimeException(e);
        }
    }

    public static List<Reservoir> getAllReservoirs() {
        try {
            Connection connection = connect();

            PreparedStatement query = connection.prepareStatement("SELECT * FROM reservoirs");
            ResultSet rs = query.executeQuery();

            return getReservoirsFromResultSet(rs);

        } catch (SQLException e) {
            logger.error("There was a problem with the application", e);
            throw new RuntimeException(e);
        }
    }

    public static List<Reservoir> getReservoirsFromResultSet(ResultSet rs) {
        try {
            Connection connection = connect();
            List<Reservoir> reservoirs = new ArrayList<>();

            while(rs.next()) {
                Integer id = rs.getInt("id");
                String model = rs.getString("model");
                String serialNumber = rs.getString("serialNumber");
                Integer capacity = rs.getInt("capacity");
                LocalDate installationDate = rs.getDate("installationDate").toLocalDate();
                boolean isInstalledInSystem = rs.getBoolean("isInstalledInSystem");
                Integer userId = rs.getInt("userId");

                reservoirs.add(new Reservoir(id, model, serialNumber, installationDate, capacity, isInstalledInSystem, userId));
            }
            connection.close();
            return reservoirs;

        } catch (SQLException e) {
            logger.error("There was a problem with the application", e);
            throw new RuntimeException(e);
        }
    }

    public static void deleteReservoir(Integer id) {
        try {
            Connection connection = connect();
            PreparedStatement query = connection.prepareStatement("DELETE FROM reservoirs where id=?");
            query.setInt(1, id);
            query.executeUpdate();

            connection.close();
        } catch (SQLException e) {
            logger.error("There was a problem with the application", e);
            throw new RuntimeException(e);
        }
    }

    public static void updateReservoir(Integer id, Reservoir reservoir) {
        try {
            Connection connection = connect();

            PreparedStatement query = connection.prepareStatement("UPDATE reservoirs SET model=?, serialNumber=?, capacity=?, installationDate=? WHERE id=?");
            query.setString(1, reservoir.getModel());
            query.setString(2, reservoir.getSerialNumber());
            query.setInt(3, reservoir.getCapacity());
            query.setDate(4, Date.valueOf(reservoir.getInstallationDate()));
            query.setInt(5, id);
            query.executeUpdate();

            connection.close();
        } catch (SQLException e) {
            logger.error("There was a problem with the application", e);
            throw new RuntimeException(e);
        }
    }

    public static void updateComponentAndAdminState(HydraulicSystem system, boolean bool) {
        try {
            Connection connection = connect();
            PreparedStatement query = connection.prepareStatement("UPDATE actuators SET isInstalledInSystem=? WHERE id=?");
            query.setBoolean(1, bool);
            query.setInt(2, system.getActuator().getId());
            query.executeUpdate();

            query = connection.prepareStatement("UPDATE pumps SET isInstalledInSystem=? WHERE id=?");
            query.setBoolean(1, bool);
            query.setInt(2, system.getPump().getId());
            query.executeUpdate();

            query = connection.prepareStatement("UPDATE reservoirs SET isInstalledInSystem=? WHERE id=?");
            query.setBoolean(1, bool);
            query.setInt(2, system.getReservoir().getId());
            query.executeUpdate();

            query = connection.prepareStatement("UPDATE valves SET isInstalledInSystem=? WHERE id=?");
            query.setBoolean(1, bool);
            query.setInt(2, system.getValve().getId());
            query.executeUpdate();

            query = connection.prepareStatement("UPDATE users SET isAdministratingASystem=? WHERE id=?");
            query.setBoolean(1, bool);
            query.setInt(2, system.getAdministrator().getId());
            query.executeUpdate();

            connection.close();

        } catch (SQLException e) {
            logger.error("There was a problem with the application", e);
            throw new RuntimeException(e);
        }
    }

    public static void insertSystem(HydraulicSystem system) {
        try {
            Connection connection = connect();
            PreparedStatement query = connection.prepareStatement("INSERT INTO systems(name, actuatorId, pumpId, reservoirId, valveId, administratorId) VALUES(?, ?, ?, ?, ?, ?)");
            query.setString(1, system.getName());
            query.setInt(2, system.getActuator().getId());
            query.setInt(3, system.getPump().getId());
            query.setInt(4, system.getReservoir().getId());
            query.setInt(5, system.getValve().getId());
            query.setInt(6, system.getAdministrator().getId());
            query.executeUpdate();

            connection.close();

            updateComponentAndAdminState(system, true);
        } catch (SQLException e) {
            logger.error("There was a problem with the application", e);
            throw new RuntimeException(e);
        }
    }

    public static void deleteSystem(HydraulicSystem system) {
        try {
            Connection connection = connect();
            PreparedStatement query = connection.prepareStatement("DELETE FROM systems WHERE id=?");
            query.setInt(1, system.getId());
            query.executeUpdate();

            connection.close();

            updateComponentAndAdminState(system, false);
        } catch (SQLException e) {
            logger.error("There was a problem with the application", e);
            throw new RuntimeException(e);
        }
    }

    public static void updateSystem(HydraulicSystem oldSystem, HydraulicSystem newSystem) {
        try {
            Connection connection = connect();
            PreparedStatement query = connection.prepareStatement("UPDATE systems SET name=?, actuatorId=?, pumpId=?, reservoirId=?, valveId=?, administratorId=? WHERE id=?");
            query.setString(1, newSystem.getName());
            query.setInt(2, newSystem.getActuator().getId());
            query.setInt(3, newSystem.getPump().getId());
            query.setInt(4, newSystem.getReservoir().getId());
            query.setInt(5, newSystem.getValve().getId());
            query.setInt(6, newSystem.getAdministrator().getId());
            query.setInt(7, oldSystem.getId());

            updateComponentAndAdminState(oldSystem, false);
            query.executeUpdate();
            updateComponentAndAdminState(newSystem, true);
        } catch (SQLException e) {
            logger.error("There was a problem with the application", e);
            throw new RuntimeException(e);
        }
    }

    public static List<HydraulicSystem> getAllSystems() {
        try {
            Connection connection = connect();
            PreparedStatement query = connection.prepareStatement("SELECT * FROM systems");
            ResultSet rs = query.executeQuery();

            return getSystemsFromResultSet(rs);
        } catch (SQLException e) {
            logger.error("There was a problem with the application", e);
            throw new RuntimeException(e);
        }
    }

    public static List<HydraulicSystem> getSystemsFromResultSet(ResultSet rs) {
        List<HydraulicSystem> systems = new ArrayList<>();
        List<Actuator> actuators = getAllActuators();
        List<Pump> pumps = getAllPumps();
        List<Reservoir> reservoirs = getAllReservoirs();
        List<Valve> valves = getAllValves();
        List<User> users =  getAllUsers().stream().filter(user -> user.getAdministrator() == 1).toList();
        List<Administrator> administrators = new ArrayList<>();
        for (User user : users) {
            administrators.add((Administrator) user);
        }
        try {
            Connection connection = connect();

            while(rs.next()) {
                Integer id = rs.getInt("id");
                String name = rs.getString("name");
                Integer actuatorId = rs.getInt("actuatorId");
                Integer pumpId = rs.getInt("pumpId");
                Integer reservoirId = rs.getInt("reservoirId");
                Integer valveId = rs.getInt("valveId");
                Integer administratorId = rs.getInt("administratorId");
                Actuator actuator = actuators.stream().filter(a -> a.getId() == actuatorId).toList().getFirst();
                Pump pump = pumps.stream().filter(p -> p.getId() == pumpId).toList().getFirst();
                Reservoir reservoir = reservoirs.stream().filter(r -> r.getId() == reservoirId).toList().getFirst();
                Valve valve = valves.stream().filter(v -> v.getId() == valveId).toList().getFirst();
                Administrator administrator = administrators.stream().filter(a -> a.getId() == administratorId).toList().getFirst();

                HydraulicSystem system = new HydraulicSystem(id, name, actuator, pump, reservoir, valve, administrator);

                systems.add(system);
            }

            connection.close();

            return systems;
        } catch (SQLException e) {
            logger.error("There was a problem with the application", e);
            throw new RuntimeException(e);
        }
    }

    public static void insertRecord(ServiceRecord sr) {
        try {
            Connection connection = connect();
            PreparedStatement query = connection.prepareStatement("INSERT INTO serviceRecords (model, serialNumber, name, date) VALUES (?, ?, ?, ?)");
            query.setString(1, sr.model());
            query.setString(2, sr.serialNumber());
            query.setString(3, sr.name());
            query.setDate(4, Date.valueOf(sr.servicedOn()));
            query.executeUpdate();

            connection.close();
        } catch (SQLException e) {
            logger.error("There was a problem with the application", e);
            throw new RuntimeException(e);
        }
    }

    public static List<ServiceRecord> getAllRecords() {
        try {
            Connection connection = connect();
            PreparedStatement query = connection.prepareStatement("SELECT * FROM serviceRecords");
            ResultSet rs = query.executeQuery();

            return getRecordsFromResultSet(rs);
        } catch (SQLException e) {
            logger.error("There was a problem with the application", e);
            throw new RuntimeException(e);
        }
    }

    public static List<ServiceRecord> getRecordsFromResultSet(ResultSet rs) {
        try {
            Connection connection = connect();
            List<ServiceRecord> records = new ArrayList<>();
            while (rs.next()) {
                String model = rs.getString("model");
                String serialNumber = rs.getString("serialNumber");
                LocalDate date = rs.getDate("date").toLocalDate();
                String name = rs.getString("name");

                ServiceRecord record = new ServiceRecord(model, serialNumber, date, name);
                records.add(record);
            }
            connection.close();

            return records;
        } catch (SQLException e) {
            logger.error("There was a problem with the application", e);
            throw new RuntimeException(e);
        }
    }

    public static void assignTechnician(Integer userId, Component component) {
        try {
            Connection connection = connect();
            StringBuilder sb = new StringBuilder("UPDATE ");
            if (component.getClass() == Actuator.class) {
                sb.append("actuators ");
            }
            if (component.getClass() == Pump.class) {
                sb.append("pumps ");
            }
            if (component.getClass() == Reservoir.class) {
                sb.append("reservoirs ");
            }
            if (component.getClass() == Valve.class) {
                sb.append("valves ");
            }

            PreparedStatement query = connection.prepareStatement(STR."\{sb} SET userId=? WHERE id=?");
            query.setInt(1, userId);
            query.setInt(2, component.getId());
            query.executeUpdate();

            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}