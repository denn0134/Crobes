import java.util.ArrayList;

public class BasePhotoMetabolism extends GenePool implements IMetabolicGenePool
{
    private static final String TS_FMT = "    vitality: %1$s vRange: %2$s healRate: %3$s stamina: %4$s sRange: %5$s mortalityRate: %6$s";

    public BasePhotoMetabolism(Crobe crobe) {
        super(crobe);
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
    public void initializeGenePool(int[] vitality, int[] vitalityRange, float[] healRate, int[] stamina, int[] staminaRange, int[] mortalityRate) {
        _vitality = new ScalarGeneInt(vitality);
        _vitalityRange = new ScalarGeneInt(vitalityRange);
        _healRate = new ScalarGeneFlt(healRate);
        _stamina = new ScalarGeneInt(stamina);
        _staminaRange = new ScalarGeneInt(staminaRange);
        _mortalityRate = new ScalarGeneInt(mortalityRate);
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
