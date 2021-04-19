package crobes.core.factors;

import crobes.core.Location;
import crobes.core.RenderContext;
import crobes.core.World;

import java.awt.*;

public abstract class Factor
{
    protected World _world;
    public World world() {
        return _world;
    }
    public void world(World world) {
        _world = world;
    }

    protected int _ttl;
    public int timeToLive() {
        return _ttl;
    }
    public void timeToLive(int ttl) {
        _ttl = ttl;
    }

    protected Location _location;
    public Location location() {
        return _location;
    }
    public void location(Location location) {
        _location = location;
    }

    public abstract boolean blocking();

    protected boolean _anchored;
    public boolean anchored() {
        return _anchored;
    }
    public void anchored(boolean anchored) {
        _anchored = anchored;
    }

    protected int _priority;
    public int priority() {
        return _priority;
    }
    public void priority(int priority) {
        _priority = priority;
    }

    public void process() {}

    public void render(Point location, RenderContext context) {}

    @Override
    public String toString() {
        return String.format("%1$s - ttl=%2$d p=%3$d a=%4$b",
                this.getClass().getSimpleName(), _ttl,
                _priority, _anchored);
    }
}
