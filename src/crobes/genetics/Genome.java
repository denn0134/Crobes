package crobes.genetics;

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

    @Override
    public String toString() {
        return toJson();
    }

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
