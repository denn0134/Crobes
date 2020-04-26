package crobes.genetics.genePools;

import crobes.core.Crobe;
import crobes.genetics.GenePool;

public abstract class Renderer extends GenePool implements IRenderGenePool
{

    public Renderer(Crobe crobe) {
        super(crobe);
    }

    @Override
    protected void initializeGeneNames() {

    }
}
