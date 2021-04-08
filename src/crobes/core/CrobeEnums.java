package crobes.core;

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

    /***
     * Enumeration of colors.
     */
    public enum CrobeColor {
        undefined,
        //pinks
        pink, lightpink, hotpink, deeppink, palevioletred,
        mediumvioletred,
        //reds
        lightsalmon, salmon, darksalmon, lightcoral,
        indianred, crimson, firebrick, darkred, red,
        //oranges
        orangered, tomato, coral, darkorange, orange,
        //yellows
        yellow, lightyellow, lemonchiffon, lightgoldenrodyellow,
        papayawhip, moccasin, peachpuff, palegoldenrod, khaki,
        darkkhaki, gold,
        //browns
        cornsilk, blanchedalmond, bisque, navajowhite, wheat,
        burlywood, tan, rosybrown, sandybrown, goldenrod,
        darkgoldenrod, peru, chocolate, saddlebrown, sienna,
        brown, maroon,
        //purples
        lavender, thistle, plum, violet, orchid, fuchsia,
        magenta, mediumorchid, mediumpurple, blueviolet,
        darkviolet, darkorchid, darkmagenta, purple, indigo,
        darkslateblue, slateblue, mediumslateblue,
        //whites
        white, snow, honeydew, mintcream, azure, aliceblue,
        ghostwhite, whitesmoke, seashell, beige, oldlace,
        floralwhite, ivory, antiquewhite, linen,
        lavenderblush, mistyrose,
        //greys and blacks
        gainsboro, lightgray, silver, darkgray, gray,
        dimgray, lightslategray, slategray,
        darkslategray, black,
        //greens
        darkolivegreen, olive, olivedrab, yellowgreen,
        limegreen, lime, lawngreen, chartreuse, greenyellow,
        springgreen, mediumspringgreen, lightgreen,
        palegreen, darkseagreen, mediumaquamarine,
        mediumseagreen, seagreen, forestgreen, green,
        darkgreen,
        //cyans
        aqua, cyan, lightcyan, paleturquoise, aquamarine,
        turquoise, mediumturquoise, darkturquoise,
        lightseagreen, cadetblue, darkcyan, teal,
        //blues
        lightsteelblue, powderblue, lightblue, skyblue,
        lightskyblue, deepskyblue, dodgerblue,
        cornflowerblue, steelblue, royalblue, blue,
        mediumblue, darkblue, navy, midnightblue
    }

    public static Enum getEnum(String enumName, String name) {
        Enum result = null;

        if(enumName.equalsIgnoreCase(MotilityType.class.getSimpleName()))
            result = MotilityType.valueOf(name);
        else if(enumName.equalsIgnoreCase(MovementType.class.getSimpleName()))
            result = MovementType.valueOf(name);
        else if(enumName.equalsIgnoreCase(CrobeColor.class.getSimpleName()))
            result = CrobeColor.valueOf(name);

        return result;
    }
}
