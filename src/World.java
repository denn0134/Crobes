import java.util.ArrayList;

public class World
{
    private Environment _environment;
    public Environment environment() {
        return _environment;
    }

    private CrobeColony _crobes;
    public CrobeColony crobes() {
        return _crobes;
    }

    private ArrayList<EnviromentalFactor> _factors = new ArrayList<EnviromentalFactor>();
    public ArrayList<EnviromentalFactor> factors() {
        return _factors;
    }

    public World(int environmentalRadix) {
        _environment = new Environment(environmentalRadix);
        _crobes = new CrobeColony();
    }

    public int getWidth() {
        return _environment.width();
    }
    public int getHeight() {
        return _environment.height();
    }
    public boolean inBounds(int X, int Y) {
        boolean inBounds = true;

        if((X < 0) || (Y < 0))
            inBounds = false;

        if(X >= _environment.width())
            inBounds = false;
        if(Y >= _environment.height())
            inBounds = false;

        return inBounds;
    }
    public Location getLocation(int X, int Y) {
        return _environment.get(X, Y);
    }

    public void processEnvironment() {
        _environment.reset();
        _environment.updateLocations();
    }
}
