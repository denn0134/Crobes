package crobes.genetics.genes;

import crobes.core.*;

import java.util.ArrayList;

public class HeritableGeneEnum extends HeritableGene
{
    private Enum[] _genotype;
    public Enum[] genoType() {
        return _genotype;
    }
    public Enum[] getGeneValues() {
        Enum[] result = new Enum[2];

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
    public Enum phenotype() {
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

    private Enum[] _dominance;
    public void dominance(Enum[] dominance) {
        _dominance = dominance;
    }
    public Enum[] dominance() {
        return _dominance;
    }

    public HeritableGeneEnum(Enum[] genotype, Enum[] dominance, CrobeEnums.MutationType type) {
        _genotype = genotype;
        _dominance = dominance;
        _mutationType = type;
    }

    @Override
    public void mutate(int stressLevel) {
        for(int i = 0; i < _genotype.length; i++) {
            int m = _rand.nextInt(Crobe.MUTATION_RATE);
            if(m < stressLevel)
                _genotype[i] = getMutation(_genotype[i]);
        }//end for i
    }
    private Enum getMutation(Enum value) {
        int idx = -1;
        for(int i = 0; i < _dominance.length; i++ ) {
            if(value == _dominance[i])
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
        HeritableGeneEnum gene;

        int genotypeSize = (genes.size() + 1) * 2;
        Enum[] genotype = new Enum[genotypeSize];
        Enum[] geneValues;
        int idx = 0;

        geneValues = this.getGeneValues();
        genotype[idx++] = geneValues[0];
        genotype[idx++] = geneValues[1];
        for(int i = 0; i < genes.size(); i++) {
            HeritableGeneEnum sg = (HeritableGeneEnum) genes.get(i);
            geneValues = sg.getGeneValues();
            genotype[idx++] = geneValues[0];
            genotype[idx++] = geneValues[1];
        }//end for i

        gene = new HeritableGeneEnum(genotype, _dominance, _mutationType);

        return gene;
    }

    @Override
    public String toString() {
        if(_genotype != null && _genotype.length > 0) {
            StringBuilder sb = new StringBuilder();
            sb.append(_genotype[0].name());
            for(int i = 1; i < _genotype.length; i++) {
                sb.append(", " + _genotype[i].name());
            }//end for gene

            return String.format("%1$s[%2$s]", phenotype().name(), sb.toString());
        }//end if
        else {
            return "HeritableGeneEnum";
        }
    }
}
