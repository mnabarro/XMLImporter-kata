package infraestructure.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import xmlmodels.Staff;

public class SalaryRepository {

  public static void insert(Connection conn, Staff staff) throws SQLException {
    try (PreparedStatement preparedStatement = conn.prepareStatement(
      "INSERT INTO salary(staff_id, currency, value) VALUES (?,?,?)")) {
      preparedStatement.setInt(1, staff.id);
      preparedStatement.setString(2, staff.salary.currency);
      preparedStatement.setInt(3, staff.salary.value);
      preparedStatement.executeUpdate();
    }
  }
}
