package crobes.core;

import java.util.ArrayList;
import java.util.Comparator;

/***
 * Class to process the drifting of crobes and factors.
 */
public class Drift
{
    /***
     * Enumeration of the basic drift mechanisms available.
     */
    public enum DriftType {
        BROWNIAN,
        FLOW
    }

    private Location _location;
    /***
     * The location where the drift is occurring.
     * @return
     */
    public Location location() {
        return _location;
    }

    private DriftType _type;
    /***
     * The type of drift which is occurring.
     * @return
     */
    public DriftType type() {
        return _type;
    }

    private World.Direction _direction;
    /***
     * The direction of the drift motion.
     * @return
     */
    public World.Direction direction() {
        return _direction;
    }

    /***
     * Constructor for a Drift object.
     * @param location Location where the drift is occurring.
     * @param type Type of drift to occur.
     * @param direction Direction of the drift motion.
     */
    public Drift(Location location, DriftType type, World.Direction direction) {
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

        public boolean addDrift(Location location, DriftType type, World.Direction direction) {
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
