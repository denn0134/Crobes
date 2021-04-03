package crobes.genetics.genomics;

import crobes.core.CrobeConstants;

public class GenomeMetabolism extends GenomePool
{
    public GenomeMetabolism() {
        super();

        genes().add(new GenomeGene(CrobeConstants.METABOLISM_GENE_VITALITY, GenomeInt.class.getSimpleName()));
        genes().add(new GenomeGene(CrobeConstants.METABOLISM_GENE_VITALITYRANGE, GenomeInt.class.getSimpleName()));
        genes().add(new GenomeGene(CrobeConstants.METABOLISM_GENE_STAMINA, GenomeInt.class.getSimpleName()));
        genes().add(new GenomeGene(CrobeConstants.METABOLISM_GENE_STAMINARANGE, GenomeInt.class.getSimpleName()));
        genes().add(new GenomeGene(CrobeConstants.METABOLISM_GENE_HEALRATE, GenomeFlt.class.getSimpleName()));
        genes().add(new GenomeGene(CrobeConstants.METABOLISM_GENE_MORTALITYRATE, GenomeInt.class.getSimpleName()));
    }
}
