package geneticAlgorithm;

import fitnessFunctions.FitnessFunction;
import fitnessFunctions.greedyAlgorithm.ChromosomeDecoder;

import java.util.ArrayList;
import java.util.List;

/**
 * This represents each of the members of the population for the {@link GeneticCore}
 *
 * <p>
 * The main purpose of this class is to contain the {@code chromosome} and provide methods to
 * get its {@code fitnessScore}
 *
 * <p>
 * There are many functionalities  that work over Individuals.
 *

 * @see Enconder
 * @see GeneticCore
 * @see ChromosomeDecoder
 */
public class Individual {

    /**
     * List of integer representing a state of the problem.
     *
     * @see Enconder
     */
    private final List<Integer> chromosome;

    /**
     * Fitness Score assigned to this individual. Null if not computed yet.
     */
    private Double fitnessScore;

    /**
     * Constructor for the class
     * @param chromosome List of integer representing a state of the problem.
     */
    public Individual(List<Integer> chromosome) {
        this.chromosome = new ArrayList<>(chromosome);
    }

    /**
     * Returns the {@code chromosome}
     * @return The {@code chromosome}
     */
    public List<Integer> getChromosome() {
        return new ArrayList<>(chromosome);
    }


    /**
     * Returns the fitness score of the Individual
     * @param fitnessFunction In case {@code fitnessScore} is not computed. It is computed with this function.
     * @return the value of {@code fitnessScore} for the indivial.
     */
    public double getFitnessScore(FitnessFunction fitnessFunction) {
        if (fitnessScore == null) {
            fitnessScore = fitnessFunction.apply(this);
        }
        return fitnessScore;
    }


    @Override
    public boolean equals(Object obj){
        if (! (obj instanceof Individual)) {
            return false;
        }
        Individual idv = (Individual) obj;
        if (idv.getChromosome().size() != getChromosome().size()) {
            return false;
        }

        for (int i = 0; i < getChromosome().size(); i++) {
            if (! getChromosome().get(i).equals(idv.getChromosome().get(i))){
                return false;
            }
        }

        return true;
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        if (chromosome.size() == 0) {
            return "Individual: []";
        }
        sb.append("Individual: [" );
        for (int i = 0; i < chromosome.size()-1; i++) {
            sb.append(chromosome.get(i));
            sb.append(", ");
        }
        sb.append(chromosome.get(chromosome.size()-1));
        sb.append("]");
        return sb.toString();
    }


}
