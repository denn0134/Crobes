package crobes.genetics.genePools;

import crobes.core.*;
import crobes.genetics.genes.*;
import java.util.ArrayList;

public interface ILifeCycleGenePool extends IGenePool
{
    /***
     * The base life span of the crobe.
     */
    ScalarGeneInt span();

    /***
     * The lifespan variability range for the crobe.
     */
    ScalarGeneInt spanRange();

    /***
     * The maturity age for the crobes expressed as a
     * percentage of the crobe's actual lifespan.
     */
    ScalarGeneFlt maturity();

    /***
     * Determines whether the crobe has a finite lifespan
     * or not.  If true, then the crobe will die after
     * reaching its maximum lifespan; if false then the
     * crobe will simply become frail after reaching the
     * maximum lifespan and will eventually die of natural
     * causes after an indeterminate amount of time.
     */
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
