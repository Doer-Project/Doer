package database;

import model.Household;
import model.Worker;

import java.sql.*;

public class UserDAO {
    Connection connection;
    ResultSet resultSet;
    public UserDAO() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/d1", "root", "");
        if (connection != null) {
            System.out.println("Database connection established successfully.");
        } else {
            System.out.println("Failed to establish database connection.");
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
        return true;
    }
}
