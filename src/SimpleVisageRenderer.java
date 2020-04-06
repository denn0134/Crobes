import java.util.ArrayList;

public class SimpleVisageRenderer extends GenePool implements IRenderGenePool
{
    private static final String TS_FMT = "    skin: %1$s face: %2$s body: %3$s";

    @Override
    public String getNamePart() {
        return "homlys";
    }

    private HeritableGeneEnum _skin;
    @Override
    public HeritableGeneEnum skin() {
        return _skin;
    }
    @Override
    public void skin(HeritableGeneEnum skin) {
        _skin = skin;
    }

    private HeritableGeneString _face;
    @Override
    public HeritableGeneString face() {
        return _face;
    }
    @Override
    public void face(HeritableGeneString face) {
        _face = face;
    }

    private HeritableGeneEnum _body;
    @Override
    public HeritableGeneEnum body() {
        return _body;
    }
    @Override
    public void body(HeritableGeneEnum body) {
        _body = body;
    }

    public SimpleVisageRenderer(Crobe crobe) {
        super(crobe);
    }

    @Override
    public void mutate(int stressLevel) {
        _skin.mutate(stressLevel);
        _face.mutate(stressLevel);
        _body.mutate(stressLevel);
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

        return pool;
    }

    @Override
    public String toString() {
        return String.format(TS_FMT,
                _skin.toString(),
                _face.toString(),
                _body.toString());
    }
}
