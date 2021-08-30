package crobes.core;

import crobes.genetics.genePools.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

/***
 * Base class for a microbe.
 */
public class Crobe
{
    /***
     * Maximum mutation variance, expressed as a percentage of the
     * phenotype of the gene.
     */
    public static final float MUTATION_RANGE = 0.40f;
    /***
     * Likelihood of mutation occurrence; the probability of a mutation
     * is s chances in m where s = the current stress of the crobe and
     * m = MUTATION_RATE.
     */
    public static int MUTATION_RATE = 100;

    private CrobeEnums.LifeStage _stage;

    /***
     * Current life stage of the crobe.
     * @return Returns the current life stage of the crobe.
     */
    public CrobeEnums.LifeStage stage() { return _stage; }
    public void stage(CrobeEnums.LifeStage stage) { _stage = stage; }

    private static final String TAXA_FORMAT = "%1$s: %2$s %3$s %4$s %5$s";
    public Random rand = new Random();

    /***
     * Reference to the parent collection of crobes.
     */
    public CrobeColony parent;

    private World _world;
    /***
     * Reference to the World object in which the crobe lives.
     * @return Returns the World object.
     */
    public World world() {
        return _world;
    }
    /***
     * Reference to the World object in which the crobe lives.
     * @param world The World object to assign to the crobe.
     */
    public void world(World world) {
        _world = world;
    }

    private int _spawned;
    public int spawned() {
        return _spawned;
    }
    public String getSpawnName() {
        _spawned++;
        return "" + _spawned;
    }

    private String _designation;
    /***
     * Human readable designation name for the crobe.
     * @return Returns the designation.
     */
    public String designation() {
        return _designation;
    }
    /***
     *
     * @param designation
     */
    public Crobe(String designation){
        _designation = designation;
        _stressLevel = 1;
    }

    private int _lifeSpan;
    /***
     * The expected lifespan of the crobe.
     * @return
     */
    public int lifeSpan(){
        return _lifeSpan;
    }
    /***
     * The expected lifespan of the crobe.
     * @param lifeSpan
     */
    public void lifeSpan(int lifeSpan){ _lifeSpan = lifeSpan; }

    private int _age;
    /***
     * The current age of the crobe.
     * @return
     */
    public int age(){
        return _age;
    }
    /***
     * The current age of the crobe.
     * @param age
     */
    public void age(int age){
        _age = age;
    }

    private int _maturityAge;
    /***
     * @return
     * The age of maturity of the crobe.
     */
    public int maturityAge() { return _maturityAge; }
    /***
     * The age of maturity of the crobe.
     * @param maturityAge
     */
    public void maturityAge(int maturityAge) { _maturityAge = maturityAge; }

    private int _health;
    /***
     * The current health status of the crobe measured in hit points.
     * @return
     */
    public int health() {
        return _health;
    }
    /***
     * The current health status of the crobe measured in hit points.
     * @param health
     */
    public void health(int health) {
        _health = health;
    }

    private int _maxHealth;
    /***
     * The maximum health of the crobe.
     * @return
     */
    public int maxHealth() {
        return _maxHealth;
    }
    /***
     * The maximum health of the crobe.
     * @param maxHealth
     */
    public void maxHealth(int maxHealth) {
        _maxHealth = maxHealth;
    }

    private int _energy;
    /***
     * The current energy level of the crobe.
     * @return
     */
    public int energy() {
        return _energy;
    }
    /***
     * The current energy level of the crobe.
     * @param energy
     */
    public void energy(int energy) {
        _energy = energy;
    }

    private int _maxEnergy;
    /***
     * The maximum energy store of the crobe.
     * @return
     */
    public int maxEnergy() {
        return _maxEnergy;
    }
    /***
     * The maximum energy store of the crobe.
     * @param maxEnergy
     */
    public void set_maxEnergy(int maxEnergy) {
        _maxEnergy = maxEnergy;
    }

