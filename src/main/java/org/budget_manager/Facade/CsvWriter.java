package org.budget_manager.Facade;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class CsvWriter {
    public void writeCsv(String filePath, List<String[]> data) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            for (String[] row : data) {
                String line = String.join(",", row);
                bw.write(line);
                bw.newLine();
            }
        }
    }
}
