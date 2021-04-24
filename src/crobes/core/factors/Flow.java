package crobes.core.factors;

import crobes.core.Location;
import crobes.core.World;

import java.awt.*;
import java.util.ArrayList;

public class Flow extends Factor
{
    protected enum AxisOrder {
        XY, YX
    }
    protected enum ProcessOrder {
        FORWARD, REVERSE
    }

    protected int _rise;
    protected int _run;
    protected int _widthCoefficient;
    protected int _speed;
    protected ArrayList<Point> _coverage;
    protected AxisOrder _axisOrder = AxisOrder.XY;
    protected ProcessOrder _processOrder = ProcessOrder.FORWARD;
    protected double _slope;
    protected Point _pointA, _pointB;

    public Flow(World world,
                int rise,
                int run,
                int widthCoefficient,
                int speed) {
        super(world, -1, 2, true);

        _rise = rise;
        _run = run;
        _widthCoefficient = widthCoefficient;
        _speed = speed;

        if(_run == 0)
            _slope = Double.NaN;
        else
            _slope = (double) _rise / _run;

        if(_slope == Double.NaN) {
            _axisOrder = AxisOrder.YX;
            _processOrder = (_rise < 0 ? ProcessOrder.FORWARD: ProcessOrder.REVERSE);
        }//end if
        else if((_slope < 1) && (_slope > -1)) {
            _axisOrder = AxisOrder.XY;
            _processOrder = (_run < 0) ? ProcessOrder.FORWARD : ProcessOrder.REVERSE;
        }//end else if
        else {
            _axisOrder = AxisOrder.YX;
            _processOrder = (_rise < 0) ? ProcessOrder.FORWARD : ProcessOrder.REVERSE;
        }//end else

        //build the coverage area
        _coverage = new ArrayList<Point>();
    }

    @Override
    public void location(Location location) {
        super.location(location);
        buildCoverage();
    }

    private void buildCoverage() {
        int indexX, indexY;
        Point low, high;

        //setup the base points
        _pointA = new Point(_location.point());
        _pointB = new Point(_location.point());
        if (_axisOrder == AxisOrder.XY) {
            _pointA.y -= _widthCoefficient;
            _pointB.y += _widthCoefficient;
        }//end if
        else {
            _pointA.x -= _widthCoefficient;
            _pointB.x += _widthCoefficient;
        }//end else

        _coverage.clear();
        if (_axisOrder == AxisOrder.XY) {
            //get the starting index
            indexX = getFirstX();

            while (indexX != -1) {
                //solve for y
                low = solveForY(indexX, _pointA);
                high = solveForY(indexX, _pointB);

                //check for swap
                if (low.y > high.y) {
                    Point p = low;
                    low = high;
                    high = p;
                }//end if

                //loop through the range of y values
                for (indexY = low.y; indexY <= high.y; indexY++) {
                    if (indexY < 0) continue;
                    if (indexY > _world.getHeight() - 1) continue;

                    _coverage.add(new Point(indexX, indexY));
                }//end for indexY

                indexX = getNextX(indexX);
            }//end while
        }//end if
        else {
            //get the starting index
            indexY = getFirstY();

            while (indexY != -1) {
                //solve for x
                low = solveForX(indexY, _pointA);
                high = solveForX(indexY, _pointB);

                //check for swap
                if (low.x > high.x) {
                    Point p = low;
                    low = high;
                    high = p;
                }//end if

                //loop through the x values
                for (indexX = low.x; indexX <= high.x; indexX++) {
                    if (indexX < 0) continue;
                    if (indexX > _world.getWidth() - 1) continue;

                    _coverage.add(new Point(indexX, indexY));
                }//end for indexX

                indexY = getNextY(indexY);
            }//end while
        }//end else
    }

    //utility methods
    private int getFirstX() {
        if (_processOrder == ProcessOrder.FORWARD)
            return 0;
        else
            return _world.getWidth() - 1;
    }
    private int getFirstY() {
        if (_processOrder == ProcessOrder.FORWARD)
            return 0;
        else
            return _world.getHeight() - 1;
    }
    private int getNextX(int index) {
        int result = index;
        if (_processOrder == ProcessOrder.FORWARD)
            result++;
        else
            result--;

        if (result > _world.getWidth()) result = -1;

        return result;
    }
    private int getNextY(int index) {
        int result = index;
        if (_processOrder == ProcessOrder.FORWARD)
            result++;
        else
            result--;

        if (result > _world.getHeight()) result = -1;

        return result;
    }
    private Point solveForX(int y, Point p) {
        int x = (int)((y - p.y + _slope * p.x) / _slope);
        return new Point(x, y);
    }
    private Point solveForY(int x, Point p) {
        int y = (int)(_slope * (x - p.x) + p.y);
        return new Point(x, y);
    }

    @Override
    public String toJson() {
        StringBuilder sb = new StringBuilder();

        sb.append("{");

        sb.append(quotedString("Class"));
        sb.append(": ");
        sb.append(quotedString(this.getClass().getSimpleName()));
        sb.append(", ");

        sb.append(super.toJson());

        sb.append(", ");

        sb.append(quotedString("rise"));
        sb.append(": ");
        sb.append(_rise);
        sb.append(", ");

        sb.append(quotedString("run"));
        sb.append(": ");
        sb.append(_run);
        sb.append(", ");

        sb.append(quotedString("widthCoefficient"));
        sb.append(": ");
        sb.append(_widthCoefficient);
        sb.append(", ");

        sb.append(quotedString("speed"));
        sb.append(": ");
        sb.append(_speed);
        sb.append(", ");

        sb.append(quotedString("slope"));
        sb.append(": ");
        sb.append(_slope);
        sb.append(", ");

        sb.append(quotedString("axisOrder"));
        sb.append(": ");
        sb.append(quotedString(_axisOrder.name()));
        sb.append(", ");

        sb.append(quotedString("processOrder"));
        sb.append(": ");
        sb.append(quotedString(_processOrder.name()));
        sb.append(", ");

        sb.append(quotedString("pointA"));
        sb.append(": ");
        sb.append(pointJson(_pointA));
        sb.append(", ");

        sb.append(quotedString("pointB"));
        sb.append(": ");
        sb.append(pointJson(_pointB));
        sb.append(", ");

        sb.append(quotedString("coverage"));
        sb.append(": ");
        sb.append("[");

        for (int i = 0; i < _coverage.size(); i++) {
            if (i > 0)
                sb.append(", ");

            sb.append(pointJson(_coverage.get(i)));
        }//end for i

        sb.append("]}");

        return sb.toString();
    }
}
