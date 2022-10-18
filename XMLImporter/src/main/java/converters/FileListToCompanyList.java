package converters;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import xmlmodels.Company;

public class FileListToCompanyList {

  public static void convert(List<Path> paths, ArrayList<Company> companies) throws JAXBException {
      for (Path path : paths) {
          File file = new File(path.toString());
          JAXBContext jaxbContext = JAXBContext.newInstance(Company.class);
          Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
          Company company = (Company) jaxbUnmarshaller.unmarshal(file);
          companies.add(company);
      }
  }
}
