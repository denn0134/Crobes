package crobes.genetics;

/***
 * This is the model of a single gene within a Genome.
 */
public class GenomeGene
{
    public String name;
    public boolean random;
    public String geneType;
    public GenomeValue geneValue;

    public GenomeGene(String geneName, String type) {
        name = geneName;
        random = false;
        geneType = type;
        geneValue = null;
    }

    String toJson() {
        StringBuilder sb = new StringBuilder();

        sb.append("{");
        sb.append(Genome.quotedString("name"));
        sb.append(": ");
        sb.append(Genome.quotedString(name));
        sb.append(", ");
        sb.append(Genome.quotedString("geneType"));
        sb.append(", ");
        sb.append(Genome.quotedString(geneType));
        sb.append(", ");
        sb.append(Genome.quotedString("random"));
        sb.append(", ");
        sb.append(random);
        sb.append(", ");
        sb.append(Genome.quotedString("geneValue"));
        sb.append(", ");

        if(geneValue != null)
            sb.append(geneValue.toJson());
        else
            sb.append("null");

        sb.append("}");

        return sb.toString();
    }
}
