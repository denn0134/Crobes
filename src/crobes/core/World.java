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
    public Direction reflect(Point point, Direction direction) {
        Direction result = null;

        boolean top, bottom, left, right;
        top = (point.y == 0);
        bottom = (point.y == getHeight() - 1);
        left = (point.x == 0);
        right = (point.x == getWidth() - 1);

        if (top) {
            if (left) {
                switch (direction) {
                    case UPLEFT:
                        result = Direction.DOWNRIGHT;
                        break;
                    case UP:
                        result = Direction.DOWN;
                        break;
                    case UPRIGHT:
                        result = Direction.DOWNRIGHT;
                        break;
                }//end switch
            }//end if
            else if (right) {
                switch (direction) {
                    case UPLEFT:
                        result = Direction.DOWNLEFT;
                        break;
                    case UP:
                        result = Direction.DOWN;
                        break;
                    case UPRIGHT:
                        result = Direction.DOWNLEFT;
                        break;
                }//end switch
            }//end else if
            else {
                switch (direction) {
                    case UPLEFT:
                        result = Direction.DOWNLEFT;
                        break;
                    case UP:
                        result = Direction.DOWN;
                        break;
                    case UPRIGHT:
                        result = Direction.DOWNRIGHT;
                        break;
                }//end switch
            }//end else
        }//end if
        else if (bottom) {
            if (left) {
                switch (direction) {
                    case DOWNLEFT:
                        result = Direction.UPRIGHT;
                        break;
                    case DOWN:
                        result = Direction.UP;
                        break;
                    case DOWNRIGHT:
                        result = Direction.UPRIGHT;
                        break;
                }//end switch
            }//end if
            else if (right) {
                switch (direction) {
                    case DOWNLEFT:
                        result = Direction.UPLEFT;
                        break;
                    case DOWN:
                        result = Direction.UP;
                        break;
                    case DOWNRIGHT:
                        result = Direction.UPLEFT;
                        break;
                }//end switch
            }//end else if
            else {
                switch (direction) {
                    case DOWNLEFT:
                        result = Direction.UPLEFT;
                        break;
                    case DOWN:
                        result = Direction.UP;
                        break;
                    case DOWNRIGHT:
                        result = Direction.UPRIGHT;
                        break;
                }//end switch
            }//end else
        }//end else if
        else {
            if (left) {
                switch (direction) {
                    case UPLEFT:
                        result = Direction.UPRIGHT;
                        break;
                    case LEFT:
                        result = Direction.RIGHT;
                        break;
                    case DOWNLEFT:
                        result = Direction.DOWNRIGHT;
                        break;
                }//end switch
            }//end if
            else if (right) {
                switch (direction) {
                    case UPRIGHT:
                        result = Direction.UPLEFT;
                        break;
                    case RIGHT:
                        result = Direction.LEFT;
                        break;
                    case DOWNRIGHT:
                        result = Direction.DOWNLEFT;
                        break;
                }//end switch
            }//end else if
        }//end else

        return result;
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
    public double distance(Point p1, Point p2) {
        return Math.sqrt(Math.pow(p1.x - p2.x, 2) + Math.pow(p1.y - p2.y, 2));
    }
    public ArrayList<Location> getInRange(Point point, int range) {
        ArrayList<Location> result = new ArrayList<Location>();

        int minX, minY, maxX, maxY;
        minX = (point.x - range < 0) ? 0 : point.x - range;
        minY = (point.y - range < 0) ? 0 : point.y - range;
        maxX = (point.x + range < getWidth()) ? point.x + range : getWidth();
        maxY = (point.y + range < getHeight()) ? point.y + range : getHeight();

        for (int x = minX; x < maxX; x++) {
            for (int y = minY; y < maxY; y++) {
                if (distance(point, new Point(x, y)) < range) {
                    result.add(getLocation(x, y));
                }//end if
            }//end for y
        }//end for x

        return result;
    }

    /***
     * Moves the specified Crobe one space in the specified
     * direction.  The movement of the Crobe can be affected
     * by blocking objects or the edge of the World.
     * @param crobe The Crobe to move.
     * @param direction The direction to move the Crobe.
     *                  Passing RANDOM will result in a random
     *                  direction being generated; this random
     *                  direction could be NONE.
     * @return Returns true if the Crobe was successfully
     * moved.  Returns false if the Crobes was not able to
     * be moved, or if the direction was NONE.
     */
    public boolean move(Crobe crobe, Direction direction) {
        boolean result = false;

        Direction dir = (direction == Direction.RANDOM) ? randomDirection() : direction;

        if (dir != Direction.NONE) {
            //find the location to move to
            Location loc = getLocation(crobe.position().x, crobe.position().y);
            Point pt = movePoint(crobe.position(), dir);
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
        }//end if

        return result;
    }
    /***
     * Moves the specified Crobe up to the specified distance
     * in the specified direction.  The movement of the Crobe
     * can be affected by blocking objects or the edge of the
     * World.
     * @param crobe The Crobe to move.
     * @param direction The direction to move the Crobe.
     *                  Passing RANDOM will result in a random
     *                  direction being generated; this random
     *                  direction could be NONE.
     * @param distance The distance in spaces to move the Crobe.
     * @return Returns true if the Crobe was successfully moved
     * the entire distance requested.  Returns false if the
     * Crobe was not able to be moved the entire distance, or if
     * the direction was NONE.
     */
    public boolean move(Crobe crobe, Direction direction, int distance) {
        boolean result = false;

        for (int i = 0; i < distance; i++) {
            result = move(crobe, direction);
            if (!result) break;
        }//end for i

        return result;
    }
    /***
     * Moves the specified Factor one space in the specified
     * direction.  The movement of the Factor can be affected
     * by blocking objects or the edge of the World.
     * @param factor The Factor to be moved.
     * @param direction The direction to move the Factor.
     *                  Passing RANDOM will result in a random
     *                  direction being generated; this random
     *                  direction could be NONE.
     * @return Returns an integer from -1 to 1 as follows:
     * <ul>
     *     <li>
     *         -1: Returns negative one(-1) if the Factor failed
     *         to move due to the edge of the World.
     *     </li>
     *     <li>
     *         0: Returns zero(0) if the move was successful.
     *     </li>
     *     <li>
     *         1: Returns one(1) if the move was blocked by an
     *         object.
     *     </li>
     * </ul>
     */
    public int move(Factor factor, Direction direction) {
        return factor.move(direction);
    }
    /***
     * Moves the specified Factor up to the specified distance in
     * the specified direction.  The movement of the Factor can be
     * affected by blocking objects and the edge of the World.
     * @param factor The Factor to move.
     * @param direction The direction to move the Factor.
     *                  Passing RANDOM will result in a random
     *                  direction being generated; this random
     *                  direction could be NONE.
     * @param distance The distance to move the Factor.
     * @return Returns an integer representing how many spaces
     * are left in order to move the entire requested distance
     * as follows:
     * <ul>
     *     <li>
     *         A negative value indicates that the Factor failed
     *         to move the entire requested distance due to encountering
     *         the edge of the World; the result is the number of
     *         spaces beyond the edge the Factor would have been moved.
     *     </li>
     *     <li>
     *         Zero(0) indicates that the Factor was successfully
     *         moved the entire requested distance.
     *     </li>
     *     <li>
     *         A positive value indicates that the Factor failed
     *         to move the entire requested distance due to encountering
     *         a blocking object; the result is the number of spaces
     *         the factor has left in order to move the requested
     *         distance.
     *     </li>
     * </ul>
     */
    public int move(Factor factor, Direction direction, int distance) {
        boolean done = false;
        int dist = distance;

        while (!done) {
            int move = move(factor, direction);

            switch (move) {
                case -1:
                    dist *= -1;
                    done = true;
                    break;
                case 0:
                    dist -= 1;
                    break;
                case 1:
                    done = true;
                    break;
            }//end switch
        }//end while

        return dist;
    }

    public void remove(Crobe crobe) {
    	System.out.println("A removal is attempted");
        crobe.stage(CrobeEnums.LifeStage.DEAD);
        crobes().purge();
    }
    public void remove(Factor factor) {
        factor.location().factors().remove(factor);
        _factors.remove(factor);
        //System.out.println("Factor Version");
    }
    public void markRemove(Factor factor) {
    	factor.markedRemoved = true;
    }
    public void removeMarked() {
    	for(int i = _factors.size()-1; i >= 0; i--) {
    		if(_factors.get(i).markedRemoved) {
    			_factors.remove(i);
    		}
    	}
    }

    public void resetElements() {
        for (ArrayList<Location> row: _environment) {
            for (Location loc: row) {
                loc.elements().reset();
            }//end for each
        }//end for each
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
