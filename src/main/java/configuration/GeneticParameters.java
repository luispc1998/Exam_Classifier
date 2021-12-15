package configuration;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class GeneticParameters {

    private int maxIterations;
    private int populationSize;
    private int loggingFrequency;
    private double mutationProbability;
    private int maxSchedulesToTake;

    private GeneticParameters(int maxIterations, int populationSize, int loggingFrequency, double mutationProbability,
                             int maxSchedulesToTake) {
        this.maxIterations = maxIterations;
        this.populationSize = populationSize;
        this.loggingFrequency = loggingFrequency;
        this.mutationProbability = mutationProbability;
        this.maxSchedulesToTake = maxSchedulesToTake;
    }

    public int getMaxIterations() {
        return maxIterations;
    }

    public int getPopulationSize() {
        return populationSize;
    }

    public int getLoggingFrequency() {
        return loggingFrequency;
    }

    public double getMutationProbability() {
        return mutationProbability;
    }

    public int getMaxSchedulesToTake() {
        return maxSchedulesToTake;
    }

    public static GeneticParameters loadFromFile(String filePath) {


        Properties geneticProperties = new Properties();

        try (InputStream configStream = new FileInputStream(filePath)) {

            geneticProperties.load(configStream);


            return new GeneticParameters(Integer.parseInt(geneticProperties.getProperty("iterations")),
                    Integer.parseInt(geneticProperties.getProperty("populationSize")),
                    Integer.parseInt(geneticProperties.getProperty("loggingFreq")),
                    Double.parseDouble(geneticProperties.getProperty("mutationProb")),
                    Integer.parseInt(geneticProperties.getProperty("maxSchedulesToTake")));


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        throw new IllegalArgumentException("Cannot parse Genetic algorithm parameters.");
    }
}
