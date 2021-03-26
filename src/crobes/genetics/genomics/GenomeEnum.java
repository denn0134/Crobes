package crobes.genetics.genomics;

import crobes.genetics.genes.HeritableGeneEnum;

public class GenomeEnum extends GenomeValue
{
    public Enum[] genotype;
    public Enum[] domain;

    public GenomeEnum(HeritableGeneEnum gene) {
        super(gene.mutationType());

        genotype = gene.genoType().clone();
        domain = gene.dominance().clone();
    }

    @Override
    String genoTypeJson() {
        StringBuilder sb = new StringBuilder();

        for(int i = 0; i < genotype.length; i++) {
            sb.append(Genome.quotedString(genotype[i].name()));

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
            sb.append(Genome.quotedString(domain[i].name()));

            if(i < domain.length - 1)
                sb.append(", ");
        }//end for i

        sb.append("]");

        return sb.toString();
    }
}
