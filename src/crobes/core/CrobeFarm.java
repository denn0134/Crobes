package crobes.core;

import crobes.genetics.genePools.*;
import java.util.Random;

/***
 * Class for generating crobes randomly or by specifying
 * the component gene pools.
 */
public class CrobeFarm
{
    /***
     * Enumeration of ILifeCycleGenePool classes.
     */
    public enum LifeCycles {
        SimpleLifeCycle;

        public static LifeCycles random() {
            Random random = new Random();
            return values()[random.nextInt(values().length)];
        }
    }
    private static ILifeCycleGenePool getLifeCycleByClass(Crobe crobe,
                                                          LifeCycles lifeCycle) {
        ILifeCycleGenePool pool = null;

        switch (lifeCycle) {
            case SimpleLifeCycle:
                pool = new SimpleLifeCycle(crobe);
                break;
        }//end switch

        return pool;
    }
    /***
     * Enumeration of IMetabolicGenePools classes.
     */
    public enum Metabolisms {
        BasePhotoMetabolism;

        public static Metabolisms random() {
            Random random = new Random();
            return values()[random.nextInt(values().length)];
        }
    }
    private static IMetabolicGenePool getMetabolismByClass(Crobe crobe,
                                                           Metabolisms metabolism) {
        IMetabolicGenePool pool = null;

        switch (metabolism) {
            case BasePhotoMetabolism:
                pool = new BasePhotoMetabolism(crobe);
                break;
        }//end switch

        return pool;
    }
    /***
     * Enumeration of IMotilityGenePool classes.
     */
    public enum Motilities {
        ImmobileMotility;

        public static Motilities random() {
            Random random = new Random();
            return values()[random.nextInt(values().length)];
        }
    }
    private static IMotilityGenePool getMotilityByClass(Crobe crobe,
                                                        Motilities motility) {
        IMotilityGenePool pool = null;

        switch (motility) {
            case ImmobileMotility:
                pool = new ImmobileMotilty(crobe);
                break;
        }//end switch

        return pool;
    }
    /***
     * Enumeration of IRendererGenePool classes.
     */
    public enum Renderers {
        SimpleVisageRenderer;

        public static Renderers random() {
            Random random = new Random();
            return values()[random.nextInt(values().length)];
        }
    }
    private static IRenderGenePool getRendererByClass(Crobe crobe,
                                                      Renderers renderer) {
        IRenderGenePool pool = null;

        switch (renderer) {
            case SimpleVisageRenderer:
                pool = new SimpleVisageRenderer(crobe);
                break;
        }//end switch

        return pool;
    }


    /***
     * Creates a completely random Crboe from the
     * available classes.
     */
    public static Crobe randomCrobe(String designation) {
        Crobe crobe = new Crobe(designation);

        crobe.lifeCycle(getLifeCycleByClass(crobe, LifeCycles.random()));
        crobe.lifeCycle().initializeRandomDefault();
        crobe.metabolism(getMetabolismByClass(crobe, Metabolisms.random()));
        crobe.metabolism().initializeRandomDefault();
        crobe.motility(getMotilityByClass(crobe, Motilities.random()));
        crobe.motility().initializeRandomDefault();
        crobe.renderer(getRendererByClass(crobe, Renderers.random()));
        crobe.renderer().initializeRandomDefault();

        crobe.init();

        return crobe;
    }
}
