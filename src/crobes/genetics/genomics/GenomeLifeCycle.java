package crobes.genetics.genomics;

import crobes.core.CrobeConstants;

public class GenomeLifeCycle extends GenomePool
{
    public GenomeLifeCycle() {
        super();

        genes().add(new GenomeGene(CrobeConstants.LIFECYCLE_GENE_SPAN, GenomeInt.class.getName()));
        genes().add(new GenomeGene(CrobeConstants.LIFECYCLE_GENE_SPANRANGE, GenomeInt.class.getName()));
        genes().add(new GenomeGene(CrobeConstants.LIFECYCLE_GENE_MATURITY, GenomeFlt.class.getName()));
        genes().add(new GenomeGene(CrobeConstants.LIFECYCLE_GENE_FINITE, GenomeBool.class.getName()));
    }
}
