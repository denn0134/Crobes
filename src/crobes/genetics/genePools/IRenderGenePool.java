package crobes.genetics.genePools;

import crobes.core.*;
import crobes.genetics.*;
import crobes.genetics.genes.*;
import java.awt.*;

public interface IRenderGenePool extends IGenePool
{
    /***
     * Foreground color of the crobe.
     */
    HeritableGeneEnum skin();
    void skin(HeritableGeneEnum skin);

    /***
     * Render character for the crobe.
     */
    HeritableGeneString face();
    void face(HeritableGeneString face);

    /***
     * Background color of the crobe.
     */
    HeritableGeneEnum body();
    void body(HeritableGeneEnum body);

    void renderCrobe(Point location,
                     RenderContext renderContext);

    Location[] getLocations(CrobeEnums.LifeStage stage);
    Location[] getAdjacents(CrobeEnums.LifeStage stage);
}
