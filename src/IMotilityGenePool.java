public interface IMotilityGenePool extends IGenePool
{
    HeritableGeneEnum motilityType();
    void motilityType(HeritableGeneEnum motilityType);
    HeritableGeneEnum moveType();
    void moveType(HeritableGeneEnum moveType);

    ScalarGeneInt moveBase();
    ScalarGeneInt moveRange();
    ScalarGeneFlt lethargy();
    ScalarGeneFlt efficiency();

    void initializeGenePool(int[] moveBase,
                            int[] moveRange,
                            float[] lethargy,
                            float[] efficiency);

}
