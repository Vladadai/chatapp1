package chat.database;

import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserDAO {

    public static boolean register(
            String username,
            String password) {

        try {

            Connection connection =
                    Database.getConnection();

            String hash =
                    BCrypt.hashpw(
                            password,
                            BCrypt.gensalt());

            String sql =
                    "INSERT INTO users(username,password) VALUES(?,?)";

            PreparedStatement statement =
                    connection.prepareStatement(sql);

            statement.setString(1, username);
            statement.setString(2, hash);

            statement.executeUpdate();

            connection.close();

            return true;

        } catch (Exception e) {

            e.printStackTrace();

            return false;
        }
    }

    public static boolean login(
            String username,
            String password) {

        try {

            Connection connection =
                    Database.getConnection();

            String sql =
                    "SELECT password FROM users WHERE username=?";

            PreparedStatement statement =
                    connection.prepareStatement(sql);

            statement.setString(1, username);

            ResultSet rs =
                    statement.executeQuery();

            if (rs.next()) {

                String hash =
                        rs.getString("password");

                return BCrypt.checkpw(
                        password,
                        hash);
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return false;
    }

    public static boolean exists(
            String username) {

        try {

            Connection connection =
                    Database.getConnection();

            String sql =
                    "SELECT id FROM users WHERE username=?";

            PreparedStatement statement =
                    connection.prepareStatement(sql);

            statement.setString(1, username);

            return statement.executeQuery()
                    .next();

        } catch (Exception e) {

            e.printStackTrace();

            return false;
        }
    }
}