package crobes.genetics.genePools;

import crobes.core.*;
import crobes.genetics.genes.*;
import crobes.genetics.genomics.Genomics;

import java.util.ArrayList;
import java.util.Arrays;

public class SimpleLifeCycle extends LifeCycle implements ILifeCycleGenePool
{
    static {
        Genomics.lifeCycles.registerGenePool(SimpleLifeCycle.class);
    }//end static

    @Override
    public String displayName() {
        return "Simple Budding Cycle";
    }

    @Override
    public String getNamePart() {
        return "simplys";
    }

    @Override
    public String description() {
        return "Basic child to adult lifecycle.";
    }

    private static final int SLCGP_ELDER_STRESS = 5;
    private static final int SLCGP_ELDER_DAMAGE = 20;

    //reproduction constants
    private static final double RPD_ENERGY_PCT  = 0.60f;
    private static final double RPD_MIN_HEALTH = 0.90f;
    private static final double RPD_CHANCE = 0.50f;
    private static final int RPD_INTERVAL = 5;

    public SimpleLifeCycle() {}
    public SimpleLifeCycle(Crobe crobe) {
        super(crobe);
    }
    public SimpleLifeCycle(Crobe crobe,
                           ScalarGeneInt span,
                           ScalarGeneInt spanRange,
                           ScalarGeneFlt maturity) {
        super(crobe);
        _span = span;
        _spanRange = spanRange;
        _maturity = maturity;
    }

<<<<<<< HEAD
    @Override
    public void initializeGenePool(int[] span, int[] spanRange, float[] maturity) {
        _span = new ScalarGeneInt(span, CrobeEnums.MutationType.SCALAR_DISCREET);
        _spanRange = new ScalarGeneInt(spanRange, CrobeEnums.MutationType.ADJACENT);
        _maturity = new ScalarGeneFlt(maturity);
        reproInterval = 0;
    }

=======
>>>>>>> 4d42c4f... Refactored the gene pools to be more generic
    @Override
    public void initializeReproduction() {
        reproMinEnergy = (int) Math.round(_crobe.energy() * RPD_ENERGY_PCT);
        reproMinHealth = (int) Math.round(_crobe.health() * RPD_MIN_HEALTH);
    }

    @Override
    public void processAging() {
        switch (_crobe.stage()) {
            case CHILD:
                //check if the crobe has matured
                if(_crobe.age() >= _crobe.maturityAge())
                    _crobe.stage(CrobeEnums.LifeStage.ADULT);
                break;
            case ADULT:
                //check if the crobe reached it lifespan
                if(_crobe.age() > _crobe.lifeSpan()) {
                    if(_finite.phenotype()) {
                        processDeath(_crobe.stage());
                        _crobe.stage(CrobeEnums.LifeStage.DEAD);
                    }//end if
                    else {
                        _crobe.stage(CrobeEnums.LifeStage.ELDER);
                    }//end else
                }//end if
                break;
            case ELDER:
                //check if the crobe deteriorates - either stressing
                //or taking health damage
                int check = _crobe.rand.nextInt(100);
                if(check < SLCGP_ELDER_STRESS)
                    _crobe.stressed(check);
                else if(check < SLCGP_ELDER_DAMAGE)
                    _crobe.wound(1);
        }//end switch
    }
    @Override
    public void processReproduction(ArrayList<Crobe> children) {
        //only adults reproduce
        if(_crobe.stage() != CrobeEnums.LifeStage.ADULT)
            return;

        if(reproInterval > 0)
            reproInterval--;

        //check for minimum energy and health requirements
        if((_crobe.energy() <= reproMinEnergy) ||
                (_crobe.health() < reproMinHealth)) {
            return;
        }//end if

        //check the reproduction interval
        if(reproInterval > 0)
            return;

        //there is a chance to reproduce
        int chance = (int)Math.round(RPD_CHANCE * 100);
        int check = _crobe.rand.nextInt(100);
        if(check < chance) {
            //spawn a new crobe
            String newName = _crobe.designation() + "-" + _crobe.getSpawnName();
            Crobe child = new Crobe(newName);
            child.world(_crobe.world());
            child.lifeCycle((ILifeCycleGenePool) _crobe.lifeCycle().recombinateGenePool(child, new ArrayList<GenePool>()));
            child.metabolism((IMetabolicGenePool) _crobe.metabolism().recombinateGenePool(child, new ArrayList<GenePool>()));
            child.motility((IMotilityGenePool) _crobe.motility().recombinateGenePool(child, new ArrayList<GenePool>()));
            child.renderer((IRenderGenePool) _crobe.renderer().recombinateGenePool(child, new ArrayList<GenePool>()));

            child.mutate();
            child.init();

            //determine an adjacent position which
            //is open to be the childs position
            ArrayList<Location> locs = new ArrayList<Location>(Arrays.asList(_crobe.adjacent()));
            for(int i = locs.size() - 1; i > -1; i--) {
                if(locs.get(i).blocking()) {
                    locs.remove(i);
                    continue;
                }//end if
            }//end for i

            if(locs.size() > 0) {
                int rnd = _crobe.rand.nextInt(locs.size());
                child.position(locs.get(rnd).point());

                children.add(child);
                reproInterval = 5;
                _crobe.exert(reproMinEnergy);
            }//end if
        }//end if
    }

    @Override
    public Genomics.GenePoolRandomizer getRandomizer() {
        Genomics.GenePoolRandomizer result = new Genomics.GenePoolRandomizer();

        Genomics.IntRandomizer span = new Genomics.IntRandomizer(CrobeConstants.LIFECYCLE_GENE_SPAN);
        span.mutationRange.low = 4;
        span.mutationRange.high = 6;
        span.genotype.low = 15;
        span.genotype.high = 30;
        result.add(span);

        Genomics.IntRandomizer spanRange = new Genomics.IntRandomizer(CrobeConstants.LIFECYCLE_GENE_SPANRANGE);
        spanRange.mutationTypes = new CrobeEnums.MutationType[] {CrobeEnums.MutationType.ADJACENT};
        spanRange.mutationRange.low = 1;
        spanRange.mutationRange.high = 1;
        spanRange.genotype.low = 4;
        spanRange.genotype.high = 6;
        result.add(spanRange);

        Genomics.FltRandomizer maturity = new Genomics.FltRandomizer(CrobeConstants.LIFECYCLE_GENE_MATURITY);
        maturity.mutationRange.low = 0.04;
        maturity.mutationRange.high = 0.06;
        maturity.genotype.low = 0.20;
        maturity.genotype.high = 0.30;
        result.add(maturity);

        Genomics.BoolRandomizer finite = new Genomics.BoolRandomizer(CrobeConstants.LIFECYCLE_GENE_FINITE);
        result.add(finite);

        return result;
    }
}
