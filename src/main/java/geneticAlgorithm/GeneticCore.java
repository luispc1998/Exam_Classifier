package geneticAlgorithm;

import geneticAlgorithm.fitnessFunctions.FitnessFunction;
import geneticAlgorithm.operators.GeneticOperators;
import geneticAlgorithm.operators.crossing.CrossingOperator;
import geneticAlgorithm.operators.mutation.MutationOperator;
import geneticAlgorithm.operators.replacement.ReplacementOperator;
import geneticAlgorithm.operators.selection.SelectionOperator;
import logger.dataGetter.fitnessLogger.GeneticLogger;
import me.tongfei.progressbar.ProgressBar;
import me.tongfei.progressbar.ProgressBarStyle;
import utils.Utils;
import utils.random.RandomGenerator;

import java.text.DecimalFormat;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * This in the Genetic Algorithm. In this class the process to produce new generations is declared.
 *
 * <p>
 * The operators used by the algorithm are externalized and they are implemented by means of the Strategy design pattern.
 *
 * @see SelectionOperator
 * @see CrossingOperator
 * @see MutationOperator
 * @see ReplacementOperator
 */
public class GeneticCore {

    /**
     * Current population of individuals.
     */
    private List<Individual> population;

    // Operators that will be used in the execution.

    /**
     * Selection operator for the Genetic Algorithm.
     * <p>
     * The design follows the "Strategy" design pattern.
     */
    private final SelectionOperator selectionOperator;

    /**
     * Mutation operator for the Genetic Algorithm.
     * <p>
     * The design follows the "Strategy" design pattern.
     */
    private final MutationOperator mutationOperator;

    /**
     * Crossing operator for the Genetic Algorithm.
     * <p>
     * The design follows the "Strategy" design pattern.
     */
    private final CrossingOperator crossingOperator;

    /**
     * Replacement operator for the Genetic Algorithm.
     * <p>
     * The design follows the "Strategy" design pattern.
     */
    private final ReplacementOperator replacementOperator;

    /**
     * Logger for the Genetic Algorithm.
     */
    private final GeneticLogger logger;

    /**
     * Constructor for the class
     * @param individualPrime First individual from which the initial population will be created.
     * @param popSize Size of the population to be handled by the algorithm.
     * @param geneticOperators Operator configuration for the algorithm.
     * @param geneticLogger The Genetic Logger for the algorithm
     */
    public GeneticCore(Individual individualPrime, int popSize, GeneticOperators geneticOperators, GeneticLogger geneticLogger) {
        if (individualPrime.getChromosome().size() == 0) {
            throw new IllegalArgumentException("There are no exams ids to work with in the given individual.");
        }
        /*
          First population of individuals
         */
        List<Individual> initialPopulation = Utils.generatePopulationOfSizeFromIndividual(popSize, individualPrime);
        population = new ArrayList<>(initialPopulation);

        this.selectionOperator = geneticOperators.getSelectionOperator();
        this.mutationOperator = geneticOperators.getMutationOperator();
        this.crossingOperator = geneticOperators.getCrossingOperator();
        this.replacementOperator = geneticOperators.getReplacementOperator();
        this.logger = geneticLogger;

    }

    /**
     * Constructor for the class.
     * @param individualPrime First individual from which the initial population will be created.
     * @param popSize Size of the population to be handled by the algorithm.
     * @param geneticOperators Operator configuration for the algorithm.
     * @param geneticLogger The Genetic Logger for the algorithm.
     * @param elite Set of Individuals that must be considered by the algorithm since generation 0.
     */
    public GeneticCore(Individual individualPrime, int popSize, GeneticOperators geneticOperators,
                       GeneticLogger geneticLogger, HashSet<Individual> elite) {

        this(individualPrime, popSize, geneticOperators, geneticLogger);

        if (elite.size() > popSize)
            throw new IllegalArgumentException("Elite indviduals set is larger than the actual population.");

        for (Individual idv : elite) {
            population.remove(0);
            population.add(idv);
        }


    }