    private int _stressLevel;
    /***
     * Current stress level of the crobe.
     * @return
     */
    public int stressLevel() {
        return _stressLevel;
    }
    /***
     * Current stress level of the crobe.
     * @param stressLevel
     */
    public void stressLevel(int stressLevel) {
        _stressLevel = stressLevel;
    }

    private Point _position;
    /***
     * The current location of the crobe within the world.
     * @return
     */
    public Point position() {
        return _position;
    }
    /***
     * The current location of the crobe within the world.
     * @param position
     */
    public void position(Point position) {
        //set the position
        _position = position;

        //make sure all inhabited Locations
        //reference the crobe
        Location[] locs = inhabits();
        for(Location l : locs) {
            l.crobe(this);
        }//end for l
    }

    /***
     * The taxanomic name of the crobe species.
     * @return Returns the taxanomic name of the crobe species.
     */
    public String getTaxa(){
        return String.format(TAXA_FORMAT,
                _designation,
                _lifeCycle.getNamePart(),
                _metabolism.getNamePart(),
                _motility.getNamePart(),
                _renderer.getNamePart());
    }

    private ILifeCycleGenePool _lifeCycle;
    /***
     * The lifecycle genepool for the crobe.
     * @return
     */
    public ILifeCycleGenePool lifeCycle(){
        return _lifeCycle;
    }
    /***
     * The lifecycle genepool for the crobe.
     * @param lifeCycle
     */
    public void lifeCycle(ILifeCycleGenePool lifeCycle){
        _lifeCycle = lifeCycle;
    }

    private IMetabolicGenePool _metabolism;
    /***
     * The metabolic genepool for the crobe.
     * @return
     */
    public IMetabolicGenePool metabolism() {
        return _metabolism;
    }
    /***
     * The metbolic genepool for the crobe.
     * @param metabolism
     */
    public void metabolism(IMetabolicGenePool metabolism) {
        _metabolism = metabolism;
    }

    private IMotilityGenePool _motility;
    /***
     * The motility genepool for the crobe.
     * @return
     */
    public IMotilityGenePool motility() {
        return _motility;
    }
    /***
     * The motility genepool for the crobe.
     * @param motility
     */
    public void motility(IMotilityGenePool motility) {
        _motility = motility;
    }

    private IRenderGenePool _renderer;
    /***
     * The rendering genepool for the crobe.
     * @return
     */
    public IRenderGenePool renderer() {
        return _renderer;
    }
    /***
     * The rendering genepool for the crobe.
     * @param renderer
     */
    public void renderer(IRenderGenePool renderer) {
        _renderer = renderer;
    }

    private int getIntRange(int base, int range) {
        int fullRange = 2 * range + 1;
        return base + rand.nextInt(fullRange) - range;
    }

    /***
     * Performs the life functions of the crobe for a day.  Note
     * that this could possibly result in the death of the crobe.
     */
    public void live(){
        //age a turn
        _age += 1;
        _lifeCycle.processAging();

        //feed
        _metabolism.processFeeding();

        //check for healing
        if(_health < _maxHealth) {
            int heal = rand.nextInt(100);
            if(heal < (Math.round(_metabolism.healRate().phenotype() * 100))) {
                heal(1);
            }
        }//end if

        //check for mortality
        int die = rand.nextInt(_metabolism.mortalityRate().phenotype());
        if(die < _stressLevel) {
            _lifeCycle.processDeath(_stage);
            stage(CrobeEnums.LifeStage.DEAD);
        }//end if
    }

    /***
     * Performs the reproduction functions of the crobe.
     * @param children Arraylist to hold the children resulting.
     */
    public void reproduce(ArrayList<Crobe> children) {
        _lifeCycle.processReproduction(children);
    }

    /***
     * Applies stress to the crobe.
     * @param stress The amount of stress to apply to the crobe.
     */
    public void stressed(int stress) {
        _stressLevel += stress;
    }

