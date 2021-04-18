package crobes.genetics.genePools;

import crobes.core.Crobe;
import crobes.core.CrobeConstants;
<<<<<<< HEAD
import crobes.genetics.genes.HeritableGeneBool;
import crobes.genetics.genes.ScalarGeneFlt;
import crobes.genetics.genes.ScalarGeneInt;
=======
import crobes.core.CrobeEnums;
import crobes.genetics.genes.Gene;
import crobes.genetics.genes.HeritableGeneBool;
import crobes.genetics.genes.ScalarGeneFlt;
import crobes.genetics.genes.ScalarGeneInt;

import java.util.ArrayList;
>>>>>>> 4d42c4f... Refactored the gene pools to be more generic

public abstract class LifeCycle extends GenePool implements ILifeCycleGenePool
{
    public static final String LIFECYCLE_DISPLAY = "Life Cycle";
    public static final String LIFECYCLE_DESCRIPTION = "Defines life stages and reproduction of a Crobe.";

    private static final String TS_FMT = "    span: %1$s spanRange: %2$s maturity: %3$s finite: %4$s reproInt: %5$s";

    protected int reproInterval;
    protected int reproMinEnergy;
    protected int reproMinHealth;

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

<<<<<<< HEAD

=======
>>>>>>> 4d42c4f... Refactored the gene pools to be more generic
    @Override
    protected void initializeGeneNames() {
        span().name(CrobeConstants.LIFECYCLE_GENE_SPAN);
        spanRange().name(CrobeConstants.LIFECYCLE_GENE_SPANRANGE);
        maturity().name(CrobeConstants.LIFECYCLE_GENE_MATURITY);
        finite().name(CrobeConstants.LIFECYCLE_GENE_FINITE);
    }

    @Override
    public void initializeGenePool(int[] span, int[] spanRange, float[] maturity) {
        _span = new ScalarGeneInt(span, CrobeEnums.MutationType.SCALAR_DISCREET);
        _spanRange = new ScalarGeneInt(spanRange, CrobeEnums.MutationType.ADJACENT);
        _maturity = new ScalarGeneFlt(maturity);
        reproInterval = 0;
    }

    @Override
    public GenePool recombinateGenePool(Crobe crobe, ArrayList<GenePool> genePools) {
        SimpleLifeCycle pool = new SimpleLifeCycle(crobe);
        ArrayList<Gene> sGenes = new ArrayList<Gene>();
        ArrayList<Gene> srGenes = new ArrayList<Gene>();
        ArrayList<Gene> mGenes = new ArrayList<Gene>();
        ArrayList<Gene> fGenes = new ArrayList<Gene>();

        for(GenePool gp : genePools) {
            SimpleLifeCycle slc = (SimpleLifeCycle) gp;
            sGenes.add(slc._span);
            srGenes.add(slc._spanRange);
            mGenes.add(slc._maturity);
            fGenes.add(slc._finite);
        }//end for gp

        pool._span = (ScalarGeneInt) this._span.recombinate(sGenes);
        pool._spanRange = (ScalarGeneInt) this._spanRange.recombinate(srGenes);
        pool._maturity = (ScalarGeneFlt) this._maturity.recombinate(mGenes);
        pool._finite = (HeritableGeneBool) this._finite.recombinate(fGenes);
        pool.reproInterval = 0;

        pool.initializeGeneNames();

        return pool;
    }

    @Override
    public void mutate(int stressLevel) {
        _span.mutate(stressLevel);
        _spanRange.mutate(stressLevel);
        _maturity.mutate(stressLevel);
        _finite.mutate(stressLevel);
    }

    @Override
    public String toString() {
        return String.format(TS_FMT,
                _span.toString(), _spanRange.toString(),
                _maturity.toString(), _finite.toString(),
                reproInterval);
    }
}
