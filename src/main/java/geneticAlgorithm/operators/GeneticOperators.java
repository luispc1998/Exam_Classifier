package geneticAlgorithm.operators;

import geneticAlgorithm.GeneticCore;
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

    public GeneticOperators() {
        this.selectionOperator = new RouletteSelection();
        this.mutationOperator = new MutationSwap();
        this.crossingOperator = new OXCrosssingOperator();
        this.replacementOperator = new ReplacementOperatorClassic(5);
    }

    public GeneticOperators(SelectionOperator selectionOperator, MutationOperator mutationOperator,
                            CrossingOperator crossingOperator, ReplacementOperator replacementOperator) {

    }


    public SelectionOperator getSelectionOperator() {
        return selectionOperator;
    }

    public void setSelectionOperator(SelectionOperator selectionOperator) {
        this.selectionOperator = selectionOperator;
    }

    public MutationOperator getMutationOperator() {
        return mutationOperator;
    }

    public void setMutationOperator(MutationOperator mutationOperator) {
        this.mutationOperator = mutationOperator;
    }

    public CrossingOperator getCrossingOperator() {
        return crossingOperator;
    }

    public void setCrossingOperator(CrossingOperator crossingOperator) {
        this.crossingOperator = crossingOperator;
    }

    public ReplacementOperator getReplacementOperator() {
        return replacementOperator;
    }

    public void setReplacementOperator(ReplacementOperator replacementOperator) {
        this.replacementOperator = replacementOperator;
    }
}
