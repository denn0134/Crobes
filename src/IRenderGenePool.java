import java.awt.*;

public interface IRenderGenePool extends IGenePool
{
    HeritableGeneEnum skin();
    void skin(HeritableGeneEnum skin);
    HeritableGeneString face();
    void face(HeritableGeneString face);
    HeritableGeneEnum body();
    void body(HeritableGeneEnum body);

    void renderCrobe(Point location,
                     RenderContext renderContext);

    Location[] getLocations(CrobeEnums.LifeStage stage);
    Location[] getAdjacents(CrobeEnums.LifeStage stage);
}
