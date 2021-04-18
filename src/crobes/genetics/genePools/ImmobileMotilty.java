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

    private static final String TS_FMT = "    motilityType: %1$s moveType: %2$s moveBase: %3$s moveRange: %4$s lethargy: %5$s efficiency: %6$s";

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

        pool.initializeGeneNames();

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
