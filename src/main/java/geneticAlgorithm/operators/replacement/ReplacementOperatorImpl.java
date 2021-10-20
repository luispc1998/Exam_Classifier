package geneticAlgorithm.operators.replacement;

import geneticAlgorithm.Individual;
import fitnessFunctions.FitnessFunction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ReplacementOperatorImpl implements ReplacementOperator {

    @Override
    public List<Individual> doReplacement(List<Individual> prevGeneration, List<Individual> childs,
                                          FitnessFunction fitnessFunction) {

        List<Individual> replacements = new ArrayList<>();

        List<Individual> tmp = new ArrayList<>();

        tmp.addAll(childs);
        tmp.addAll(prevGeneration);


        Collections.sort(tmp, new Comparator<Individual>() {
            @Override
            public int compare(Individual c1, Individual c2) {
                return Double.compare(c1.getFitnessScore(fitnessFunction),
                        c2.getFitnessScore(fitnessFunction));
            }
        });

    /*
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
     */

        replacements = tmp.subList(0, prevGeneration.size());

        return replacements;
    }



}
