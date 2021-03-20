package crobes.genetics.genePools;

import crobes.genetics.*;
import crobes.genetics.genes.*;

public interface IMetabolicGenePool extends IGenePool
{
    /***
     * Base health level for the crobe.
     */
    ScalarGeneInt vitality();

    /***
     * The health variability range for the crobe.
     */
    ScalarGeneInt vitalityRange();

    /***
     * Rate of healing for the crobe, expressed as a percentage
     * chance of healing each day.
     */
    ScalarGeneFlt healRate();

    /***
     * Base energy level for the crobe.
     */
    ScalarGeneInt stamina();

    /***
     * The enregy variability range for the crobe.
     */
    ScalarGeneInt staminaRange();

    /***
     * Mortality rate - this looks like it needs work
     */
    ScalarGeneInt mortalityRate();

    void initializeGenePool(int[] vitality,
                            int[] vitalityRange,
                            float[] healRate,
                            int[] stamina,
                            int[] staminaRange,
                            int[] mortalityRate);

    void processFeeding();
}
