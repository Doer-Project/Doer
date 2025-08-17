package app.household;

import database.household.WorkReqDAO;
import database.household.WorkReqDAOImpl;
import model.household.WorkRequest;
import util.DatabaseConnection;
import util.MessageBox;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;

public class WorkReqService {

    // ✅ Create WorkRequest object with validations
    public static WorkRequest createRequest(String title, String description, int categoryId,
                                            String city, String area, double budget,
                                            LocalDate date, String householdId) {
        // Basic validations
        if (title == null || title.trim().isEmpty()) return null;
        if (description == null || description.trim().isEmpty()) return null;
        if (budget <= 0) {
            MessageBox.showAlert("Validation Error", "Please enter a valid budget!");
            return null;
        }
        if (date == null) {
            MessageBox.showAlert("Validation Error", "Please enter a valid Date!");
            return null;
        }
        if (city == null || city.trim().isEmpty()) return null;

        // Valid → create object
        return new WorkRequest(title, description, categoryId, city, area, budget, date, householdId);
    }

    // ✅ Save WorkRequest with fresh connection each time
    public boolean saveWorkRequest(WorkRequest request) {
        if (request == null) return false;

        try (Connection conn = DatabaseConnection.getConnection()) {
            WorkReqDAO dao = new WorkReqDAOImpl(conn);
            return dao.insert(request);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            MessageBox.showAlert("DB Error", "Failed to save work request.");
            return false;
        }
    }
}
