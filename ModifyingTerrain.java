/**
 * takes input from user and modifies the matrix about the input.
 * finally, finds puddles and places them and calculates volume
 * @author Nuri Basar, Student ID: 2021400129
 * @since date: 02.05.2023
 */
import java.util.ArrayList;
import java.util.Scanner;
public class ModifyingTerrain {
    /**
     * takes input and modifies the matrix about the input
     */
    public static void takingConsoleInputAnChanging() {
        int i = 1;
        Scanner scan = new Scanner(System.in);
        while (i < 11) {
            System.out.println(i + " / 10 to coordinate:");
            String input = scan.next(); // takes input
            if (input.length()==2) { // Controls whether input is correct or not
                String rowNum = input.substring(1); // splits the input two, second one gives row number
                String columnLetter = input.substring(0, 1); //splits the input two, first one gives column name
                if (Variables.additionalRow.contains(columnLetter) && Variables.additionalColumn.contains(rowNum)) {
                    // finds coordinates including number
                    int rowIndex = Variables.additionalColumn.indexOf(rowNum);
                    int columnIndex = Variables.additionalRow.indexOf(columnLetter);
                    int newValue = Integer.parseInt(Variables.inputMatrix.get(rowIndex).get(columnIndex)) + 1; // finds the value and updates the value by adding
                    Variables.inputMatrix.get(rowIndex).set(columnIndex, newValue + ""); // changes the value with new value
                    Displaying.printingMatrix();
                    System.out.println("---------------");
                    i++;
                }else{
                    System.out.println("Not a valid step!");
                    System.out.println("---------------");
                }
            }else{
                System.out.println("Not a valid step!");
                System.out.println("---------------");
            }
        }
    }
    /**
     *  finds the neighbor coordinates
     * @param currentCoordinate is the coordinate whose neighbor coordinates should be found
     * @return an array of neighbor coordinates
     */
    private ArrayList<ArrayList<Integer>> findingBordersCoordinate(ArrayList<Integer> currentCoordinate) {
        int x = currentCoordinate.get(0);
        int y = currentCoordinate.get(1);
        ArrayList<ArrayList<Integer>> bordersCoordinate = new ArrayList<>(); // output array
        for (int i = x - 1; i <= x + 1; i++) {
            for (int j = y - 1; j <= y + 1; j++) {
                ArrayList<Integer> borderCoordinate = new ArrayList<>(); // stores the neighbor coordinates to add them output  array
                if (i == x && j == y) // these values are coordinate values of currentCoordinate
                    continue;
                borderCoordinate.add(i);
                borderCoordinate.add(j);
                bordersCoordinate.add(borderCoordinate);
            }
        }
        return bordersCoordinate;
    }
    /**
     * determines the min value.
     * if it returns -1, currentCoordinate cannot be puddle
     * if not, it can be puddle, and the number is the min value
     * @param bordersCoordinate is array  of neighbors
     * @param currentCoordinate is the coordinate we have
     * @return an integer value
     */
    private int determiningLimitingValue(ArrayList<ArrayList<Integer>> bordersCoordinate, ArrayList<Integer> currentCoordinate){
        boolean doesItContainPuddleNeighbor = false; // to control if there is any puddle among its neighbors
        //I assign some variables to make coding easier
        ArrayList<ArrayList<String>> tempInputMatrix = Variables.inputMatrix;
        int x = currentCoordinate.get(0);
        int y = currentCoordinate.get(1);
        int currentValue = Integer.parseInt(tempInputMatrix.get(x).get(y));
        ArrayList<String> stringBorderValues =new ArrayList<>(); //Border values is important to find min value
        for (ArrayList<Integer> coordinate: bordersCoordinate){
            int row = coordinate.get(0);
            int column = coordinate.get(1);
            String val = tempInputMatrix.get(row).get(column);
            if(Variables.copiedAllPossibilitiesForLake.contains(val)) // check whether there is any puddle among neighbors
                doesItContainPuddleNeighbor= true;
            stringBorderValues.add(val);
        }
        if (doesItContainPuddleNeighbor) // if there is, it cannot be puddle because it has already tried for it.
            return -1;
        ArrayList<Integer> integerBorderValues = new ArrayList<>();
        for (String val: stringBorderValues)
            integerBorderValues.add(Integer.parseInt(val));  //convert them int to make comparison easy
        if(Variables.oneStepInnerCoordinates.contains(currentCoordinate)){ //checks whether current coordinate is a nearest coordinate to border
            // if it is, it should be greater than its border min value to be puddle
            int index = Variables.oneStepInnerCoordinates.indexOf(currentCoordinate);
            int minValue=Variables.oneStepInnerCoordinatesMinValues.get(index);
            if(currentValue>=minValue)
                return -1;
            // some neighbors smaller than min value can be one of the nearest coordinates,
            // if they are, min value should be set again
            for(int i=0;i<integerBorderValues.size();i++){
                if(minValue>integerBorderValues.get(i)){
                    if(Variables.oneStepInnerCoordinates.contains(bordersCoordinate.get(i))){
                        int ind= Variables.oneStepInnerCoordinates.indexOf(bordersCoordinate.get(i));
                        int minValueTry = Variables.oneStepInnerCoordinatesMinValues.get(ind);
                        if(minValueTry<minValue)
                            minValue=minValueTry;
                    }
                }
            }
            if(currentValue>=minValue) // Controls the value again because min value can be changed
                return -1;
            Variables.minValues.add(minValue); // it's one of the min values and add it to the min values list
            return minValue;
        }else{ // current coordinate is not a nearest coordinate to border
            int minValue=0;
            for (int i=0; i<bordersCoordinate.size();i++) {
                int value = integerBorderValues.get(i);
                // but its neighbors can be one of them.
                if(Variables.oneStepInnerCoordinates.contains(bordersCoordinate.get(i))) { // if they, find the min values of the neighbor borders
                    // and  if the value smaller than min value and if it is needed, change the min value
                    int ind= Variables.oneStepInnerCoordinates.indexOf(bordersCoordinate.get(i));
                    int minValueTry = Variables.oneStepInnerCoordinatesMinValues.get(ind);
                    if (minValueTry>value) {
                        if(minValue==0)
                            minValue=minValueTry;
                        else
                            if (minValueTry < minValue)
                                minValue = minValueTry;
                    }
                }
            }
            if(minValue==0) { // this means none of neighbors has a smaller neighbor value
                // finds the min value of neighbors and compare current value with it
                minValue = integerBorderValues.get(0);
                for (int intVal:integerBorderValues) {
                    if(intVal<minValue)
                        minValue=intVal;
                }
                if(currentValue>=minValue)
                    return -1;
                Variables.minValues.add(minValue);
                return minValue;
            }else {
                // compare current value with min value and determine whether current value is a puddle or not
                if (currentValue >= minValue)
                    return -1;
                Variables.minValues.add(minValue);
                return minValue;
            }
        }
    }
    /**
     * finds the neighbors which can be puddle
      * @param bordersCoordinate is an array storing all neighbor coordinates
     * @param minValue is the min value for puddles
     * @return the neighbor array
     */
    private ArrayList<ArrayList<Integer>> getNeighbors(ArrayList<ArrayList<Integer>> bordersCoordinate, int minValue){
        ArrayList<ArrayList<Integer>> neighborCoordinates = new ArrayList<>();  //output array
        ArrayList<ArrayList<String>> tempInputMatrix = Variables.inputMatrix;
        // finds which coordinate can be puddle by comparing its value with min value
        for (ArrayList<Integer> coordinate: bordersCoordinate){
            int row = coordinate.get(0);
            int column = coordinate.get(1);
            String stringValue = tempInputMatrix.get(row).get(column);
            int value = Integer.parseInt(stringValue);
            if (value<minValue)
                if(Variables.coordinatesCanBePuddle.contains(coordinate))
                    neighborCoordinates.add(coordinate);
        }
        return neighborCoordinates;
    }

