package geneticAlgorithm;

import java.util.ArrayList;
import java.util.List;

public class Individual {

    private List<Integer> cromosome;

    public Individual(List<Integer> cromosome) {
        this.cromosome = new ArrayList<>(cromosome);
    }

    public List<Integer> getCromosome() {
        return new ArrayList<>(cromosome);
    }

    @Override
    public boolean equals(Object obj){
        if (! (obj instanceof Individual)) {
            return false;
        }
        Individual idv = (Individual) obj;
        if (((Individual) obj).getCromosome().size() != getCromosome().size()) {
            return false;
        }

        for (int i = 0; i < getCromosome().size(); i++) {
            if (getCromosome().get(i).equals(idv.getCromosome().get(i))){
                return false;
            }
        }

        return true;


    }
}
