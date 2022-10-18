import infraestructure.LocalFileSystem;
import infraestructure.database.CompanyRepository;
import infraestructure.database.PostgresConnection;
import infraestructure.database.SalaryRepository;
import infraestructure.database.StaffRepository;
import jakarta.xml.bind.JAXBException;
import xmlmodels.Company;
import xmlmodels.Staff;

import java.io.IOException;
import java.nio.file.Path;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BatchXmlImporter {
    public void importFiles(Path folderPath) throws IOException, JAXBException, SQLException {

        List<Path> paths = LocalFileSystem.getPathList(folderPath);

        ArrayList<Company> companies = new ArrayList<>();

        LocalFileSystem.fileListToCompanyList(paths, companies);

        processCompanies(companies);
    }

    private static void processCompanies(ArrayList<Company> companies) throws SQLException {

        Connection conn = PostgresConnection.getConnection();

        for (Company company : companies) {

            final int companyId = CompanyRepository.insert(company, conn);

            for (Staff staff : company.staff) {
                StaffRepository.insert(conn, companyId, staff);
                SalaryRepository.insert(conn, staff);
            }
        }
    }
}
