package crobes.core;

public class IncompatibleGenomeException extends Exception
{
    private static final String IGE_FMT = "%1$s is not compatible with %2$s";

    private String _genome;
    private String _badGenome;

    public IncompatibleGenomeException(String genome, String badGenome) {
        _genome = genome;
        _badGenome = badGenome;
    }

    @Override
    public String getMessage() {
        return super.getMessage() +
                String.format(IGE_FMT, _genome, _badGenome);
    }
}
