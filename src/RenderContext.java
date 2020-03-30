/***
 * Data structure for returning render values.
 */
public class RenderContext
{
    public String background;
    public String foreground;
    public String content;

    public boolean matches(String fGround, String bGround) {
        return ((foreground.equalsIgnoreCase(fGround)) &&
                (background.equalsIgnoreCase(bGround)));
    }
}
