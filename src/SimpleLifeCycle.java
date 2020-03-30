import java.util.ArrayList;

public class SimpleLifeCycle extends GenePool implements ILifeCycleGenePool
{
    private static final String TS_FMT = "    span: %1$s spanRange: %2$s maturity: %3$s finite: %4$s reproInt: %5$s";
    private static final int SLCGP_ELDER_STRESS = 5;
    private static final int SLCGP_ELDER_DAMAGE = 20;

    //reproduction constants
    private static final double RPD_ENERGY_PCT  = 0.60f;
    private static final double RPD_MIN_HEALTH = 0.90f;
    private static final double RPD_CHANCE = 0.50f;
    private static final int RPD_INTERVAL = 5;

    private int reproInterval;
    private int reproMinEnergy;
    private int reproMinHealth;

    public SimpleLifeCycle(Crobe crobe) {
        super(crobe);
    }

    @Override
    public String getNamePart() {
        return "simplys";
    }

    private ScalarGeneInt _span;
    @Override
    public ScalarGeneInt span() {
        return _span;
    }

    private ScalarGeneInt _spanRange;
    @Override
    public ScalarGeneInt spanRange() {
        return _spanRange;
    }

    private ScalarGeneFlt _maturity;
    @Override
    public ScalarGeneFlt maturity() {
        return _maturity;
    }

    private HeritableGeneBool _finite;
    @Override
    public HeritableGeneBool finite() {
        return _finite;
    }
    @Override
    public void finite(HeritableGeneBool value) {
        _finite = value;
    }

    @Override
    public void initializeGenePool(int[] span, int[] spanRange, float[] maturity) {
        _span = new ScalarGeneInt(span);
        _spanRange = new ScalarGeneInt(spanRange);
        _maturity = new ScalarGeneFlt(maturity);
        reproInterval = 0;
    }

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
                        System.out.println("Crobe [" + _crobe.designation() + "] died of old age");
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
            child.lifeCycle((ILifeCycleGenePool) _crobe.lifeCycle().recombinateGenePool(child, new ArrayList<GenePool>()));
            child.metabolism((IMetabolicGenePool) _crobe.metabolism().recombinateGenePool(child, new ArrayList<GenePool>()));

            child.mutate();
            child.init();

            children.add(child);

            System.out.println("Crobe[" + _crobe.designation() + "] spawned [" + newName + "]");

            reproInterval = 5;
            _crobe.exert(reproMinEnergy);
        }//end if
    }

    @Override
    public GenePool recombinateGenePool(Crobe crobe, ArrayList<GenePool> genePools) {
        SimpleLifeCycle pool = new SimpleLifeCycle(crobe);
        ArrayList<Gene> sGenes = new ArrayList<Gene>();
        ArrayList<Gene> srGenes = new ArrayList<Gene>();
        ArrayList<Gene> mGenes = new ArrayList<Gene>();
        ArrayList<Gene> fGenes = new ArrayList<Gene>();

        for(GenePool gp : genePools) {
            SimpleLifeCycle slc = (SimpleLifeCycle) gp;
            sGenes.add(slc._span);
            srGenes.add(slc._spanRange);
            mGenes.add(slc._maturity);
            fGenes.add(slc._finite);
        }//end for gp

        pool._span = (ScalarGeneInt) this._span.recombinate(sGenes);
        pool._spanRange = (ScalarGeneInt) this._spanRange.recombinate(srGenes);
        pool._maturity = (ScalarGeneFlt) this._maturity.recombinate(mGenes);
        pool._finite = (HeritableGeneBool) this._finite.recombinate(fGenes);
        pool.reproInterval = 0;

        return pool;
    }

    @Override
    public void mutate(int stressLevel) {
        _span.mutate(stressLevel);
        _spanRange.mutate(stressLevel);
        _maturity.mutate(stressLevel);
        _finite.mutate(stressLevel);
    }

    @Override
    public String toString() {
        return String.format(TS_FMT,
                _span.toString(), _spanRange.toString(),
                _maturity.toString(), _finite.toString(),
                reproInterval);
    }
}
