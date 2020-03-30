import java.util.ArrayList;

public abstract class GenePool implements IGenePool
{
    public abstract String getNamePart();
    public abstract GenePool recombinateGenePool(ArrayList<GenePool> genePools);
    public abstract void mutate(int stressLevel);
}
