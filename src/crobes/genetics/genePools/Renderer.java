package crobes.genetics.genePools;

import crobes.core.Crobe;
import crobes.core.CrobeConstants;
import crobes.genetics.genes.HeritableGeneEnum;
import crobes.genetics.genes.HeritableGeneString;
import crobes.genetics.genes.Gene;

import java.util.ArrayList;

public abstract class Renderer extends GenePool implements IRenderGenePool
{
    public static final String RENDERER_DISPLAY = "Renderer";
    public static final String RENDERER_DESCRIPTION = "Defines the appearance of a Crobe.";

    private static final String TS_FMT = "    skin: %1$s face: %2$s body: %3$s";

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

    @Override
    public GenePool recombinateGenePool(Crobe crobe, ArrayList<GenePool> genePools) {
        SimpleVisageRenderer pool = new SimpleVisageRenderer(crobe);
        ArrayList<Gene> sGenes = new ArrayList<Gene>();
        ArrayList<Gene> fGenes = new ArrayList<Gene>();
        ArrayList<Gene> bGenes = new ArrayList<Gene>();

        for(GenePool gp : genePools) {
            SimpleVisageRenderer svr = (SimpleVisageRenderer) gp;
            sGenes.add(svr._skin);
            fGenes.add(svr._face);
            bGenes.add(svr._body);
        }//end for gp

        pool._skin = (HeritableGeneEnum) this._skin.recombinate(sGenes);
        pool._face = (HeritableGeneString) this._face.recombinate(fGenes);
        pool._body = (HeritableGeneEnum) this._body.recombinate(bGenes);

        pool.initializeGeneNames();

        return pool;
    }

    @Override
    public void mutate(int stressLevel) {
        _skin.mutate(stressLevel);
        _face.mutate(stressLevel);
        _body.mutate(stressLevel);
    }

    @Override
    public String toString() {
        return String.format(TS_FMT,
                _skin.toString(),
                _face.toString(),
                _body.toString());
    }
}
