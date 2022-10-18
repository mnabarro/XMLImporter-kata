import converters.FileListToCompanyList;
import infraestructure.LocalFileSystem;
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

        FileListToCompanyList.convert(paths, companies);

        processCompanies(companies);
    }


    private static void processCompanies(ArrayList<Company> companies) throws SQLException {

        Connection conn = PostgresConnection.getConnection();

        for (Company company : companies) {

            final int companyId = insertCompany(company, conn);

            for (Staff staff : company.staff) {
                StaffRepository.insert(conn, companyId, staff);
                SalaryRepository.insert(conn, staff);
            }
        }
    }

    private static int insertCompany(Company company, Connection conn) throws SQLException {
        final int companyId;
        try (PreparedStatement preparedStatement = conn.prepareStatement("INSERT INTO company(name) VALUES (?)", Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, company.name);
            preparedStatement.executeUpdate();

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    companyId = (int) generatedKeys.getLong(1);
                } else throw new SQLException("No ID obtained.");
            }
        }
        return companyId;
    }

}
