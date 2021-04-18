package crobes.genetics.genePools;

import crobes.core.Crobe;
import crobes.core.CrobeConstants;
import crobes.core.CrobeEnums;
import crobes.core.Location;
import crobes.genetics.genomics.Genomics;

import java.util.ArrayList;

public class BaseThermoMetabolism extends Metabolism implements IMetabolicGenePool
{
    static {
        Genomics.metabolisms.registerGenePool(BaseThermoMetabolism.class);
    }//end static

    @Override
    public String displayName() {
        return "Thermosynthetic";
    }

    @Override
    public String getNamePart() {
        return "calorus";
    }

    @Override
    public String description() {
        return "Basic thermophyle which generates energy from ambient heat in the environment.";
    }

    public BaseThermoMetabolism() {}
    public BaseThermoMetabolism(Crobe crobe) {
        super(crobe);
    }

    @Override
    public Genomics.GenePoolRandomizer getRandomizer() {
        Genomics.GenePoolRandomizer result = new Genomics.GenePoolRandomizer();

        Genomics.IntRandomizer vitality = new Genomics.IntRandomizer(CrobeConstants.METABOLISM_GENE_VITALITY);
        vitality.mutationRange.low = 4;
        vitality.mutationRange.high = 5;
        vitality.genotype.low = 20;
        vitality.genotype.high = 40;
        result.add(vitality);

        Genomics.IntRandomizer vitRange = new Genomics.IntRandomizer(CrobeConstants.METABOLISM_GENE_VITALITYRANGE);
        vitRange.mutationTypes = new CrobeEnums.MutationType[] {CrobeEnums.MutationType.ADJACENT};
        vitRange.mutationRange.low = 1;
        vitRange.mutationRange.high = 1;
        vitRange.genotype.low = 9;
        vitRange.genotype.high = 11;
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
        //recharge one for every two thermal units
        //available to the crobe
        Location[] locs = _crobe.inhabits();
        int heat = 0;
        for(Location l: locs) {
            heat += l.thermalLevel();
        }//end for each

        _crobe.recharge(heat / 2);
    }
}
