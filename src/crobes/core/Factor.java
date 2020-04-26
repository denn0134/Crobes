import org.omg.Messaging.SYNC_WITH_TRANSPORT;

public class Factor
{
    private World _world;
    public World world() {
        return _world;
    }
    public void world(World world) {
        _world = world;
    }
}
