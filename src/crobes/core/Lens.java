package crobes.core;

import crobes.*;
import java.awt.*;
import java.util.ArrayList;

public class Lens
{
    public enum Mode {
        NONE,
        LIGHT,
        TEMPERATURE,
        FLOW
    }

    public static final String FMT_FONT_STYLE = "<span style='font-family: %1$s; font-size: %2$d'>";
    public static final String FMT_FONT_COLORS = "<span style='color: %1$s; background-color: %2$s'>";
    public static final String END_LINE = "<br>";
    public static final String END_SPAN = "</span>";
    public static final String OUT_OF_BOUNDS_CONTENT = "X";

    private StringBuilder geoCache;

    private Location _selection;
    public Location selection() {
        return _selection;
    }
    public void selection(Location selection) {
        _selection = selection;
    }

    private Point _center;
    public Point center() {
        return _center;
    }
    public void center(Point center) {
        _center = center;
    }

    private Point _origin = new Point();
    public Point origin() {
        return _origin;
    }

    /***
     * The scale to render at; this can be converted
     * to a font size with the formula fs=(scale * 4) + 4
     * so scale 1 = fontsize 8, scale 2 = font size 12
     * etc.
     */
    private int _scale;
    public int scale() {
        return _scale;
    }
    public void scale(int scale) {
        _scale = scale;
    }
    public int fontSize() {
        return (_scale + 1) * 4;
    }

    private int _width;
    public int width() {
        return _width;
    }
    public void width(int width) {
        _width = width;
    }
    public int widthOffset() {
        if((_width % 2) == 0)
            return _width / 2;
        else
            return (_width - 1) / 2;
    }

    private int _height;
    public int height() {
        return _height;
    }
    public void height(int height) {
        _height = height;
    }
    public int heightOffset() {
        if((_height % 2) == 0)
            return _height / 2;
        else
            return (_height - 1) / 2;
    }

    private Mode _mode;
    public Mode mode() {
        return _mode;
    }
    public void mode(Mode mode) {
        _mode = mode;
    }

    private World _world;
    public World world() {
        return _world;
    }

    public Lens(World world) {
        _world = world;

        //set the center
        _center = new Point();
        _center.x = (_world.environment().width() - 1) / 2;
        _center.y = (_world.environment().height() - 1) / 2;

        //default the scale to 2 (font size 12)
        _scale = 2;
    }

    public void renderWorld() {
        geoCache = new StringBuilder();
        geoCache.append(String.format(FMT_FONT_STYLE, Microscope.FONT_NAME, fontSize()));

        CrobeEnums.CrobeColor currentFGround = CrobeEnums.CrobeColor.undefined;
        CrobeEnums.CrobeColor currentBGround = CrobeEnums.CrobeColor.undefined;

        //render the location within the lens view
        _origin.x = _center.x - widthOffset();
        _origin.y = _center.y - heightOffset();
        for(int y = 0; y < _height; y++) {
            int mixelY = _origin.y + y;
            for(int x = 0; x < _width; x++) {
                RenderContext rc;
                int mixelX = _origin.x + x;
                if(_world.inBounds(mixelX, mixelY)) {
                    rc = _world.getLocation(mixelX, mixelY).render(_mode);
                }//end if
                else {
                    rc = new RenderContext();
                    rc.foreground = CrobeEnums.CrobeColor.yellow;
                    rc.background = CrobeEnums.CrobeColor.black;
                    rc.content = OUT_OF_BOUNDS_CONTENT;
                }//end else

                if((currentFGround == CrobeEnums.CrobeColor.undefined) && (currentBGround == CrobeEnums.CrobeColor.undefined)) {
                    geoCache.append(String.format(FMT_FONT_COLORS, rc.foreground, rc.background));
                    geoCache.append(rc.content);
                }//end if
                else if(!rc.matches(currentFGround, currentBGround)) {
                    geoCache.append(END_SPAN);
                    geoCache.append(String.format(FMT_FONT_COLORS, rc.foreground, rc.background));
                }//end else if

                geoCache.append(rc.content);

                currentFGround = rc.foreground;
                currentBGround = rc.background;
            }//end for x

            geoCache.append(END_SPAN);

            if(y != (_height - 1))
                geoCache.append(END_LINE);
            currentFGround = CrobeEnums.CrobeColor.undefined;
            currentBGround = CrobeEnums.CrobeColor.undefined;
        }//end for y

        geoCache.append(END_SPAN);
    }
    public String flush() {
        if(geoCache != null)
            return geoCache.toString();
        else
            return "";
    }
}
