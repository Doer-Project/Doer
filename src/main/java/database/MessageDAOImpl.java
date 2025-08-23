package database;

import java.sql.*;
import datastructures.CustomStack;
import util.DBConnection;

public class MessageDAOImpl implements MessageDAO {

    @Override
    public CustomStack<String> getMessagesForUser(String userId) {
        CustomStack<String> messageStack = new CustomStack<>();
        String query = "SELECT message, created_at FROM notifications WHERE receiver_id = ? ORDER BY created_at ASC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String msg = rs.getString("message");
                System.out.println(msg);
                Timestamp ts = rs.getTimestamp("created_at");
                messageStack.push(msg + " (" + ts.toString() + ")");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return messageStack;
    }

    // insert message into database.
    public boolean insertMessage(String receiverId, String message) {
        String query = "insert into notifications(receiver_id, message) values(?,?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, receiverId);
            stmt.setString(2, message);

            int rows = stmt.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
