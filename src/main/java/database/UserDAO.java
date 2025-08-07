package database;

import model.Household;
import model.Worker;
import util.MessageBox;
import util.Validations;

import java.sql.*;

public class UserDAO {
    Connection connection;
    ResultSet resultSet;
    public UserDAO() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/doer_testing", "root", "");
        if (connection != null) {
            System.out.println("Database connection established successfully.");
        } else {
            System.out.println("Failed to establish database connection.");
        }
    }

    public String getUserNameByEmail(String email) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT username FROM users WHERE email = ?");
        statement.setString(1, email);
        resultSet = statement.executeQuery();

        if (resultSet.next()) {
            return resultSet.getString("username");
        } else {
            return null;
        }
    }

    public boolean registerHousehold(Household user) throws SQLException {
        String firstName = user.getFirstName(), lastName = user.getLastName(), userName = user.getUserName(), email = user.getEmail(), gender = user.getGender(), address = user.getAddress(), pinCode=user.getPin_code(), city = user.getCity(), passwordHash = user.getPasswordHash();
        int age = user.getAge();

        PreparedStatement userStatement = connection.prepareStatement("INSERT INTO users (username, firstname, lastname, age, gender, email) VALUES (?, ?, ?, ?, ?, ?)");

        userStatement.setString(1, userName);
        userStatement.setString(2, firstName);
        userStatement.setString(3, lastName);
        userStatement.setInt(4, age);
        userStatement.setString(5, gender);
        userStatement.setString(6, email);

        userStatement.executeUpdate();

        PreparedStatement typeStatement = connection.prepareStatement("INSERT INTO household (username, address, pin_code, city, password) VALUES (?, ?, ?, ?, ?)");

        typeStatement.setString(1, userName);
        typeStatement.setString(2, address);
        typeStatement.setString(3, pinCode);
        typeStatement.setString(4, city);
        typeStatement.setString(5, passwordHash);

        typeStatement.executeUpdate();

        System.out.println("Household registered: " + firstName + " " + lastName);
        return true;
    }

    public boolean registerWorker(Worker user) throws SQLException {
        String firstName = user.getFirstName(), lastName = user.getLastName(), userName = user.getUserName(), email = user.getEmail(), passwordHash = user.getPasswordHash(), preferredLocation = user.getPreferredLocation(), gender = user.getGender(), category = user.getCategory();
        int age = user.getAge(), experience = user.getExperience();

        PreparedStatement userStatement = connection.prepareStatement("INSERT INTO users (username, firstname, lastname, age, gender, email) VALUES (?, ?, ?, ?, ?, ?)");

        userStatement.setString(1, userName);
        userStatement.setString(2, firstName);
        userStatement.setString(3, lastName);
        userStatement.setInt(4, age);
        userStatement.setString(5, gender);
        userStatement.setString(6, email);

        userStatement.executeUpdate();

        PreparedStatement typeStatement = connection.prepareStatement("INSERT INTO worker (username, category, experience, preferred_city, password) VALUES (?, ?, ?, ?, ?)");

        typeStatement.setString(1, userName);
        typeStatement.setString(2, category);
        typeStatement.setInt(3, experience);
        typeStatement.setString(4, preferredLocation);
        typeStatement.setString(5, passwordHash);

        typeStatement.executeUpdate();

        System.out.println("Worker registered: " + firstName + " " + lastName);
        return true;
    }

    public boolean verifyUser(String username, String password) {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT acc_type FROM users WHERE username = ?");
            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                if (resultSet.getString(1).equals("household")) {
                    statement = connection.prepareStatement("SELECT password FROM household WHERE username = ?");
                } else {
                    statement = connection.prepareStatement("SELECT password FROM worker WHERE username = ?");
                }
                statement.setString(1, username);
                resultSet = statement.executeQuery();

                if (resultSet.next()) {
                    String storedPassword = resultSet.getString("password");
                    if (storedPassword.equals(password)) {
                        System.out.println("Worker user verified successfully.");
                        return true;
                    } else {
                        System.out.println("Invalid password for worker user.");
                        return false;
                    }
                } else {
                    System.out.println("Worker user not found.");
                    return false;
                }
            } else {
                System.out.println("User verification failed (user not found).");
                return false;
            }
        } catch (SQLException e) {
            System.err.println("Error verifying user: " + e.getMessage());
            return false;
        }
    }
}
