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
    }
}
