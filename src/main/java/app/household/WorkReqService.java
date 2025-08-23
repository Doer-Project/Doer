package app.household;

import database.household.WorkReqDAO;
import database.household.WorkReqDAOImpl;
import model.household.WorkRequest;
import util.MessageBox;
import util.Validations;

import java.time.LocalDate;

public class WorkReqService {

    private static WorkReqDAO dao;

    public WorkReqService(){
        dao = new WorkReqDAOImpl();
    }

    public WorkRequest createRequest(String title, String description, String category, String city, String area, String pinCode, LocalDate date, String householdId) {
        if (title == null || title.trim().isEmpty()) {
            MessageBox.showAlert("Validation Error", "Please enter a valid title!");
            return null;
        }
        if (description == null || description.trim().isEmpty()) {
            MessageBox.showAlert("Validation Error", "Please enter a valid description!");
            return null;
        }
        if (category == null || category.trim().isEmpty()) {
            MessageBox.showAlert("Validation Error", "Please select a valid category!");
            return null;
        }
        if (city == null || city.trim().isEmpty()) {
            MessageBox.showAlert("Validation Error", "Please enter a valid city!");
            return null;
        }
        if (area == null || area.trim().isEmpty()) {
            MessageBox.showAlert("Validation Error", "Please enter a valid area!");
            return null;
        }
        if (date == null || date.isBefore(LocalDate.now())) {
            MessageBox.showAlert("Validation Error", "Please select a valid date!");
            return null;
        }

        int pinCodeInt;
        if (!Validations.isValidPinCode(pinCode)) {
            MessageBox.showAlert("Validation Error", "Please enter a valid pin code!");
            return null;
        } else {
            pinCodeInt = Integer.parseInt(pinCode);
        }


        return new WorkRequest(title, description, category, city, area, pinCodeInt, date, householdId);
    }

    public boolean saveWorkRequest(String title, String description, String category, String city, String area, String pinCode, LocalDate date, String householdId) {
        WorkRequest request = createRequest(title, description, category, city, area, pinCode, date, householdId);

        if (request == null) return false;

        return dao.insert(request);
    }
}
