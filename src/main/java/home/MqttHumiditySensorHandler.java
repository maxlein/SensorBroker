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

        SensorItem item = SensorItem.createHumiditySensorItem(location);
        String id = item.item.getName();
        addItemIfAbsent(item);

        SensorItem sensorItem = sensorItems.get(id);
        sensorItems.get(id).item.setState(humidity);
        sensorItem.lastUpdateTime = System.currentTimeMillis();
        log.debug("Added humidity " + humidity);
    }
}
