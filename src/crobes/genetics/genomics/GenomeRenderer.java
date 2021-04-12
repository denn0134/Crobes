package crobes.genetics.genomics;

import crobes.core.CrobeConstants;
import crobes.core.CrobeEnums;

public class GenomeRenderer extends GenomePool
{
    public GenomeRenderer() {
        super();

        genes().add(new GenomeGene(CrobeConstants.RENDERER_GENE_SKIN, GenomeEnum.class.getSimpleName()));
        genes().add(new GenomeGene(CrobeConstants.RENDERER_GENE_FACE, GenomeString.class.getSimpleName()));
        genes().add(new GenomeGene(CrobeConstants.RENDERER_GENE_BODY, GenomeEnum.class.getSimpleName()));

        GenomeEnum ge;
        ge = (GenomeEnum) getGene(CrobeConstants.RENDERER_GENE_SKIN).geneValue;
        ge.enumClass(CrobeEnums.CrobeColor.class);
        ge = (GenomeEnum) getGene(CrobeConstants.RENDERER_GENE_BODY).geneValue;
        ge.enumClass(CrobeEnums.CrobeColor.class);
    }
}
