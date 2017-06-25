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

        SensorItem item = SensorItem.createTemperatureSensorItem(location);
        String id = item.item.getName();
        addItemIfAbsent(item);

        SensorItem sensorItem = sensorItems.get(id);
        sensorItems.get(id).item.setState(temperature);
        sensorItem.lastUpdateTime = System.currentTimeMillis();
        log.debug("Added temperature " + temperature);
    }
}
