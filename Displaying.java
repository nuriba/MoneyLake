/**
 * includes two methods. One of them is used to print the matrix. the other used to call mainMethod to run and related methods with it
 * @author Nuri Basar, Student ID: 2021400129
 *  @since date: 02.05.2023
 */
import java.io.FileNotFoundException;
import java.util.ArrayList;
public class Displaying {
    ModifyingTerrain modifyingTerrain = new ModifyingTerrain();
    /**
     * prints the matrix
     */
    public static void printingMatrix(){

        for(int i=0; i<=Variables.matrixRow; i++){
            String initialThing=Variables.additionalColumn.get(i); // finds which number should be written first
            // finds the length of the space and prints it
            int thingLength = initialThing.length();
            for (int k=0; k< 3-thingLength; k++){
                System.out.print(" ");
            }
            System.out.print(initialThing);
            if (i==Variables.matrixRow) {
                for (int j=0; j<Variables.matrixColumn; j++){
                    String word=Variables.additionalRow.get(j); // takes the coordinate name
                    // finds the length of the space and prints it
                    int wordLength = word.length();
                    int spaceLength = 3-wordLength;
                    for (int k=0; k<spaceLength; k++)
                        System.out.print(" ");
                    System.out.print(word);
                }
                System.out.println();
            }else{
                for (int j=0; j<Variables.matrixColumn; j++){
                    ArrayList<String> row = Variables.inputMatrix.get(i); // takes the matrix value
                    // finds the length of the space and prints it
                    String word=row.get(j);
                    int wordLength = word.length();
                    int spaceLength = 3-wordLength;
                    for (int k=0; k<spaceLength; k++)
                        System.out.print(" ");
                    System.out.print(row.get(j));
                }
                System.out.println();
            }
        }
    }

    /**
     * calls all methods which need to run the program
     * @throws FileNotFoundException
     */
    public void mainCode() throws FileNotFoundException {
        Variables.readingFileAndCreatingVariables(); // reads file
        Variables.findingPossibilities(); // finds possible words and letter
        Displaying.printingMatrix(); // print the matrix with initial shape
        ModifyingTerrain.takingConsoleInputAnChanging(); // taking input and modifies the matrix
        Variables.determiningBordersValue(); // finds min borders value
        // default values for variables in the mainMethod
        String currentPuddle = Variables.allPossibilitiesForLake.get(0);
        ArrayList<Integer> currentCoordinate=Variables.coordinatesCanBePuddle.get(0);
        boolean isItUsed = false;
        int value=0;
        ArrayList<ArrayList<String>> copiedMatrix = Variables.cloneArray(Variables.inputMatrix);
        modifyingTerrain.mainMethod(copiedMatrix,currentCoordinate,value,currentPuddle,isItUsed); // run the main algorithm
        Displaying.printingMatrix(); // prints final shape of the matrix
        modifyingTerrain.calculatingVolume(); // calculates volume and prints it
    }
}
