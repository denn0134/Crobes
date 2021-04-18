package crobes.genetics.genePools;

import crobes.core.*;
import crobes.genetics.genes.*;
import crobes.genetics.genomics.Genomics;

import java.util.ArrayList;

public class ImmobileMotilty extends Motility implements IMotilityGenePool
{
    static {
        Genomics.motilities.registerGenePool(ImmobileMotilty.class);
    }//end static

    @Override
    public String displayName() {
        return "Immobility";
    }

    @Override
    public String getNamePart() {
        return "stasus";
    }

    @Override
    public String description() {
        return "Non-mobile motility plan.";
    }

    public ImmobileMotilty() {}
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
    public Genomics.GenePoolRandomizer getRandomizer() {
        Genomics.GenePoolRandomizer result = new Genomics.GenePoolRandomizer();

        Genomics.EnumRandomizer mot = new Genomics.EnumRandomizer(CrobeConstants.MOTILITY_GENE_MOTILITYTYPE);
        mot.domain = new Enum[] {CrobeEnums.MotilityType.NON_MOTILE, CrobeEnums.MotilityType.ANCHORED};
        result.add(mot);

        Genomics.EnumRandomizer mov= new Genomics.EnumRandomizer(CrobeConstants.MOTILITY_GENE_MOVETYPE);
        mov.domainChance = 0.50f;
        result.add(mov);

        Genomics.IntRandomizer movBase = new Genomics.IntRandomizer(CrobeConstants.MOTILITY_GENE_MOVEBASE);
        movBase.mutationRange.low = 1;
        movBase.mutationRange.high = 1;
        movBase.genotype.low = 1;
        movBase.genotype.high = 1;
        result.add(movBase);

        Genomics.IntRandomizer movRng = new Genomics.IntRandomizer(CrobeConstants.MOTILITY_GENE_MOVERANGE);
        movRng.mutationTypes = new CrobeEnums.MutationType[] {CrobeEnums.MutationType.ADJACENT};
        movRng.mutationRange.low = 1;
        movRng.mutationRange.high = 1;
        movRng.genotype.low = 1;
        movRng.genotype.high = 2;
        result.add(movRng);

        Genomics.FltRandomizer leth = new Genomics.FltRandomizer(CrobeConstants.MOTILITY_GENE_LETHARGY);
        leth.mutationRange.low = 0.04;
        leth.mutationRange.high = 0.06;
        leth.genotype.low = 0.20;
        leth.genotype.high = 0.40;
        result.add(leth);

        Genomics.FltRandomizer eff = new Genomics.FltRandomizer(CrobeConstants.MOTILITY_GENE_EFFICIENCY);
        eff.mutationRange.low = 0.01;
        eff.mutationRange.high = 0.03;
        eff.genotype.low = 0.75;
        eff.genotype.high = 1.00;
        result.add(eff);

        return result;
    }
}
