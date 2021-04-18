package crobes.genetics.genePools;

import crobes.core.Crobe;
import crobes.core.CrobeConstants;
import crobes.genetics.genes.HeritableGeneEnum;
import crobes.genetics.genes.HeritableGeneString;

public abstract class Renderer extends GenePool implements IRenderGenePool
{
    public static final String RENDERER_DISPLAY = "Renderer";
    public static final String RENDERER_DESCRIPTION = "Defines the appearance of a Crobe.";

    public Renderer() {}
    public Renderer(Crobe crobe) {
        super(crobe);
    }

    protected HeritableGeneEnum _skin;
    @Override
    public HeritableGeneEnum skin() {
        return _skin;
    }
    @Override
    public void skin(HeritableGeneEnum skin) {
        _skin = skin;
    }

    protected HeritableGeneString _face;
    @Override
    public HeritableGeneString face() {
        return _face;
    }
    @Override
    public void face(HeritableGeneString face) {
        _face = face;
    }

    protected HeritableGeneEnum _body;
    @Override
    public HeritableGeneEnum body() {
        return _body;
    }
    @Override
    public void body(HeritableGeneEnum body) {
        _body = body;
    }


    @Override
    protected void initializeGeneNames() {
        skin().name(CrobeConstants.RENDERER_GENE_SKIN);
        face().name(CrobeConstants.RENDERER_GENE_FACE);
        body().name(CrobeConstants.RENDERER_GENE_BODY);
    }
}
