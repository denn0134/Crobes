package crobes.core.factors;

import crobes.core.*;

import java.awt.*;
import java.util.ArrayList;

public class Flow extends Factor
{
    protected static final int PRIORITY_FLOW = 3;

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
    protected int _delay;
    protected World.Direction _direction;
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
        super(world, -1, PRIORITY_FLOW, true);

        _rise = rise;
        _run = run;
        _widthCoefficient = widthCoefficient;
        _speed = speed;
        _delay = _speed;

        if(_run == 0)
            _slope = Double.NaN;
        else
            _slope = (double) _rise / _run;

        if(Double.isNaN(_slope)) {
            _axisOrder = AxisOrder.YX;
            _processOrder = (_rise < 0) ? ProcessOrder.FORWARD: ProcessOrder.REVERSE;
        }//end if
        else if((_slope < 1) && (_slope > -1)) {
            _axisOrder = AxisOrder.XY;
            _processOrder = (_run < 0) ? ProcessOrder.FORWARD : ProcessOrder.REVERSE;
        }//end else if
        else {
            _axisOrder = AxisOrder.YX;
            _processOrder = (_rise < 0) ? ProcessOrder.FORWARD : ProcessOrder.REVERSE;
        }//end else

        //set the drift direction
        if (Math.abs(_slope) < (2.0 / 3.0)) {
            //direction is horizontal
            _direction = (_run < 0) ? World.Direction.LEFT : World.Direction.RIGHT;
        }//end if
        else if (Math.abs(_slope) <= (3.0 / 2.0)) {
            //direction is diagonal
            if (_slope < 0)
                _direction = (_run < 0) ? World.Direction.DOWNLEFT : World.Direction.UPRIGHT;
            else
                _direction = (_run < 0) ? World.Direction.UPLEFT : World.Direction.DOWNRIGHT;
        }//end else if
        else {
            //direction is vertical
            _direction = (_rise < 0) ? World.Direction.UP : World.Direction.DOWN;
        }//end else

        //build the coverage area
        _coverage = new ArrayList<Point>();
    }

    @Override
    public void location(Location location) {
        super.location(location);
        buildCoverage();

        //add the covered locations
        for (Point p: _coverage) {
            Location l = _world.getLocation(p.x, p.y);

            if (!l.factors().contains(this))
                l.factors().add(this);
        }//end for each
    }

    @Override
    public void render(Point location, Lens.Mode mode, RenderContext context) {
        if (mode != Lens.Mode.FLOW) return;

        context.background = CrobeEnums.CrobeColor.skyblue;
        context.foreground = CrobeEnums.CrobeColor.cornflowerblue;

        if (_axisOrder == AxisOrder.XY) {
            if (_processOrder == ProcessOrder.FORWARD)
                context.content = "<";
            else
                context.content = ">";
        }//end if
        else {
            if (_processOrder == ProcessOrder.FORWARD)
                context.content = "˄";
            else
                context.content = "˅";
        }//end if
    }

    @Override
    public void process() {
        _delay--;
        if (_delay == 0) {
            for (Point p : _coverage) {
                Location location = _world.getLocation(p.x, p.y);
                if (location != null)
                    _world.driftList().addDrift(location, Drift.DriftType.FLOW, _direction);
            }//end for each

            _delay = _speed;
        }//end if
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

        if (result > _world.getWidth() - 1) result = -1;

        return result;
    }
    private int getNextY(int index) {
        int result = index;
        if (_processOrder == ProcessOrder.FORWARD)
            result++;
        else
            result--;

        if (result > _world.getHeight() - 1) result = -1;

        return result;
    }
    private Point solveForX(int y, Point p) {
        int x;
        if (Double.isNaN(_slope))
            x = p.x;
        else
            x = (int)((y - p.y + _slope * p.x) / _slope);
        return new Point(x, y);
    }
    private Point solveForY(int x, Point p) {
        int y;
        if (Double.isNaN(_slope))
            y = p.y;
        else
            y = (int)(_slope * (x - p.x) + p.y);
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

    @Override
    public String toString() {
        return super.toString() +
                String.format(" rs=%1$d rn=%2$d wc=%3$d sp=%4$d",
                        _rise, _run, _widthCoefficient, _speed);
    }
}
