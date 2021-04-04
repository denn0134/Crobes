package crobes.genetics.genomics;

import crobes.genetics.genes.ScalarGeneInt;

public class GenomeInt extends GenomeValue
{
    private int[] _genotype;
    public int[] genoType() {
        return _genotype;
    }
    public void genoType(int[] genoType) {
        _genotype = genoType.clone();
    }
    private int _mutationRange;
    public int mutationRange() {
        return _mutationRange;
    }
    public void mutationRange(int mutationRange) {
        _mutationRange = mutationRange;
    }

    public GenomeInt() {
        super();
    }

    @Override
    String genoTypeJson() {
        StringBuilder sb = new StringBuilder();

        if(_genotype != null) {
            for (int i = 0; i < _genotype.length; i++) {
                sb.append(_genotype[i]);

                if (i < _genotype.length - 1)
                    sb.append(", ");
            }//end for i
        }//end if

        return sb.toString();
    }

    @Override
    String domainJson() {
        return "" + _mutationRange;
    }
}
