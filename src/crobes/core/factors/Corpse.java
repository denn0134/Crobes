package crobes.core.factors;

import crobes.core.CrobeEnums;
import crobes.core.Location;
import crobes.core.RenderContext;

import java.awt.*;

public class Corpse extends Factor
{
    @Override
    public boolean blocking() {
        return true;
    }

    @Override
    public void process() {
        _ttl -= 1;
    }

    @Override
    public void render(Point location, RenderContext context) {
        context.background = CrobeEnums.CrobeColor.black;
        context.foreground = CrobeEnums.CrobeColor.crimson;
        context.content = "X";
    }
}
