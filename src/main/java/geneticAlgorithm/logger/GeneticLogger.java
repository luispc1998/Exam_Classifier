package geneticAlgorithm.logger;

import geneticAlgorithm.Individual;

/**
 * This logs the current state of the genetic algorithm over the generations.
 */
public class GeneticLogger {

    /**
     * The logged String to be written to a file afterwards.
     */
    private StringBuilder data;

    /**
     * Default constructor for the class
     */
    public GeneticLogger() {
        resetLogger();
    }

    /**
     * Resets the logger state, that is, removes all content from {@code data}.
     */
    public void resetLogger() {
        data = new StringBuilder();
    }

    /**
     * Adds a log string to {@code data}.
     * @param iterations Number of iterations (generations) of the genetic algorithm.
     * @param bestIndividual The current best individual of the genetic algorithm.
     * @param avgFitness The average fitness of the population of the genetic algorithm.
     */
    public void log(int iterations, Individual bestIndividual, double avgFitness) {
        data.append("-------------------------------------------");
        data.append("\n");
        data.append("Generations: ");
        data.append(iterations);
        data.append("\n");
        data.append("Avg Fitness: ");
        data.append(String.format("%.2f",avgFitness));
        data.append("\n");
        data.append("Best Fitness: ");
        data.append(String.format("%.2f",bestIndividual.getFitnessScore(null)));
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
}
