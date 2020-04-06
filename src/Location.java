import java.awt.*;

/***
 * A single location within the world map.
 */
public class Location
{
    private static final String BG_SELECTED = "fuchsia";
    private static final String BG_LIGHT_0 = "darkgray";
    private static final String BG_LIGHT_1 = "silver";
    private static final String BG_LIGHT_2 = "lightgray";
    private static final String BG_LIGHT_3 = "white";
    private static final String BG_TEMP_0 = "lightblue";
    private static final String BG_TEMP_1 = "pink";
    private static final String BG_TEMP_2 = "lightsalmon";
    private static final String BG_TEMP_3 = "lightcoral";
    private static final String BG_TEMP_4 = "red";

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

    public Location(int X, int Y) {
        _point = new Point(X, Y);
    }

    private String getBackgroundByMode(Lens.Mode mode) {
        String bg = "white";

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
        if(_selected)
            rc.background = BG_SELECTED;
        else
            rc.background = getBackgroundByMode(mode);

        rc.foreground = "black";
        rc.content = "&nbsp;";

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

        return sb.toString();
    }
}