    /**
     * finds the puddles recursively, and modify inputMatrix about the result
     * @param copiedMatrix is the clone matrix to make changes without changing main input matrix
     * @param currentCoordinate is the current coordinate to control whether it can be puddle
     * @param value is the min value
     * @param currentPuddle is the puddle name
     * @param isItUsed controls if puddle name is used or not
     */
    public void mainMethod(ArrayList<ArrayList<String>> copiedMatrix, ArrayList<Integer> currentCoordinate, int value, String currentPuddle,boolean isItUsed){
        if (Variables.coordinatesCanBePuddle.size()==0){ // if there is no any probability for new puddle, the method should be finished
             return;
        }
        ArrayList<ArrayList<Integer>> bordersCoordinate = findingBordersCoordinate(currentCoordinate); // finds neighbor coordinates
        Variables.coordinatesCanBePuddle.remove(currentCoordinate); // this coordinate cannot be puddle because it has already tried
        if (value==0) { // if it is not a neighbor of a puddle, its min value should be calculated
            int result = determiningLimitingValue(bordersCoordinate, currentCoordinate);
            if (result == -1) { //it cannot be puddle
                if (Variables.coordinatesCanBePuddle.size() > 0)
                    //change the coordinate and continue
                    currentCoordinate = Variables.coordinatesCanBePuddle.get(0);
                else
                    return;
                mainMethod(copiedMatrix, currentCoordinate, 0, currentPuddle, isItUsed);
                return;
            } else {
                // new value should be assigned as min value
                if (value == 0) {
                    value = result;
                }
            }
        }
        ArrayList<ArrayList<Integer>> neighbors = getNeighbors(bordersCoordinate,value); // finds it value which are suitable for being puddle
        if (neighbors.size()==0){
            //there is no continued value for puddle. assign it and change this puddle name
            copiedMatrix.get(currentCoordinate.get(0)).set(currentCoordinate.get(1), currentPuddle);
            isItUsed=true;
            if (Variables.coordinatesCanBePuddle.size()>0){
                //change the coordinate and continue
                currentCoordinate = Variables.coordinatesCanBePuddle.get(0);
            }else {
                Variables.inputMatrix = Variables.cloneArray(copiedMatrix);
                return;
            }
            if (isItUsed){
                // changes the puddle name
                Variables.allPossibilitiesForLake.remove(0);
                currentPuddle= Variables.allPossibilitiesForLake.get(0);
                isItUsed=false;
            }
            Variables.inputMatrix=Variables.cloneArray(copiedMatrix);
            mainMethod(copiedMatrix,currentCoordinate,0,currentPuddle,isItUsed);
            return;
        }else{
            // controls whether neighbors contain any border value, if it does, it cannot be puddle
            // because money will fall out
            for (ArrayList<Integer> neighborCoordinate: neighbors){
                if(Variables.coordinatesCannotBePuddle.contains(neighborCoordinate)){
                    copiedMatrix=Variables.cloneArray(Variables.inputMatrix);
                    if (Variables.coordinatesCanBePuddle.size()==0)
                        return;
                    currentCoordinate=Variables.coordinatesCanBePuddle.get(0);
                    isItUsed=false;
                    mainMethod(copiedMatrix,currentCoordinate,0,currentPuddle,isItUsed);
                    return;
                }
            }
            copiedMatrix.get(currentCoordinate.get(0)).set(currentCoordinate.get(1),currentPuddle); // changes the value with puddle name
            isItUsed=true;
            // try all neighbor as a current coordinate recursively.
            for (ArrayList<Integer> neighborCoordinate: neighbors){
                currentCoordinate=neighborCoordinate;
                //these two prevent them from changes.
                int tempValue =value;
                ArrayList<ArrayList<Integer>> tempNeighbors =  Variables.cloneArrayInteger(neighbors);
                mainMethod(copiedMatrix,currentCoordinate,value,currentPuddle,isItUsed);
                value=tempValue;
                neighbors=tempNeighbors;
            }
        }
        return;
    }

