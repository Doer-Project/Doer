package database.worker;

import model.worker.AvailableWork;
import util.MessageBox;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AvailableRequestImpl implements AvailableRequestDAO{
    private Connection conn;

    public AvailableRequestImpl(Connection conn) {
        this.conn = conn;
    }

    @Override
    public List<AvailableWork> getAvailableRequests(String userId) {

        /// temporary solution ⬇️, This is not final we have to change it.

        List<AvailableWork> availableWorks = new ArrayList<>();
        String sql = "select request_id from requestrecipients where worker_id = ? and interest_status = 'pending'";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                availableWorks.add(addAvailableWork(rs.getString("request_id")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return availableWorks;
    }

    private AvailableWork addAvailableWork(String requestId) {
        String sql = "SELECT household_id, title, description, preferred_work_date, address, pincode FROM workrequests WHERE request_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, requestId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String sql2 = "SELECT first_name, last_name FROM users WHERE user_id = ?";
                PreparedStatement stmt2 = conn.prepareStatement(sql2);
                stmt2.setString(1, rs.getString("household_id"));
                ResultSet rs2 = stmt2.executeQuery();

                String householdName = "";

                if (rs2.next()) {
                    householdName = rs2.getString("first_name") + " " + rs2.getString("last_name");
                }

                return new AvailableWork(
                        requestId,
                        householdName,
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getString("preferred_work_date"),
                        rs.getString("address"),
                        rs.getInt("pincode")+""
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean markAsInterested(String requestId, String workerId, java.sql.Time startTime, java.sql.Time endTime, double estimatedCost) {
        if (startTime.compareTo(endTime) >= 0) {
            MessageBox.showError("Invalid Time", "Start time must be before end time.");
            return false;
        }
        String sql = "{CALL AcceptWorkRequest(?, ?, ?, ?, ?, ?)}";
        try (java.sql.CallableStatement cs = conn.prepareCall(sql)) {
            cs.setString(1, requestId);
            cs.setString(2, workerId);
            cs.setTime(3, startTime);
            cs.setTime(4, endTime);
            cs.setDouble(5, estimatedCost);
            cs.registerOutParameter(6, java.sql.Types.VARCHAR);
            cs.execute();
            String resultStatus = cs.getString(6);
            if ("SUCCESS".equals(resultStatus)) {
                return true;
            } else if ("TIME_CONFLICT".equals(resultStatus)) {
                MessageBox.showError("Time Conflict", "The selected time overlaps with another task.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            MessageBox.showError("Database Error", "Could not update interest status.");
        }
        return false;
    }

    @Override
    public boolean markAsNotInterested(String requestId, String workerId) {
        String sql = "UPDATE requestrecipients SET interest_status = 'not interested' WHERE request_id = ? AND worker_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, requestId);
            stmt.setString(2, workerId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}