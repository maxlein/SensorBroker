package home;

import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/**
 * Created by max on 15.06.17.
 */
@NoArgsConstructor
@Component
@Profile({"mqtt", "all"})
public class MqttSensorHandler extends IFSensorData {

    private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(MqttSensorHandler.class);

    public void addTemperatureReading(String location, String temperature){

        String id = location+temperature;
        if (!sensorItems.containsKey(id)) {
            sensorItems.put(id, SensorItem.createTemperatureSensorItem(location));
        }

        SensorItem sensorItem = sensorItems.get(id);
        sensorItems.get(id).item.setState(temperature);
        sensorItem.lastUpdateTime = System.currentTimeMillis();
        log.debug("Added temperature " + temperature);
    }
}
