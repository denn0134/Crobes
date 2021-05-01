package crobes.core;

/***
 * Holds the environmental properties for a location.
 */
public class Elements
{
    private int _ambientLight;
    private int _environmentalLight;
    private int _ambientTemperature;
    private int _environmentalTemperature;

    /***
     * Sets the ambient light level, the constant
     * level of light available in a location.
     * @param light Light level in lumens.
     */
    public void ambientLight(int light) {
        if (light > _ambientLight)
            _ambientLight = light;
    }
    /***
     * Sets the environmental light, the transitory
     * level of light available in a location at a
     * moment in time measured in lumens.
     * @param light
     */
    public void environmentalLight(int light) {
        _environmentalLight = light;
    }
    /***
     * Retrieves the overall light level in a location.
     * @return Returns the overall light level in lumens.
     */
    public int lightLevel() {
        return _ambientLight + _environmentalLight;
    }

    /***
     * Sets the ambient temperature level, the constant
     * background temperature level in a location.  Ambient
     * temperature can build up from multiple sources as
     * it dissipates at a relatively slow rate.
     * @param temp Temperature level.
     */
    public void ambientTemperature(int temp) {
        _ambientTemperature /= 3;
        _ambientTemperature += temp;
    }
    /***
     * Sets the environmental temperature level, the
     * transient temperature level in a location at a
     * moment in time.  Environmental temperature
     * levels dissipate relatively slowly and can build
     * up over time.
     * @param temp Temperature level.
     */
    public void environmentalTemperature(int temp) {
        _environmentalTemperature += temp;
    }
    /***
     * Retrieves the overall temperature in a location.
     * @return Returns the oveerall temperature level.
     */
    public int temperatureLevel() {
        return _ambientTemperature + _environmentalTemperature;
    }

    /***
     * Resets all elements to their base values for the start
     * of a new turn.
     */
    public void reset() {
        _environmentalLight = 0;
        _environmentalTemperature /= 2;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("====================\n");
        sb.append("Light level:\n");
        sb.append(String.format("  amb: %1$d  cur: %2$d", _ambientLight, lightLevel()));
        sb.append("\n");
        sb.append("Thermal level:\n");
        sb.append(String.format("  amb: %1$d  cur: %2$d", _ambientTemperature, temperatureLevel()));
        sb.append("\n");

        return sb.toString();
    }
}
