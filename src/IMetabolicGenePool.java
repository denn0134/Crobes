public interface IMetabolicGenePool extends IGenePool
{
    ScalarGeneInt vitality();
    ScalarGeneInt vitalityRange();
    ScalarGeneFlt healRate();
    ScalarGeneInt stamina();
    ScalarGeneInt staminaRange();
    ScalarGeneInt mortalityRate();

    void initializeGenePool(int[] vitality,
                            int[] vitalityRange,
                            float[] healRate,
                            int[] stamina,
                            int[] staminaRange,
                            int[] mortalityRate);

    void processFeeding(Crobe crobe);
}
