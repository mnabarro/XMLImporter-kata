package infraestructure.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import xmlmodels.Company;

public class CompanyRepository {

  public static int insertAndReturnId(Company company, Connection conn) throws SQLException {

    try (PreparedStatement preparedStatement = conn.prepareStatement("INSERT INTO company(name) VALUES (?)",
      Statement.RETURN_GENERATED_KEYS)) {
      preparedStatement.setString(1, company.name);
      preparedStatement.executeUpdate();

      return getNewCompanyId(preparedStatement);
    }
  }

  private static int getNewCompanyId(PreparedStatement preparedStatement) throws SQLException {
    final int companyId;

    try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
        if (generatedKeys.next()) {
            companyId = (int) generatedKeys.getLong(1);
        } else {
            throw new SQLException("No ID obtained.");
        }
    }
    return companyId;

  }
}
