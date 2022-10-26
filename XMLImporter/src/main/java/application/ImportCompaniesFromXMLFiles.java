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
import xmlmodels.Staff;

public class ImportCompaniesFromXMLFiles {

  private static void saveCompaniesToDatabase(ArrayList<Company> companies) throws SQLException {

    Connection connection = PostgresConnector.getConnection();

    for (Company company : companies) {

      final int companyId = CompanyRepository.insert(company, connection);

      for (Staff staff : company.staff) {
        StaffRepository.insert(connection, companyId, staff);
        SalaryRepository.insert(connection, staff);
      }
    }
  }

  public void importCompaniesFromFiles(Path folderPath) throws IOException, JAXBException, SQLException {
    LocalFileSystem.importFiles(folderPath);
    saveCompaniesToDatabase(CompanyList.companies);
  }
}
