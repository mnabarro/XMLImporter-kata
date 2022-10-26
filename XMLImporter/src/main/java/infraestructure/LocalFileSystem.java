package infraestructure;

import static java.nio.file.Files.walk;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import xmlmodels.Company;

public class LocalFileSystem {

  public List<Path> getFileWithExtensionPathList(Path folderPath, String fileExtension) throws IOException {

    List<Path> paths;
    try (Stream<Path> pathStream = walk(folderPath)
      .filter(Files::isRegularFile)
      .filter(filePath ->
        filePath.toString()
          .endsWith(fileExtension))) {
      paths = pathStream
        .collect(Collectors.toList());
    }
    return paths;
  }

  public void fileListToCompanyList(List<Path> paths, ArrayList<Company> companies) throws JAXBException {
    for (Path path : paths) {
      File file = new File(path.toString());

      Company company = parseXmlCompany(file);

      companies.add(company);
    }
  }

  private Company parseXmlCompany(File file) throws JAXBException {

    JAXBContext jaxbContext = JAXBContext.newInstance(Company.class);
    Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
    return (Company) jaxbUnmarshaller.unmarshal(file);

  }

  public void importFiles(Path folderPath, ArrayList<Company> companies) throws JAXBException, IOException {
    final String fileExtension = ".xml";
    List<Path> paths = getFileWithExtensionPathList(folderPath, fileExtension);
    fileListToCompanyList(paths, companies);
  }
}
