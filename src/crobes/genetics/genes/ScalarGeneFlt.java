package crobes.genetics.genes;

import crobes.core.*;
import crobes.genetics.genomics.GenomeFlt;
import crobes.genetics.genomics.GenomeValue;
import crobes.genetics.genomics.Genomics;

import java.util.ArrayList;

public class ScalarGeneFlt extends Gene
{
    static {
        Genomics.genes.registerGene(ScalarGeneFlt.class);
    }//end static

    private float[] _genotype;
    public float[] genoType() {
        return _genotype;
    }
    public float[] getGeneValues() {
        float[] result = new float[2];

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
    public float phenotype() {
        float result = 0.0f;

        for(int i = 0; i < _genotype.length; i++) {
            result += _genotype[i];
        }//end for i
        result = result / _genotype.length;

        return result;
    }

    private float _mutationRange;
    public float mutationRange() {
        return _mutationRange;
    }
    private float getMutation(float value) {
        float result = value;
        int sign = _rand.nextInt(2);

        float change = _rand.nextFloat() * _mutationRange;

        if(sign == 0) {
            result -= change;
            if(result <= 0)
                result += 0.01f;
        }//end if
        else {
            result += change;
        }//end else

        result = Math.round(result * 100.00f) / 100.00f;

        return result;
    }

    public ScalarGeneFlt() {
        super();
    }
    public ScalarGeneFlt(float[] genotype,
                         float mutationRange) {
        _genotype = genotype;
        _mutationType = CrobeEnums.MutationType.SCALAR_DISCREET;
        _mutationRange = mutationRange;
    }
    public ScalarGeneFlt(float[] genotype) {
        _genotype = genotype;
        _mutationType = CrobeEnums.MutationType.SCALAR_DISCREET;
        _mutationRange = phenotype() * Crobe.MUTATION_RANGE;
    }

    @Override
    public Gene recombinate(ArrayList<Gene> genes) {
        ScalarGeneFlt gene;

        int genotypeSize = (genes.size() + 1) * 2;
        float[] genotype = new float[genotypeSize];
        float[] geneValues;
        int idx = 0;

        geneValues = this.getGeneValues();
        genotype[idx++] = geneValues[0];
        genotype[idx++] = geneValues[1];
        for(int i = 0; i < genes.size(); i++) {
            ScalarGeneFlt sg = (ScalarGeneFlt)genes.get(i);
            geneValues = sg.getGeneValues();
            genotype[idx++] = geneValues[0];
            genotype[idx++] = geneValues[1];
        }//end for i

        gene = new ScalarGeneFlt(genotype, _mutationRange);

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

    @Override
    public CrobeEnums.MutationType[] allowedMutations() {
        return new CrobeEnums.MutationType[] {CrobeEnums.MutationType.SCALAR_DISCREET};
    }
    @Override
    public Class<? extends GenomeValue> geneValueClass() {
        return GenomeFlt.class;
    }
}
