package fitnessFunctions.greedyAlgorithm;

import configuration.WeightConfigurer;
import domain.DataHandler;
import domain.constrictions.Constriction;
import domain.constrictions.counter.ConstrictionCounter;
import domain.constrictions.counter.ConstrictionCounterImpl;
import domain.constrictions.types.SameDayConstriction;
import domain.constrictions.types.TimeDisplacementConstriction;
import geneticAlgorithm.Individual;
import fitnessFunctions.FitnessFunction;

public class FitnessFunctionImpl implements FitnessFunction {

    private DataHandler dataHandler;
    private CromosomeDecoder decoder;

    public FitnessFunctionImpl(){
        decoder = new CromosomeDecoder();
    }

    public FitnessFunctionImpl(DataHandler dataHandler){
        this();
        this.dataHandler = dataHandler;
    }

    @Override
    public double apply(Individual a) {

        // Deconde the cromosome
        decoder.decode(a, dataHandler);

        //Count constrictions
        ConstrictionCounter counter = new ConstrictionCounterImpl();
        for (Constriction constriction: dataHandler.getConstrictions()) {
                constriction.isFulfilled(counter);
        }


        dataHandler.resetScheduling();


        //Do the formula.



        return formula(counter);
    }

    private double formula(ConstrictionCounter counter) {
        WeightConfigurer wc = dataHandler.getConfigurer().getWeightConfigurer();

        return counter.getCountOfTimeDisplacementConstriction()
                        * wc.getConstrictionWeight(TimeDisplacementConstriction.CONSTRICTION_ID) +
                counter.getCountOfDaysBannedConstriction()
                        * wc.getConstrictionWeight(SameDayConstriction.CONSTRICTION_ID) +
                counter.getCountOfSameDayConstriction()
                        * wc.getConstrictionWeight(SameDayConstriction.CONSTRICTION_ID);

    }
}
