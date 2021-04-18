package crobes.genetics.genePools;

import crobes.core.*;
import crobes.genetics.genes.*;
import crobes.genetics.genomics.Genomics;

import java.util.ArrayList;

public class BasePhotoMetabolism extends Metabolism implements IMetabolicGenePool
{
    static {
        Genomics.metabolisms.registerGenePool(BasePhotoMetabolism.class);
    }//end static

    @Override
    public String displayName() {
        return "Photosynthetic";
    }

    @Override
    public String getNamePart() {
        return "photus";
    }

    @Override
    public String description() {
        return "Basic no-frills photosynthetic driven metabolism.";
    }

    private static final String TS_FMT = "    vitality: %1$s vRange: %2$s healRate: %3$s stamina: %4$s sRange: %5$s mortalityRate: %6$s";

    public BasePhotoMetabolism() {}
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
    public Genomics.GenePoolRandomizer getRandomizer() {
        Genomics.GenePoolRandomizer result = new Genomics.GenePoolRandomizer();

        Genomics.IntRandomizer vitality = new Genomics.IntRandomizer(CrobeConstants.METABOLISM_GENE_VITALITY);
        vitality.mutationRange.low = 3;
        vitality.mutationRange.high = 4;
        vitality.genotype.low = 10;
        vitality.genotype.high = 20;
        result.add(vitality);

        Genomics.IntRandomizer vitRange = new Genomics.IntRandomizer(CrobeConstants.METABOLISM_GENE_VITALITYRANGE);
        vitRange.mutationTypes = new CrobeEnums.MutationType[] {CrobeEnums.MutationType.ADJACENT};
        vitRange.mutationRange.low = 1;
        vitRange.mutationRange.high = 1;
        vitRange.genotype.low = 4;
        vitRange.genotype.high = 6;
        result.add(vitRange);

        Genomics.FltRandomizer heal = new Genomics.FltRandomizer(CrobeConstants.METABOLISM_GENE_HEALRATE);
        heal.mutationRange.low = 0.01;
        heal.mutationRange.high = 0.02;
        heal.genotype.low = 0.05;
        heal.genotype.high = 0.15;
        result.add(heal);

        Genomics.IntRandomizer sta = new Genomics.IntRandomizer(CrobeConstants.METABOLISM_GENE_STAMINA);
        sta.mutationRange.low = 2;
        sta.mutationRange.high = 3;
        sta.genotype.low = 10;
        sta.genotype.high = 15;
        result.add(sta);

        Genomics.IntRandomizer stRng = new Genomics.IntRandomizer(CrobeConstants.METABOLISM_GENE_STAMINARANGE);
        stRng.mutationTypes = new CrobeEnums.MutationType[] {CrobeEnums.MutationType.ADJACENT};
        stRng.mutationRange.low = 1;
        stRng.mutationRange.high = 1;
        stRng.genotype.low = 3;
        stRng.genotype.high = 5;
        result.add(stRng);

        Genomics.IntRandomizer mort = new Genomics.IntRandomizer(CrobeConstants.METABOLISM_GENE_MORTALITYRATE);
        mort.mutationRange.low = 50;
        mort.mutationRange.high = 100;
        mort.genotype.low = 900;
        mort.genotype.high = 1100;
        result.add(mort);

        return result;
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
        //recharge 1 energy for each light level
        //currently available to the crobe
        Location[] locs = _crobe.inhabits();
        int light = 0;
        for(Location l : locs) {
            if(l.lightLevel() > light) {
                light = l.lightLevel();
            }//end if
        }//end for l

        _crobe.recharge(light);
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

    @Override
    public String toString() {
        return String.format(TS_FMT, _vitality.toString(),
                _vitalityRange.toString(), _healRate.toString(),
                _stamina.toString(), _staminaRange.toString(),
                _mortalityRate.toString());
    }
}
