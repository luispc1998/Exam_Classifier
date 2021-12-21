package configuration;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class GeneticParameters {

    private final int maxIterations;
    private final int populationSize;
    private final int loggingFrequency;
    private final double mutationProbability;
    private final int maxSchedulesToTake;
    private double crossingProbability;

    private GeneticParameters(int maxIterations, int populationSize, int loggingFrequency, double mutationProbability,
                             int maxSchedulesToTake, double crossingProbability) {
        this.maxIterations = maxIterations;
        this.populationSize = populationSize;
        this.loggingFrequency = loggingFrequency;
        this.mutationProbability = mutationProbability;
        this.maxSchedulesToTake = maxSchedulesToTake;
        this.crossingProbability = crossingProbability;
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

    public double getCrossingProbability() {
        return crossingProbability;
    }

    public static GeneticParameters loadFromFile(String filePath) {

        Properties geneticProperties = new Properties();
        try (InputStream configStream = new FileInputStream(filePath)) {

            geneticProperties.load(configStream);

            return new GeneticParameters(Integer.parseInt(geneticProperties.getProperty("iterations")),
                    Integer.parseInt(geneticProperties.getProperty("populationSize")),
                    Integer.parseInt(geneticProperties.getProperty("loggingFreq")),
                    Double.parseDouble(geneticProperties.getProperty("mutationProb")),
                    Integer.parseInt(geneticProperties.getProperty("maxSchedulesToTake")),
                    Double.parseDouble(geneticProperties.getProperty("crossingProb")));


        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException("Could not parse Genetic parameters configuration file");
        } catch (IOException e) {
            throw new IllegalArgumentException("Could not parse properties in Genetic parameters configuration file");
        }
    }


}
