package crobes.genetics.genomics;

import crobes.core.CrobeEnums;

public abstract class GenomeValue
{
    private CrobeEnums.MutationType _mutationType;
    public CrobeEnums.MutationType mutationType() {
        return _mutationType;
    }
    public void mutationType(CrobeEnums.MutationType  mutationType) {
        _mutationType = mutationType;
    }

    public GenomeValue() {

    }

    String toJson() {
        StringBuilder sb = new StringBuilder();

        sb.append("{");
        sb.append(Genome.quotedString("genoType"));
        sb.append(": [");
        sb.append(genoTypeJson());
        sb.append("], ");
        sb.append(Genome.quotedString("domain"));
        sb.append(": ");
        sb.append(domainJson());
        sb.append(", ");
        sb.append(Genome.quotedString("mutationType"));
        sb.append(": ");
        sb.append(Genome.quotedString(_mutationType.name()));
        sb.append("}");

        return sb.toString();
    }

    abstract String genoTypeJson();
    abstract String domainJson();

}
