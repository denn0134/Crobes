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
    public void drift(Drift.DriftDirection direction) {
        //calculate the point to drift to
        Point driftPoint = Drift.getDriftPoint(location().point(), direction);

        //get the location
        Location driftLoc = world().getLocation(driftPoint.x, driftPoint.y);
        if (driftLoc != null) {
            //check if there is anything blocking the drift
            if (!driftLoc.blocking()) {
                //we can drift here
                location().factors().remove(this);
                driftLoc.factors().add(this);
                location(driftLoc);
            }//end if
        }//end if
        else {
            //if the location came back that means we
            //drifted off the map, remove this corpse
            location().factors().remove(this);
            world().factors().remove(this);
        }//end else
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
