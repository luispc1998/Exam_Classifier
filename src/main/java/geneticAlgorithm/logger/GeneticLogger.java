package geneticAlgorithm.logger;

import geneticAlgorithm.Individual;

public class GeneticLogger {

    private StringBuilder data;


    public GeneticLogger() {
        resetLogger();


    }

    public void resetLogger() {
        data = new StringBuilder();
    }

    public void log(int iterations, Individual bestIndividual, double avgFitness) {
        data.append("-------------------------------------------");
        data.append("\n");
        data.append("Iterations: ");
        data.append(iterations);
        data.append("\n");
        data.append("Avg Fitness: ");
        data.append(avgFitness);
        data.append("\n");
        data.append("Best Fitness: ");
        data.append(bestIndividual.getFitnessScore(null));
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
