package crobes.genetics.genePools;

import crobes.core.Crobe;

public abstract class Renderer extends GenePool implements IRenderGenePool
{

    public Renderer(Crobe crobe) {
        super(crobe);
    }

    @Override
    protected void initializeGeneNames() {

    }
}
