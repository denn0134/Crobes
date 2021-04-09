package crobes.genetics.genes;

import crobes.core.*;
import crobes.genetics.genomics.GenomeValue;
import crobes.genetics.gui.GeneEditor;

import java.util.ArrayList;
import java.util.Random;

public abstract class Gene
{
    public abstract CrobeEnums.MutationType[] allowedMutations();
    public abstract Class<? extends GeneEditor> geneEditorClass();
    public abstract Class<? extends GenomeValue> geneValueClass();

    protected String _name;
    public String name() {
        return _name;
    }
    public void name(String name) {
        _name = name;
    }

    protected Random _rand = new Random();

    public Gene() {}

    /***
     * <p>Using an array of parent Gene objects as a starting
     * point, this method determines the inheritance to
     * pass down to the next generation Gene and creates
     * this new Gene.</p>
     * <p>Although the following properties are not fully
     * polymorphic and therefore do not appear here, they
     * are integral to the functioning of the recombinate
     * method and so they will be documented here.</p>
     * <ul>
     *     <li>
     *         genotype:
     *         <p>The genotype property is the
     *         set of genetic values that the Gene possesses.
     *         It is in essence the whole of the Gene's
     *         genetic information.</p>
     *     </li>
     *     <li>
     *         phenotype:
     *         <p>The phenotype is the specific information
     *         which is expressed by the Gene.  This may be
     *         selected from the values within the genotype
     *         or may be calculated from the genotype, this
     *         depends on the class implementation.</p>
     *     </li>
     * </ul>
     * @param genes Array of parent Gene objects.
     * @return Returns a new Gene derived from the parents.
     */
    public abstract Gene recombinate(ArrayList<Gene> genes);
    public Gene combine(ArrayList<Gene> genes) throws IncompatibleGenomeException{
        if(canCombine(genes))
            return recombinate(genes);
        else
            return null;
    }

    protected boolean canCombine(Gene gene) throws IncompatibleGenomeException{
        if(this.getClass() == gene.getClass())
            return true;
        else
            throw new IncompatibleGenomeException(this.getClass().getName(), gene.getClass().getName());

    }
    protected boolean canCombine(ArrayList<Gene> genes) throws IncompatibleGenomeException {
        boolean result = true;

        for (Gene g : genes) {
            result = (result && canCombine(g));
        }//end for g

        return result;
    }

    protected CrobeEnums.MutationType _mutationType;
    public CrobeEnums.MutationType mutationType() {
        return _mutationType;
    }
    public abstract void mutate(int stressLevel);
}
