import java.io. *;
import java.util.HashMap;



public class Main {

    public static void main(String[] args) throws FileNotFoundException {

        String csvFilePath = args[0];

        String outputFile = "output.csv";

        HashMap<String, String> input = CSVHandler.parseCSV(csvFilePath);

        HashMap<String, String> evaluatedMap = CellCalculator.evaluateExpressions(input);
        
        CSVHandler.writeHashMapToCSV(evaluatedMap, outputFile);

    }
}

