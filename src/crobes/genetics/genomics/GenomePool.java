package crobes.genetics.genomics;

import java.util.ArrayList;

/***
 * This is the model of a single gene pool within a
 * a Genome.
 */
public abstract class GenomePool
{
    public String genePool;

    private ArrayList<GenomeGene> _genes;
    public ArrayList<GenomeGene> genes() {
        return _genes;
    }

    public GenomeGene getGene(String name) {
        GenomeGene result = null;

        for(GenomeGene g : _genes) {
            if(g.name.equalsIgnoreCase(name)) {
                result = g;
                break;
            }//end if
        }//end for each

        return result;
    }
    public void setGene(GenomeGene gene) {
        boolean found = false;

        for(int i = 0; i < _genes.size(); i++) {
            GenomeGene g = _genes.get(i);

            if(g.name.equalsIgnoreCase(gene.name)) {
                _genes.set(i, gene);
                found = true;
                break;
            }//end if
        }//end for i

        if(!found) {
            _genes.add(gene);
        }//end if
    }

    public GenomePool() {
        _genes = new ArrayList<GenomeGene>();
    }

    public void initRandom(boolean randomize) {
        for(GenomeGene g: _genes)
            g.random = randomize;
    }

    public String getTaxa() {
        Genomics.GenePoolInfo gpi = Genomics.getGenePoolInfo(genePool);
        if(gpi != null)
            return gpi.taxanomicName;
        else
            return "";
    }

    String toJson() {
        StringBuilder sb = new StringBuilder();

        sb.append("{");
        sb.append(Genome.quotedString("genePool"));
        sb.append(": ");
        sb.append(Genome.quotedString(genePool));
        sb.append(", ");
        sb.append(Genome.quotedString("genes"));
        sb.append(": [");

        for(int i = 0; i < _genes.size(); i++) {
            GenomeGene gene = _genes.get(i);

            sb.append(gene.toJson());

            if(i < _genes.size() - 1) {
                sb.append(", ");
            }//end if
        }//end for i

        sb.append("] }");

        return sb.toString();
    }
}
