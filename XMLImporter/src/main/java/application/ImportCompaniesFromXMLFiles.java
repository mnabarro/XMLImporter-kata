package application;

import infraestructure.LocalFileSystem;
import infraestructure.database.CompanyRepository;
import infraestructure.database.PostgresConnector;
import infraestructure.database.SalaryRepository;
import infraestructure.database.StaffRepository;
import jakarta.xml.bind.JAXBException;
import java.io.IOException;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import xmlmodels.Company;
import xmlmodels.Salary;
import xmlmodels.Staff;

public class ImportCompaniesFromXMLFiles {

  public void importCompaniesFromFiles(Path folderPath) throws IOException, JAXBException, SQLException {
    LocalFileSystem.importFiles(folderPath);
    saveCompaniesToDatabase(CompanyList.companies);
  }

  private static void saveCompaniesToDatabase(ArrayList<Company> companies) throws SQLException {

    Connection connection = PostgresConnector.getConnection();

    for (Company company : companies) {

      final int companyId = CompanyRepository.insertAndReturnId(company, connection);
      createCompanyStaff(company, companyId, connection);
    }
  }
  private static void createCompanyStaff(Company company, int companyId, Connection connection) throws SQLException {

    for (Staff staff : company.staff) {
      StaffRepository.insert(connection, companyId, staff);
      createStaffSalary(connection, staff.id, staff.salary);
    }
  }

  private static void createStaffSalary(Connection connection, int staffId, Salary salary) throws SQLException {

    SalaryRepository.insert(connection, staffId, salary);
  }
}
