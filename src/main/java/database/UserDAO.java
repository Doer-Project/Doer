package database;

import model.Household;
import model.Worker;

import java.sql.*;

public class UserDAO {
    Connection connection;
    ResultSet resultSet;
    public UserDAO() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/java_testing", "root", "");
        if (connection != null) {
            System.out.println("Database connection established successfully.");
        } else {
            System.out.println("Failed to establish database connection.");
        }
    }

    public boolean registerHousehold(Household user) throws SQLException {
        String firstName = user.getFirstName(), lastName = user.getLastName(), userName = user.getUserName(), email = user.getEmail(), gender = user.getGender(), address = user.getAddress(), city = user.getCity();
        int age = user.getAge();

        PreparedStatement statement = connection.prepareStatement("INSERT INTO household_users (first_name, last_name, user_name, email, age, gender, address, city) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");

        statement.setString(1, firstName);
        statement.setString(2, lastName);
        statement.setString(3, userName);
        statement.setString(4, email);
        statement.setInt(5, age);
        statement.setString(6, gender);
        statement.setString(7, address);
        statement.setString(8, city);

        statement.executeUpdate();

        System.out.println("Household registered: " + firstName + " " + lastName);
        return true;
    }

    public boolean registerWorker(Worker user) throws SQLException {
        String firstName = user.getFirstName(), lastName = user.getLastName(), userName = user.getUserName(), email = user.getEmail(), gender = user.getGender(), category = user.getCategory(), role = user.getRole();
        int age = user.getAge(), experience = user.getExperience();

        PreparedStatement statement;

        return true;
    }
}
