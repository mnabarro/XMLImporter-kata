import converters.FileListToCompanyList;
import infraestructure.LocalFileSystem;
import infraestructure.database.PostgresConnection;
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
                insertStaff(conn, companyId, staff);
                insertStaffSalary(conn, staff);
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

    private static void insertStaff(Connection conn, int companyId, Staff staff) throws SQLException {
        try (PreparedStatement preparedStatement = conn.prepareStatement(
                "INSERT INTO staff(id,company_id, first_name, last_name, nick_name) VALUES (?,?,?,?,?)")) {
            preparedStatement.setInt(1, staff.id);
            preparedStatement.setInt(2, companyId);
            preparedStatement.setString(3, staff.firstname);
            preparedStatement.setString(4, staff.lastname);
            preparedStatement.setString(5, staff.nickname);
            preparedStatement.executeUpdate();
        }
    }

    private static void insertStaffSalary(Connection conn, Staff staff) throws SQLException {
        try (PreparedStatement preparedStatement = conn.prepareStatement(
                "INSERT INTO salary(staff_id, currency, value) VALUES (?,?,?)")) {
            preparedStatement.setInt(1, staff.id);
            preparedStatement.setString(2, staff.salary.currency);
            preparedStatement.setInt(3, staff.salary.value);
            preparedStatement.executeUpdate();
        }
    }

}
