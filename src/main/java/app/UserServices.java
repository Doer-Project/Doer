package app;

import database.UserDAO;
import model.Household;
import model.SessionManager;
import model.Worker;
import util.MessageBox;
import util.Validations;

public class UserServices {
    private static UserDAO userDAO;

    public UserServices() {
        userDAO = new UserDAO();
    }

    public boolean registerHousehold(String role, String firstName, String lastName, String email, String password, String age, String gender, String address, String city, String pin_code){
        if (!(Validations.isAlphabetOnly(firstName) || Validations.isAlphabetOnly(lastName))) {
            MessageBox.showAlert("Invalid Input", "First name and last name must contain only alphabets.");
            return false;
        }
        if (!Validations.isValidEmail(email)) {
            MessageBox.showAlert("Invalid Input", "Email format is invalid.");
            return false;
        }
        if (Validations.isRegistered(email)) {
            MessageBox.showAlert("Registration Failed", "Email is already registered.");
            return false;
        }
        if (!Validations.isStrongPassword(password)) {
            MessageBox.showAlert("Invalid Input", "Password must be between 8 to 15 characters and contain at least one uppercase letter, one lowercase letter, one digit, and one special character.");
            return false;
        }
        int ageInt, pinCodeInt;
        try {
            ageInt = Integer.parseInt(age);
        } catch (NumberFormatException e) {
            MessageBox.showAlert("Invalid Input", "Age must be a valid number.");
            return false;
        }
        if (!Validations.isValidPinCode(pin_code)) {
            MessageBox.showAlert("Invalid Input", "Pin code must be a valid 6-digit number.");
            return false;
        } else {
            pinCodeInt = Integer.parseInt(pin_code);
        }

        Household user = new Household(role, firstName, lastName, email, password, ageInt, gender, address, city, pinCodeInt);

        String userId = userDAO.generateHouseholdId(user);
        SessionManager.setUserID(userId);

        if (userDAO.registerHousehold(userId, user)) {
            System.out.println("Household registered successfully: " + firstName + " " + lastName);
            return true;
        } else {
            MessageBox.showError("Registration Failed", "Failed to register household.");
            return false;
        }
    }

    public boolean registerWorker(String role, String firstName, String lastName, String email, String password, String age, String gender, String category, String experience, String workArea){
        if (!(Validations.isAlphabetOnly(firstName) || Validations.isAlphabetOnly(lastName))) {
            MessageBox.showAlert("Invalid Input", "First name and last name must contain only alphabets.");
            return false;
        }
        if (!Validations.isValidEmail(email)) {
            MessageBox.showAlert("Invalid Input", "Email format is invalid.");
            return false;
        }
        if (Validations.isRegistered(email)) {
            MessageBox.showAlert("Registration Failed", "Email is already registered.");
            return false;
        }
        if (!Validations.isStrongPassword(password)) {
            MessageBox.showAlert("Invalid Input", "Password must be between 8 to 15 characters and contain at least one uppercase letter, one lowercase letter, one digit, and one special character.");
            return false;
        }
        if (!Validations.isAlphabetOnly(workArea)) {
            MessageBox.showAlert("Invalid Input", "Work area must contain only alphabets.");
            return false;
        }
        int ageInt, experienceInt;
        try {
            ageInt = Integer.parseInt(age);
        } catch (NumberFormatException e) {
            MessageBox.showAlert("Invalid Input", "Age must be valid numbers.");
            return false;
        }
        try {
            experienceInt = Integer.parseInt(experience);
        } catch (NumberFormatException e) {
            MessageBox.showAlert("Invalid Input", "Experience must be valid numbers.");
            return false;
        }

        Worker user = new Worker(role, firstName, lastName, email, password, ageInt, gender, category, experienceInt, workArea);

        String userId = userDAO.generateWorkerId(user);
        SessionManager.setUserID(userId);

        if (userDAO.registerWorker(userId, user)) {
            System.out.println("Worker registered successfully: " + firstName + " " + lastName);
            return true;
        } else {
            MessageBox.showError("Registration Failed", "Failed to register worker.");
            return false;
        }
    }

    public boolean verifyUser(String email, String password) {
        if (!Validations.isValidEmail(email)) {
            MessageBox.showAlert("Invalid Input", "Email format is invalid.");
            return false;
        }

        if (userDAO.verifyUser(email, password)) {
            System.out.println("User verified successfully.");
            return true;
        } else {
            MessageBox.showError("Verification Failed", "Invalid email or password.");
            return false;
        }
    }

    public String getUserIdByEmail(String email) {
        return userDAO.getUserIdByEmail(email);
    }

    public String getEmailByUserId(String userId) {
        return userDAO.getUserIdByEmail(userId);
    }
}