package crobes.core;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class Environment extends ArrayList<ArrayList<Location>>
{
    private Random rand = new Random();

    private int _radix;
    public int radix() {
        return _radix;
    }
    private int _width;
    public int width() {
        return _width;
    }
    private int _height;
    public int height() {
        return _height;
    }

    public Environment(int radix) {
        //set the width and height of the environment
        _radix = radix;
        _width = (4 * _radix) - 1;
        _height = (4 * _radix) - 1;

        for(int i = 0; i < _height; i++) {
            ArrayList<Location> row = new ArrayList<Location>();
            add(row);
            for(int j = 0; j < _width; j++) {
                Location loc = new Location(j, i);
                row.add(loc);
            }//end for j
        }//end for i
    }

    public Location get(int X, int Y) {
        return get(Y).get(X);
    }
    public Location get(Point p) {
        return get(p.x, p.y);
    }

    private boolean inBounds(int row, int col) {
        boolean result = true;

        if((row < 0) || (col < 0))
            result = false;

        if((row >= _width) || (col >= _height))
            result = false;

        return result;
    }
    private int getOffset(int offset) {
        return rand.nextInt((2 * offset) + 1) - offset;

    }

    public void setAmbience() {
        Point p;
        //first quadrant
        p = new Point(_radix - 1, _radix - 1);
        setAmbientLightSource(p);
        setAmbientThermalZone(p);

        //second quadrant
        p = new Point((3 * _radix) - 1, _radix - 1);
        setAmbientLightSource(p);
        setAmbientThermalZone(p);

        //third quadrant
        p = new Point(_radix - 1, (3 * _radix) - 1);
        setAmbientLightSource(p);
        setAmbientThermalZone(p);

        //fourth quadrant
        p = new Point((3 * _radix) - 1, (3 * _radix) - 1);
        setAmbientLightSource(p);
        setAmbientThermalZone(p);

        //center
        p = new Point((2 * _radix) - 1, (2 * _radix) - 1);
        setAmbientLightSource(p);
        setAmbientThermalZone(p);
    }
    private void setAmbientLightSource(Point p) {
        //determine the variables
        //bRadius is the base radius of the light
        //offset determines how much the radius varies from the base
        //radius is the final radius of the light
        //light is the light intensity
        int bRadius, offset, radius, light;
        bRadius = _radix - 1;
        offset = (int)(Math.ceil(_radix / 3.0));
        radius = bRadius + getOffset(offset);

        //light intensity is skewed to the low end
        int v = rand.nextInt(6);
        switch (v) {
            case 3:
            case 4:
                light = 2;
                break;
            case 5:
                light = 3;
                break;
            default:
                light = 1;
        }//end switch

        //get the offset point
        Point lightSource = new Point();
        lightSource.x = p.x += getOffset(offset);
        lightSource.y = p.y += getOffset(offset);

        //now determine the radii of the light areas
        //if the radius is less than twice the light
        //then reduce the light
        while (radius < (light * 2)) {
            light--;
        }//end while

        int[] lightRad = new int[light];
        int idx = 1;
        for(int i = light; i > 0; i--) {
            double div = (double)i / (double)light;
            lightRad[idx - 1] = (int)Math.round(radius * div);
            idx++;
        }//end for i

        for(int i = 0; i < light; i++) {
            radius = lightRad[i];
            int diameter = (2 * radius) + 1;
            int lightLevel = i + 1;

            int col, row;
            col = lightSource.x - radius;
            row = lightSource.y - radius;
            for(int j = 0; j < diameter; j++) {
                for(int k = 0; k < diameter; k++) {
                    int c = col + k;
                    int r = row + j;
                    if(inBounds(c, r)) {
                        double dist = Math.sqrt((Math.pow(c - lightSource.x, 2) + Math.pow(r - lightSource.y, 2)));
                        if (dist <= radius) {
                            Location l = get(c, r);
                            l.elements().ambientLight(lightLevel);
                        }//end if
                    }//end if
                }//end for k
            }//end for j
        }//end for i
    }
    private void setAmbientThermalZone(Point p) {
        //there is a 40% chance that this thermal
        //zone is not created
        int exists = rand.nextInt(10);
        if(exists < 4)
            return;

        //set the variables
        //range is the offset range for the zone position
        //radius is radius of the zone
        //intensity is the thermal intensity of the zone
        int range, radius, intensity;
        range = (int)Math.round(_radix * 1.1);

        //the formula for the radius is complex
        //but is designed to keep thermal zones reasonably
        //small and their size be somewhat dependant on
        //the radix value
        //2d4 + 1dRadix - 8 (reflect any values below 3)
        radius = rand.nextInt(4) + 1;
        radius += rand.nextInt(4) + 1;
        radius += rand.nextInt(_radix) + 1;
        if(radius < 11)
            radius = Math.abs(radius - 8) + 3;
        else
            radius -= 8;

        //thermal intensity is skewed to lower levels
        int v = rand.nextInt(9);
        switch (v) {
            case 3:
            case 4:
            case 5:
                intensity = 2;
                break;
            case 6:
            case 7:
                intensity = 3;
                break;
            case 8:
                intensity = 4;

            default:
                intensity = 1;
        }//end switch

        Point thermalCenter = new Point();
        thermalCenter.x = p.x + getOffset(range);
        thermalCenter.y = p.y + getOffset(range);

        for(int heat = 1; heat <= intensity; heat++) {
            int bleed = (intensity - heat);
            int diameter = (2 * (radius + bleed)) + 1;

            int col, row;
            col = thermalCenter.x - (radius + bleed);
            row = thermalCenter.y - (radius + bleed);
            for(int i = 0; i < diameter; i++) {
                for(int j = 0; j < diameter; j++) {
                    int c = col + j;
                    int r = row + i;
                    if(inBounds(c, r)) {
                        double dist = Math.sqrt(Math.pow(c - thermalCenter.x, 2) + Math.pow(r - thermalCenter.y, 2));
                        if(dist < (radius + bleed)) {
                            Location loc = get(c, r);
                            loc.elements().ambientTemperature(heat);
                        }//end if
                    }//end if
                }//end for j
            }//end for i
        }//end for heat
    }
}
