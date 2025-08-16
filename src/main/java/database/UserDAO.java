package database;

import model.Household;
import model.Worker;
import util.DBConnection;

import java.sql.*;

public class UserDAO {
    public boolean registerHousehold(Household user) {
        String firstName = user.getFirstName(), lastName = user.getLastName(), email = user.getEmail(), password = user.getPassword(), gender = user.getGender(), address = user.getAddress(), city = user.getCity();
        int age = user.getAge(), pinCode = user.getPin_code();
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement userStatement = connection.prepareStatement("INSERT INTO users (user_id, role, first_name, last_name, email, password_hash, age, gender) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
             PreparedStatement typeStatement = connection.prepareStatement("INSERT INTO households (household_id, address, city, pincode) VALUES (?, ?, ?, ?)")
        ) {
            userStatement.setString(1, "useridToBePassed");
            userStatement.setString(2, "household");
            userStatement.setString(3, firstName);
            userStatement.setString(4, lastName);
            userStatement.setString(5, email);
            userStatement.setString(6, password);
            userStatement.setInt(7, age);
            userStatement.setString(8, gender);
            userStatement.executeUpdate();

            typeStatement.setString(1, "useridToBePassed");
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

    public boolean registerWorker(Worker user) {
        String firstName = user.getFirstName(), lastName = user.getLastName(), email = user.getEmail(), password = user.getPassword(), gender = user.getGender(), category = user.getCategory(), workArea = user.getWorkArea();
        int age = user.getAge(), experience = user.getExperience();
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement userStatement = connection.prepareStatement("INSERT INTO users (user_id, role, first_name, last_name, email, password_hash, age, gender) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
             PreparedStatement typeStatement = connection.prepareStatement("INSERT INTO workers (worker_id, category, work_area, experience_years, rating_avg) VALUES (?, ?, ?, ?, ?)")
        ) {
            userStatement.setString(1, "useridToBePassed");
            userStatement.setString(2, "worker");
            userStatement.setString(3, firstName);
            userStatement.setString(4, lastName);
            userStatement.setString(5, email);
            userStatement.setString(6, password);
            userStatement.setInt(7, age);
            userStatement.setString(8, gender);
            userStatement.executeUpdate();

            typeStatement.setString(1, "useridToBePassed");
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
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT password_hash FROM users WHERE email = ?")
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
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT COUNT(*) FROM users WHERE email = ?")
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
}
