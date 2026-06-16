package chat.database;

import chat.model.Message;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.util.ArrayList;
import java.util.List;

public class MessageDAO {

    public static void saveMessage(
            String sender,
            String message) {

        try {

            Connection connection =
                    Database.getConnection();

            String sql =
                    "INSERT INTO messages(sender,message) VALUES (?,?)";

            PreparedStatement statement =
                    connection.prepareStatement(sql);

            statement.setString(1, sender);
            statement.setString(2, message);

            statement.executeUpdate();

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public static List<Message> getLastMessages(
            int limit) {

        List<Message> messages =
                new ArrayList<>();

        try {

            Connection connection =
                    Database.getConnection();

            String sql =
                    """
                    SELECT *
                    FROM messages
                    ORDER BY id DESC
                    LIMIT ?
                    """;

            PreparedStatement statement =
                    connection.prepareStatement(sql);

            statement.setInt(1, limit);

            ResultSet rs =
                    statement.executeQuery();

            while (rs.next()) {

                messages.add(
                        new Message(
                                rs.getInt("id"),
                                rs.getString("sender"),
                                rs.getString("message"),
                                rs.getTimestamp("created_at")
                        )
                );
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return messages;
    }
}