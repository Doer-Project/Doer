package app;

import database.UserDAO;
import model.Household;
import model.Worker;
import util.MessageBox;
import util.Validations;

public class UserServices {
    public static boolean registerHousehold(String firstName, String lastName, String userName, String email, String age, String gender, String address, String city, String pin_code, String passwordHash) {
        if (!(Validations.isAlphabetOnly(firstName) || Validations.isAlphabetOnly(lastName))) {
            MessageBox.showAlert("Invalid Input", "First name and last name must contain only alphabets.");
            return false;
        }

        if (!Validations.isValidUsername(userName)) {
            MessageBox.showAlert("Invalid Input", "Username must be between 3 to 20 characters and can only contain letters, numbers, and underscores.");
            return false;
        }

        if (!Validations.isValidEmail(email)) {
            MessageBox.showAlert("Invalid Input", "Email format is invalid.");
            return false;
        }

        int ageInt;

        try {
            ageInt = Integer.parseInt(age);
        } catch (NumberFormatException e) {
            MessageBox.showAlert("Invalid Input", "Age must be a valid number.");
            return false;
        }

        if (!Validations.isAlphabetOnly(city)) {
            MessageBox.showAlert("Invalid Input", "City must contain only alphabets.");
            return false;
        }

        if (!Validations.isStrongPassword(passwordHash)) {
            MessageBox.showAlert("Invalid Input", "Password must be between 8 to 15 characters and contain at least one uppercase letter, one lowercase letter, one digit, and one special character.");
            return false;
        }

        try {
            Integer.parseInt(pin_code);
        } catch (NumberFormatException e) {
            MessageBox.showAlert("Invalid Input", "Pin code must be a valid number.");
            return false;
        }

        Household user = new Household(firstName, lastName, userName, email, ageInt, gender, address, city, pin_code, passwordHash);

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

    public static boolean registerWorker(String firstName, String lastName, String userName, String email, String age, String gender, String category, String experience, String passwordHash, String preferredCity) {
        if (!(Validations.isAlphabetOnly(firstName) || Validations.isAlphabetOnly(lastName))) {
            MessageBox.showAlert("Invalid Input", "First name and last name must contain only alphabets.");
            return false;
        }

        if (!Validations.isValidUsername(userName)) {
            MessageBox.showAlert("Invalid Input", "Username must be between 3 to 20 characters and can only contain letters, numbers, and underscores.");
            return false;
        }

        if (!Validations.isValidEmail(email)) {
            MessageBox.showAlert("Invalid Input", "Email format is invalid.");
            return false;
        }

        int ageInt;
        try {
            ageInt = Integer.parseInt(age);
        } catch (NumberFormatException e) {
            MessageBox.showAlert("Invalid Input", "Age must be a valid number.");
            return false;
        }

        if (!Validations.isAlphabetOnly(preferredCity)) {
            MessageBox.showAlert("Invalid Input", "Preferred city must contain only alphabets.");
            return false;
        }

        if (!Validations.isStrongPassword(passwordHash)) {
            MessageBox.showAlert("Invalid Input", "Password must be between 8 to 15 characters and contain at least one uppercase letter, one lowercase letter, one digit, and one special character.");
            return false;
        }

        int experienceInt;

        try {
            experienceInt = Integer.parseInt(experience);
        } catch (NumberFormatException e) {
            MessageBox.showAlert("Invalid Input", "Experience must be a valid number.");
            return false;
        }

        Worker user = new Worker(firstName, lastName, userName, email, ageInt, gender, category, experienceInt, passwordHash, preferredCity);

        try {
            UserDAO userDAO = new UserDAO();
            if (userDAO.registerWorker(user)) {
                MessageBox.showInfo("Registration Successful", "Worker registered successfully.");
                return true;
            } else {
                MessageBox.showError("Registration Failed", "Failed to register worker.");
                return false;
            }
        } catch (Exception e) {
            MessageBox.showError("Database Error", "An error occurred while connecting to the database: " + e.getMessage());
            return false;
        }
    }

    public static String getUserEmail(String email) {
        try {
            UserDAO userDAO = new UserDAO();
            String userName = userDAO.getUserNameByEmail(email);
            if (userName != null) {
                return userName;
            } else {
                MessageBox.showError("User Not Found", "No user found with the provided email.");
                return null;
            }
        } catch (Exception e) {
            MessageBox.showError("Database Error", "An error occurred while connecting to the database: " + e.getMessage());
            return null;
        }
    }

    public static boolean verifyUser(String username, String password) {
        try {
            UserDAO userDAO = new UserDAO();
            if (userDAO.verifyUser(username, password)) {
                MessageBox.showInfo("Verification Successful", "User verified successfully.");
                return true;
            } else {
                MessageBox.showError("Verification Failed", "Invalid email or password.");
                return false;
            }
        } catch (Exception e) {
            MessageBox.showError("Database Error", "An error occurred while connecting to the database: " + e.getMessage());
            return false;
        }
    }
}