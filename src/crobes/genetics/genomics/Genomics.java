package crobes.genetics.genomics;

import crobes.core.Crobe;
import crobes.core.CrobeConstants;
import crobes.genetics.genePools.ILifeCycleGenePool;
import crobes.genetics.genePools.IMetabolicGenePool;
import crobes.genetics.genePools.IMotilityGenePool;
import crobes.genetics.genePools.IRenderGenePool;

public class Genomics
{
    public static Genome extractGenome(Crobe crobe) {
        Genome genome = new Genome();

        ILifeCycleGenePool clc = crobe.lifeCycle();
        GenomeLifeCycle glc = genome.lifeCycle();
        glc.genePool = clc.getClass().getName();
        glc.getGene(CrobeConstants.LIFECYCLE_GENE_SPAN).geneValue = new GenomeInt(clc.span());
        glc.getGene(CrobeConstants.LIFECYCLE_GENE_SPANRANGE).geneValue = new GenomeInt(clc.spanRange());
        glc.getGene(CrobeConstants.LIFECYCLE_GENE_MATURITY).geneValue = new GenomeFlt(clc.maturity());
        glc.getGene(CrobeConstants.LIFECYCLE_GENE_FINITE).geneValue = new GenomeBool(clc.finite());

        IMetabolicGenePool cmb = crobe.metabolism();
        GenomeMetabolism gmb = genome.metabolism();
        gmb.genePool = cmb.getClass().getName();
        gmb.getGene(CrobeConstants.METABOLISM_GENE_VITALITY).geneValue = new GenomeInt(cmb.vitality());
        gmb.getGene(CrobeConstants.METABOLISM_GENE_VITALITYRANGE).geneValue = new GenomeInt(cmb.vitalityRange());
        gmb.getGene(CrobeConstants.METABOLISM_GENE_STAMINA).geneValue = new GenomeInt(cmb.stamina());
        gmb.getGene(CrobeConstants.METABOLISM_GENE_STAMINARANGE).geneValue = new GenomeInt(cmb.staminaRange());
        gmb.getGene(CrobeConstants.METABOLISM_GENE_HEALRATE).geneValue = new GenomeFlt(cmb.healRate());
        gmb.getGene(CrobeConstants.METABOLISM_GENE_MORTALITYRATE).geneValue = new GenomeInt(cmb.mortalityRate());

        IMotilityGenePool cmt = crobe.motility();
        GenomeMotility gmt = genome.motility();
        gmt.genePool = cmt.getClass().getName();
        gmt.getGene(CrobeConstants.MOTILITY_GENE_MOTILITYTYPE).geneValue = new GenomeEnum(cmt.motilityType());
        gmt.getGene(CrobeConstants.MOTILITY_GENE_MOVETYPE).geneValue = new GenomeEnum(cmt.moveType());
        gmt.getGene(CrobeConstants.MOTILITY_GENE_MOVEBASE).geneValue = new GenomeInt(cmt.moveBase());
        gmt.getGene(CrobeConstants.MOTILITY_GENE_MOVERANGE).geneValue = new GenomeInt(cmt.moveRange());
        gmt.getGene(CrobeConstants.MOTILITY_GENE_LETHARGY).geneValue = new GenomeFlt(cmt.lethargy());
        gmt.getGene(CrobeConstants.MOTILITY_GENE_EFFICIENCY).geneValue = new GenomeFlt(cmt.efficiency());

        IRenderGenePool crd = crobe.renderer();
        GenomeRenderer grd = genome.renderer();
        grd.genePool = crd.getClass().getName();
        grd.getGene(CrobeConstants.RENDERER_GENE_SKIN).geneValue = new GenomeEnum(crd.skin());
        grd.getGene(CrobeConstants.RENDERER_GENE_FACE).geneValue = new GenomeString(crd.face());
        grd.getGene(CrobeConstants.RENDERER_GENE_BODY).geneValue = new GenomeEnum(crd.body());

        return genome;
    }
}
