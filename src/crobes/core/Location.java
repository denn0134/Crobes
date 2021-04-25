package crobes.core;

import crobes.core.factors.Factor;

import java.awt.*;
import java.util.ArrayList;

/***
 * A single location within the world map.
 */
public class Location
{
    private static final CrobeEnums.CrobeColor BG_SELECTED = CrobeEnums.CrobeColor.fuchsia;
    private static final CrobeEnums.CrobeColor BG_NONE = CrobeEnums.CrobeColor.lightgray;
    private static final CrobeEnums.CrobeColor BG_LIGHT_0 = CrobeEnums.CrobeColor.darkgray;
    private static final CrobeEnums.CrobeColor BG_LIGHT_1 = CrobeEnums.CrobeColor.silver;
    private static final CrobeEnums.CrobeColor BG_LIGHT_2 = CrobeEnums.CrobeColor.lightgray;
    private static final CrobeEnums.CrobeColor BG_LIGHT_3 = CrobeEnums.CrobeColor.white;
    private static final CrobeEnums.CrobeColor BG_TEMP_0 = CrobeEnums.CrobeColor.lightblue;
    private static final CrobeEnums.CrobeColor BG_TEMP_1 = CrobeEnums.CrobeColor.pink;
    private static final CrobeEnums.CrobeColor BG_TEMP_2 = CrobeEnums.CrobeColor.lightsalmon;
    private static final CrobeEnums.CrobeColor BG_TEMP_3 = CrobeEnums.CrobeColor.lightcoral;
    private static final CrobeEnums.CrobeColor BG_TEMP_4 = CrobeEnums.CrobeColor.red;


    private Point _point;
    public Point point() {
        return _point;
    }

    private boolean _selected;
    public boolean selected() {
        return _selected;
    }
    public void selected(boolean selected) {
        _selected = selected;
    }

    private int _ambientLight;
    public int ambientLight() {
        return _ambientLight;
    }
    public void ambientLight(int light) {
        _ambientLight = light;
    }
    private int _lightLevel;
    public int lightLevel() {
        return _lightLevel;
    }
    public void lightLevel(int light) {
        _lightLevel = light;
    }

    private int _ambientHeat;
    public int ambientHeat() {
        return _ambientHeat;
    }
    public void ambientHeat(int ambientHeat) {
        _ambientHeat = ambientHeat;
    }
    private int _thermalLevel;
    public int thermalLevel() {
        return _thermalLevel;
    }
    public void thermalLevel(int thermalLevel) {
        _thermalLevel = thermalLevel;
    }

    private Crobe _crobe;
    public Crobe crobe() {
        return _crobe;
    }
    public void crobe(Crobe crobe) {
        _crobe = crobe;
    }

    private ArrayList<Factor> _factors;
    public ArrayList<Factor> factors() {
        return _factors;
    }

    public Location(int X, int Y) {
        _point = new Point(X, Y);
        _factors = new ArrayList<Factor>();
    }

    private CrobeEnums.CrobeColor getBackgroundByMode(Lens.Mode mode) {
        CrobeEnums.CrobeColor bg = CrobeEnums.CrobeColor.white;

        switch (mode) {
            case NONE:
                bg = BG_NONE;
                break;
            case LIGHT:
                switch (_lightLevel) {
                    case 0:
                        bg = BG_LIGHT_0;
                        break;
                    case 1:
                        bg = BG_LIGHT_1;
                        break;
                    case 2:
                        bg = BG_LIGHT_2;
                        break;
                    case 3:
                        bg = BG_LIGHT_3;
                        break;
                }//end switch lightLevel
                break;
            case TEMPERATURE:
                switch (_thermalLevel) {
                    case 0:
                        bg = BG_TEMP_0;
                        break;
                    case 1:
                        bg = BG_TEMP_1;
                        break;
                    case 2:
                        bg = BG_TEMP_2;
                        break;
                    case 3:
                        bg = BG_TEMP_3;
                        break;
                    case 4:
                        bg = BG_TEMP_4;
                        break;
                }//end switch tempLevel
                break;
            case FLOW:
                bg = BG_NONE;
                break;
        }//end switch mode

        return bg;
    }

    public boolean blocking() {
        boolean result = false;

        //check if there is a crobe in the location
        if(_crobe != null) {
            result = true;
        }//end if
        else {
            for(Factor f: _factors) {
                if(f.blocking()) {
                    result = true;
                    break;
                }//end if
            }//end for each
        }//end else

        return result;
    }

    public RenderContext render(Lens.Mode mode) {
        RenderContext rc = new RenderContext();

        //set the context values based on the environment,
        //crobes and environmental objects in the location
        //first get the background color based on the mode
        rc.background = getBackgroundByMode(mode);

        if(_factors.size() > 0) {
            RenderContext frc = new RenderContext();

            //get the factor with the lowest priority
            Factor factor = null;
            for(int i = 0; i < _factors.size(); i++) {
                if((factor == null) ||
                        (_factors.get(i).priority() < factor.priority())) {
                    factor = _factors.get(i);
                }//end if
            }//end for i

            factor.render(point(), frc);

            rc.foreground = frc.foreground;
            rc.content = frc.content;
            rc.background = frc.background;
        }//end if

        if(_crobe != null) {
            RenderContext crc = new RenderContext();
            _crobe.renderer().renderCrobe(point(), crc);

            rc.foreground = crc.foreground;
            rc.content = crc.content;
            rc.background = crc.background;
        }//end if
        else {
            rc.foreground = CrobeEnums.CrobeColor.black;
            rc.content = "&nbsp;";
        }//end else

        //if the location is selected set the background
        if(selected()) {
            rc.background = CrobeEnums.CrobeColor.fuchsia;
        }//end if

        return rc;
    }

    public void reset() {
        _lightLevel = 0;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append(String.format("Location ( %1$d, %2$d )", _point.x, _point.y));
        sb.append("\n");
        sb.append("====================\n");
        sb.append("Light level:\n");
        sb.append(String.format("  amb: %1$d  cur: %2$d", _ambientLight, _lightLevel));
        sb.append("\n");
        sb.append("Thermal level:\n");
        sb.append(String.format("  amb: %1$d  cur: %2$d", _ambientHeat, _thermalLevel));
        sb.append("\n");

        if(_factors.size() > 0) {
            sb.append("====================\n");
            sb.append("Factors:\n");
            for(Factor f: _factors)
                sb.append(f.toString());
        }//end if

        if(_crobe != null) {
            sb.append("====================\n");
            sb.append("Crobe:\n");
            sb.append("  " + _crobe.getTaxa());
        }//end if

        return sb.toString();
    }
}
