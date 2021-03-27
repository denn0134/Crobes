package crobes.genetics.genePools;

import crobes.core.Crobe;

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

    }
}
