package crobes.genetics.genomics;

import crobes.core.Crobe;
import crobes.core.CrobeConstants;
import crobes.core.CrobeEnums;
import crobes.genetics.genePools.*;
import crobes.genetics.genes.*;
import org.reflections.Reflections;

import java.util.Set;
import java.util.Map;
import java.util.HashMap;

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

    private static final String FMT_CNFE = "Class not found [%1$s]";

    /***
     * Initializes the genetic classes for use in Genomics.
     * Registers the classes into registries for lookup to
     * allow for creating genetic objects by class name.
     */
    public static void initializeGenomics() {
        Reflections ref = new Reflections();

        //load life cycles
        genePools.registerGenePool(LifeCycle.LIFECYCLE_DISPLAY, LifeCycle.LIFECYCLE_DESCRIPTION, LifeCycle.class);
        Set<Class<? extends LifeCycle>> lifeCycles = ref.getSubTypesOf(LifeCycle.class);
        for(Class<? extends LifeCycle> lc : lifeCycles) {
            try {
                Class.forName(lc.getName());
            }//end try
            catch(ClassNotFoundException cnfe) {
                System.out.printf(FMT_CNFE, lc.getName());
            }//end catch cnfe
        }//end for lc

        //load metabolisms
        genePools.registerGenePool(Metabolism.METABOLISM_DISPLAY, Metabolism.METABOLISM_DESCRIPTION, Metabolism.class);
        Set<Class<? extends Metabolism>> metabolisms = ref.getSubTypesOf(Metabolism.class);
        for(Class<? extends Metabolism> m : metabolisms) {
            try {
                Class.forName(m.getName());
            }//end try
            catch(ClassNotFoundException cnfe) {
                System.out.printf(FMT_CNFE, m.getName());
            }//end catch cnfe
        }//end for lc

        //load motilities
        genePools.registerGenePool(Motility.MOTILITY_DISPLAY, Motility.MOTILITY_DESCRIPTION, Motility.class);
        Set<Class<? extends Motility>> motilities = ref.getSubTypesOf(Motility.class);
        for(Class<? extends Motility> m : motilities) {
            try {
                Class.forName(m.getName());
            }//end try
            catch(ClassNotFoundException cnfe) {
                System.out.printf(FMT_CNFE, m.getName());
            }//end catch cnfe
        }//end for lc

        //load renderers
        genePools.registerGenePool(Renderer.RENDERER_DISPLAY, Renderer.RENDERER_DESCRIPTION, Renderer.class);
        Set<Class<? extends Renderer>> renderers = ref.getSubTypesOf(Renderer.class);
        for(Class<? extends Renderer> r : renderers) {
            try {
                Class.forName(r.getName());
            }//end try
            catch(ClassNotFoundException cnfe) {
                System.out.printf(FMT_CNFE, r.getName());
            }//end catch cnfe
        }//end for lc

        //load geneTypes
        Set<Class<? extends Gene>> geneTypes = ref.getSubTypesOf(Gene.class);
        for(Class<? extends Gene> g : geneTypes) {
            try {
                Class.forName(g.getName());
            }//end try
            catch(ClassNotFoundException cnfe) {
                System.out.printf(FMT_CNFE, g.getName());
            }//end catch cnfe
        }
    }

    /***
     * Searches all of the gene pool registers for the
     * specified GenePoolInfo.
     * @param name The name of the GenePool to search for.
     * @return Returns the GenePoolInfo if it is found.
     */
    public static GenePoolInfo getGenePoolInfo(String name) {
        GenePoolInfo result = null;

        result = lifeCycles.getInfo(name);
        if(result == null)
            result = metabolisms.getInfo(name);
        if(result == null)
            result = motilities.getInfo(name);
        if(result == null)
            result = renderers.getInfo(name);

        return result;
    }

    /***
     * Finds the gene information for a specified GenomeGene.
     * @param genomeGeneType The type name for the GenomeGene.
     * @return Returns the gene information if it is found; if
     *         it is not found returns null.
     */
    public static GeneInfo getGeneInfo(String genomeGeneType) {
        //we will brute force this for now
        if(genomeGeneType.equalsIgnoreCase(GenomeInt.class.getSimpleName()))
            return genes.getInfo(ScalarGeneInt.class.getSimpleName());
        else if(genomeGeneType.equalsIgnoreCase(GenomeFlt.class.getSimpleName()))
            return genes.getInfo(ScalarGeneFlt.class.getSimpleName());
        else if(genomeGeneType.equalsIgnoreCase(GenomeBool.class.getSimpleName()))
            return genes.getInfo(HeritableGeneBool.class.getSimpleName());
        else if(genomeGeneType.equalsIgnoreCase(GenomeEnum.class.getSimpleName()))
            return genes.getInfo(HeritableGeneEnum.class.getSimpleName());
        else if(genomeGeneType.equalsIgnoreCase(GenomeString.class.getSimpleName()))
            return genes.getInfo(HeritableGeneString.class.getSimpleName());
        else
            return null;
    }

    //class registries
    public static final GenePoolRegister genePools = new GenePoolRegister();
    public static final GenePoolClassRegister<LifeCycle> lifeCycles = new GenePoolClassRegister<LifeCycle>();
    public static final GenePoolClassRegister<Metabolism> metabolisms = new GenePoolClassRegister<>();
    public static final GenePoolClassRegister<Motility> motilities = new GenePoolClassRegister<>();
    public static final GenePoolClassRegister<Renderer> renderers = new GenePoolClassRegister<>();
    public static final GeneRegister genes = new GeneRegister();

    //class register static classes
    public static class GenePoolInfo
    {
        public Class<? extends GenePool> genePoolClass;
        public String displayName;
        public String taxanomicName;
        public String description;
    }
    public static class GenePoolRegister
    {
        private Map<String, GenePoolInfo> genePools = new HashMap<>();
        public int count() {
            return genePools.size();
        }
        public void registerGenePool(String name,
                                     String description,
                                     Class<? extends GenePool> genePool) {
            GenePoolInfo info = new GenePoolInfo();
            info.genePoolClass = genePool;
            info.displayName = name;
            info.description = description;
            info.taxanomicName = null;

            genePools.put(genePool.getSimpleName(), info);
        }
        public String[] getGenePools() {
            String[] result = new String[genePools.size()];
            genePools.keySet().toArray(result);
            return result;
        }
        public GenePoolInfo getInfo(String name) {
            return genePools.get(name);
        }
    }
    public static class GenePoolClassRegister<T extends GenePool>
    {
        private Map<String, GenePoolInfo> pools = new HashMap<>();
        public int count() {
            return pools.size();
        }
        public void registerGenePool(Class<? extends T> genePool) {
            try {
                T pool = genePool.newInstance();
                GenePoolInfo info = new GenePoolInfo();

                info.genePoolClass = genePool;
                info.displayName = pool.displayName();
                info.description = pool.description();
                info.taxanomicName = pool.getNamePart();

                pools.put(genePool.getSimpleName(), info);
            }//end try
            catch (IllegalAccessException|InstantiationException ex) {
                System.out.println(ex.getMessage());
            }//end catch
        }
        public String[] getGenePools() {
            String[] result = new String[pools.size()];
            pools.keySet().toArray(result);
            return result;
        }
        public GenePoolInfo getInfo(String name) {
            if(pools.keySet().contains(name))
                return pools.get(name);
            else
                return null;
        }
        public T createGenePool(String name, Crobe crobe) {
            T result = null;
            GenePoolInfo info = pools.get(name);
            if(info != null) {
                try {
                    result = (T)info.genePoolClass.newInstance();
                    result.crobe(crobe);
                }//end try
                catch (IllegalAccessException|InstantiationException ex) {
                    System.out.println(ex.getMessage());
                }//end catch ex
            }//end if

            return result;
        }
    }
    public static class GeneInfo
    {
        public Class<? extends Gene> geneClass;
        public CrobeEnums.MutationType[] allowedMutations;
        public Class<? extends GenomeValue> genomeValueClass;
    }
    public static class GeneRegister
    {
        private Map<String, GeneInfo> _genes = new HashMap<>();
        public int count() {
            return _genes.size();
        }
        public void registerGene(Class<? extends Gene> geneClass) {
            try {
                Gene g = geneClass.newInstance();
                GeneInfo info = new GeneInfo();

                info.geneClass = geneClass;
                info.allowedMutations = g.allowedMutations();
                info.genomeValueClass = g.geneValueClass();

                _genes.put(geneClass.getSimpleName(), info);
            }//end try
            catch (IllegalAccessException|InstantiationException ex) {
                System.out.println(ex.getMessage());
            }//end catch ex
        }
        public String[] getGenes() {
            String[] result = new String[_genes.size()];
            result = _genes.keySet().toArray(result);
            return result;
        }
        public GeneInfo getInfo(String name) {
            if(_genes.containsKey(name))
                return _genes.get(name);
            else
                return null;
        }
        public Gene createGene(String name) {
            Gene result = null;

            return result;
        }
    }
}
