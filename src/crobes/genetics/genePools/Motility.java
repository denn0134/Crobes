package crobes.genetics.genePools;

import crobes.core.Crobe;
import crobes.core.CrobeConstants;
import crobes.genetics.GenePool;

public abstract class Motility extends GenePool implements IMotilityGenePool
{

    public Motility(Crobe crobe) {
        super(crobe);
    }

    @Override
    protected void initializeGeneNames() {
        motilityType().name(CrobeConstants.MOTILITY_GENE_MOTILITYTYPE);
        moveType().name(CrobeConstants.MOTILITY_GENE_MOVETYPE);
        moveBase().name(CrobeConstants.MOTILITY_GENE_MOVEBASE);
        moveRange().name(CrobeConstants.MOTILITY_GENE_MOVERANGE);
        lethargy().name(CrobeConstants.MOTILITY_GENE_LETHARGY);
        efficiency().name(CrobeConstants.MOTILITY_GENE_EFFICIENCY);
    }
}
