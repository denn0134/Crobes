package crobes.genetics;

import crobes.genetics.genes.ScalarGeneInt;

public class GenomeInt extends GenomeValue
{
    public int[] genotype;
    public int mutationRange;

    public GenomeInt(ScalarGeneInt gene) {
        super(gene.mutationType());

        genotype = gene.genoType().clone();
        mutationRange = gene.mutationRange();
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
        return "" + mutationRange;
    }
}
