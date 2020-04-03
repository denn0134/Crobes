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

    public enum MotilityType {
        ANCHORED, NON_MOTILE, MOTILE
    }
}
