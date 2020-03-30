import java.util.ArrayList;

public interface IGenePool
{
    String getNamePart();
    GenePool recombinateGenePool(ArrayList<GenePool> genePools);
    void mutate(int stressLevel);
}
