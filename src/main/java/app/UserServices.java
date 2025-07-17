package app;

import database.UserDAO;
import model.Household;
import model.User;
import util.MessageBox;

public class UserServices {
    public static boolean registerUser(String firstName, String lastName, String userName, String email, String age, String gender, String address, String city) {
        if (firstName.isEmpty() || lastName.isEmpty()) {
            MessageBox.showAlert("Invalid Input", "First name and last name cannot be empty.");
            return false;
        }

        if (userName.isEmpty()) {
            MessageBox.showAlert("Invalid Input", "Username cannot be empty.");
            return false;
        }

        if (email.isEmpty()) {
            MessageBox.showAlert("Invalid Input", "Email cannot be empty.");
            return false;
        }

        if (age.isEmpty()) {
            MessageBox.showAlert("Invalid Input", "Age cannot be empty.");
            return false;
        }

        if (gender.isEmpty()) {
            MessageBox.showAlert("Invalid Input","gender cannot be empty.");
            return false;
        }

        if (address.isEmpty()) {
            MessageBox.showAlert("Invalid Input", "Address cannot be empty.");
            return false;
        }

        if (city.isEmpty()) {
            MessageBox.showAlert("Invalid Input", "City cannot be empty.");
            return false;
        }

        Household user = new Household(firstName, lastName, userName, email, Integer.parseInt(age), gender, address, city);

        try {
            UserDAO userDAO = new UserDAO();
            if (userDAO.registerHousehold(user)) {
                MessageBox.showInfo("Registration Successful", "Household registered successfully.");
                return true;
            } else {
                MessageBox.showError("Registration Failed", "Failed to register household.");
                return false;
            }
        } catch (Exception e) {
            MessageBox.showError("Database Error", "An error occurred while connecting to the database: " + e.getMessage());
            return false;
        }
    }
}
