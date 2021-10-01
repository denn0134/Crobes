package crobes.genetics.genomics;

/***
 * A Genome is a map of the genetic material of a crobe.
 * This class is used to put this genetic information
 * into a format which is more compatible with UI elements.
 */
public class Genome
{
    private GenomeLifeCycle _lifeCycle;
    public GenomeLifeCycle lifeCycle() {
        return _lifeCycle;
    }
    private GenomeMetabolism _metabolism;
    public  GenomeMetabolism metabolism() {
        return _metabolism;
    }
    private GenomeMotility _motility;
    public GenomeMotility motility() {
        return _motility;
    }
    private GenomeRenderer _renderer;
    public GenomeRenderer renderer() {
        return _renderer;
    }

    public static String quotedString(String str) {
        return "\"" + str + "\"";
    }

    public Genome() {
        _lifeCycle = new GenomeLifeCycle();
        _metabolism = new GenomeMetabolism();
        _motility = new GenomeMotility();
        _renderer = new GenomeRenderer();
    }

    /***
     * Calulates the taxonomic name of the genome.
     * @return Returns the taxonomic name of the genome, which is made up
     * from the taxonomies of the constituent genePools.
     */
    public String getTaxonomy() {
        return String.format("%1$s %2$s %3$s %4$s",
                _lifeCycle.getTaxa(),
                _metabolism.getTaxa(),
                _motility.getTaxa(),
                _renderer.getTaxa());
    }

    /***
     *
     * @param randomize
     */
    public void initRandom(boolean randomize) {
        _lifeCycle.initRandom(randomize);
        _metabolism.initRandom(randomize);
        _motility.initRandom(randomize);
        _renderer.initRandom(randomize);
    }

    /***
     * Determines if this Genome is allowed to randomly generated.
     * @return Returns True if the Genome can be randomly generated; False if not.
     */
    public boolean randomizable() {
        if(_lifeCycle.randomizable())
            return true;
        if(_metabolism.randomizable())
            return true;
        if(_motility.randomizable())
            return true;
        if(_renderer.randomizable())
            return true;

        return false;
    }

    @Override
    public String toString() {
        return toJson();
    }

    /***
     * Serializes the Genome to a Json string.
     * @return Returns the serialization of the genome as a Json string.
     */
    String toJson() {
        StringBuilder sb = new StringBuilder();

        sb.append("{");
        sb.append(Genome.quotedString("lifeCycle"));
        sb.append(": ");
        sb.append(_lifeCycle.toJson());
        sb.append(", ");
        sb.append(Genome.quotedString("metabolism"));
        sb.append(": ");
        sb.append(_metabolism.toJson());
        sb.append(", ");
        sb.append(Genome.quotedString("motility"));
        sb.append(": ");
        sb.append(_motility.toJson());
        sb.append(", ");
        sb.append(Genome.quotedString("renderer"));
        sb.append(": ");
        sb.append(_renderer.toJson());
        sb.append("}");

        return sb.toString();
    }
}
