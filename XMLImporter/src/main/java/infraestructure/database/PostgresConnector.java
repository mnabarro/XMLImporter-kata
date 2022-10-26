package infraestructure.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class PostgresConnector {

  public static Connection getConnection() {
    Connection connection;
    try {
      connection = DriverManager.getConnection("jdbc:postgresql://127.0.0.1:5432/postgres", "postgres", "postgres");
    } catch (SQLException exception) {
      throw new RuntimeException(exception);
    }
    return connection;
  }
}
