package org.budget_manager.Facade;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CsvReader {
    public List<String[]> readCsv(String filePath) throws IOException {
        List<String[]> data = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] row = line.split(",");
                data.add(row);
            }
        }
        return data;
    }
}
