package geneticAlgorithm.configuration;

import utils.ConsoleLogger;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Properties;

/**
 * This is in charge of providing the weights of the {@link domain.constrictions.Constriction} for the
 * {@link geneticAlgorithm.fitnessFunctions.FitnessFunction}.
 * <p>
 * It is also used to check the existence of {@link domain.constrictions.Constriction}, because
 * a given ID does not appear in {@code weights} as a key, then it won't be related to a constriction.
 */
public class WeightConfigurer {

    /**
     * Hashmap where the keys are constriction ids, and the values their corresponding weights
     * (coefficients) in the {@link geneticAlgorithm.fitnessFunctions.FitnessFunction}
     */
    final HashMap<String, Double> weights;

    /**
     * Constructor for the class
     * @param weightFilepath filepath to the properties file where the constriction weights are provided.
     */
    public WeightConfigurer(String weightFilepath) {
        weights = new HashMap<>();

        InputStream configStream;
        Properties weigthProperties = new Properties();

        try {
            configStream = new FileInputStream(weightFilepath);
            weigthProperties.load(configStream);
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException("Could not find file with constrinction weigths");
        } catch (IOException e) {
            throw new IllegalArgumentException("Could not parse properties in constriction weights file");
        }


        // Properties p = new Properties();
        // p.load(getClass().getClassLoader().getResourceAsStream(weightFilepath));

        ConsoleLogger.getConsoleLoggerInstance().logInfo("Parseando pesos fitness...");

        for (String key: weigthProperties.stringPropertyNames()) {
            try {
                weights.put(key, Double.parseDouble(weigthProperties.getProperty(key)));
            } catch (NumberFormatException e) {
                ConsoleLogger.getConsoleLoggerInstance().logError("Could not get weigth for cosntriction of ID: "
                        + key + ". Got value: "
                        + weigthProperties.getProperty(key) + ", using 0 instead");
                weights.put(key, 0d);
            }
        }

        ConsoleLogger.getConsoleLoggerInstance().logInfo("Parseados " + weights.size() + " pesos");
    }

    /**
     * Returns the weight of a given constriction id.
     * @param constrinctionID The constriction id whose weight is wanted to be returned.
     * @return the corresponding weight to the constriction id provided as parameter.
     */
    public double getConstrictionWeight(String constrinctionID){
        return weights.get(constrinctionID);
    }

    /**
     * Checks wether a given string matches the id of a key in {@code weights}
     * @param id the id to be checked
     * @return true if it is a key in the map, false otherwise.
     */
    public boolean existsConstrictionID(String id){
        return weights.containsKey(id);
    }


    /**
     * Changes the current value for all the {@link domain.constrictions.types.weakConstriction.hardifiableConstrictions.UserConstriction} weights.
     * @param userConstraintsWeight New value for the weight of all the User Constrictions.
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
     * Changes the current value for the {@link domain.constrictions.types.weakConstriction.fullyWeakConstrictions.ProhibitedIntervalPenalization} weight.
     * @param prohibitedIntervalWeight New value for the weight of the Prohibited Interval Penalization.
     */
    public void setProhibitedIntervalWeight(double prohibitedIntervalWeight) {
        weights.put("PIP", prohibitedIntervalWeight);
    }

    /**
     * Changes the current value for the {@link domain.constrictions.types.weakConstriction.fullyWeakConstrictions.NumericalComplexityPenalization} weight.
     * @param numericalComplexityWeight New value for the weight of the Numerical Complexity Penalization.
     */
    public void setNumericalComplexityWeight(double numericalComplexityWeight) {
        weights.put("NCP", numericalComplexityWeight);
    }
}
