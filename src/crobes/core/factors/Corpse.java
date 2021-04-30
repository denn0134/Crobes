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
    public boolean move(World.Direction direction) {
        boolean result = false;

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

                result = true;
            }//end if
        }//end if
        else {
            //if the location came back null that means we
            //drifted off the map, remove this corpse
            _world.remove(this);

            result = false;
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
