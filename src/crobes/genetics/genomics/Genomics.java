package crobes.genetics.genomics;

import crobes.core.Crobe;
import crobes.core.CrobeConstants;
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
        genePools.registerGenePool(LifeCycle.class.getName(), LifeCycle.class);
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
        genePools.registerGenePool(Metabolism.class.getName(), Metabolism.class);
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
        genePools.registerGenePool(Motility.class.getName(), Motility.class);
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
        genePools.registerGenePool(Renderer.class.getName(), Renderer.class);
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

    //class registries
    public static final GenePoolRegister genePools = new GenePoolRegister();
    public static final LifeCycleRegister lifeCycles = new LifeCycleRegister();
    public static final MetabolismRegister metabolisms = new MetabolismRegister();
    public static final MotilityRegister motilities = new MotilityRegister();
    public static final RendererRegister renderers = new RendererRegister();


    //class register static classes
    public static class GenePoolRegister
    {
        private Map<String, Class<? extends GenePool>> genePools = new HashMap<>();
        public int count() {
            return genePools.size();
        }
        public void registerGenePool(String name, Class<? extends GenePool> genePool) {
            genePools.put(name, genePool);
        }
        public String[] getGenePools() {
            String[] result = new String[genePools.size()];
            genePools.keySet().toArray(result);
            return result;
        }
    }
    public static class LifeCycleRegister
    {
        private Map<String, Class<? extends LifeCycle>> lifeCycles = new HashMap<>();
        public int count() {
            return lifeCycles.size();
        }
        public void registerLifeCycle(String name, Class<? extends LifeCycle> lifeCycle) {
            lifeCycles.put(name, lifeCycle);
        }
        public LifeCycle getLifeCycle(String name, Crobe crobe) {
            LifeCycle result = null;

            Class<? extends LifeCycle> _class = lifeCycles.get(name);
            if(_class != null) {
                try {
                    result = _class.newInstance();
                    result.crobe(crobe);
                }//end try
                catch(IllegalAccessException iae) {

                }//end catch iae
                catch(InstantiationException ie) {

                }//end catch ie
            }//end if

            return result;
        }
        public String[] getLifeCycles() {
            String[] result = new String[lifeCycles.size()];
            lifeCycles.keySet().toArray(result);
            return result;
        }
    }
    public static class MetabolismRegister
    {
        private Map<String, Class<? extends Metabolism>> metabolisms = new HashMap<>();
        public int count() {
            return metabolisms.size();
        }
        public void registerMetabolism(String name, Class<? extends Metabolism> metabolism) {
            metabolisms.put(name, metabolism);
        }
        public Metabolism getMetabolism(String name, Crobe crobe) {
            Metabolism result = null;

            Class<? extends Metabolism> _class = metabolisms.get(name);
            if(_class != null) {
                try {
                    result = _class.newInstance();
                    result.crobe(crobe);
                }//end try
                catch(IllegalAccessException iae) {

                }//end catch iae
                catch(InstantiationException ie) {

                }//end catch ie
            }//end if

            return result;
        }
        public String[] getMetabolisms() {
            String[] result = new String[metabolisms.size()];
            metabolisms.keySet().toArray(result);
            return result;
        }
    }
    public static class MotilityRegister
    {
        private Map<String, Class<? extends Motility>> motilities = new HashMap<>();
        public int count() {
            return motilities.size();
        }
        public void registerMotility(String name, Class<? extends Motility> motility) {
            motilities.put(name, motility);
        }
        public Motility getMotility(String name, Crobe crobe) {
            Motility result = null;

            Class<? extends Motility> _class = motilities.get(name);
            if(_class != null) {
                try {
                    result = _class.newInstance();
                    result.crobe(crobe);
                }//end try
                catch(IllegalAccessException iae) {

                }//end catch iae
                catch(InstantiationException ie) {

                }//end catch ie
            }//end if

            return result;
        }
        public String[] getMotilities() {
            String[] result = new String[motilities.size()];
            motilities.keySet().toArray(result);
            return result;
        }
    }
    public static class RendererRegister
    {
        private Map<String, Class<? extends Renderer>> renderers = new HashMap<>();
        public int count() {
            return renderers.size();
        }
        public void registerRenderer(String name, Class<? extends Renderer> renderer) {
            renderers.put(name, renderer);
        }
        public Renderer getRenderer(String name, Crobe crobe) {
            Renderer result = null;

            Class<? extends Renderer> _class = renderers.get(name);
            if(_class != null) {
                try {
                    result = _class.newInstance();
                    result.crobe(crobe);
                }//end try
                catch(IllegalAccessException iae) {

                }//end catch iae
                catch(InstantiationException ie) {

                }//end catch ie
            }//end if

            return result;
        }
        public String[] getRenderers() {
            String[] result = new String[renderers.size()];
            renderers.keySet().toArray(result);
            return result;
        }
    }
}
