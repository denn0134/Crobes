public class CrobeEnums
{
    /***
     * Defines the life cycle stage of the crobe.
     */
    public enum LifeStage {
        CHILD,
        ADULT,
        ELDER,
        DEAD
    }

    /***
     * Defines the mechanics of how a gene will
     * mutate in the case that it does.
     */
    public enum MutationType {
        RANDOM,           //pick a random value from the domain
        ADJACENT,         //pick an adjacent value within the dominance chain, for ordinal values pick an adjacent value
        SCALAR_DISCREET   //change the value by a random range
    }

    /***
     * Defines the basic movement abilities of the crobe.
     */
    public enum MotilityType {
        ANCHORED,     //does not move on it's own, does not drift
        NON_MOTILE,   //does not move, can drift
        MOTILE        //can move and drift
    }

    /***
     * Defines the pathing mechanics for movement.
     */
    public enum MovementType {
        CREEPER,      //single direction, tends to low range
        HOPPER,       //single direction, avoids obstacles
        ZIGGER,       //two legs of movement in different directions
        SPRINTER      //single direction, tends to high range
    }
}
