package crobes.genetics.genomics;

public class GenomeFlt extends GenomeValue
{
    private float[] _genotype;
    public float[] genoType() {
        return _genotype;
    }
    public void genoType(float[] genoType) {
        _genotype = genoType.clone();
    }
    private float _mutationRange;
    public float mutationRange() {
        return _mutationRange;
    }
    public void mutationRange(float mutationRange) {
        _mutationRange = mutationRange;
    }

    public GenomeFlt() {
        super();
    }

    @Override
    String genoTypeJson() {
        StringBuilder sb = new StringBuilder();

        for(int i = 0; i < _genotype.length; i++) {
            sb.append(_genotype[i]);

            if(i < _genotype.length - 1)
                sb.append(", ");
        }//end for i

        return sb.toString();
    }

    @Override
    String domainJson() {
        return "" + _mutationRange;
    }
}
