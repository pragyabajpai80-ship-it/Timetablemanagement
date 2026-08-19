import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static final String URL =
            "jdbc:mariadb://mysql-29c31589-pbjpai56-0f90.e.aivencloud.com:23290/defaultdb?useSsl=true&trustServerCertificate=true";

    private static final String USER = "avnadmin";

    private static final String PASSWORD =
        System.getenv("AIVEN_DB_PASSWORD");

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void main(String[] args) {
        try {
            Connection connection = getConnection();

            System.out.println("Database connected successfully!");

            connection.close();

        } catch (SQLException e) {
            System.out.println("Database connection failed!");
            e.printStackTrace();
        }
    }
}
