package crobes.genetics.genes;

import crobes.core.*;
import crobes.genetics.genomics.GenomeString;
import crobes.genetics.genomics.GenomeValue;
import crobes.genetics.genomics.Genomics;

import java.util.ArrayList;

public class HeritableGeneString extends HeritableGene
{
    static {
        Genomics.genes.registerGene(HeritableGeneString.class);
    }//end static

    private String[] _genotype;
    public String[] genoType() {
        return _genotype;
    }
    public String[] getGeneValues() {
        String[] result = new String[2];

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
    public String phenotype() {
        int[] counts = new int[_dominance.length];
        for(int i : counts) {
            i = 0;
        }//end for i

        for(int i = 0; i < _dominance.length; i++) {
            for(int j = 0; j < _genotype.length; j++) {
                if(_genotype[j] == _dominance[i])
                    counts[i]++;
            }//end for j
        }//end for i

        int idx = getInheritanceIndex(counts);
        return _dominance[idx];
    }

    private String[] _dominance;
    public void dominance(String[] dominance) {
        _dominance = dominance;
    }
    public String[] dominance() {
        return _dominance;
    }

    public HeritableGeneString() {
        super();
    }
    public HeritableGeneString(String[] genotype,
                               String[] dominance,
                               CrobeEnums.MutationType mutationType) {
        _genotype = genotype;
        _dominance = dominance;
        mutationType(mutationType);
    }

    @Override
    public void mutate(int stressLevel) {
        for(int i = 0; i < _genotype.length; i++) {
            int m = _rand.nextInt(Crobe.MUTATION_RATE);
            if(m < stressLevel)
                _genotype[i] = getMutation(_genotype[i]);
        }//end for i
    }
    public String getMutation(String value) {
        int idx = -1;
        for(int i = 0; i < _dominance.length; i++ ) {
            if(value.equalsIgnoreCase(_dominance[i]))
                idx = i;
        }//end for i

        switch (_mutationType) {
            case RANDOM:
                idx = _rand.nextInt(_dominance.length);
                break;
            case ADJACENT:
                int r = _rand.nextInt(2);
                if(idx == 0)
                    idx++;
                else if(idx == (_dominance.length - 1))
                    idx--;
                else if(r == 0)
                    idx++;
                else
                    idx--;
        }//end switch

        return _dominance[idx];
    }

    @Override
    public Gene recombinate(ArrayList<Gene> genes) {
        HeritableGeneString gene;

        int genotypeSize = (genes.size() + 1) * 2;
        String[] genotype = new String[genotypeSize];
        String[] geneValues;
        int idx = 0;

        geneValues = this.getGeneValues();
        genotype[idx++] = geneValues[0];
        genotype[idx++] = geneValues[1];
        for(int i = 0; i < genes.size(); i++) {
            HeritableGeneString sg = (HeritableGeneString) genes.get(i);
            geneValues = sg.getGeneValues();
            genotype[idx++] = geneValues[0];
            genotype[idx++] = geneValues[1];
        }//end for i

        gene = new HeritableGeneString(genotype, _dominance, _mutationType);

        return gene;
    }

    @Override
    public String toString() {
        if(_genotype != null && _genotype.length > 0) {
            StringBuilder sb = new StringBuilder();
            sb.append(_genotype[0]);
            for(int i = 1; i < _genotype.length; i++) {
                sb.append(", " + _genotype[i]);
            }//end for gene

            return String.format("%1$s[%2$s]", phenotype(), sb.toString());
        }//end if
        else {
            return "HeritableGeneString";
        }
    }

    @Override
    public Class<? extends GenomeValue> geneValueClass() {
        return GenomeString.class;
    }
}
