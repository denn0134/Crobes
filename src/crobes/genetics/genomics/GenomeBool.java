package crobes.genetics.genomics;

import crobes.genetics.genes.HeritableGeneBool;

public class GenomeBool extends GenomeValue
{
    public boolean[] genotype;
    public boolean[] domain;

    public GenomeBool(HeritableGeneBool gene) {
        super(gene.mutationType());

        genotype = gene.genoType().clone();
        domain = gene.dominance().clone();
    }

    @Override
    String genoTypeJson() {
        StringBuilder sb = new StringBuilder();

        for(int i = 0; i < genotype.length; i++) {
            sb.append(genotype[i]);

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
            sb.append(domain[i]);

            if(i < domain.length - 1)
                sb.append(", ");
        }//end for i

        sb.append("]");

        return sb.toString();
    }
}
