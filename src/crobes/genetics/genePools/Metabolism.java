package crobes.genetics.genePools;

import crobes.core.Crobe;
import crobes.core.CrobeConstants;
import crobes.genetics.genes.ScalarGeneFlt;
import crobes.genetics.genes.ScalarGeneInt;

public abstract class Metabolism extends GenePool implements IMetabolicGenePool
{
    public static final String METABOLISM_DISPLAY = "Metabolism";
    public static final String METABOLISM_DESCRIPTION = "Defines how the Crobe maintains and uses energy.";

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


    @Override
    protected void initializeGeneNames() {
        vitality().name(CrobeConstants.METABOLISM_GENE_VITALITY);
        vitalityRange().name(CrobeConstants.METABOLISM_GENE_VITALITYRANGE);
        healRate().name(CrobeConstants.METABOLISM_GENE_HEALRATE);
        stamina().name(CrobeConstants.METABOLISM_GENE_STAMINA);
        staminaRange().name(CrobeConstants.METABOLISM_GENE_STAMINARANGE);
        mortalityRate().name(CrobeConstants.METABOLISM_GENE_MORTALITYRATE);
    }
}
