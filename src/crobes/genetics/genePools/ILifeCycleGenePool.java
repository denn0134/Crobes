import java.util.ArrayList;

public interface ILifeCycleGenePool extends IGenePool
{
    ScalarGeneInt span();
    ScalarGeneInt spanRange();
    ScalarGeneFlt maturity();
    HeritableGeneBool finite();
    void finite(HeritableGeneBool value);

    void initializeGenePool(int[] span,
                            int[] spanRange,
                            float[] maturity);
    void initializeReproduction();

    void processAging();
    void processReproduction(ArrayList<Crobe> children);
    void processDeath(CrobeEnums.LifeStage stage);
}
