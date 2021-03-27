package crobes.genetics.genePools;

import crobes.core.Crobe;
import crobes.core.CrobeConstants;

public abstract class LifeCycle extends GenePool implements ILifeCycleGenePool
{
    public static final String LIFECYCLE_DISPLAY = "Life Cycle";
    public static final String LIFECYCLE_DESCRIPTION = "Defines life stages and reproduction of a Crobe.";

    public LifeCycle() {}
    public LifeCycle(Crobe crobe) {
        super(crobe);
    }

    @Override
    protected void initializeGeneNames() {
        span().name(CrobeConstants.LIFECYCLE_GENE_SPAN);
        spanRange().name(CrobeConstants.LIFECYCLE_GENE_SPANRANGE);
        maturity().name(CrobeConstants.LIFECYCLE_GENE_MATURITY);
        finite().name(CrobeConstants.LIFECYCLE_GENE_FINITE);
    }
}
