package crobes.genetics.genePools;

import crobes.core.Crobe;
import crobes.core.CrobeConstants;

public abstract class Metabolism extends GenePool implements IMetabolicGenePool
{
    public static final String METABOLISM_DISPLAY = "Metabolism";
    public static final String METABOLISM_DESCRIPTION = "Defines how the Crobe maintains and uses energy.";

    public Metabolism() {}
    public Metabolism(Crobe crobe) {
        super(crobe);
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
