package database;

import model.Household;
import model.Worker;
import util.DBConnection;
import util.MessageBox;

import java.sql.*;
import java.time.LocalDate;

public class UserDAO {
    Connection connection;
    public UserDAO() {
        try {
            connection = DBConnection.getConnection();
        } catch (SQLException e) {
            System.err.println("Error connecting to the database: " + e.getMessage());
            throw new RuntimeException("Database connection failed", e);
        }
    }

    public boolean registerHousehold(String userID, Household user) {
        String firstName = user.getFirstName(), lastName = user.getLastName(), email = user.getEmail(), password = user.getPassword(), gender = user.getGender(), address = user.getAddress(), city = user.getCity();
        int age = user.getAge(), pinCode = user.getPin_code();
        try (PreparedStatement userStatement = connection.prepareStatement("INSERT INTO users (user_id, role, first_name, last_name, email, password_hash, age, gender) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
             PreparedStatement typeStatement = connection.prepareStatement("INSERT INTO households (household_id, address, city, pincode) VALUES (?, ?, ?, ?)")
        ) {
            userStatement.setString(1, userID);
            userStatement.setString(2, "household");
            userStatement.setString(3, firstName);
            userStatement.setString(4, lastName);
            userStatement.setString(5, email);
            userStatement.setString(6, password);
            userStatement.setInt(7, age);
            userStatement.setString(8, gender);
            userStatement.executeUpdate();

            typeStatement.setString(1, userID);
            typeStatement.setString(2, address);
            typeStatement.setString(3, city);
            typeStatement.setInt(4, pinCode);
            typeStatement.executeUpdate();

            System.out.println("Household registered: " + firstName + " " + lastName);
            return true;
        } catch (SQLException e) {
            System.out.println("Error registering household: " + e.getMessage());
            return false;
        }
    }

    public boolean registerWorker(String userID, Worker user) {
        String firstName = user.getFirstName(), lastName = user.getLastName(), email = user.getEmail(), password = user.getPassword(), gender = user.getGender(), category = user.getCategory(), workArea = user.getWorkArea();
        int age = user.getAge(), experience = user.getExperience();
        try (PreparedStatement userStatement = connection.prepareStatement("INSERT INTO users (user_id, role, first_name, last_name, email, password_hash, age, gender) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
             PreparedStatement typeStatement = connection.prepareStatement("INSERT INTO workers (worker_id, category, work_area, experience_years, rating_avg) VALUES (?, ?, ?, ?, ?)")
        ) {
            userStatement.setString(1, userID);
            userStatement.setString(2, "worker");
            userStatement.setString(3, firstName);
            userStatement.setString(4, lastName);
            userStatement.setString(5, email);
            userStatement.setString(6, password);
            userStatement.setInt(7, age);
            userStatement.setString(8, gender);
            userStatement.executeUpdate();

            typeStatement.setString(1, userID);
            typeStatement.setString(2, category);
            typeStatement.setString(3, workArea);
            typeStatement.setInt(4, experience);
            typeStatement.setString(5, "0.0");
            typeStatement.executeUpdate();

            System.out.println("Worker registered: " + firstName + " " + lastName);
            return true;
        } catch (SQLException e) {
            System.out.println("Error registering worker: " + e.getMessage());
            return false;
        }
    }

    public boolean verifyUser(String email, String password) {
        try (PreparedStatement statement = connection.prepareStatement("SELECT password_hash FROM users WHERE email = ?")
        ) {
            statement.setString(1, email);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    String storedPassword = resultSet.getString("password_hash");
                    return storedPassword.equals(password);
                } else {
                    System.out.println("User not found with email: " + email);
                    return false;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error verifying user: " + e.getMessage());
            return false;
        }
    }

    public boolean isRegistered(String email) {
        try (PreparedStatement statement = connection.prepareStatement("SELECT COUNT(*) FROM users WHERE email = ?")
        ) {
            statement.setString(1, email);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int count = resultSet.getInt(1);
                    return count > 0;
                } else {
                    return false;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error checking registration: " + e.getMessage());
            return false;
        }
    }

    public String generateHouseholdId(Household user) {
        String role = user.getRole().substring(0, 1).toUpperCase();
        String cityCode = user.getCity().substring(0, 3).toUpperCase();
        String year = String.valueOf(LocalDate.now().getYear()).substring(2);
        String categoryCode = "HH";
        String serial = getSerial();

        return role + cityCode + year + categoryCode + serial;
    }

    public String generateWorkerId(Worker user) {
        String role = user.getRole().substring(0, 1).toUpperCase();
        String cityCode = user.getWorkArea().substring(0, 3).toUpperCase();
        String year = String.valueOf(LocalDate.now().getYear()).substring(2);
        String categoryCode = user.getCategory().substring(0, 2).toUpperCase();
        String serial = getSerial();

        return role + cityCode + year + categoryCode + serial;
    }

    public String getSerial () {
        String serial = "";
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("SELECT count(*) FROM users");
            if (resultSet.next()) {
                String count = resultSet.getInt(1)+"";
                switch (count.length()) {
                    case 1 -> serial = "000" + count;
                    case 2 -> serial = "00" + count;
                    case 3 -> serial = "0" + count;
                    default -> serial = count;
                }
            }
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return serial;
    }

    public String getUserIdByEmail(String email) {
        String userId = null;
        try (PreparedStatement statement = connection.prepareStatement("SELECT user_id FROM users WHERE email = ?")) {
            statement.setString(1, email);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    userId = resultSet.getString("user_id");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching user ID: " + e.getMessage());
        }
        return userId;
    }

    public String getEmailByUserId(String userId) {
        String sql = "Select email from users where user_id = ?";
        String email = "";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1,userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                email = rs.getString("email");
            }
        } catch (SQLException e) {
            MessageBox.showAlert("Databse","There is some error in Databse");
        }
        return email;
    }
}