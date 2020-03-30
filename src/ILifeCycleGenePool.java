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
    void initializeReproduction(Crobe crobe);

    void processAging(Crobe crobe);
    void processReproduction(Crobe crobe, ArrayList<Crobe> children);
}
