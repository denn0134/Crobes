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
