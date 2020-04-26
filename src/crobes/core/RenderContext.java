/***
 * Data structure for returning render values.
 */
public class RenderContext
{
    public CrobeEnums.CrobeColor background;
    public CrobeEnums.CrobeColor foreground;
    public String content;

    public boolean matches(CrobeEnums.CrobeColor fGround,
                           CrobeEnums.CrobeColor bGround) {
        return ((foreground == fGround) &&
                (background == bGround));
    }
}
