package org.budget_manager.Facade;
import java.io.IOException;
import java.util.List;

public class CsvFacade {
    private CsvReader csvReader;
    private CsvWriter csvWriter;

    public CsvFacade() {
        this.csvReader = new CsvReader();
        this.csvWriter = new CsvWriter();
    }

    // Importuj dane z pliku CSV
    public List<String[]> importCsv(String filePath) throws IOException {
        return csvReader.readCsv(filePath);
    }

    // Eksportuj dane do pliku CSV
    public void exportCsv(String filePath, List<String[]> data) throws IOException {
        csvWriter.writeCsv(filePath, data);
    }
}
