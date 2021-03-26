package crobes.genetics.genomics;

import crobes.genetics.genes.HeritableGeneString;

public class GenomeString extends GenomeValue
{
    public String[] genotype;
    public String[] domain;

    public GenomeString(HeritableGeneString gene) {
        super(gene.mutationType());

        genotype = gene.genoType().clone();
        domain = gene.dominance().clone();
    }

    @Override
    String genoTypeJson() {
        StringBuilder sb = new StringBuilder();

        for(int i = 0; i < genotype.length; i++) {
            sb.append(Genome.quotedString(genotype[i]));

            if(i < genotype.length - 1)
                sb.append(", ");
        }//end for i

        return sb.toString();
    }

    @Override
    String domainJson() {
        StringBuilder sb = new StringBuilder();

        sb.append("[");

        for(int i = 0; i < domain.length; i++) {
            sb.append(Genome.quotedString(domain[i]));

            if(i < domain.length - 1)
                sb.append(", ");
        }//end for i

        sb.append("]");

        return sb.toString();
    }
}
