package crobes.core;

import java.util.ArrayList;

/***
 * Collection of crobes.
 */
public class CrobeColony extends ArrayList<Crobe>
{
    @Override
    public boolean add(Crobe crobe) {
        boolean result = super.add(crobe);
        crobe.parent = this;
        return result;
    }

    /***
     * Tells each crobe in the collection to perform its live() functionality.
     */
    public void live() {
        for(Crobe c : this) {
            c.live();
        }//end for c
    }

    /***
     * Tells each crobe in the collection to perform its reproduce() functionality.
     */
    public void reproduce() {
        ArrayList<Crobe> children = new ArrayList<Crobe>();

        for(Crobe c : this) {
            c.reproduce(children);
        }//end for c

        for(Crobe c : children) {
            add(c);
        }//end for c
    }

    /***
     * Removes any crobes from the collection which have died.
     * @return Returns True if any corbes have been removed; False if not.
     */
    public boolean purge() {
        boolean result = false;

        //find any dead crobes in the colony
        ArrayList<Crobe> dead = new ArrayList<Crobe>();
        for(Crobe c : this) {
            if(c.stage() == CrobeEnums.LifeStage.DEAD) {
                dead.add(c);
                result = true;
            }//end if
        }//end for c

        //remove the dead crobes from the world
        for(Crobe c : dead) {
            //first remove the crobe references from
            //any locations they are in
            Location[] locs = c.inhabits();
            for(Location l : locs) {
                l.crobe(null);
            }//end for l

            this.remove(c);
        }//end for c

        return result;
    }
}
