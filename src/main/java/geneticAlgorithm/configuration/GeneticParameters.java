package geneticAlgorithm.configuration;

import logger.dataGetter.fitnessLogger.GeneticLogger;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Properties;

/**
 * This represents all the parameters to configure the genetic algorithm.
 */
public class GeneticParameters {

    /**
     * Maximun number of iterations to be done by the algorithm.
     */
    private int generations;

    /**
     * Population size to be considered by the algorithm.
     */
    private int populationSize;

    /**
     * Iterations waited by the genetic logger before writing an entry.
     *
     * @see GeneticLogger
     */
    private final int loggingFrequency;

    /**
     * Mutation probability to be considered by the algorithm.
     */
    private double mutationProbability;

    /**
     * Schedule number to be taken when finishing the execution.
     */
    private final int maxSchedulesToTake;

    /**
     * Crossing probability to be considered by the algorithm.
     */
    private double crossingProbability;

    /**
     * Maximum depth of the tree of changes of the repairing algorithm.
     */
    private int repairingDepth;

    /**
     * Number of times the genetic algorithm must be repeated.
     *
     * <p>
     * This is used for statistical purposes, normally it will be set to one.
     */
    private final int algorithmRepetitions;

    /**
     * States whether the application must stop and inform the user in case it detects warnings or errors in the inputs.
     *
     * <p>
     * Normally it will be set to true, but it can be disabled for multiple executions in order to get statistics.
     */
    private final boolean errorAsking;

    private GeneticParameters(int generations, int populationSize, int loggingFrequency, double mutationProbability,
                              int maxSchedulesToTake, double crossingProbability, int repairingDepth,
                              int algorithmRepetitions, boolean errorAsking) {
        this.generations = generations;
        this.populationSize = populationSize;
        this.loggingFrequency = loggingFrequency;
        this.mutationProbability = mutationProbability;
        this.maxSchedulesToTake = maxSchedulesToTake;
        this.crossingProbability = crossingProbability;
        this.repairingDepth = repairingDepth;
        this.algorithmRepetitions = algorithmRepetitions;
        this.errorAsking = errorAsking;
    }

    public int getRepairingDepth() {
        return repairingDepth;
    }

    public int getAlgorithmRepetitions() {
        return algorithmRepetitions;
    }

    public boolean isErrorAsking() {
        return errorAsking;
    }

    public int getGenerations() {
        return generations;
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

    public int getRepairingAlgorithmDepth() {
        return repairingDepth;
    }

    public static GeneticParameters loadFromFile(String filePath) {

        Properties geneticProperties = new Properties();
        try (InputStream configStream = new FileInputStream(filePath)) {

            geneticProperties.load(configStream);

            return new GeneticParameters(Integer.parseInt(geneticProperties.getProperty("generations")),
                    Integer.parseInt(geneticProperties.getProperty("populationSize")),
                    Integer.parseInt(geneticProperties.getProperty("loggingFreq")),
                    Double.parseDouble(geneticProperties.getProperty("mutationProb")),
                    Integer.parseInt(geneticProperties.getProperty("maxSchedulesToTake")),
                    Double.parseDouble(geneticProperties.getProperty("crossingProb")),
                    Integer.parseInt(geneticProperties.getProperty("repairingDepth")),
                    Integer.parseInt(geneticProperties.getProperty("algorithmRepetitions")),
                    Boolean.parseBoolean(geneticProperties.getProperty("inputWarningsStop")));


        } catch (NullPointerException e) {
            String[] neededProperties = {"generations", "populationSize", "loggingFreq", "mutationProb",
                "maxSchedulesToTake", "crossingProb", "repairingDepth", "algorithmRepetitions", "inputWarningsStop"};
            throw new IllegalArgumentException("Missing properties in genetic parameters configuration file.\n" +
                    "The following properties are mandatory: " + Arrays.toString(neededProperties));
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException("Could not parse Genetic parameters geneticAlgorithm.configuration file");
        } catch (IOException e) {
            throw new IllegalArgumentException("Could not parse properties in Genetic parameters geneticAlgorithm.configuration file");
        }
    }


    public void setPopulationSize(int populationSize) {
        this.populationSize = populationSize;
    }

    public void setMutationProb(double mutationProb) {
        this.mutationProbability = mutationProb;
    }

    public void setCrossingProb(double crossingProb) {
        this.crossingProbability = crossingProb;
    }

    public void setRepairingDepth(int repairingDepth) {
        this.repairingDepth = repairingDepth;
    }

    public void setGenerations(int generations) {
        this.generations = generations;
    }
}
