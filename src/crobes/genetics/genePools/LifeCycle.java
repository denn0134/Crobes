package crobes.genetics.genePools;

import crobes.core.Crobe;
import crobes.core.CrobeConstants;
import crobes.genetics.genes.HeritableGeneBool;
import crobes.genetics.genes.ScalarGeneFlt;
import crobes.genetics.genes.ScalarGeneInt;

public abstract class LifeCycle extends GenePool implements ILifeCycleGenePool
{
    public static final String LIFECYCLE_DISPLAY = "Life Cycle";
    public static final String LIFECYCLE_DESCRIPTION = "Defines life stages and reproduction of a Crobe.";

    public LifeCycle() {}
    public LifeCycle(Crobe crobe) {
        super(crobe);
    }

    protected ScalarGeneInt _span;
    @Override
    public ScalarGeneInt span() {
        return _span;
    }
    @Override
    public void span(ScalarGeneInt value) {
        _span = value;
    }

    protected ScalarGeneInt _spanRange;
    @Override
    public ScalarGeneInt spanRange() {
        return _spanRange;
    }
    @Override
    public void spanRange(ScalarGeneInt value) {
        _spanRange = value;
    }

    protected ScalarGeneFlt _maturity;
    @Override
    public ScalarGeneFlt maturity() {
        return _maturity;
    }
    @Override
    public void maturity(ScalarGeneFlt value) {
        _maturity = value;
    }

    protected HeritableGeneBool _finite;
    @Override
    public HeritableGeneBool finite() {
        return _finite;
    }
    @Override
    public void finite(HeritableGeneBool value) {
        _finite = value;
    }


    @Override
    protected void initializeGeneNames() {
        span().name(CrobeConstants.LIFECYCLE_GENE_SPAN);
        spanRange().name(CrobeConstants.LIFECYCLE_GENE_SPANRANGE);
        maturity().name(CrobeConstants.LIFECYCLE_GENE_MATURITY);
        finite().name(CrobeConstants.LIFECYCLE_GENE_FINITE);
    }
}
