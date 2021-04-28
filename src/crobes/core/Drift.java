package crobes.core;

import crobes.genetics.genomics.Genomics;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;

/***
 * Class to process the drifting of crobes and factors.
 */
public class Drift
{
    /***
     * Enumeration of possible drift directions.
     */
    public enum DriftDirection {
        NONE,
        UP,
        UPLEFT,
        UPRIGHT,
        LEFT,
        RIGHT,
        DOWNLEFT,
        DOWNRIGHT,
        DOWN
    }

    public enum DriftType {
        BROWNIAN,
        FLOW
    }

    public static DriftDirection randomDrift() {
        int index = Genomics.random().nextInt(DriftDirection.values().length);
        return DriftDirection.values()[index];
    }
    public static Point getDriftPoint(Point point, DriftDirection direction) {
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

    private Location _location;
    public Location location() {
        return _location;
    }

    private DriftType _type;
    public DriftType type() {
        return _type;
    }

    private DriftDirection _direction;
    public DriftDirection direction() {
        return _direction;
    }

    public Drift(Location location, DriftType type, DriftDirection direction) {
        _location = location;
        _type = type;
        _direction = direction;
    }

    public static final class DriftList extends ArrayList<Drift>
    {
        private static Comparator<Drift> comparator = new Comparator<Drift>() {
            @Override
            public int compare(Drift o1, Drift o2) {
                int result = 0;

                if(o1.location().point().y == o2.location().point().y) {
                    result = o1.location().point().x - o2.location().point().x;
                }//end if
                else {
                    result = o1.location().point().y - o2.location().point().y;
                }//end else

                return result;
            }
        };

        public boolean addDrift(Location location, DriftType type, DriftDirection direction) {
            boolean result;

            Drift drift = this.findByLocation(location);

            if (drift == null) {
                drift = new Drift(location, type, direction);
                result = this.add(drift);
            }//end if
            else {
                drift._direction = direction;
                result = true;
            }//end else

            //this.sort(comparator);
            return result;
        }

        public Drift findByLocation(Location location) {
            Drift result = null;

            for (int i = 0; i < this.size(); i++) {
                if(location.equals(get(i).location())) {
                    result = this.get(i);
                    break;
                }//end if
            }//end for i

            return result;
        }
    }
}
