package crobes.genetics.genePools;

import crobes.core.*;
import crobes.genetics.genes.*;
import java.awt.*;
import java.util.ArrayList;

public class SimpleVisageRenderer extends Renderer implements IRenderGenePool
{
    @Override
    public String description() {
        return "Simple no-frills renderer for single location crobes.";
    }

    private static final String TS_FMT = "    skin: %1$s face: %2$s body: %3$s";

    @Override
    public String getNamePart() {
        return "homlys";
    }

    public SimpleVisageRenderer(Crobe crobe) {
        super(crobe);
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

    @Override
    public void initializeRandomDefault() {
        if((_skin != null) ||
                (_face != null) ||
                (_body != null)) {
            return;
        }//end if

        //skin
        _skin = new HeritableGeneEnum(new Enum[] {CrobeEnums.CrobeColor.black, CrobeEnums.CrobeColor.brown},
                new Enum[] {CrobeEnums.CrobeColor.brown, CrobeEnums.CrobeColor.maroon, CrobeEnums.CrobeColor.black},
                CrobeEnums.MutationType.RANDOM);

        //face
        String[] faces = new String[] {"O", "W", "M", "X"};
        _face = new HeritableGeneString(new String[] {faces[0], faces[3]},
                faces, CrobeEnums.MutationType.RANDOM);

        //body
        _body = new HeritableGeneEnum(new Enum[] {CrobeEnums.CrobeColor.yellow, CrobeEnums.CrobeColor.yellow},
                new Enum[] {CrobeEnums.CrobeColor.lightblue, CrobeEnums.CrobeColor.pink, CrobeEnums.CrobeColor.yellow},
                CrobeEnums.MutationType.RANDOM);

        initializeGeneNames();
    }

    @Override
    public void renderCrobe(Point location,
                            RenderContext renderContext) {
        renderContext.foreground = (CrobeEnums.CrobeColor) _skin.phenotype();
        renderContext.background = (CrobeEnums.CrobeColor) _body.phenotype();
        renderContext.content = _face.phenotype();
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

        pool.initializeGeneNames();

        return pool;
    }

    @Override
    public Location[] getLocations(CrobeEnums.LifeStage stage) {
        //this crobe is only ever a single location
        return new Location[] {_crobe.world().environment().get(_crobe.position())};
    }

    @Override
    public Location[] getAdjacents(CrobeEnums.LifeStage stage) {
        int count = 8;
        int xMin, xMax, yMin, yMax;
        xMin = -1;
        xMax = 2;
        yMin = -1;
        yMax = 2;

        //check if we are on the edge of the environment
        Point p = _crobe.position();
        int maxW, maxH;
        maxW = _crobe.world().environment().width();
        maxH = _crobe.world().environment().height();

        if(p.x == 0) {
            count -= 2;
            xMin = 0;
        }//end if
        else if(p.x == maxW) {
            count -= 2;
            xMax = 1;
        }//end else if

        if(p.y == 0) {
            count -= 2;
            yMin = 0;
        }//end if
        else if(p.y == maxH) {
            count -= 2;
            yMax = 1;
        }//end else if

        if(count < 8) {
            count -= 1;
        }//end if

        Location[] result = new Location[count];

        int idx = 0;
        for(int x = xMin; x < xMax; x++) {
            for(int y = yMin; y < yMax; y++) {
                if((x != 0) || (y != 0)) {
                    result[idx] = _crobe.world().environment().get(p.x + x, p.y + y);
                    idx++;
                }//end if
            }//end for y
        }//end for x

        return result;
    }

    @Override
    public String toString() {
        return String.format(TS_FMT,
                _skin.toString(),
                _face.toString(),
                _body.toString());
    }
}
