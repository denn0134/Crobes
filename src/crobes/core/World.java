package crobes.core;

import crobes.core.factors.Factor;
import crobes.genetics.genomics.Genomics;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;

public class World
{
    public enum Direction {
        NONE,
        RANDOM,
        UP,
        DOWN,
        LEFT,
        RIGHT,
        UPLEFT,
        UPRIGHT,
        DOWNLEFT,
        DOWNRIGHT
    }
    public static Direction randomDirection() {
        Direction d = Direction.RANDOM;
        while (d == Direction.RANDOM) {
            Direction[] ds = Direction.values();
            d = ds[Genomics.random().nextInt(ds.length)];
        }//end while

        return d;
    }
    public static Point movePoint(Point point, Direction direction) {
        Point result = new Point(point);
        switch (direction) {
            case UP:
                result.y--;
                break;
            case DOWN:
                result.y++;
                break;
            case LEFT:
                result.x--;
                break;
            case RIGHT:
                result.x++;
                break;
            case UPLEFT:
                result.x--;
                result.y--;
                break;
            case UPRIGHT:
                result.x++;
                result.y--;
                break;
            case DOWNLEFT:
                result.x--;
                result.y++;
                break;
            case DOWNRIGHT:
                result.x++;
                result.y++;
                break;
        }//end switch

        return result;
    }

    private int _age;
    public int age() {
        return _age;
    }

    private Environment _environment;
    public Environment environment() {
        return _environment;
    }

    private CrobeColony _crobes;
    public CrobeColony crobes() {
        return _crobes;
    }

    private ArrayList<Factor> _factors = new ArrayList<Factor>();
    public ArrayList<Factor> factors() {
        return _factors;
    }

    private Drift.DriftList _driftList = new Drift.DriftList();
    public Drift.DriftList driftList() {
        return _driftList;
    }

    public World(int environmentalRadix) {
        Genomics.initializeGenomics();
        _age = 0;
        _factors = new ArrayList<Factor>();
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
        //check if the coordinates are valid
        boolean wValid = ((X > -1) && (X < _environment.width()));
        boolean hValid = ((Y > -1) && (Y < _environment.height()));

        if(wValid && hValid)
            return _environment.get(X, Y);
        else
            return null;
    }

    public boolean move(Crobe crobe, Direction direction) {
        boolean result = false;

        //find the location to move to
        Location loc = getLocation(crobe.position().x, crobe.position().y);
        Point pt = movePoint(crobe.position(), direction);
        if (pt != null) {
            Location moveLoc = getLocation(pt.x, pt.y);
            if ((moveLoc != null) &&
                    (!moveLoc.blocking())) {
                loc.crobe(null);
                moveLoc.crobe(crobe);
                crobe.position(pt);

                result = true;
            }//end if
        }//end if

        return result;
    }
    public boolean move(Crobe crobe, Direction direction, int distance) {
        boolean result = false;

        for (int i = 0; i < distance; i++) {
            result = move(crobe, direction);
            if (!result) break;
        }//end for i

        return result;
    }
    public boolean move(Factor factor, Direction direction) {
        return factor.move(direction);
    }
    public boolean move(Factor factor, Direction direction, int distance) {
        boolean result = false;

        for (int i = 0; i < distance; i++) {
            result = move(factor, direction);
            if (!result) break;
        }//end for i

        return result;
    }

    public void remove(Crobe crobe) {
        crobe.stage(CrobeEnums.LifeStage.DEAD);
        crobes().purge();
    }
    public void remove(Factor factor) {
        factor.location().factors().remove(factor);
        _factors.remove(factor);
    }

    public void updateEnvironment() {
        _environment.reset();
        _environment.updateLocations();
    }
    public void updateFactors() {
        //sort the factors by priority
        _factors.sort(new Comparator<Factor>() {
            @Override
            public int compare(Factor o1, Factor o2) {
                return o1.priority() - o2.priority();
            }
        });

        //process all factors
        for(Factor f: _factors) {
            f.process();
        }//end for each

        //remove any factor with a ttl of zero
        for(int i = _factors.size(); i > 0; i--) {
            Factor f = _factors.get(i - 1);
            if(f.timeToLive() == 0) {
                f.location().factors().remove(f);
                _factors.remove(f);
            }//end if
        }//end for i
    }
    public void processDrift() {
        for (Drift d: _driftList) {
            d.location().drift(d.direction());
        }//end for each
    }
    public void brownianMotion() {
        //brownian motion is always first
        //therefore we should reset the drift list prior
        //to generating the brownian motion
        _driftList.clear();

        for (ArrayList<Location> row: _environment) {
            for (Location l: row) {
                if(Genomics.random().nextInt(100) == 0) {
                    World.Direction d = randomDirection();
                    if (d != World.Direction.NONE) {
                        _driftList.addDrift(l, Drift.DriftType.BROWNIAN, d);
                    }//end if
                }//end if
            }//end for each
        }//end for each
    }
}
