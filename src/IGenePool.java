import java.util.ArrayList;

public interface IGenePool
{
    String getNamePart();
    GenePool recombinateGenePool(Crobe crobe, ArrayList<GenePool> genePools);
    void mutate(int stressLevel);
}
