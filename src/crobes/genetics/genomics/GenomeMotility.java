package crobes.genetics.genomics;

import crobes.core.CrobeConstants;
import crobes.core.CrobeEnums;

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

        GenomeEnum ge;
        ge = (GenomeEnum) getGene(CrobeConstants.MOTILITY_GENE_MOTILITYTYPE).geneValue;
        ge.enumClass(CrobeEnums.MotilityType.class);
        ge = (GenomeEnum) getGene(CrobeConstants.MOTILITY_GENE_MOVETYPE).geneValue;
        ge.enumClass(CrobeEnums.MovementType.class);
    }
}
