# SWE Spreadsheet Task Report


## Description of Code

My program take a command line argument of a file name and passes that to the `parseCSV` function. This function parses the CSV using the `java.util.Scannar` class. It iterates over the CSV while there are still new lines and then for each column converts the int `row` `col` values to cell notation. It stores the content of the cell value in the `cellContent` variable. The reference to the cell and the corresponding cell contents are then put in the HasMap `cellContentsMap`. 

Then the `evaluateExpressions` function is called taking the HashMap returned by the parseCSV function as its input. `evaluateExpressions` creates a local variable `evaluatedMap` to store the results of each cell once it has been evaluated. Then it iterates over the `cellContentsMap` local variable that was passed to it. For each cell it checks if the cell contents is valid. If it is valid then it checks to see if the cell contains letters. If it doesn't contain letters the `evaluatePostfix` function is called and the result is put into the `evaluatedMap` variable. If there are letters the `replaceCellReferences` function is called to replace references to other cells that may be in the current cell. Then the function is evaluated as in the previous step and the values are again stored in the `evaluatedMap` variable.

Finally, the `writeHashMapToCSV` function is called. This function takes the `evaluatedMap` returned by the `evaluatedExpressions` function as well as a `filePath` which is the location that the output csv file will be written to.


## Limitations
There are some limitations that I ran into when coding this. The main one is that it cannot handle the case when a cell contains a reference to another cell which then contains a reference to another cell. This is due to a limitation of the `replaceCellReferences` function that seems to create a null reference when a cell reference to another cell then refers to another cell.

## Trade-offs / design decisions
The main design decision I made when writing this program was to seperate out the main two pieces of logic from each other. The `CSVHandler.java` class handles everything to do with reading / writing the CSV files. The `CellCalculator.java` class handles the maths required to evaluate the expressions and check that the cells are valid.

By seperating the logic out like this it make the code readable and easier to understand.