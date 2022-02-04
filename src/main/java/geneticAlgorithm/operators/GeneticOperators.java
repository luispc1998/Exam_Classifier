package geneticAlgorithm.operators;

import geneticAlgorithm.operators.crossing.CrossingOperator;
import geneticAlgorithm.operators.crossing.OXCrosssingOperator;
import geneticAlgorithm.operators.mutation.MutationOperator;
import geneticAlgorithm.operators.mutation.MutationSwap;
import geneticAlgorithm.operators.replacement.ReplacementOperator;
import geneticAlgorithm.operators.replacement.ReplacementOperatorClassic;
import geneticAlgorithm.operators.selection.RouletteSelection;
import geneticAlgorithm.operators.selection.SelectionOperator;

/**
 * This groups the operators to be passed as parameter to the genetic algorithm.
 */
public class GeneticOperators {

    /**
     * The selection operator.
     *
     * @see SelectionOperator
     */
    private SelectionOperator selectionOperator;

    /**
     * A mutation operator.
     *
     * @see MutationOperator
     */
    private MutationOperator mutationOperator;

    /**
     * A crossing operator.
     *
     * @see CrossingOperator
     */
    private CrossingOperator crossingOperator;

    /**
     * A replacement operator.
     *
     * @see ReplacementOperator
     */
    private ReplacementOperator replacementOperator;

    /**
     * Default constructor of the class, providing a default configuration of operators.
     */
    public GeneticOperators(int populationSize) {
        this.selectionOperator = new RouletteSelection();
        this.mutationOperator = new MutationSwap();
        this.crossingOperator = new OXCrosssingOperator();
        //this.replacementOperator = new ReplacementOperatorFitnessBiased();
        this.replacementOperator = new ReplacementOperatorClassic(populationSize/25);
    }

    /**
     * Constructor fully parametrized.
     * @param selectionOperator The selection operator.
     * @param mutationOperator The mutation operator.
     * @param crossingOperator The crossing operator.
     * @param replacementOperator The replacement operator.
     */
    public GeneticOperators(SelectionOperator selectionOperator, MutationOperator mutationOperator,
                            CrossingOperator crossingOperator, ReplacementOperator replacementOperator) {

        this.selectionOperator = selectionOperator;
        this.mutationOperator = mutationOperator;
        this.crossingOperator = crossingOperator;
        this.replacementOperator = replacementOperator;
    }

    /**
     * Returns the configured Selection operator.
     * @return The configured Selection operator.
     */
    public SelectionOperator getSelectionOperator() {
        return selectionOperator;
    }

    /**
     * Returns the configured Mutation operator.
     * @return The configured Mutation operator.
     */
    public MutationOperator getMutationOperator() {
        return mutationOperator;
    }

    /**
     * Returns the configured Crossing operator.
     * @return The configured Crossing operator.
     */
    public CrossingOperator getCrossingOperator() {
        return crossingOperator;
    }

    /**
     * Returns the configured Replacement operator.
     * @return The configured Replacement operator.
     */
    public ReplacementOperator getReplacementOperator() {
        return replacementOperator;
    }

}
