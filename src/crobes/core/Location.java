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
    private boolean _selected;
    private Elements _elements;
    private Crobe _crobe;
    private ArrayList<Factor> _factors;

    /***
     * The coordinates of the Location in the World.
     * @return Returns the Point coordinates of the
     * Location within the World.
     */
    public Point point() {
        return _point;
    }
    /***
     * The selected state of the location.
     * @return Returnms the selected state of the Location.
     */
    public boolean selected() {
        return _selected;
    }
    /***
     * Sets the selected state of the Location.
     * @param selected The selected state of the Location.
     */
    public void selected(boolean selected) {
        _selected = selected;
    }
    /***
     * The evironmental conditions within the Location.
     * @return Returns the Elements of the Location.
     */
    public Elements elements() {
        return _elements;
    }

    /***
     * The single Crobe (if any) which exists in this
     * Location.
     * @return Returns the Crobes which exists in this
     * Location; returns null if there is no Crobe
     * within the Location.
     */
    public Crobe crobe() {
        return _crobe;
    }
    /***
     * Sets the Crobe which exists within this Location.
     * @param crobe The Crobe.
     */
    public void crobe(Crobe crobe) {
        _crobe = crobe;
    }
    /***
     * Environmental Factors which effect this Location.
     * @return Returns a list of Factors which have an
     * effect within this Location.
     */
    public ArrayList<Factor> factors() {
        return _factors;
    }

    public Location(int X, int Y) {
        _point = new Point(X, Y);
        _factors = new ArrayList<Factor>();
        _elements = new Elements();
    }

    private CrobeEnums.CrobeColor getBackgroundByMode(Lens.Mode mode) {
        CrobeEnums.CrobeColor bg = CrobeEnums.CrobeColor.white;

        switch (mode) {
            case NONE:
                bg = BG_NONE;
                break;
            case LIGHT:
                switch (_elements.lightLevel()) {
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
                switch (_elements.temperatureLevel()) {
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
            case FACTORS:
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
        rc.foreground = CrobeEnums.CrobeColor.black;
        rc.content = "&nbsp;";

        if(_factors.size() > 0) {
            RenderContext frc = new RenderContext(rc);

            //get the factor with the lowest priority
            Factor factor = null;
            for(int i = 0; i < _factors.size(); i++) {
                if((factor == null) ||
                        (_factors.get(i).priority() < factor.priority())) {
                    factor = _factors.get(i);
                }//end if
            }//end for i

            factor.render(point(), mode, frc);

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

        //if the location is selected set the background
        if(selected()) {
            rc.background = CrobeEnums.CrobeColor.fuchsia;
        }//end if

        return rc;
    }

    public void drift(World.Direction direction) {
        //drift crobes first
        if (_crobe != null)
            _crobe.world().move(_crobe, direction);

        //then factors
        /*
        for (Factor f: _factors) {
            if (f.world().move(f, direction) == -1) {
                //remove the Factor if it has left the World
                f.world().remove(f);
            }//end if
        }//end if
        */
        for (int i = 0; i< _factors.size(); i++) {
            if (_factors.get(i).world().move(_factors.get(i), direction) == -1) {
                //remove the Factor if it has left the World
            	_factors.get(i).world().remove(_factors.get(i));
            }//end if
        }
        
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append(String.format("Location ( %1$d, %2$d )", _point.x, _point.y));
        sb.append("\n");

        sb.append(_elements.toString());

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
