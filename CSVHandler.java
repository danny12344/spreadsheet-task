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

    
    /** 
     * @param filePath of the CSV to evaluate
     * @return HashMap<String, String> representing the CSV as a HashMap
     * @throws FileNotFoundException
     */
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

        return cellContentsMap;
    }

    
    /** 
     * @param hashMap of the CSV to conver to a CSV
     * @param filePath location to write the CSV to
     */
    public static void writeHashMapToCSV(HashMap<String, String> hashMap, String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {

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

            String[][] csvData = new String[maxRow + 1][maxCol + 1];

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

    
    /** 
     * @param cellReference in Letter Number notation
     * @return String[] with two elements in to represent the row & column of cell reference
     */
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


    
    /** 
     * @param row
     * @param col
     * @return String representation in letter number notation of a provided int row col
     */
    private static String toCellNotation(int row, int col) {
        StringBuilder cellNotation = new StringBuilder();
        col++; // Adjust column index to start from 1 (A=1, B=2, ..., Z=26)
        while (col > 0) {
            int remainder = (col - 1) % 26;
            cellNotation.insert(0, (char) (remainder + 'a'));
            col = (col - 1) / 26;
        }
        cellNotation.append(row + 1);

        return cellNotation.toString();
    }
    

    
    /** 
     * @param str to check
     * @return boolean true if str contains letters; false otherwise
     */
    public static boolean containsLetters(String str) {
        for (char c : str.toCharArray()) {
            if (Character.isLetter(c)) {
                return true;
            }
        }
        return false;
    }
    
    /** 
     * @param input string to check
     * @return boolean true if input contains / * + -; false otherwise
     */
    private static boolean containsOperator(String input) {
        return input.matches(".*[/*\\-+].*");
    }

    /** 
     * @param str to check
     * @return boolean true if str contains numbers; false otherwise
     */
    public static boolean containsDigits(String str) {
        for (char c : str.toCharArray()) {
            if (Character.isDigit(c)) {
                return true; // Found a digit, return true immediately
            }
        }
        return false; // No digit found in the string
    }



    
    /** 
     * @param key of HashMap
     * @param value of Hashmap
     * @return boolean true if the cell can be evaluated according to conditions outlined in task; false otherwise
     */
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

    /** 
     * @param key of HashMap
     * @param value of HashMap
     * @return String of cell without references to other cells - not fully functional
     */
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
