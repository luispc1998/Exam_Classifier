package domain.configuration;

import logger.dataGetter.fitnessLogger.GeneticLogger;
import utils.Utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Properties;

/**
 * This represents all the parameters to configure the Genetic Algorithm.
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
    private double crossoverProbability;

    /**
     * Maximum depth of the tree of changes of the repairing algorithm.
     */
    private int repairingDepth;

    /**
     * Number of times the Genetic Algorithm must be repeated.
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
                              int maxSchedulesToTake, double crossoverProbability, int repairingDepth,
                              int algorithmRepetitions, boolean errorAsking) {
        this.generations = generations;
        this.populationSize = populationSize;
        this.loggingFrequency = loggingFrequency;
        this.mutationProbability = mutationProbability;
        this.maxSchedulesToTake = maxSchedulesToTake;
        this.crossoverProbability = crossoverProbability;
        this.repairingDepth = repairingDepth;
        this.algorithmRepetitions = algorithmRepetitions;
        this.errorAsking = errorAsking;
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

    public double getCrossoverProbability() {
        return crossoverProbability;
    }

    public int getRepairingAlgorithmMaxDepth() {
        return repairingDepth;
    }

    public static GeneticParameters loadFromFile(String filePath) {

        Properties geneticProperties = new Properties();
        try (InputStream configStream = new FileInputStream(filePath)) {

            geneticProperties.load(configStream);
            return new GeneticParameters(Integer.parseInt(Utils.nullFilter(geneticProperties.getProperty("generations"))),
                    Integer.parseInt(Utils.nullFilter(geneticProperties.getProperty("populationSize"))),
                    Integer.parseInt(Utils.nullFilter(geneticProperties.getProperty("loggingFreq"))),
                    Double.parseDouble(Utils.nullFilter(geneticProperties.getProperty("mutationProb"))),
                    Integer.parseInt(Utils.nullFilter(geneticProperties.getProperty("maxSchedulesToTake"))),
                    Double.parseDouble(Utils.nullFilter(geneticProperties.getProperty("crossingProb"))),
                    Integer.parseInt(Utils.nullFilter(geneticProperties.getProperty("repairingDepth"))),
                    Integer.parseInt(Utils.nullFilter(geneticProperties.getProperty("algorithmRepetitions"))),
                    Boolean.parseBoolean(Utils.nullFilter(geneticProperties.getProperty("inputWarningsStop"))));


        } catch (NullPointerException e) {
            String[] neededProperties = {"generations", "populationSize", "loggingFreq", "mutationProb",
                "maxSchedulesToTake", "crossingProb", "repairingDepth", "algorithmRepetitions", "inputWarningsStop"};
            throw new IllegalArgumentException("Missing properties in genetic parameters configuration file.\n" +
                    "The following properties are mandatory: " + Arrays.toString(neededProperties));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Could not parse property/ies in Genetic Parameters configuration file due to number format problems.");
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException("Could not find Genetic parameters configuration file");
        } catch (IOException e) {
            throw new IllegalArgumentException("Could not parse properties in Genetic parameters configuration file");
        }
    }


    public void setPopulationSize(int populationSize) {
        this.populationSize = populationSize;
    }

    public void setMutationProb(double mutationProb) {
        this.mutationProbability = mutationProb;
    }

    public void setCrossoverProb(double crossoverProb) {
        this.crossoverProbability = crossoverProb;
    }

    public void setRepairingDepth(int repairingDepth) {
        this.repairingDepth = repairingDepth;
    }

    public void setGenerations(int generations) {
        this.generations = generations;
    }

    public String getStatusMessage() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n");
        sb.append("PARÁMETROS DE COFIGURACIÓN DEL ALGORITMO:");
        sb.append("\n");
        sb.append("Population Size: ");
        sb.append(getPopulationSize());
        sb.append("\n");

        sb.append("Mutation Probability: ");
        sb.append(getMutationProbability());
        sb.append("\n");

        sb.append("Crossover Probability: ");
        sb.append(getCrossoverProbability());
        sb.append("\n");

        sb.append("Repairing MaxDepth: ");
        sb.append(getRepairingAlgorithmMaxDepth());
        sb.append("\n");

        sb.append("Algorithm Generations: ");
        sb.append(getGenerations());
        sb.append("\n");

        sb.append("Algorithm Repetitions: ");
        sb.append(getAlgorithmRepetitions());
        sb.append("\n");

        return sb.toString();
    }
}
