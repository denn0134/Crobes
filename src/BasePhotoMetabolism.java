import java.util.ArrayList;

public class BasePhotoMetabolism extends GenePool implements IMetabolicGenePool
{
    private static final String TS_FMT = "    vitality: %1$s vRange: %2$s healRate: %3$s stamina: %4$s sRange: %5$s mortalityRate: %6$s";

    public BasePhotoMetabolism(Crobe crobe) {
        super(crobe);
    }
    public BasePhotoMetabolism(Crobe crobe,
                               ScalarGeneInt vitality,
                               ScalarGeneInt vitalityRange,
                               ScalarGeneFlt healRate,
                               ScalarGeneInt stamina,
                               ScalarGeneInt staminaRange,
                               ScalarGeneInt mortalityRate) {
        super(crobe);
        _vitality = vitality;
        _vitalityRange = vitalityRange;
        _healRate = healRate;
        _stamina = stamina;
        _staminaRange = staminaRange;
        _mortalityRate = mortalityRate;
    }

    @Override
    public String getNamePart() {
        return "photus";
    }

    private ScalarGeneInt _vitality;
    @Override
    public ScalarGeneInt vitality() {
        return _vitality;
    }

    private ScalarGeneInt _vitalityRange;
    @Override
    public ScalarGeneInt vitalityRange() {
        return _vitalityRange;
    }

    private ScalarGeneFlt _healRate;
    @Override
    public ScalarGeneFlt healRate() {
        return _healRate;
    }

    private ScalarGeneInt _stamina;
    @Override
    public ScalarGeneInt stamina() {
        return _stamina;
    }

    private ScalarGeneInt _staminaRange;
    @Override
    public ScalarGeneInt staminaRange() {
        return _staminaRange;
    }

    private ScalarGeneInt _mortalityRate;
    @Override
    public ScalarGeneInt mortalityRate() {
        return _mortalityRate;
    }

    @Override
    public void initializeRandomDefault() {
        if ((_vitality != null) ||
                (_vitalityRange != null) ||
                (_healRate != null) ||
                (_stamina != null) ||
                (_staminaRange != null) ||
                (_mortalityRate != null)) {
            return;
        }

        //vitality  10-20
        int int1 = getIntRange(10, 20);
        int int2 = getIntRange(10, 20);
        _vitality = new ScalarGeneInt(new int[] {int1, int2},
                CrobeEnums.MutationType.SCALAR_DISCREET,
                4);

        //vitalityRange 5
        _vitalityRange = new ScalarGeneInt(new int[] {5, 5},
                CrobeEnums.MutationType.ADJACENT);

        //healRate 5% - 15%
        float flt1 = getFloatRange(0.05f, 0.15f);
        float flt2 = getFloatRange(0.05f, 0.15f);
        _healRate = new ScalarGeneFlt(new float[] {flt1, flt2},
                0.01f);

        //stamina 10-15
        int1 = getIntRange(10, 15);
        int2 = getIntRange(10, 15);
        _stamina = new ScalarGeneInt(new int[] {int1, int2},
                CrobeEnums.MutationType.SCALAR_DISCREET,
                3);

        //staminaRange 4
        _staminaRange = new ScalarGeneInt(new int[] {4, 4},
                CrobeEnums.MutationType.ADJACENT);

        //mortalityRate 1000
        _mortalityRate = new ScalarGeneInt(new int[] {1000, 1000},
                CrobeEnums.MutationType.SCALAR_DISCREET,
                100);
    }

    @Override
    public void initializeGenePool(int[] vitality, int[] vitalityRange, float[] healRate, int[] stamina, int[] staminaRange, int[] mortalityRate) {
        _vitality = new ScalarGeneInt(vitality, CrobeEnums.MutationType.SCALAR_DISCREET);
        _vitalityRange = new ScalarGeneInt(vitalityRange, CrobeEnums.MutationType.ADJACENT);
        _healRate = new ScalarGeneFlt(healRate);
        _stamina = new ScalarGeneInt(stamina, CrobeEnums.MutationType.SCALAR_DISCREET);
        _staminaRange = new ScalarGeneInt(staminaRange, CrobeEnums.MutationType.ADJACENT);
        _mortalityRate = new ScalarGeneInt(mortalityRate, CrobeEnums.MutationType.SCALAR_DISCREET);
    }

    @Override
    public void processFeeding() {
        //for now simply assume 1 light is available
        //recharge 1 energy for each light available
        _crobe.recharge(1);
    }

    @Override
    public GenePool recombinateGenePool(Crobe crobe, ArrayList<GenePool> genePools) {
        BasePhotoMetabolism pool = new BasePhotoMetabolism(crobe);
        ArrayList<Gene> vGenes = new ArrayList<Gene>();
        ArrayList<Gene> vrGenes = new ArrayList<Gene>();
        ArrayList<Gene> hrGenes = new ArrayList<Gene>();
        ArrayList<Gene> sGenes = new ArrayList<Gene>();
        ArrayList<Gene> srGenes = new ArrayList<Gene>();
        ArrayList<Gene> mrGenes = new ArrayList<Gene>();

        for(GenePool gp : genePools) {
            BasePhotoMetabolism bpm = (BasePhotoMetabolism) gp;
            vGenes.add(bpm._vitality);
            vrGenes.add(bpm._vitalityRange);
            hrGenes.add(bpm._healRate);
            sGenes.add(bpm._stamina);
            srGenes.add(bpm._staminaRange);
            mrGenes.add(bpm._mortalityRate);
        }//end for gp

        pool._vitality = (ScalarGeneInt) this._vitality.recombinate(vGenes);
        pool._vitalityRange = (ScalarGeneInt) this._vitalityRange.recombinate(vrGenes);
        pool._healRate = (ScalarGeneFlt) this._healRate.recombinate(hrGenes);
        pool._stamina = (ScalarGeneInt) this._stamina.recombinate(sGenes);
        pool._staminaRange = (ScalarGeneInt) this._staminaRange.recombinate(srGenes);
        pool._mortalityRate = (ScalarGeneInt) this._mortalityRate.recombinate(mrGenes);

        return pool;
    }

    @Override
    public void mutate(int stressLevel) {
        _vitality.mutate(stressLevel);
        _vitalityRange.mutate(stressLevel);
        _healRate.mutate(stressLevel);
        _stamina.mutate(stressLevel);
        _staminaRange.mutate(stressLevel);
    }

    @Override
    public String toString() {
        return String.format(TS_FMT, _vitality.toString(),
                _vitalityRange.toString(), _healRate.toString(),
                _stamina.toString(), _staminaRange.toString(),
                _mortalityRate.toString());
    }
}
