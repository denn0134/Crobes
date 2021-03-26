package crobes.genetics;

import crobes.core.CrobeConstants;

public class GenomeMotility extends GenomePool
{
    public GenomeMotility() {
        super();

        genes().add(new GenomeGene(CrobeConstants.MOTILITY_GENE_MOTILITYTYPE, GenomeEnum.class.getName()));
        genes().add(new GenomeGene(CrobeConstants.MOTILITY_GENE_MOVETYPE, GenomeEnum.class.getName()));
        genes().add(new GenomeGene(CrobeConstants.MOTILITY_GENE_MOVEBASE, GenomeInt.class.getName()));
        genes().add(new GenomeGene(CrobeConstants.MOTILITY_GENE_MOVERANGE, GenomeInt.class.getName()));
        genes().add(new GenomeGene(CrobeConstants.MOTILITY_GENE_LETHARGY, GenomeFlt.class.getName()));
        genes().add(new GenomeGene(CrobeConstants.MOTILITY_GENE_EFFICIENCY, GenomeFlt.class.getName()));
    }
}
