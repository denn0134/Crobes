package crobes.core.factors;

import crobes.core.Location;
import crobes.core.World;
import crobes.core.factors.gui.PodEditor;
import crobes.core.factors.gui.ThermoPodEditor;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/***
 * A Pod type Factor which modifies the temperature
 * of the World in its vicinity.  Depending on its
 * thermal value it will radiate heat/cold within a
 * given range.
 */
public class ThermoPod extends Pod
{
    public static int intensityRange(int intensity,
                                     int heat,
                                     int range,
                                     PodSize size) {
        int center;
        if (size == PodSize.LARGE)
            center = 2;
        else if (size == PodSize.MEDIUM)
            center = 1;
        else
            center = 0;

        return (range + (intensity - heat)) + center;
    }

    private int _thermalValue;
    private int _range;

    /***
     * The thermal potential of the pod, the magnitude
     * of this value will drop as the range increases.
     * @return Returns the thermal value.
     */
    public int thermalValue() {
        return _thermalValue;
    }
    /***
     * The range of efficacy of the Pod.
     * @return Returns the range of efficacy.
     */
    public int range() {
        return _range;
    }

    public ThermoPod(World world,
                     int timeToLive,
                     PodSize size,
                     MotionType motionType,
                     int thermalValue,
                     int range) {
        super(world, timeToLive, size, motionType);

        _thermalValue = thermalValue;
        _range = range;
    }

    @Override
    public void process() {
        super.process();

        //update Location temps within range
        Point point = _location.point();

        //get the radii of the intensity zones
        int[] radii = new int[_thermalValue];
        for (int i = 0; i < _thermalValue; i++) {
            radii[i] = intensityRange(_thermalValue, i + 1, _range, _size);
        }//end for i

        //get a list of all the locations within the
        //maximum range (minimum heat)
        ArrayList<Location> locations = _world.getInRange(point, radii[0]);
        for (Location l: locations) {
            Point lPoint = l.point();
            double dist = _world.distance(point, lPoint);

            for (int i = _thermalValue - 1; i >= 0; i--) {
                if (dist < radii[i]) {
                    l.elements().environmentalTemperature(i + 1);
                    break;
                }//end for i
            }//end for i
        }//end for each
    }

    @Override
    public String toJson() {
        StringBuilder sb = new StringBuilder();

        sb.append("{");

        sb.append(super.toJson());

        sb.append(", ");
        sb.append(quotedString("thermalValue"));
        sb.append(": ");
        sb.append(_thermalValue);

        sb.append(", ");
        sb.append(quotedString("range"));
        sb.append(": ");
        sb.append(_range);

        sb.append("}");

        return sb.toString();
    }
}
