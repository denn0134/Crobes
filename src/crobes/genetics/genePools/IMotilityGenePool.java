package crobes.genetics.genePools;

import crobes.genetics.genes.*;

public interface IMotilityGenePool extends IGenePool
{
    /***
     * Base movement capabilities of the crobe. Enumerated.
     */
    HeritableGeneEnum motilityType();
    void motilityType(HeritableGeneEnum motilityType);

    /***
     * Movement mechanics class for the crobe. Enumerated.
     */
    HeritableGeneEnum moveType();
    void moveType(HeritableGeneEnum moveType);

    /***
     * Base movement rate for the crobe.
     */
    ScalarGeneInt moveBase();
    void moveBase(ScalarGeneInt value);

    /***
     * Movement rate variability range for the crobe.
     */
    ScalarGeneInt moveRange();
    void moveRange(ScalarGeneInt value);

    /***
     * Chance of crobe not moving at all on a given day.
     */
    ScalarGeneFlt lethargy();
    void lethargy(ScalarGeneFlt value);

    /***
     * Rate of exchange from energy to movement.
     */
    ScalarGeneFlt efficiency();
    void efficiency(ScalarGeneFlt value);

    void initializeGenePool(int[] moveBase,
                            int[] moveRange,
                            float[] lethargy,
                            float[] efficiency);

}
