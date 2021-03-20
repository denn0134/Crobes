package crobes.genetics.genePools;

import crobes.core.*;
import crobes.genetics.genePools.GenePool;

import java.util.ArrayList;

public interface IGenePool
{
    /***
     * Gets the gene pools name part for use
     * in the Taxanomic name.
     * @return Returns the taxa name part for this
     * gene pool.
     */
    String getNamePart();

    /***
     * Builds a chile gene pool based on parentage
     * from this and a list of other gene pools.
     * @param crobe The child crobe.
     * @param genePools List of other parent gene pools.
     * @return Returns the child gene pool.
     */
    GenePool recombinateGenePool(Crobe crobe, ArrayList<GenePool> genePools);

    /***
     * Checks this gene pool to see if any mutations
     * occur.  This should only be called once when
     * this gene pool is first recombinated.
     * @param stressLevel The current stress level of
     *                    the carrying parent.
     */
    void mutate(int stressLevel);

    /***
     * Initializes the gene pool from default ranges
     * specific to the gene pool class.  This will only
     * work if the genes are all uninstantiated,
     * otherwise this does nothing.
     */
    void initializeRandomDefault();
}
