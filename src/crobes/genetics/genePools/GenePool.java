package crobes.genetics.genePools;

import crobes.core.*;

import java.util.ArrayList;

public abstract class GenePool implements IGenePool
{
    public abstract String description();

    protected float getFloatRange(float low, float high) {
        //generates a random float from low to high
        //inclusive to a precision of 1/100.
        float rng = high - low;
        rng = (float)Math.floor(rng * 100.0f) / 100.0f + 0.01f;
        return low + (float)Math.floor(_crobe.rand.nextFloat() * rng * 100.0f) / 100.0f;
    }
    protected int getIntRange(int low, int high) {
        int rng = high - low + 1;
        return low + _crobe.rand.nextInt(rng);
    }

    protected Crobe _crobe;
    public Crobe crobe() {
        return _crobe;
    }
    public void crobe(Crobe crobe) {
        this._crobe = crobe;
    }

    public GenePool(Crobe crobe) {
        _crobe = crobe;
    }

    protected abstract void initializeGeneNames();
    public abstract void initializeRandomDefault();

    public abstract String getNamePart();
    public abstract GenePool recombinateGenePool(Crobe crobe, ArrayList<GenePool> genePools);
    public abstract void mutate(int stressLevel);
}
