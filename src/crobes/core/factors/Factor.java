package crobes.core.factors;

import crobes.core.Location;
import crobes.core.World;

public abstract class Factor
{
    private World _world;
    public World world() {
        return _world;
    }
    public void world(World world) {
        _world = world;
    }

    private int _ttl;
    public int timeToLive() {
        return _ttl;
    }
    public void timeToLive(int ttl) {
        _ttl = ttl;
    }

    private Location _location;
    public Location location() {
        return _location;
    }
    public void location(Location location) {
        _location = location;
    }

    public void process() {}
}
