package home;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by max on 15.06.17.
 */
public abstract class IFSensorData {

    protected Map<String,SensorItem> sensorItems = new HashMap<>();

    Map<String,SensorItem> getItems(){return sensorItems;};
}
