package tests;

import crobes.core.World;
import crobes.core.factors.Corpse;
import crobes.core.factors.Factor;
import crobes.core.factors.Flow;

public class FactorTest
{
    public static void main(String[] args) {
        World world = new World(5);

        Factor f = new Corpse(world, 3, 1, true);
        f.location(world.getLocation(5,5));

        System.out.println(f.toJson());

        f = new Flow(world, 1, 2, 1, 1);
        f.location(world.getLocation(10, 10));

        System.out.println(f.toJson());


        System.out.println("1/2: " + getAngle(1, 2));
        System.out.println("1/-2: " + getAngle(1, -2));
        System.out.println("-1/-2: " + getAngle(-1, -2));
        System.out.println("-1/2: " + getAngle(-1, 2));
    }

    private static double getAngle(int rise, int run) {
        double rad = Math.asin(rise / Math.sqrt(Math.pow(rise, 2.0) + Math.pow(run, 2.0)));
        double mod;

        if (rise < 0) {
            if (run < 0)
                mod = Math.PI;
            else
                mod = 3 * Math.PI / 2;
        }//end if
        else {
            if (run < 0)
                mod = Math.PI / 2;
            else
                mod = 0.0;
        }//end else

        rad = Math.abs(rad);
        return Math.toDegrees(rad + mod);
    }
}