    /**
     * Genetic algorithm skeleton.
     * @param mutationProbability Probability for new individuals to mutate.
     * @param fitnessFunction Fitness function to be used by the algorithm.
     * @param maxIterations Maximum number of iterations that the algorithm will do.
     * @param loggingFrequency Number of iterations after which the algorithm logs its state.
     * @return The best individual.
     */
    public Individual geneticAlgorithm(double mutationProbability, double crossingProbability, FitnessFunction fitnessFunction, int maxIterations,
                                       int loggingFrequency) {

        long initialTime = System.currentTimeMillis();
        // Coger al mejor individuo y hacer la media del fitness para estudiar convergencia.
        int genCounter = 0;

        // Needed to be repeated for initial generation
        Individual bestIndividual = getBestIndividual(fitnessFunction);
        double averageFitness = averageFitness(fitnessFunction);

        logger.addAverageFitnessOnIt(genCounter, bestIndividual.getFitnessScore(fitnessFunction), averageFitness,
                (System.currentTimeMillis() - initialTime)/1000);



        System.out.println("\n" + "Gen: " + genCounter
                + ", Best Fitness: " + bestIndividual.getFitnessScore(fitnessFunction)
                + ", Avg Fitness: " + averageFitness);
         System.out.println(bestIndividual);






        logger.log(genCounter, bestIndividual, averageFitness, fitnessFunction);


        try (ProgressBar pb =
                     new ProgressBar("GA", maxIterations, 1500, System.out, ProgressBarStyle.UNICODE_BLOCK,
                             "", 1L, false, (DecimalFormat)null, ChronoUnit.SECONDS, 0L, Duration.ZERO)) { // name, initial max
            while (genCounter < maxIterations) { //limit by iterations, limit by finding a solution.

                population = computeNewGeneration(fitnessFunction, mutationProbability, crossingProbability);
                genCounter++;

                bestIndividual = getBestIndividual(fitnessFunction);
                averageFitness = averageFitness(fitnessFunction);

                logger.addAverageFitnessOnIt(genCounter, bestIndividual.getFitnessScore(fitnessFunction), averageFitness,
                        (System.currentTimeMillis() - initialTime)/1000);

                if (genCounter % loggingFrequency == 0) {
                    logger.log(genCounter, bestIndividual, averageFitness, fitnessFunction);
                }

                pb.step();
                pb.setExtraMessage("BF: " + String.format("%.2f",bestIndividual.getFitnessScore(fitnessFunction)) +
                        ", AF: " + String.format("%.2f",averageFitness));
            }

            System.out.println("\n" + "[Gen: " + genCounter
                    + ", Best Fitness: " + String.format("%.2f",bestIndividual.getFitnessScore(fitnessFunction))
                    + ", Avg Fitness: " + String.format("%.2f",averageFitness) + "]");

            System.out.println(bestIndividual);


        }
            return bestIndividual;
    }

    /**
     * Computes the average fitness of the current population.
     * @param fitnessFunction Fitness function to be used by the algorithm.
     * @return The average fitness of the current population.
     */
    private double averageFitness(FitnessFunction fitnessFunction) {

        double accumulator = 0;

        for (Individual idv: population) {
            accumulator += idv.getFitnessScore(fitnessFunction);
        }

        return accumulator / population.size();
    }

    /**
     * Returns the best individual of the population.
     *
     * <p>
     * The best individual is the one with less fitness value. We are maximizing f(x) = 1 / fitnessValue.
     * @param fitnessFunction Fitness function to be used by the algorithm.
     * @return The individual with less fitness value of the population.
     */
    private Individual getBestIndividual(FitnessFunction fitnessFunction) {
        // We are minimizing, the best individual is the closest fitness to 0
        Individual bestIndividual = null;
        double bestFitnessFound = Double.POSITIVE_INFINITY;

        for (Individual idv: population) {
            double idvFitness = idv.getFitnessScore(fitnessFunction);
            if (idvFitness < bestFitnessFound){
                bestFitnessFound = idvFitness;
                bestIndividual = idv;
            }
        }

        return bestIndividual;
    }

    /**
     * Computes a new generation of the population.
     * @param fitnessFunction Fitness function to be used by the algorithm.
     * @param mutationProbability The mutation probability of the new individuals.
     * @param crossingProb The crossing probability of the individuals.
     * @return The new population.
     */
    private List<Individual> computeNewGeneration(FitnessFunction fitnessFunction, double mutationProbability,
                                                  double crossingProb) {

        List<Individual> newGenChilds = new ArrayList<>(population.size());

        while (newGenChilds.size() != population.size()) {
            Individual father = selectionOperator.selection(population, fitnessFunction);
            Individual mother = selectionOperator.selection(population, fitnessFunction);

            if (RandomGenerator.getGenerator().nextDouble() <= crossingProb){
                List<Individual> childs = crossingOperator.crossover(father, mother);
                checkForMutation(childs, mutationProbability);
                newGenChilds.addAll(childs);
            }

        }

        return replacementOperator.doReplacement(population, newGenChilds, fitnessFunction);
    }

    /**
     * Checks if a child must have a mutation.
     * @param childs List of the new individuals.
     * @param mutationProb The mutation probability of the new individuals.
     */
    private void checkForMutation(List<Individual> childs, double mutationProb) {
        for (int i = 0; i < childs.size(); i++) {
            if (RandomGenerator.getGenerator().nextDouble() <= mutationProb){
                childs.set(i, mutationOperator.mutation(childs.get(i)));
            }
        }
    }

    /**
     * Returns the current population.
     * @return The current population.
     */
    public List<Individual> getPopulation(){
        return population;
    }

    /**
     * Returns the logged data written by {@code GeneticLogger}.
     * @return Logged data written by the {@code GeneticLogger}.
     */
    public String getLogging() {
        return logger.getLoggedData();
    }

    /**
     * Returns the logged data to generate the fitness graph.
     * @return the logged data to generate the fitness graph.
     */
    public String getFitnessGraphData() {
        return logger.getFitnessGraphData();
    }
}
