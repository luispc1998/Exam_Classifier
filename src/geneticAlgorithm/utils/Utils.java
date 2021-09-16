package geneticAlgorithm.utils;

public class Utils {


    public static double[] normalizeDoubleArray(double[] numberArray){
        double divisor = 0;
        for (double number: numberArray) {
            divisor += number;
        }

        int arrayLength = numberArray.length;
        double[] normalizedArray = new double[arrayLength];

        if (divisor!=0) {

            for (int i = 0; i < arrayLength; i++) {
                normalizedArray[i] = numberArray[i] / divisor;
            }
        }
        return normalizedArray;


    }
}
