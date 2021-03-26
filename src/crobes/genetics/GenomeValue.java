package crobes.genetics;

import crobes.core.CrobeEnums;

public abstract class GenomeValue
{
    public CrobeEnums.MutationType mutationType;

    public GenomeValue(CrobeEnums.MutationType mutation) {
        mutationType = mutation;
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
        sb.append(Genome.quotedString(mutationType.name()));
        sb.append("}");

        return sb.toString();
    }

    abstract String genoTypeJson();
    abstract String domainJson();

}
