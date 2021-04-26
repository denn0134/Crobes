package crobes.core.factors;

import crobes.core.*;

import java.awt.*;

public class Corpse extends Factor
{
    @Override
    public boolean blocking() {
        return true;
    }

    public Corpse(World world,
                  int timeToLive,
                  int priority,
                  boolean anchored) {
        super(world, timeToLive, priority, anchored);
    }

    @Override
    public void process() {
        _ttl -= 1;
    }

    @Override
    public void render(Point location, Lens.Mode mode, RenderContext context) {
        context.background = CrobeEnums.CrobeColor.black;
        context.foreground = CrobeEnums.CrobeColor.crimson;
        context.content = "X";
    }

    @Override
    public String toJson() {
        StringBuilder sb = new StringBuilder();

        sb.append("{");

        sb.append(quotedString("Class"));
        sb.append(": ");
        sb.append(quotedString(this.getClass().getSimpleName()));
        sb.append(", ");

        sb.append(super.toJson());

        sb.append("}");

        return sb.toString();
    }
}
