package crobes.genetics.genomics;

import crobes.core.CrobeConstants;

public class GenomeRenderer extends GenomePool
{
    public GenomeRenderer() {
        super();

        genes().add(new GenomeGene(CrobeConstants.RENDERER_GENE_SKIN, GenomeEnum.class.getName()));
        genes().add(new GenomeGene(CrobeConstants.RENDERER_GENE_FACE, GenomeString.class.getName()));
        genes().add(new GenomeGene(CrobeConstants.RENDERER_GENE_BODY, GenomeEnum.class.getName()));
    }
}
