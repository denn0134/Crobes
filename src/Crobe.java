import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class Crobe
{
    public static final float MUTATION_RANGE = 0.40f;
    public static int MUTATION_RATE = 100;

    private CrobeEnums.LifeStage _stage;
    public CrobeEnums.LifeStage stage() { return _stage; }
    public void stage(CrobeEnums.LifeStage stage) { _stage = stage; }

    private static final String TAXA_FORMAT = "%1$s: %2$s %3$s %4$s %5$s";
    public Random rand = new Random();

    public CrobeColony parent;
    private World _world;
    public World world() {
        return _world;
    }
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
    public String designation() {
        return _designation;
    }

    private int _lifeSpan;
    public int lifeSpan(){
        return _lifeSpan;
    }
    public void lifeSpan(int lifeSpan){ _lifeSpan = lifeSpan; }

    private int _age;
    public int age(){
        return _age;
    }
    public void age(int age){
        _age = age;
    }

    private int _maturityAge;
    public int maturityAge() { return _maturityAge; }
    public void maturityAge(int maturityAge) { _maturityAge = maturityAge; }

    private int _health;
    public int health() {
        return _health;
    }
    public void health(int health) {
        _health = health;
    }
    private int _maxHealth;
    public int maxHealth() {
        return _maxHealth;
    }
    public void maxHealth(int maxHealth) {
        _maxHealth = maxHealth;
    }

    private int _energy;
    public int energy() {
        return _energy;
    }
    public void energy(int energy) {
        _energy = energy;
    }
    private int _maxEnergy;
    public int maxEnergy() {
        return _maxEnergy;
    }
    public void set_maxEnergy(int maxEnergy) {
        _maxEnergy = maxEnergy;
    }

    private int _stressLevel;
    public int stressLevel() {
        return _stressLevel;
    }
    public void stressLevel(int stressLevel) {
        _stressLevel = stressLevel;
    }

    private Point _position;
    public Point position() {
        return _position;
    }
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

    public Crobe(String designation){
        _designation = designation;
        _stressLevel = 1;
    }

    public String getTaxa(){
        return String.format(TAXA_FORMAT,
                _designation,
                _lifeCycle.getNamePart(),
                _metabolism.getNamePart(),
                _motility.getNamePart(),
                _renderer.getNamePart());
    }

    private ILifeCycleGenePool _lifeCycle;
    public ILifeCycleGenePool lifeCycle(){
        return _lifeCycle;
    }
    public void lifeCycle(ILifeCycleGenePool lifeCycle){
        _lifeCycle = lifeCycle;
    }

    private IMetabolicGenePool _metabolism;
    public IMetabolicGenePool metabolism() {
        return _metabolism;
    }
    public void metabolism(IMetabolicGenePool metabolism) {
        _metabolism = metabolism;
    }

    private IMotilityGenePool _motility;
    public IMotilityGenePool motility() {
        return _motility;
    }
    public void motility(IMotilityGenePool motility) {
        _motility = motility;
    }

    private IRenderGenePool _renderer;
    public IRenderGenePool renderer() {
        return _renderer;
    }
    public void renderer(IRenderGenePool renderer) {
        _renderer = renderer;
    }

    private int getIntRange(int base, int range) {
        int fullRange = 2 * range + 1;
        return base + rand.nextInt(fullRange) - range;
    }

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
    public void reproduce(ArrayList<Crobe> children) {
        _lifeCycle.processReproduction(children);
    }

    public void stressed(int stress) {
        _stressLevel += stress;
    }
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
    public void recharge(int energy) {
        if(_energy < _maxEnergy) {
            _energy += energy;
            if(_energy > _maxEnergy)
                _energy = _maxEnergy;
        }//end if
    }
    public boolean exert(int energy) {
        boolean result = false;

        if(energy <= _energy) {
            _energy -= energy;
            result = true;
        }//end if

        return result;
    }
    public void mutate() {
        _lifeCycle.mutate(_stressLevel);
        _metabolism.mutate(_stressLevel);
        _motility.mutate(_stressLevel);
        _renderer.mutate(_stressLevel);
    }

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
