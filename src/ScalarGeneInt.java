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

    private int getMutation(int value) {
        System.out.println("MUTATION!-INT");

        int result = value;
        int rng = (int) Math.round(value * Crobe.MUTATION_RANGE);
        int sign = _rand.nextInt(2);
        int change = _rand.nextInt(rng) + 1;
        if(sign == 0) {
            result -= change;
            if(result <= 0)
                result = 1;
        }//end if
        else {
            result += change;
        }//end else

        return result;
    }

    public ScalarGeneInt(int[] genotype) {
        _genotype = genotype;
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

        gene = new ScalarGeneInt(genotype);

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
