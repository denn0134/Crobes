import java.util.ArrayList;

public class HeritableGeneBool extends HeritableGene
{
    private boolean[] _genotype;
    public boolean[] getGeneValues() {
        boolean[] result = new boolean[2];

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
    public boolean phenotype() {
        int[] counts = {0, 0};
        for(int i = 0; i < _dominance.length; i++) {
            for(int j = 0; j < _genotype.length; j++) {
                if(_genotype[j] == _dominance[i])
                    counts[i]++;
            }//end for j
        }//end for i

        int idx = getInheritanceIndex(counts);
        return _dominance[idx];
    }

    private boolean[] _dominance;
    public void dominance(boolean[] hierarchy) { _dominance = hierarchy; }

    public HeritableGeneBool(boolean[] genotype,
                             boolean[] hierarchy,
                             CrobeEnums.MutationType mutationType) {
        _genotype = genotype;
        _dominance = hierarchy;
        mutationType(mutationType);
    }

    private String getBoolStr(boolean b) {
        if(b)
            return "T";
        else
            return "F";
    }

    @Override
    public void mutate(int stressLevel) {
        for(int i = 0; i < _genotype.length; i++) {
            int m = _rand.nextInt(Crobe.MUTATION_RATE);
            if(m < stressLevel)
                _genotype[i] = getMutation(_genotype[i]);
        }//end for i
    }
    private boolean getMutation(boolean value) {
        boolean result = value;

        switch (_mutationType) {
            case RANDOM:
                int b = _rand.nextInt(2);
                result = (b == 1);
                break;
            case ADJACENT:
                result = !result;
        }//end switch

        return result;
    }

    @Override
    protected Gene recombinate(ArrayList<Gene> genes) {
        HeritableGeneBool gene;

        int genotypeSize = (genes.size() + 1) * 2;
        boolean[] genotype = new boolean[genotypeSize];
        boolean[] geneValues;
        int idx = 0;

        geneValues = this.getGeneValues();
        genotype[idx++] = geneValues[0];
        genotype[idx++] = geneValues[1];
        for(int i = 0; i < genes.size(); i++) {
            HeritableGeneBool sg = (HeritableGeneBool) genes.get(i);
            geneValues = sg.getGeneValues();
            genotype[idx++] = geneValues[0];
            genotype[idx++] = geneValues[1];
        }//end for i

        gene = new HeritableGeneBool(genotype, _dominance, _mutationType);

        return gene;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getBoolStr(_genotype[0]));
        for(int i = 1; i < _genotype.length; i++) {
            sb.append(", " + getBoolStr(_genotype[i]));
        }//end for i
        return phenotype() + "[" + sb.toString() + "]";
    }
}
