package crobes.genetics.genePools;

import crobes.core.*;
import crobes.genetics.genes.*;
import crobes.genetics.genomics.Genomics;

import java.awt.*;
import java.util.ArrayList;

public class SimpleVisageRenderer extends Renderer implements IRenderGenePool
{
    static {
        Genomics.renderers.registerGenePool(SimpleVisageRenderer.class);
    }//end static

    @Override
    public String displayName() {
        return "Basic Render";
    }

    @Override
    public String getNamePart() {
        return "homlys";
    }

    @Override
    public String description() {
        return "Simple no-frills renderer for single location crobes.";
    }

    public SimpleVisageRenderer() {}
    public SimpleVisageRenderer(Crobe crobe) {
        super(crobe);
    }

    @Override
    public Genomics.GenePoolRandomizer getRandomizer() {
        Genomics.GenePoolRandomizer result = new Genomics.GenePoolRandomizer();

        Genomics.EnumRandomizer skn = new Genomics.EnumRandomizer(CrobeConstants.RENDERER_GENE_SKIN);
        skn.domainChance = 0.25f;
        result.add(skn);

        Genomics.StringRandomizer fc = new Genomics.StringRandomizer(CrobeConstants.RENDERER_GENE_FACE);
        fc.domain = new String[] {"O", "X", "M", "W"};
        result.add(fc);

        Genomics.EnumRandomizer bod = new Genomics.EnumRandomizer(CrobeConstants.RENDERER_GENE_BODY);
        bod.domainChance = 0.25f;
        result.add(bod);

        return result;
    }

    @Override
    public void renderCrobe(Point location,
                            RenderContext renderContext) {
        renderContext.foreground = (CrobeEnums.CrobeColor) _skin.phenotype();
        renderContext.background = (CrobeEnums.CrobeColor) _body.phenotype();
        renderContext.content = _face.phenotype();
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
        maxW = _crobe.world().environment().width() - 1;
        maxH = _crobe.world().environment().height() - 1;

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
}