    /***
     * Applies wounds to the crobe, possibly resulting in death.
     * @param damage The amount of wounds to apply to the crobe.
     * @return Returns True if the crobe survived the wound; False if if has died.
     */
    public boolean wound(int damage) {
        if(damage < _health) {
            _health -= damage;
            return true;
        }//end if
        else {
            System.out.println("Crobe [" + _designation + "] died from health damage");
            _health = 0;
            _lifeCycle.processDeath(_stage);
            _stage = CrobeEnums.LifeStage.DEAD;
            return false;
        }//end else
    }

    /***
     * Performs health recovery for the crobe; this health recovery requires
     * energy in order to be successful.  Note that this will not allow the
     * crobe to heal above its maximum health.
     * @param health The amount of health to heal.
     */
    public void heal(int health) {
        for(int i = 0; i < health; i++) {
            if(exert(2)) {
                if(_health < _maxHealth)
                    _health += 1;
                else
                    break;
            }//end if
            else {
                break;
            }//end else
        }//end for i
    }

    /***
     * Recharges the energy of the crobe.
     * @param energy The amount of energy to recharge/
     */
    public void recharge(int energy) {
        if(_energy < _maxEnergy) {
            _energy += energy;
            if(_energy > _maxEnergy)
                _energy = _maxEnergy;
        }//end if
    }

    /***
     * Peforms energy expenditure for the crobe.  This will take the current
     * energy level into account and will not allow expenditure of energy
     * which the crobe does nmot have.
     * @param energy The amount of enmergy to expend.
     * @return Returns True if all energy was successfully spent; False if
     * the crobe did not have enough energy to expend the total amount.
     */
    public boolean exert(int energy) {
        boolean result = false;

        if(energy <= _energy) {
            _energy -= energy;
            result = true;
        }//end if

        return result;
    }

    /***
     * Peforms the mutate functionality on all genepools.
     */
    public void mutate() {
        _lifeCycle.mutate(_stressLevel);
        _metabolism.mutate(_stressLevel);
        _motility.mutate(_stressLevel);
        _renderer.mutate(_stressLevel);
    }

    /***
     * Initializes the crobe, setting it base statistics.
     */
    public void init(){
        //set the crobe attributes
        _age = 0;
        _spawned = 0;
        _stressLevel = 1;
        _lifeSpan = getIntRange(_lifeCycle.span().phenotype(), _lifeCycle.spanRange().phenotype());
        _stage = CrobeEnums.LifeStage.CHILD;
        _maturityAge = (int)Math.round(_lifeSpan * _lifeCycle.maturity().phenotype());
        _health = getIntRange(_metabolism.vitality().phenotype(), _metabolism.vitalityRange().phenotype());
        _maxHealth = _health;
        _energy = getIntRange(_metabolism.stamina().phenotype(), _metabolism.staminaRange().phenotype());
        _maxEnergy = _energy;

        //initialize any gene pool functions
        _lifeCycle.initializeReproduction();
    }

    /***
     * Returns an array of the Locations which this
     * Crobe inhabits.
     * @return Array of Locations this Crobe inhabits.
     */
    public Location[] inhabits() {
        return _renderer.getLocations(_stage);
    }
    /***
     * Returns an array of Locations which are adjacent
     * to this Crobe.
     * @return Array of adjacent Locations.
     */
    public Location[] adjacent() {
        return _renderer.getAdjacents(_stage);
    }

    public String toString(){
        return getTaxa() + "\n" +
                String.format(" age: %1$d", _age) +
                String.format(" lifespan: %1$d", _lifeSpan) +
                String.format(" maturity: %1$d", _maturityAge) +
                String.format(" stage: %1$s", _stage.name()) +
                String.format(" health: %1$d(%2$d)", _health, _maxHealth) +
                String.format(" energy: %1$d(%2$d)", _energy, _maxEnergy) +
                String.format(" stress: %1$d", _stressLevel) + "\n" +
                _lifeCycle.toString() + "\n" +
                _metabolism.toString() + "\n" +
                _motility.toString() + "\n" +
                _renderer.toString();
    }
}
