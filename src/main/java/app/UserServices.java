package app;

import database.UserDAO;
import model.household.Household;
import model.worker.Worker;
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
        try {
            pinCodeInt = Integer.parseInt(pin_code);
        } catch (NumberFormatException e) {
            MessageBox.showAlert("Invalid Input", "Pin code must be a valid number.");
            return false;
        }

        Household user = new Household(role, firstName, lastName, email, password, ageInt, gender, address, city, pinCodeInt);

        if (userDAO.registerHousehold(user)) {
            MessageBox.showInfo("Registration Successful", "Household registered successfully.");
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

        if (userDAO.registerWorker(user)) {
            MessageBox.showInfo("Registration Successful", "Worker registered successfully.");
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
}