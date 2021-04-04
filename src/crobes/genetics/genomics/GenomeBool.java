package crobes.genetics.genomics;

public class GenomeBool extends GenomeValue
{
    private boolean[] _genotype;
    public boolean[] genoType() {
        return _genotype;
    }
    public void genoType(boolean[] genoType) {
        _genotype = genoType.clone();
    }
    private boolean[] _domain;
    public boolean[] domain() {
        return _domain;
    }
    public void domain(boolean[] domain) {
        _domain = domain.clone();
    }

    public GenomeBool() {
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
        StringBuilder sb = new StringBuilder();

        sb.append("[");

        if(_domain != null) {
            for (int i = 0; i < _domain.length; i++) {
                sb.append(_domain[i]);

                if (i < _domain.length - 1)
                    sb.append(", ");
            }//end for i
        }//end if

        sb.append("]");

        return sb.toString();
    }
}
