import java.util.ArrayList;

public class ImmobileMotilty extends GenePool implements IMotilityGenePool
{
    private static final String TS_FMT = "    motilityType: %1$s moveType: %2$s moveBase: %3$s moveRange: %4$s lethargy: %5$s efficiency: %6$s";

    public ImmobileMotilty(Crobe crobe) {
        super(crobe);
    }
    public ImmobileMotilty(Crobe crobe,
                           ScalarGeneInt moveBase,
                           ScalarGeneInt moveRange,
                           ScalarGeneFlt lethargy,
                           ScalarGeneFlt efficiency) {
        super(crobe);
        _moveBase = moveBase;
        _moveRange = moveRange;
        _lethargy = lethargy;
        _efficiency = efficiency;
    }

    @Override
    public String getNamePart() {
        return "stasus";
    }

    private HeritableGeneEnum _motilityType;
    @Override
    public HeritableGeneEnum motilityType() {
        return _motilityType;
    }
    @Override
    public void motilityType(HeritableGeneEnum motilityType) {
        _motilityType = motilityType;
    }

    private HeritableGeneEnum _moveType;
    @Override
    public HeritableGeneEnum moveType() {
        return _moveType;
    }
    @Override
    public void moveType(HeritableGeneEnum moveType) {
        _moveType = moveType;
    }

    private ScalarGeneInt _moveBase;
    @Override
    public ScalarGeneInt moveBase() {
        return _moveBase;
    }

    private ScalarGeneInt _moveRange;
    @Override
    public ScalarGeneInt moveRange() {
        return _moveRange;
    }

    private ScalarGeneFlt _lethargy;
    @Override
    public ScalarGeneFlt lethargy() {
        return _lethargy;
    }

    private ScalarGeneFlt _efficiency;
    @Override
    public ScalarGeneFlt efficiency() {
        return _efficiency;
    }

    @Override
    public void initializeRandomDefault() {
        if((_motilityType != null) ||
                (_moveType != null) ||
                (_moveBase != null) ||
                (_moveRange != null) ||
                (_lethargy != null) ||
                (_efficiency != null)) {
            return;
        }//end if

        //motilityType NON_MOTILE
        _motilityType = new HeritableGeneEnum(
                new Enum[] {CrobeEnums.MotilityType.NON_MOTILE, CrobeEnums.MotilityType.NON_MOTILE},
                new Enum[] {CrobeEnums.MotilityType.ANCHORED, CrobeEnums.MotilityType.NON_MOTILE, CrobeEnums.MotilityType.MOTILE},
                CrobeEnums.MutationType.RANDOM);

        //moveType CREEPER
        _moveType = new HeritableGeneEnum(
                new Enum[] {CrobeEnums.MovementType.CREEPER, CrobeEnums.MovementType.CREEPER},
                new Enum[] {CrobeEnums.MovementType.CREEPER, CrobeEnums.MovementType.HOPPER, CrobeEnums.MovementType.ZIGGER},
                CrobeEnums.MutationType.ADJACENT);

        //moveBase 1-2
        int int1 = getIntRange(1, 2);
        int int2 = getIntRange(1, 2);
        _moveBase = new ScalarGeneInt(new int[] {int1, int2},
                CrobeEnums.MutationType.ADJACENT);

        //moveRange 0-1
        int1 = getIntRange(0, 1);
        int2 = getIntRange(0, 1);
        _moveRange = new ScalarGeneInt(new int[] {int1, int2},
                CrobeEnums.MutationType.ADJACENT);

        //lethargy 20% - 40%
        float flt1 = getFloatRange(0.20f, 0.40f);
        float flt2 = getFloatRange(0.20f, 0.40f);
        _lethargy = new ScalarGeneFlt(new float[] {flt1, flt2},
                0.05f);

        //efficiency 0.75 - 1.00
        flt1 = getFloatRange(0.75f, 1.00f);
        flt2 = getFloatRange(0.75f, 1.00f);
        _efficiency = new ScalarGeneFlt(new float[] {flt1, flt2},
                0.02f);
    }

    @Override
    public void initializeGenePool(int[] moveBase, int[] moveRange, float[] lethargy, float[] efficiency) {
        _moveBase = new ScalarGeneInt(moveBase, CrobeEnums.MutationType.SCALAR_DISCREET);
        _moveRange = new ScalarGeneInt(moveRange, CrobeEnums.MutationType.ADJACENT);
        _lethargy = new ScalarGeneFlt(lethargy);
        _efficiency = new ScalarGeneFlt(efficiency);
    }

    @Override
    public GenePool recombinateGenePool(Crobe crobe, ArrayList<GenePool> genePools) {
        ImmobileMotilty pool = new ImmobileMotilty(crobe);
        ArrayList<Gene> mttGenes = new ArrayList<Gene>();
        ArrayList<Gene> mvtGenes = new ArrayList<Gene>();
        ArrayList<Gene> mbGenes = new ArrayList<Gene>();
        ArrayList<Gene> mrGenes = new ArrayList<Gene>();
        ArrayList<Gene> lGenes = new ArrayList<Gene>();
        ArrayList<Gene> eGenes = new ArrayList<Gene>();

        for(GenePool gp : genePools) {
            ImmobileMotilty imm = (ImmobileMotilty)gp;
            mttGenes.add(imm._motilityType);
            mvtGenes.add(imm._moveType);
            mbGenes.add(imm._moveBase);
            mrGenes.add(imm._moveRange);
            lGenes.add(imm._lethargy);
            eGenes.add(imm._efficiency);
        }//end for gp

        pool._motilityType = (HeritableGeneEnum) this._motilityType.recombinate(mttGenes);
        pool._moveType = (HeritableGeneEnum) this._moveType.recombinate(mvtGenes);
        pool._moveBase = (ScalarGeneInt) this._moveBase.recombinate(mbGenes);
        pool._moveRange = (ScalarGeneInt) this._moveRange.recombinate(mrGenes);
        pool._lethargy = (ScalarGeneFlt) this._lethargy.recombinate(lGenes);
        pool._efficiency = (ScalarGeneFlt) this._efficiency.recombinate(eGenes);

        return pool;
    }

    @Override
    public void mutate(int stressLevel) {
        _motilityType.mutate(stressLevel);
        _moveType.mutate(stressLevel);
        _moveBase.mutate(stressLevel);
        _moveRange.mutate(stressLevel);
        _lethargy.mutate(stressLevel);
        _efficiency.mutate(stressLevel);
    }

    @Override
    public String toString() {
        return String.format(TS_FMT,
                _motilityType.toString(),
                _moveType.toString(),
                _moveBase.toString(),
                _moveRange.toString(),
                _lethargy.toString(),
                _efficiency.toString());
    }
}
