import java.util.ArrayList;
import java.util.Random;

public abstract class Gene
{
    protected Random _rand = new Random();
    protected abstract Gene recombinate(ArrayList<Gene> genes);
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

    public abstract void mutate(int stressLevel);
}
