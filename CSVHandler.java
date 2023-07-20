import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class CSVHandler {

    public static HashMap<String, String> cellContentsMap = new HashMap<>();

    public static HashMap<String, String> parseCSV(String filePath) throws FileNotFoundException{

        File csvFile = new File(filePath);
        Scanner scanner = new Scanner(csvFile);

        int row = 0;
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] cells = line.split(",");

            for (int col = 0; col < cells.length; col++) {
                String cellNotation = toCellNotation(row, col);
                String cellContent = cells[col].trim();
                cellContentsMap.put(cellNotation, cellContent);
            }
            row++;
        }

        scanner.close();
        // Print the HashMap for verification (Optional)
        // for (String cellNotation : cellContentsMap.keySet()) {
        //     String cellContent = cellContentsMap.get(cellNotation);
        //     System.out.println("Cell: " + cellNotation + "     Cell CONTENT: " + cellContent);
        // }
        return cellContentsMap;
    }

    public static void writeHashMapToCSV(HashMap<String, String> hashMap, String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            // Find the maximum row and column indices from the cell references
            int maxRow = -1;
            int maxCol = -1;

            for (String cellReference : hashMap.keySet()) {
                String[] rowCol = getRowAndColumnIndices(cellReference);
                if (rowCol != null) {
                    int row = Integer.parseInt(rowCol[0]);
                    int col = Integer.parseInt(rowCol[1]);

                    maxRow = Math.max(maxRow, row);
                    maxCol = Math.max(maxCol, col);
                }
            }

            // Create a 2D array to hold the CSV data
            String[][] csvData = new String[maxRow + 1][maxCol + 1];

            // Fill the CSV data with values from the HashMap
            for (Map.Entry<String, String> entry : hashMap.entrySet()) {
                String cellReference = entry.getKey();
                String cellValue = entry.getValue();
                String[] rowCol = getRowAndColumnIndices(cellReference);

                if (rowCol != null) {
                    int row = Integer.parseInt(rowCol[0]);
                    int col = Integer.parseInt(rowCol[1]);
                    csvData[row][col] = cellValue;
                }
            }

            // Write the CSV data to the file
            for (int row = 0; row < csvData.length; row++) {
                for (int col = 0; col < csvData[row].length; col++) {
                    writer.write(csvData[row][col] != null ? String.valueOf(csvData[row][col]) : "");
                    if (col < csvData[row].length - 1) {
                        writer.write(",");
                    }
                }
                writer.newLine();
            }

            System.out.println("HashMap data has been written to the CSV file successfully.");

        } catch (IOException e) {
            System.err.println("Error writing to CSV file: " + e.getMessage());
        }
    }

    public static String[] getRowAndColumnIndices(String cellReference) {
        if (cellReference == null || !cellReference.matches("[A-Za-z]+[0-9]+")) {
            System.err.println("Invalid cell reference: " + cellReference);
            return null;
        }

        int col = 0;
        for (int i = 0; i < cellReference.length(); i++) {
            char c = cellReference.charAt(i);
            if (Character.isLetter(c)) {
                col = col * 26 + (Character.toUpperCase(c) - 'A');
            }
        }

        int row = Integer.parseInt(cellReference.replaceAll("[A-Za-z]", "")) - 1;

        return new String[]{String.valueOf(row), String.valueOf(col)};
    }


    private static String toCellNotation(int row, int col) {
        StringBuilder cellNotation = new StringBuilder();
        // Convert column index to LETTER notation
        col++; // Adjust column index to start from 1 (A=1, B=2, ..., Z=26)
        while (col > 0) {
            int remainder = (col - 1) % 26;
            cellNotation.insert(0, (char) (remainder + 'a'));
            col = (col - 1) / 26;
        }
        // Append row index to cell notation
        cellNotation.append(row + 1);

        return cellNotation.toString();
    }

    public static String getCellContents(String cellRef){
        return cellContentsMap.get(cellRef);
    }

    public static void updateCellContents(String cellRef, String valueToInsert){

        cellContentsMap.put(cellRef, valueToInsert);

    }


    public static boolean containsLetters(String str) {
        for (char c : str.toCharArray()) {
            if (Character.isLetter(c)) {
                return true;
            }
        }
        return false;
    }

    private static boolean containsOperator(String input) {
        return input.matches(".*[/*\\-+].*");
    }

    public static boolean containsDigits(String str) {
        for (char c : str.toCharArray()) {
            if (Character.isDigit(c)) {
                return true; // Found a digit, return true immediately
            }
        }
        return false; // No digit found in the string
    }

    public static boolean cellIsValid(String key, String value){

        if(!containsLetters(value) && !containsDigits(value)){
            return false;
        }

        if (key.equals(value)){
            return false;
        } 
        else {

            String[] components = value.split("\\s+");

            if (components.length > 1 && !containsOperator(value)){
                return false;
            }
        }
        return true;

    }

    public static String replaceCellReferences(String key, String value){

        String[] components = value.split("\\s+");

        for (int i = 0; i < components.length; i++) {

            while (containsLetters(components[i])){

                String actualValue = cellContentsMap.get(components[i]);
                components[i] =  actualValue;
            }
        }

        String updatedExpression = String.join(" ", components);

        return updatedExpression;
    }
}
