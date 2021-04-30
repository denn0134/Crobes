package crobes.core.factors;

import crobes.core.CrobeEnums;
import crobes.core.Location;
import crobes.core.World;
import crobes.genetics.genomics.Genomics;
import org.reflections.vfs.Vfs;

import java.awt.*;

public abstract class Pod extends Factor
{
    public enum PodMotion {
        NONE,
        LINE_RAY,
        LINE_PINBALL,
        WANDER,
        WANDER_PINBALL
    }
    public enum PodSize {
        SMALL,
        MEDIUM,
        LARGE
    }

    private static final int PRIORITY_POD = 5;

    protected CrobeEnums.MotilityType _motility;
    protected PodMotion _motion;
    protected int _speed;
    protected PodSize _size;
    protected World.Direction _direction;

    public Pod(World world,
               int timeToLive,
               CrobeEnums.MotilityType motility,
               PodMotion motion,
               int speed,
               PodSize size,
               World.Direction direction) {
        super(world, timeToLive,
                PRIORITY_POD,
                (motility == CrobeEnums.MotilityType.ANCHORED));

        _motility = motility;
        _motion = motion;
        _speed = speed;
        _size = size;

        if (_motion == PodMotion.WANDER)
            _direction = World.Direction.RANDOM;
        else
            _direction = direction;
    }

    @Override
    public void process() {
        if (_motility == CrobeEnums.MotilityType.MOTILE) {
            int dist = Genomics.random().nextInt(_speed);
            if (dist == 0) dist++;

            //get the direction
            World.Direction dir = _direction;
            if (dir == World.Direction.RANDOM) dir = World.randomDirection();

            //reset the direct to random if in pinball mode
            switch (_motion) {
                case WANDER_PINBALL:
                case LINE_PINBALL:
                    dir = World.Direction.RANDOM;
            }//end switch

            //move base on the motion type
            if (dir != World.Direction.NONE) {
                switch (_motion) {
                    case WANDER:
                    case LINE_RAY:
                        //move the distance in the direction
                        dist = _world.move(this, dir, dist);

                        //check if the pod moved off the map
                        if (dist < 0)
                            _world.remove(this);
                        break;
                    case WANDER_PINBALL:
                    case LINE_PINBALL:
                        //try to move the distance in the direction
                        //if it would move off the map, then reflect
                        while (dist > 0) {
                            dist = _world.move(this, dir, dist);

                            if (dist < 0) {
                                dir = _world.reflect(_location.point(), dir);
                                dist *= -1;
                            }//end if
                        }//end while
                        break;
                }//end switch
            }//end if
        }//end if
    }

    @Override
    public int move(World.Direction direction) {
        int result;

        Point pt = World.movePoint(_location.point(), direction);
        Location moveLoc = _world.getLocation(pt.x, pt.y);
        if (moveLoc != null) {
            Location loc = _location;
            loc.factors().remove(this);
            moveLoc.factors().add(this);
            location(moveLoc);

            result = 0;
        }//end if
        else {
            //moved off the map
            result = -1;
        }//end else

        return result;
    }

    @Override
    public String toJson() {
        StringBuilder sb = new StringBuilder();

        sb.append(super.toJson());

        sb.append(", ");
        sb.append(quotedString("motility"));
        sb.append(": ");
        sb.append(quotedString(_motility.name()));

        sb.append(", ");
        sb.append(quotedString("motion"));
        sb.append(": ");
        sb.append(quotedString(_motion.name()));

        sb.append(", ");
        sb.append(quotedString("speed"));
        sb.append(": ");
        sb.append(_speed);

        sb.append(", ");
        sb.append(quotedString("size"));
        sb.append(": ");
        sb.append(_size);

        sb.append(", ");
        sb.append(quotedString("direction"));
        sb.append(": ");
        sb.append(quotedString(_direction.name()));

        return sb.toString();
    }
}
