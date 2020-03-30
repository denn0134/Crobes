import java.util.ArrayList;

public class CrobeColony extends ArrayList<Crobe>
{
    @Override
    public boolean add(Crobe crobe) {
        boolean result = super.add(crobe);
        crobe.parent = this;
        return result;
    }
}
