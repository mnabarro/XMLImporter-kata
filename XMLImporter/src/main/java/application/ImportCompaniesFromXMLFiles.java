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

  private final PostgresConnector postgresConnector = new PostgresConnector();
  private final CompanyRepository companyRepository = new CompanyRepository();
  private final StaffRepository staffRepository = new StaffRepository();
  private final SalaryRepository salaryRepository = new SalaryRepository();

  public ImportCompaniesFromXMLFiles() {

  }
  final ArrayList<Company> companies = new ArrayList<>();
  LocalFileSystem localFileSystem = new LocalFileSystem();
  public void importCompaniesFromFiles(Path folderPath) throws IOException, JAXBException, SQLException {
    localFileSystem.importFiles(folderPath, companies);
    saveCompaniesToDatabase(companies);
  }

  private void saveCompaniesToDatabase(ArrayList<Company> companies) throws SQLException {

    Connection connection = postgresConnector.getConnection();

    for (Company company : companies) {

      final int companyId = companyRepository.insertAndReturnId(company, connection);
      createCompanyStaff(connection, company, companyId);
    }
  }
  private void createCompanyStaff(Connection connection, Company company, int companyId) throws SQLException {

    for (Staff staff : company.staff) {
      staffRepository.insert(connection, companyId, staff);
      createStaffSalary(connection, staff.id, staff.salary);
    }
  }

  private void createStaffSalary(Connection connection, int staffId, Salary salary) throws SQLException {

    salaryRepository.insert(connection, staffId, salary);
  }
}
