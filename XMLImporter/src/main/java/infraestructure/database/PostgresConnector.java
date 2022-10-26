package infraestructure.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class PostgresConnector {

  public static Connection getConnection() {
    Connection conn;
    try {
      conn = DriverManager.getConnection("jdbc:postgresql://127.0.0.1:5432/postgres", "postgres", "postgres");
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    return conn;
  }
}
