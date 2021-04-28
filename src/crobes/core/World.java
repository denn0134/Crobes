package crobes.core;

import crobes.core.factors.Factor;
import crobes.genetics.genomics.Genomics;

import java.util.ArrayList;

public class World
{
    private int _age;
    public int age() {
        return _age;
    }

    private Environment _environment;
    public Environment environment() {
        return _environment;
    }

    private CrobeColony _crobes;
    public CrobeColony crobes() {
        return _crobes;
    }

    private ArrayList<Factor> _factors = new ArrayList<Factor>();
    public ArrayList<Factor> factors() {
        return _factors;
    }

    private Drift.DriftList _driftList = new Drift.DriftList();
    public Drift.DriftList driftList() {
        return _driftList;
    }

    public World(int environmentalRadix) {
        Genomics.initializeGenomics();
        _age = 0;
        _factors = new ArrayList<Factor>();
        _environment = new Environment(environmentalRadix);
        _crobes = new CrobeColony();
    }

    public int getWidth() {
        return _environment.width();
    }
    public int getHeight() {
        return _environment.height();
    }
    public boolean inBounds(int X, int Y) {
        boolean inBounds = true;

        if((X < 0) || (Y < 0))
            inBounds = false;

        if(X >= _environment.width())
            inBounds = false;
        if(Y >= _environment.height())
            inBounds = false;

        return inBounds;
    }
    public Location getLocation(int X, int Y) {
        //check if the coordinates are valid
        boolean wValid = ((X > -1) && (X < _environment.width()));
        boolean hValid = ((Y > -1) && (Y < _environment.height()));

        if(wValid && hValid)
            return _environment.get(X, Y);
        else
            return null;
    }

    public void updateEnvironment() {
        _environment.reset();
        _environment.updateLocations();
    }
    public void updateFactors() {
        //process all factors
        for(Factor f: _factors) {
            f.process();
        }//end for each

        //remove any factor with a ttl of zero
        for(int i = _factors.size(); i > 0; i--) {
            Factor f = _factors.get(i - 1);
            if(f.timeToLive() == 0) {
                f.location().factors().remove(f);
                _factors.remove(f);
            }//end if
        }//end for i
    }
    public void processDrift() {
        for (Drift d: _driftList) {
            d.location().drift(d.direction());
        }//end for each
    }
    public void brownianMotion() {
        //brownian motion is always first
        //therefore we should reset the drift list prior
        //to generating the brownian motion
        _driftList.clear();

        for (ArrayList<Location> row: _environment) {
            for (Location l: row) {
                if(Genomics.random().nextInt(100) == 0) {
                    Drift.DriftDirection d = Drift.randomDrift();
                    if (d != Drift.DriftDirection.NONE) {
                        _driftList.addDrift(l, Drift.DriftType.BROWNIAN, d);
                    }//end if
                }//end if
            }//end for each
        }//end for each
    }
}
