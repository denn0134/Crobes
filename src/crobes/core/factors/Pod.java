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

    public static final class MotionType
    {
        public CrobeEnums.MotilityType motility;
        public PodMotion motion;
        public int speed;
        public World.Direction direction;

        public MotionType(CrobeEnums.MotilityType motType,
                          PodMotion podMot,
                          int spd,
                          World.Direction dir) {
            motility = motType;
            motion = podMot;
            speed = spd;
            direction = dir;
        }

        public String toJson() {
            StringBuilder sb = new StringBuilder();

            sb.append("{");

            sb.append(quotedString("motility"));
            sb.append(": ");
            if (motility != null)
                sb.append(quotedString(motility.name()));
            else
                sb.append("null");

            sb.append(", ");
            sb.append(quotedString("motion"));
            sb.append(": ");
            if (motion != null)
                sb.append(quotedString(motion.name()));
            else
                sb.append("null");

            sb.append(", ");
            sb.append(quotedString("speed"));
            sb.append(": ");
            sb.append(speed);

            sb.append(", ");
            sb.append(quotedString("direction"));
            sb.append(": ");
            if (direction != null)
                sb.append(quotedString(direction.name()));
            else
                sb.append("null");

            sb.append("}");

            return sb.toString();
        }
    }

    private static final int PRIORITY_POD = 5;

    protected PodSize _size;
    protected MotionType _motionType;

    public Pod(World world,
               int timeToLive,
               PodSize size,
               MotionType motionType) {
        super(world, timeToLive,
                PRIORITY_POD,
                false);
        _size = size;

        if (motionType == null) {
            _motionType = new MotionType(CrobeEnums.MotilityType.NON_MOTILE,
                    PodMotion.NONE,
                    0, World.Direction.NONE);
        }//end if
        else {
            _motionType = motionType;
        }//end else
    }

    @Override
    public void process() {
        if (_motionType.motility == CrobeEnums.MotilityType.MOTILE) {
            int dist = Genomics.random().nextInt(_motionType.speed);
            if (dist == 0) dist++;

            //get the direction
            World.Direction dir = _motionType.direction;
            if (dir == World.Direction.RANDOM) dir = World.randomDirection();

            //reset the direct to random if in pinball mode
            switch (_motionType.motion) {
                case WANDER_PINBALL:
                case LINE_PINBALL:
                    dir = World.Direction.RANDOM;
            }//end switch

            //move base on the motion type
            if (dir != World.Direction.NONE) {
                switch (_motionType.motion) {
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
        sb.append(quotedString("size"));
        sb.append(": ");
        sb.append(quotedString(_size.name()));

        sb.append(", ");
        sb.append(quotedString("motionType"));
        sb.append(": ");
        if (_motionType != null)
            sb.append(_motionType.toJson());
        else
            sb.append("null");

        return sb.toString();
    }
}
