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
    public int move(World.Direction direction) {
        int result = 0;

        //calculate the point to drift to
        Point movePoint = World.movePoint(location().point(), direction);

        //get the location
        Location moveLoc = world().getLocation(movePoint.x, movePoint.y);
        if (moveLoc != null) {
            //check if there is anything blocking the drift
            if (!moveLoc.blocking()) {
                //we can drift here
                location().factors().remove(this);
                moveLoc.factors().add(this);
                location(moveLoc);

                result = 1;
            }//end if
        }//end if
        else {
            result = -1;
        }//end else

        return result;
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
