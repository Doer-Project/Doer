package database.household;

import model.PastWork;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PastWorkDAOImpl implements PastWorkDAO{

    private Connection conn;
    public PastWorkDAOImpl(Connection conn){
        this.conn = conn;
    }

    public List<PastWork> getPastWorkForUser(String username){
        List<PastWork> pastWorks = new ArrayList<>();

        String sql = "SELECT work_id, service_name, worker_name, date_completed, rating, review, amount_paid " +
                "FROM past_works " +
                "WHERE household_username = ? AND status = 'Completed' " +
                "ORDER BY date_completed DESC";
// ----------------   will be replace
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                PastWork work = new PastWork(
                        rs.getInt("work_id"),
                        rs.getString("service_name"),
                        rs.getString("worker_name"),
                        rs.getString("date_completed"),
                        rs.getInt("rating"),
                        rs.getString("review"),
                        rs.getDouble("amount_paid")
                );
                pastWorks.add(work);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
// ----------------   will be replace


        return  pastWorks;
    }
}
