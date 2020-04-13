import java.util.ArrayList;

public class ScalarGeneInt extends Gene
{
    private int[] _genotype;
    public int[] getGeneValues() {
        int[] result = new int[2];

        int idx1, idx2;
        idx1 = _rand.nextInt(_genotype.length);
        idx2 = idx1;
        while(idx2  == idx1) {
            idx2 = _rand.nextInt(_genotype.length);
        }//end while

        result[0] = _genotype[idx1];
        result[1] = _genotype[idx2];

        return result;
    }
    public int phenotype() {
        int result = 0;

        for(int i = 0; i < _genotype.length; i++) {
            result += _genotype[i];
        }//end for i
        result = result / _genotype.length;

        return result;
    }

    private void mutationType(CrobeEnums.MutationType mutationType) {
        //RANDOM mutation type is not allowed
        if(mutationType == CrobeEnums.MutationType.RANDOM)
            _mutationType = CrobeEnums.MutationType.SCALAR_DISCREET;
        else
            _mutationType = mutationType;
    }
    private int _mutationRange;

    public ScalarGeneInt(int[] genotype,
                         CrobeEnums.MutationType mutationType,
                         int mutationRange) {
        _genotype = genotype;
        mutationType(mutationType);
        _mutationRange = mutationRange;
    }
    public ScalarGeneInt(int[] genotype,
                         CrobeEnums.MutationType mutationType) {
        _genotype = genotype;
        mutationType(mutationType);

        if(_mutationType == CrobeEnums.MutationType.ADJACENT)
            _mutationRange = 1;
        else
            _mutationRange = Math.round(phenotype() * Crobe.MUTATION_RANGE);
    }

    @Override
    protected Gene recombinate(ArrayList<Gene> genes) {
        ScalarGeneInt gene;

        int genotypeSize = (genes.size() + 1) * 2;
        int[] genotype = new int[genotypeSize];
        int[] geneValues;
        int idx = 0;

        geneValues = this.getGeneValues();
        genotype[idx++] = geneValues[0];
        genotype[idx++] = geneValues[1];
        for(int i = 0; i < genes.size(); i++) {
            ScalarGeneInt sg = (ScalarGeneInt)genes.get(i);
            geneValues = sg.getGeneValues();
            genotype[idx++] = geneValues[0];
            genotype[idx++] = geneValues[1];
        }//end for i

        gene = new ScalarGeneInt(genotype, _mutationType, _mutationRange);

        return gene;
    }

    @Override
    public void mutate(int stressLevel) {
        for(int i = 0; i < _genotype.length; i++) {
            int m = _rand.nextInt(Crobe.MUTATION_RATE);
            if(m < stressLevel)
                _genotype[i] = getMutation(_genotype[i]);
        }//end for i
    }
    private int getMutation(int value) {
        int result = value;
        int sign = _rand.nextInt(2);
        int change = 0;

        switch (_mutationType) {
            case SCALAR_DISCREET:
                change = _rand.nextInt(_mutationRange) + 1;
                break;
            case ADJACENT:
                change = 1;
                break;
        }//end switch

        if (sign == 0) {
            result -= change;
            if (result <= 0)
                result = 1;
        }//end if
        else {
            result += change;
        }//end else

        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(_genotype[0]);
        for(int i = 1; i < _genotype.length; i++) {
            sb.append(", " + _genotype[i]);
        }//end for i
        return phenotype() + "[" + sb.toString() + "]";
    }
}
