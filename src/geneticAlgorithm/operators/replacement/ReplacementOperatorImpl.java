package geneticAlgorithm.operators.replacement;

import geneticAlgorithm.Individual;
import fitnessFunctions.FitnessFunction;

import java.util.ArrayList;
import java.util.List;

public class ReplacementOperatorImpl implements ReplacementOperator {

    @Override
    public List<Individual> doReplacement(Individual father, Individual mother, List<Individual> childs,
                                          FitnessFunction fitnessFunction) {

        List<Individual> replacements = new ArrayList<>();

        List<Individual> tmp = new ArrayList<>();

        tmp.add(father);
        tmp.add(mother);
        tmp.addAll(childs);

        Individual survivor1 = tmp.get(0);
        Individual survivor2 = tmp.get(1);

        for (Individual idv: tmp) {
            double fitness = fitnessFunction.apply(idv);
            double fitnessS1 = fitnessFunction.apply(survivor1);
            double fitnessS2 = fitnessFunction.apply(survivor2);

            if (fitness < fitnessS1 && idv != survivor2) {
                survivor1 = idv;
                continue;
            }

            if (fitness < fitnessS2 && idv != survivor1) {
                survivor2 = idv;
            }

        }


        replacements.add(survivor1);
        replacements.add(survivor2);

        return replacements;
    }



}
