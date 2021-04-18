package crobes.genetics.genePools;

import crobes.core.Crobe;
import crobes.core.CrobeConstants;
import crobes.genetics.genes.HeritableGeneEnum;
import crobes.genetics.genes.ScalarGeneFlt;
import crobes.genetics.genes.ScalarGeneInt;

public abstract class Motility extends GenePool implements IMotilityGenePool
{
    public static final String MOTILITY_DISPLAY = "Motility";
    public static final String MOTILITY_DESCRIPTION = "Defines how and when the Crobe moves.";

    public Motility() {}
    public Motility(Crobe crobe) {
        super(crobe);
    }

    protected HeritableGeneEnum _motilityType;
    @Override
    public HeritableGeneEnum motilityType() {
        return _motilityType;
    }
    @Override
    public void motilityType(HeritableGeneEnum motilityType) {
        _motilityType = motilityType;
    }

    protected HeritableGeneEnum _moveType;
    @Override
    public HeritableGeneEnum moveType() {
        return _moveType;
    }
    @Override
    public void moveType(HeritableGeneEnum moveType) {
        _moveType = moveType;
    }

    protected ScalarGeneInt _moveBase;
    @Override
    public ScalarGeneInt moveBase() {
        return _moveBase;
    }
    @Override
    public void moveBase(ScalarGeneInt value) {
        _moveBase = value;
    }

    protected ScalarGeneInt _moveRange;
    @Override
    public ScalarGeneInt moveRange() {
        return _moveRange;
    }
    @Override
    public void moveRange(ScalarGeneInt value) {
        _moveRange = value;
    }

    protected ScalarGeneFlt _lethargy;
    @Override
    public ScalarGeneFlt lethargy() {
        return _lethargy;
    }
    @Override
    public void lethargy(ScalarGeneFlt value) {
        _lethargy = value;
    }

    protected ScalarGeneFlt _efficiency;
    @Override
    public ScalarGeneFlt efficiency() {
        return _efficiency;
    }
    @Override
    public void efficiency(ScalarGeneFlt value) {
        _efficiency = value;
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