    /**
     * calculates volume by finding used puddle names, min values and initial values of puddles
     */
    public void calculatingVolume(){
        ArrayList<String> usedName = new ArrayList<>();
        // finds used different puddle names by looking the number of min values
        // because when min value changes, puddle names also changes
        for (int i=0; i<Variables.minValues.size(); i++){
            String word = Variables.copiedAllPossibilitiesForLake.get(i);
            usedName.add(word);
        }
        ArrayList<ArrayList<Integer>> puddlesInitialValue = new ArrayList<>();
        // finds initial puddles values to calculate volume by calculating difference
        for (String word : usedName){
            ArrayList<Integer> insideArrayToAddOutsideArray= new ArrayList<>();
            for(int i= 0; i<Variables.matrixRow; i++){
                for(int j=0; j<Variables.matrixColumn; j++){
                    String value = Variables.inputMatrix.get(i).get(j);
                    if (value.equals(word)){
                        String initialStringValue = Variables.initialInputMatrix.get(i).get(j);
                        int initialValue = Integer.parseInt(initialStringValue);
                        insideArrayToAddOutsideArray.add(initialValue);
                    }
                }
            }
            puddlesInitialValue.add(insideArrayToAddOutsideArray);
        }
        // calculates volume by taking difference between puddle initial values and its min value
        double volume = 0;
        for (int i=0; i<usedName.size(); i++){
            int minValue= Variables.minValues.get(i);
            ArrayList<Integer> initialPuddleValues = puddlesInitialValue.get(i);
            double sum =0;
            for (int initialValue: initialPuddleValues){
                int difference = minValue-initialValue;
                sum += difference;
            }
            volume += Math.pow(sum,0.5);
        }
        System.out.printf("Final score: %1.2f",volume);
    }
}