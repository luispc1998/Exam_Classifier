package geneticAlgorithm.logger;

import geneticAlgorithm.Individual;
import geneticAlgorithm.fitnessFunctions.FitnessFunction;
import geneticAlgorithm.fitnessFunctions.greedyAlgorithm.LinearFitnessFunction;
import utils.Utils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This logs the current state of the genetic algorithm over the generations.
 */
public class GeneticLogger {

    /**
     * The logged String to be written to a file afterwards.
     */
    private StringBuilder data;

    /**
     * Fitness data to obtaing the graph.
     */
    private StringBuilder fitnessGraphData;

    private HashMap<Integer, List<Double>> fitnessGraphDataMap;
    /**
     * Default constructor for the class
     */
    public GeneticLogger() {
        data = new StringBuilder();
        fitnessGraphData = new StringBuilder();
        fitnessGraphDataMap = new HashMap<>();
    }



    /**
     * Adds a log string to {@code data}.
     * @param iterations Number of iterations (generations) of the genetic algorithm.
     * @param bestIndividual The current best individual of the genetic algorithm.
     * @param avgFitness The average fitness of the population of the genetic algorithm.
     * @param fitnessFunction The current fitness function used.
     */
    public void log(int iterations, Individual bestIndividual, double avgFitness, FitnessFunction fitnessFunction) {
        data.append("-------------------------------------------");
        data.append("\n");
        data.append("Generations: ");
        data.append(iterations);
        data.append("\n");
        data.append("Avg Fitness: ");
        data.append(String.format("%.2f",avgFitness));
        data.append("\n");
        data.append("Best Fitness: ");
        data.append(String.format("%.2f",bestIndividual.getFitnessScore(fitnessFunction)));
        data.append("\n");
        data.append("Best Individual");
        data.append("\n");
        data.append(bestIndividual);
        data.append("\n");
        data.append("-------------------------------------------");
        data.append("\n");
    }


    /**
     * Returns the logged String.
     * @return The logged String.
     */
    public String getLoggedData() {
        return data.toString();
    }

    /**
     * Adds a new line to the fitness graph data.
     * @param genCounter Current generation.
     * @param averageFitness Current mean fitness.
     */
    public void addAverageFitnessOnIt(int genCounter, double averageFitness) {
        /*
        fitnessGraphData.append(genCounter);
        fitnessGraphData.append(",");
        fitnessGraphData.append(averageFitness);
        fitnessGraphData.append("\n");

         */

        if (!fitnessGraphDataMap.containsKey(genCounter)){
            fitnessGraphDataMap.put(genCounter, new ArrayList<>());
        }
        fitnessGraphDataMap.get(genCounter).add(averageFitness);
    }

    /**
     * Returns the logged data to generate the fitness graph.
     * @return The logged data to generate the fitness graph.
     */
    public String getFitnessGraphData() {
        List<double[]> values = new ArrayList<>();
        for (Map.Entry<Integer, List<Double>> entry : fitnessGraphDataMap.entrySet()) {
            double accumulator = 0;
            for (Double value: entry.getValue()) {
                accumulator+=value;
            }
               values.add(new double[] {entry.getKey(), accumulator/entry.getValue().size()});
        }

        StringBuilder sb = new StringBuilder();

        for (double[] pair : values) {
            sb.append(pair[0]);
            sb.append(",");
            sb.append(pair[1]);
            sb.append("\n");
        }

        return sb.toString(); //fitnessGraphData.toString();
    }

    /**
     * Writes the fitness graph data to a .csv file.
     * @param directory Folder in which the graph file will be written.
     * @param filename The filename of the graph file.
     */
    public void writeFitnessGraphData(String directory, String filename) {
        Utils.createDirectory(directory);
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(directory + "/" + filename + ".csv"))) {
            bw.write(getFitnessGraphData());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
