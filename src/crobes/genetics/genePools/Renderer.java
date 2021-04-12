package crobes.genetics.genePools;

import crobes.core.Crobe;
import crobes.core.CrobeConstants;

public abstract class Renderer extends GenePool implements IRenderGenePool
{
    public static final String RENDERER_DISPLAY = "Renderer";
    public static final String RENDERER_DESCRIPTION = "Defines the appearance of a Crobe.";

    public Renderer() {}
    public Renderer(Crobe crobe) {
        super(crobe);
    }

    @Override
    protected void initializeGeneNames() {
        skin().name(CrobeConstants.RENDERER_GENE_SKIN);
        face().name(CrobeConstants.RENDERER_GENE_FACE);
        body().name(CrobeConstants.RENDERER_GENE_BODY);
    }
}
