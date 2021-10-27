package configuration;

import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;

public class WeightConfigurer {

    HashMap<String, Double> weights;

    public WeightConfigurer(String inputFilePath) throws IOException {
        weights = new HashMap<>();

        Properties p = new Properties();
        p.load(getClass().getClassLoader().getResourceAsStream(inputFilePath));

        for (String key: p.stringPropertyNames()) {
            weights.put(key, Double.parseDouble(p.getProperty(key)));
        }
    }

    public double getConstrictionWeight(String constrinctionID){
        return weights.get(constrinctionID);
    }

    public boolean existsConstrictionID(String id){
        return weights.keySet().contains(id);
    }
}
