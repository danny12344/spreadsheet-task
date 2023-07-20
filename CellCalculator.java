import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Stack;

public class CellCalculator {

    
    
    private static float evaluatePostfix(String exp) {

        String expCleaned = exp.replaceAll("[^a-zA-Z0-9*+/\\-\\s]", "");


        Scanner scanner = new Scanner(expCleaned);
        Stack<Float> stack = new Stack<Float>();

        while (scanner.hasNext()) {
            if (scanner.hasNextFloat()) {
                stack.push(scanner.nextFloat());
            } else {
                float b = stack.pop();
                float a = stack.pop();
                char operator = scanner.next().charAt(0);
                switch (operator) {
                case '+':
                    stack.push(a + b);
                    break;

                case '-':
                    stack.push(a - b);
                    break;

                case '*':
                    stack.push(a * b);
                    break;

                case '/':
                    stack.push(a / b);
                    break;

                case '%':
                    stack.push(a % b);
                    break;

                case '^':
                    stack.push((float) Math.pow(a, b));
                    break;
                }
            }
        }
        scanner.close();
        return stack.pop();
    }



    
    public static HashMap<String, String> evaluateExpressions(HashMap<String, String> cellContentsMap) {


        HashMap<String, String> evaluatedMap = new HashMap<>();

        for (Map.Entry<String, String> entry : cellContentsMap.entrySet()) {

            String key = entry.getKey();
            String value = entry.getValue();

            String result = "";

            if(!CSVHandler.cellIsValid(key, value)){
                result = "#ERR";
                evaluatedMap.put(key, result);
            } else {

                if (!CSVHandler.containsLetters(value)){

                    result = Float.toString(evaluatePostfix(value));
                    evaluatedMap.put(key, result);
                } else {

                    String updatedExpression = CSVHandler.replaceCellReferences(key, value);

                    result = Float.toString(evaluatePostfix(updatedExpression));
                
                    evaluatedMap.put(key, result);

                }

            }
            
        }
        return evaluatedMap;
    }
}



                        
// String actualValue = cellContentsMap.get(components[i]);
// String actualValueCalc = Float.toString(evaluatePostfix(actualValue));
// components[i] = actualValueCalc;
