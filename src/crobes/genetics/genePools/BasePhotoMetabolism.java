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
    public void processFeeding() {
        //recharge 1 energy for each light level
        //currently available to the crobe
        //use the location with the most intense light
        Location[] locs = _crobe.inhabits();
        int light = 0;
        for(Location l : locs) {
            if(l.elements().lightLevel() > light) {
                light = l.elements().lightLevel();
            }//end if
        }//end for l

        _crobe.recharge(light);
    }
}
