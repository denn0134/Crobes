package crobes.core.factors;

import crobes.core.*;
import crobes.core.factors.gui.PodEditor;

import java.awt.*;

public abstract class Factor
{
    protected World _world;
    protected int _ttl;
    protected boolean _anchored;
    protected int _priority;
    protected Location _location;

    /***
     * The World object which this Factor is part of.
     * @return Returns the World object.
     */
    public World world() {
        return _world;
    }
    /***
     * The amount of time before this Factor will
     * disappear or decay.
     * @return Returns the time to live.
     */
    public int timeToLive() {
        return _ttl;
    }
    /***
     * Determines whether this Factor object blocks
     * other objects from inhabiting the same location.
     * @return Returns the blocking property.
     */
    public boolean blocking() {
        return false;
    }
    /***
     * Determines whether this Factor is allowed
     * to drift.
     * @return Returns the anchored property.
     */
    public boolean anchored() {
        return _anchored;
    }
    /***
     * The rendering/processing priority for this
     * Factor in situations where more than a single
     * Factor exists within a single Location.
     * @return Returns the priority property.
     */
    public int priority() {
        return _priority;
    }
    public Location location() {
        return _location;
    }
    public void location(Location location) {
        _location = location;
    }

    public Factor(World world,
                  int timeToLive,
                  int priority,
                  boolean anchored) {
        _world = world;
        _ttl = timeToLive;
        _priority = priority;
        _anchored = anchored;
    }

    public void process() {}
    public void render(Point location, Lens.Mode mode, RenderContext context) {}

    /***
     * Moves the Factor one space in the specified direction.
     * The move can be affected by blocking objects or the edge
     * of the World.
     * @param direction The direction to move.  Passing RANDOM
     *                  will result in a random direction being
     *                  generated, which be NONE.
     * @return Returns zero(0) if the move was not blocked, one(1)
     * if the move was blocked by an object, negative one(-1) if
     * the move would have resulted in move off the edge of the
     * World.
     */
    public int move(World.Direction direction) {
        //the default behaviour is that Factors do not
        //move, return a success by default as nothing
        //was blocked
        return 0;
    }

    @Override
    public String toString() {
        return String.format("%1$s - ttl=%2$d p=%3$d a=%4$b",
                this.getClass().getSimpleName(), _ttl,
                _priority, _anchored);
    }

    protected static String quotedString(String s) {
        return "\"" + s + "\"";
    }
    protected String worldJson(World w) {
        StringBuilder sb = new StringBuilder();

        sb.append("{");

        sb.append(quotedString("width"));
        sb.append(": ");
        sb.append(w.getWidth());
        sb.append(", ");

        sb.append(quotedString("height"));
        sb.append(": ");
        sb.append(w.getHeight());

        sb.append("}");

        return sb.toString();
    }
    protected String locationJson(Location l) {
        StringBuilder sb = new StringBuilder();

        sb.append("{");

        sb.append(quotedString("point"));
        sb.append(": ");
        sb.append(pointJson(l.point()));

        sb.append("}");

        return sb.toString();
    }
    protected String pointJson(Point p) {
        StringBuilder sb = new StringBuilder();

        sb.append("{");

        sb.append(quotedString("x"));
        sb.append(": ");
        sb.append(p.x);
        sb.append(", ");

        sb.append(quotedString("y"));
        sb.append(": ");
        sb.append(p.y);

        sb.append("}");

        return sb.toString();
    }
    public String toJson() {
        StringBuilder sb = new StringBuilder();

        sb.append(quotedString("world"));
        sb.append(": ");
        sb.append(worldJson(_world));
        sb.append(", ");

        sb.append(quotedString("timeToLive"));
        sb.append(": ");
        sb.append(_ttl);
        sb.append(", ");

        sb.append(quotedString("blocking"));
        sb.append(": ");
        sb.append(blocking());
        sb.append(", ");

        sb.append(quotedString("anchored"));
        sb.append(": ");
        sb.append(_anchored);
        sb.append(", ");

        sb.append(quotedString("priority"));
        sb.append(": ");
        sb.append(_priority);
        sb.append(", ");

        sb.append(quotedString("location"));
        sb.append(": ");
        sb.append(locationJson(_location));

        return sb.toString();
    }
}
