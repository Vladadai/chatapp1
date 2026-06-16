package chat.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class Database {

    private static final String URL =
            "jdbc:sqlite:chat.db";

    public static Connection getConnection() throws Exception {

        Connection conn =
                DriverManager.getConnection(URL);

        Statement st = conn.createStatement();
        st.execute("PRAGMA journal_mode=WAL;");
        st.execute("PRAGMA busy_timeout = 5000;");

        return conn;
    }
}