package chat.service;

import chat.database.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ChatService {

    // сохранить сообщение
    public static void saveMessage(String sender, String message) {

        try (Connection conn = Database.getConnection()) {

            String sql = """
                    INSERT INTO messages(sender, message)
                    VALUES (?, ?)
                    """;

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, sender);
            ps.setString(2, message);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // загрузить историю
    public static List<String> loadLastMessages(int limit) {

        List<String> list = new ArrayList<>();

        try (Connection conn = Database.getConnection()) {

            String sql = """
                    SELECT sender, message
                    FROM messages
                    ORDER BY id DESC
                    LIMIT ?
                    """;

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, limit);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(
                        rs.getString("sender")
                                + ": "
                                + rs.getString("message")
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
}