package geneticAlgorithm.logger;

/**
 * This groups and stores the data that can be used to plot a fitness graph for a generation of the problem.
 */
public class FitnessGraphDto {

    /**
     * Best fitness during the generation.
     */
    private double bestFitness;

    /**
     * Average fitness during the generation.
     */
    private double averageFitness;

    /**
     * How much time has taken the algorithm until reaching this point.
     */
    private long seconds;

    /**
     * Default constructor for the class.
     * @param bestFitness Best fitness during the generation.
     * @param averageFitness Average fitness during the generation.
     * @param seconds How much time has taken the algorithm until reaching this point.
     */
    public FitnessGraphDto(double bestFitness, double averageFitness, long seconds) {
        this.averageFitness = averageFitness;
        this.bestFitness = bestFitness;
        this.seconds = seconds;
    }

    public double getBestFitness() {
        return bestFitness;
    }

    public double getAverageFitness() {
        return averageFitness;
    }

    public long getSeconds() {
        return seconds;
    }
}
