package infraestructure.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import xmlmodels.Salary;

public class SalaryRepository {

  public static void insert(Connection connection, int staffId, Salary salary) throws SQLException {
    try (PreparedStatement preparedStatement = connection.prepareStatement(
      "INSERT INTO salary(staff_id, currency, value) VALUES (?,?,?)")) {
      preparedStatement.setInt(1, staffId);
      preparedStatement.setString(2, salary.currency);
      preparedStatement.setInt(3, salary.value);
      preparedStatement.executeUpdate();
    }
  }
}
