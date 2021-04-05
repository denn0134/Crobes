package crobes.genetics.genomics;

public class GenomeString extends GenomeValue
{
    private String[] _genotype;
    public String[] genoType() {
        return _genotype;
    }
    public void genoType(String[] genoType) {
        _genotype = genoType.clone();
    }
    private String[] _domain;
    public String[] domain() {
        return _domain;
    }
    public void domain(String[] domain) {
        _domain = domain;
    }

    public GenomeString() {
        super();
    }

    @Override
    String genoTypeJson() {
        StringBuilder sb = new StringBuilder();

        if(_genotype != null) {
            for (int i = 0; i < _genotype.length; i++) {
                sb.append(Genome.quotedString(_genotype[i]));

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
                sb.append(Genome.quotedString(_domain[i]));

                if (i < _domain.length - 1)
                    sb.append(", ");
            }//end for i
        }//end if

        sb.append("]");

        return sb.toString();
    }
}
