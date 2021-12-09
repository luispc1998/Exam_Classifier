package configuration;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Properties;

/**
 * This is in charge of providing the weights of the {@link domain.constrictions.Constriction} for the
 * {@link fitnessFunctions.FitnessFunction}.
 * <p>
 * It is also used to check the existence of {@link domain.constrictions.Constriction}, because
 * a given ID does not appear in {@code weights} as a key, then it won't be related to a constriction.
 */
public class WeightConfigurer {

    /**
     * Hashmap where the keys are constriction ids, and the values their corresponding weights
     * (coefficients) in the {@link fitnessFunctions.FitnessFunction}
     */
    HashMap<String, Double> weights;

    /**
     * Constructor for the class
     * @param weightFilepath filepath to the properties file where the constriction weights are provided.
     * @throws IOException in case the property loading fails.
     */
    public WeightConfigurer(String weightFilepath) throws IOException {
        weights = new HashMap<>();

        InputStream configStream;
        Properties weigthProperties = new Properties();

        configStream = new FileInputStream(weightFilepath);
        weigthProperties.load(configStream);

        // Properties p = new Properties();
        // p.load(getClass().getClassLoader().getResourceAsStream(weightFilepath));

        for (String key: weigthProperties.stringPropertyNames()) {
            weights.put(key, Double.parseDouble(weigthProperties.getProperty(key)));
        }
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
}
