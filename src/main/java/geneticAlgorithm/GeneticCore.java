package geneticAlgorithm;

import fitnessFunctions.FitnessFunction;
import geneticAlgorithm.operators.crossing.CrossingOperator;
import geneticAlgorithm.operators.mutation.MutationOperator;
import geneticAlgorithm.operators.replacement.ReplacementOperator;
import geneticAlgorithm.operators.selection.SelectionOperator;
import geneticAlgorithm.utils.Utils;
import me.tongfei.progressbar.ProgressBar;
import random.RandomGenerator;

import java.util.ArrayList;
import java.util.List;

public class GeneticCore {

    private List<Individual> initialPopulation;
    private List<Individual> population;

    // Operators that will be used in the execution
    private SelectionOperator selectionOperator;
    private MutationOperator mutationOperator;
    private CrossingOperator crossingOperator;

    private ReplacementOperator replacementOperator;


    //Probabilities to be considered during the execution
    private double selectionProbability;
    private double mutationProbability;

    public GeneticCore(Individual individualPrime, int popSize) {
        initialPopulation = Utils.generatePopulationOfSizeFromIndividual(popSize, individualPrime);
        population = new ArrayList<>(initialPopulation);
    }


    private Individual geneticAlgorithm(double mutationProbability, FitnessFunction fitnessFunction, int maxIterations) {

        // Coger al mejor individuo y hacer la media del fitness para estudiar convergencia.
        int genCounter = 0;

        // Needed to be repeated for initial generation
        Individual bestIndividual = getBestIndividual(fitnessFunction);
        double averageFitness = averageFitness(fitnessFunction);

        System.out.println("\n" + "Gen: " + genCounter
                + ", Best Fitness: " + fitnessFunction.apply(bestIndividual)
                + ", Avg Fitness: " + averageFitness);

        int counter = 0;

        try (ProgressBar pb = new ProgressBar("Genetic Algorithm", maxIterations)) { // name, initial max
            while (counter < maxIterations) { //limit by iterations, limit by finnding a solution.

                population = computeNewGeneration(fitnessFunction);


                bestIndividual = getBestIndividual(fitnessFunction);
                averageFitness = averageFitness(fitnessFunction);

                System.out.println("\n" + "Gen: " + genCounter
                        + ", Best Fitness: " + fitnessFunction.apply(bestIndividual)
                        + ", Avg Fitness: " + averageFitness);

                counter++;
                pb.step();
            }
        }
            return null;
    }

    private double averageFitness(FitnessFunction fitnessFunction) {

        double accumulator = 0;

        for (Individual idv: population) {
            accumulator += fitnessFunction.apply(idv);
        }

        return accumulator / population.size();
    }

    private Individual getBestIndividual(FitnessFunction fitnessFunction) {
        // We are minimizing, the best individual is the closest fitness to 0
        Individual bestIndividual = null;
        double bestFitnessFound = Double.POSITIVE_INFINITY;

        for (Individual idv: population) {
            double idvFitness = fitnessFunction.apply(idv);
            if (idvFitness < bestFitnessFound){
                bestFitnessFound = idvFitness;
                bestIndividual = idv;
            }
        }

        return bestIndividual;
    }


    private List<Individual> computeNewGeneration(FitnessFunction fitnessFunction) {

        List<Individual> newGeneration = new ArrayList<>(population.size());

        selectionOperator.reset();
        for (int i = 0; i < selectionOperator.maxPairs(); i++) {
            Individual father = selectionOperator.selection(population, fitnessFunction);
            Individual mother = selectionOperator.selection(population, fitnessFunction);

            List<Individual> childs = crossingOperator.doCrossing(father, mother);
            checkForMutation(childs, mutationProbability);
            newGeneration.addAll(replacementOperator.doReplacement(father, mother, childs, fitnessFunction));

        }

        return newGeneration;
    }

    private void checkForMutation(List<Individual> childs, double mutationProb) {
        for (Individual child: childs) {
            if (RandomGenerator.getGenerator().nextDouble() <= mutationProb){
              mutationOperator.mutation(child);
            }
        }

    }
}