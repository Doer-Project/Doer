package database;

import java.sql.*;
import datastructures.CustomStack;
import util.DBConnection;

public class MessageDAOImpl implements MessageDAO {

    @Override
    public CustomStack<String> getMessagesForUser(String userId) {
        CustomStack<String> messageStack = new CustomStack<>();
        String query = "SELECT sender_name, message, created_at FROM notifications WHERE receiver_id = ? ORDER BY created_at ASC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

//            stmt.setString(1, userId);
            /// checking
            stmt.setString(1,"H100");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String sender = rs.getString("sender_name");
                String msg = rs.getString("message");
                Timestamp ts = rs.getTimestamp("created_at");
                messageStack.push(sender + ": " + msg + " (" + ts.toString() + ")");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return messageStack;
    }

    // insert message into database.
    public boolean insertMessage(String receiverId, String senderName, String message) {
        String query = "INSERT INTO notification (receiver_id, sender_name, message) VALUES (?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, receiverId);
            stmt.setString(2, senderName);
            stmt.setString(3, message);

            int rows = stmt.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
