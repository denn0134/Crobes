package crobes.genetics.genePools;

import crobes.genetics.genes.*;

public interface IMetabolicGenePool extends IGenePool
{
    /***
     * Base health level for the crobe.
     */
    ScalarGeneInt vitality();
    void vitality(ScalarGeneInt value);

    /***
     * The health variability range for the crobe.
     */
    ScalarGeneInt vitalityRange();
    void vitalityRange(ScalarGeneInt value);

    /***
     * Rate of healing for the crobe, expressed as a percentage
     * chance of healing each day.
     */
    ScalarGeneFlt healRate();
    void healRate(ScalarGeneFlt value);

    /***
     * Base energy level for the crobe.
     */
    ScalarGeneInt stamina();
    void stamina(ScalarGeneInt value);

    /***
     * The energy variability range for the crobe.
     */
    ScalarGeneInt staminaRange();
    void staminaRange(ScalarGeneInt value);

    /***
     * Mortality rate - this looks like it needs work
     */
    ScalarGeneInt mortalityRate();
    void mortalityRate(ScalarGeneInt value);

    void initializeGenePool(int[] vitality,
                            int[] vitalityRange,
                            float[] healRate,
                            int[] stamina,
                            int[] staminaRange,
                            int[] mortalityRate);

    void processFeeding();
}
