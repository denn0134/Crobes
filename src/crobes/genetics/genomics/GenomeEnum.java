package crobes.genetics.genomics;

public class GenomeEnum extends GenomeValue
{
    private Enum[] _genotype;
    public Enum[] genoType() {
        return _genotype;
    }
    public void genoType(Enum[] genoType) {
        _genotype = genoType.clone();
    }
    private Enum[] _domain;
    public Enum[] domain() {
        return _domain;
    }
    public void domain(Enum[] domain) {
        _domain = domain;
    }

    public GenomeEnum() {
        super();
    }

    @Override
    String genoTypeJson() {
        StringBuilder sb = new StringBuilder();

        if(_genotype != null) {
            for (int i = 0; i < _genotype.length; i++) {
                sb.append(Genome.quotedString(_genotype[i].name()));

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
                sb.append(Genome.quotedString(_domain[i].name()));

                if (i < _domain.length - 1)
                    sb.append(", ");
            }//end for i
        }//end if

        sb.append("]");

        return sb.toString();
    }
}
