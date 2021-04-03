package crobes.genetics.genomics;

import crobes.core.CrobeConstants;

public class GenomeMotility extends GenomePool
{
    public GenomeMotility() {
        super();

        genes().add(new GenomeGene(CrobeConstants.MOTILITY_GENE_MOTILITYTYPE, GenomeEnum.class.getSimpleName()));
        genes().add(new GenomeGene(CrobeConstants.MOTILITY_GENE_MOVETYPE, GenomeEnum.class.getSimpleName()));
        genes().add(new GenomeGene(CrobeConstants.MOTILITY_GENE_MOVEBASE, GenomeInt.class.getSimpleName()));
        genes().add(new GenomeGene(CrobeConstants.MOTILITY_GENE_MOVERANGE, GenomeInt.class.getSimpleName()));
        genes().add(new GenomeGene(CrobeConstants.MOTILITY_GENE_LETHARGY, GenomeFlt.class.getSimpleName()));
        genes().add(new GenomeGene(CrobeConstants.MOTILITY_GENE_EFFICIENCY, GenomeFlt.class.getSimpleName()));
    }
}
