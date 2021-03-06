package domain.configuration;

import domain.constraints.types.softConstraints.fullySoftConstraints.RestingIntervalPenalization;
import domain.constraints.types.softConstraints.userConstraints.UserConstraint;
import logger.ConsoleLogger;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Properties;

/**
 * This is in charge of providing the weights of the {@link domain.constraints.types.softConstraints.SoftConstraint} for the
 * {@link geneticAlgorithm.fitnessFunctions.FitnessFunction}.
 * <p>
 * It is also used to check the existence of {@link domain.constraints.types.softConstraints.SoftConstraint}, because
 * a given ID does not appear in {@code weights} as a key, then it won't be related to a constraint.
 */
public class WeightConfigurer {

    /**
     * Hashmap where the keys are constraint ids, and the values their corresponding weights.
     * (coefficients) in the {@link geneticAlgorithm.fitnessFunctions.FitnessFunction}
     */
    final HashMap<String, Double> weights;

    /**
     * Constructor for the class
     * @param weightFilepath filepath to the properties file where the constraint weights are provided.
     */
    public WeightConfigurer(String weightFilepath) {
        weights = new HashMap<>();

        parseWeights(weightFilepath);
    }

    private void parseWeights(String weightFilepath) {
        InputStream configStream;
        Properties weigthProperties = new Properties();

        try {
            configStream = new FileInputStream(weightFilepath);
            weigthProperties.load(configStream);
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException("Could not find file with constrinction weigths");
        } catch (IOException e) {
            throw new IllegalArgumentException("Could not parse properties in constraint weights file");
        }


        // Properties p = new Properties();
        // p.load(getClass().getClassLoader().getResourceAsStream(weightFilepath));

        ConsoleLogger.getConsoleLoggerInstance().logInfo("Parsing fitness weights...");
        String[] neededProperties = {"DB", "TD", "SD", "DD",
                "OE", "DI", "RIP", "NCP", "UE", "SCDD"};
        for (String key: neededProperties) {
            try {
                weights.put(key, Double.parseDouble(weigthProperties.getProperty(key)));
            } catch (NullPointerException e) {

                throw new IllegalArgumentException("Missing properties in weights configuration file.\n" +
                        "The following properties are mandatory: " + Arrays.toString(neededProperties));

            } catch (NumberFormatException e) {
                ConsoleLogger.getConsoleLoggerInstance().logError("Could not get weigth for cosntriction of ID: "
                        + key + ". Got value: "
                        + weigthProperties.getProperty(key) + ", using 0 instead");
                weights.put(key, 0d);
            }
        }

        ConsoleLogger.getConsoleLoggerInstance().logInfo("Fitness weights parsed:" + weights.size());
    }

    /**
     * Returns the weight of a given constraint id.
     * @param constraintID The constraint id whose weight is wanted to be returned.
     * @return the corresponding weight to the constraint id provided as parameter.
     */
    public double getConstraintWeight(String constraintID){
        return weights.get(constraintID);
    }

    /**
     * Checks wether a given string matches the id of a key in {@code weights}.
     * @param id the id to be checked.
     * @return true if it is a key in the map, false otherwise.
     */
    public boolean existsConstraintID(String id){
        return weights.containsKey(id);
    }


    /**
     * Changes the current value for all the {@link UserConstraint} weights.
     * @param userConstraintsWeight New value for the weight of all the User Constraints.
     */
    public void setUserConstraintsWeight(double userConstraintsWeight) {
        weights.put("DB", userConstraintsWeight);
        weights.put("TD", userConstraintsWeight);
        weights.put("SD", userConstraintsWeight);
        weights.put("DD", userConstraintsWeight);
        weights.put("OE", userConstraintsWeight);
        weights.put("DI", userConstraintsWeight);
    }

    /**
     * Changes the current value for the {@link RestingIntervalPenalization} weight.
     * @param restingIntervalWeight New value for the weight of the Prohibited Interval Penalization.
     */
    public void setProhibitedIntervalWeight(double restingIntervalWeight) {
        weights.put("RIP", restingIntervalWeight);
    }

    /**
     * Changes the current value for the {@link domain.constraints.types.softConstraints.fullySoftConstraints.NumericalComplexityPenalization} weight.
     * @param numericalComplexityWeight New value for the weight of the Numerical Complexity Penalization.
     */
    public void setNumericalComplexityWeight(double numericalComplexityWeight) {
        weights.put("NCP", numericalComplexityWeight);
    }
}
