package home;

import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
@Profile({"mqtt", "all"})
public class MqttHumiditySensorHandler extends IFSensorData {

    private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(MqttHumiditySensorHandler.class);

    private SensorItem sensorItem = null;

    public void addHumidityReading(String location, String humidity){

        String id = location+humidity;
        if (!sensorItems.containsKey(id)) {
            sensorItems.put(id, SensorItem.createHumiditySensorItem(location));
        }

        SensorItem sensorItem = sensorItems.get(id);
        sensorItems.get(id).item.setState(humidity);
        sensorItem.lastUpdateTime = System.currentTimeMillis();
        log.debug("Added humidity " + humidity);
    }
}
