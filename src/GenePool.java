import java.util.ArrayList;

public abstract class GenePool implements IGenePool
{
    protected Crobe _crobe;

    public GenePool(Crobe crobe) {
        _crobe = crobe;
    }

    public abstract String getNamePart();
    public abstract GenePool recombinateGenePool(Crobe crobe, ArrayList<GenePool> genePools);
    public abstract void mutate(int stressLevel);
}
