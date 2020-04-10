import java.awt.*;

/***
 * A single location within the world map.
 */
public class Location
{
    private static final CrobeEnums.CrobeColor BG_SELECTED = CrobeEnums.CrobeColor.fuchsia;
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

    public Location(int X, int Y) {
        _point = new Point(X, Y);
    }

    private CrobeEnums.CrobeColor getBackgroundByMode(Lens.Mode mode) {
        CrobeEnums.CrobeColor bg = CrobeEnums.CrobeColor.white;

        switch (mode) {
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
        }//end switch mode

        return bg;
    }

    public RenderContext render(Lens.Mode mode) {
        RenderContext rc = new RenderContext();

        //set the context values based on the environment,
        //crobes and environmental objects in the location
        //first get the background color based on the mode
        rc.background = getBackgroundByMode(mode);

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

    public void setCrobe(Crobe crobe) {
        _crobe = crobe;
        _crobe.locations(new Location[] {this});
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

        if(_crobe != null) {
            sb.append("\n");
            sb.append("Crobe:\n");
            sb.append("  " + _crobe.getTaxa());
        }//end if

        return sb.toString();
    }
}
