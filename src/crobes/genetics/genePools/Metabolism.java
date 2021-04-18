package crobes.genetics.genePools;

import crobes.core.Crobe;
import crobes.core.CrobeConstants;
<<<<<<< HEAD
import crobes.genetics.genes.ScalarGeneFlt;
import crobes.genetics.genes.ScalarGeneInt;
=======
import crobes.core.CrobeEnums;
import crobes.genetics.genes.Gene;
import crobes.genetics.genes.ScalarGeneFlt;
import crobes.genetics.genes.ScalarGeneInt;

import java.util.ArrayList;
>>>>>>> 4d42c4f... Refactored the gene pools to be more generic

public abstract class Metabolism extends GenePool implements IMetabolicGenePool
{
    public static final String METABOLISM_DISPLAY = "Metabolism";
    public static final String METABOLISM_DESCRIPTION = "Defines how the Crobe maintains and uses energy.";

    protected static final String TS_FMT = "    vitality: %1$s vRange: %2$s healRate: %3$s stamina: %4$s sRange: %5$s mortalityRate: %6$s";

    public Metabolism() {}
    public Metabolism(Crobe crobe) {
        super(crobe);
    }

    protected ScalarGeneInt _vitality;
    @Override
    public ScalarGeneInt vitality() {
        return _vitality;
    }
    @Override
    public void vitality(ScalarGeneInt value) {
        _vitality = value;
    }

    protected ScalarGeneInt _vitalityRange;
    @Override
    public ScalarGeneInt vitalityRange() {
        return _vitalityRange;
    }
    @Override
    public void vitalityRange(ScalarGeneInt value) {
        _vitalityRange = value;
    }

    protected ScalarGeneFlt _healRate;
    @Override
    public ScalarGeneFlt healRate() {
        return _healRate;
    }
    @Override
    public void healRate(ScalarGeneFlt value) {
        _healRate = value;
    }

    protected ScalarGeneInt _stamina;
    @Override
    public ScalarGeneInt stamina() {
        return _stamina;
    }
    @Override
    public void stamina(ScalarGeneInt value) {
        _stamina = value;
    }

    protected ScalarGeneInt _staminaRange;
    @Override
    public ScalarGeneInt staminaRange() {
        return _staminaRange;
    }
    @Override
    public void staminaRange(ScalarGeneInt value) {
        _staminaRange = value;
    }

    protected ScalarGeneInt _mortalityRate;
    @Override
    public ScalarGeneInt mortalityRate() {
        return _mortalityRate;
    }
    @Override
    public void mortalityRate(ScalarGeneInt value) {
        _mortalityRate = value;
    }

<<<<<<< HEAD
=======
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

        pool.initializeGeneNames();

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
>>>>>>> 4d42c4f... Refactored the gene pools to be more generic

    @Override
    protected void initializeGeneNames() {
        vitality().name(CrobeConstants.METABOLISM_GENE_VITALITY);
        vitalityRange().name(CrobeConstants.METABOLISM_GENE_VITALITYRANGE);
        healRate().name(CrobeConstants.METABOLISM_GENE_HEALRATE);
        stamina().name(CrobeConstants.METABOLISM_GENE_STAMINA);
        staminaRange().name(CrobeConstants.METABOLISM_GENE_STAMINARANGE);
        mortalityRate().name(CrobeConstants.METABOLISM_GENE_MORTALITYRATE);
    }

    @Override
    public String toString() {
        return String.format(TS_FMT, _vitality.toString(),
                _vitalityRange.toString(), _healRate.toString(),
                _stamina.toString(), _staminaRange.toString(),
                _mortalityRate.toString());
    }
}
