package home;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import static home.FileHelper.getTemperatureFromDS1820SensorReading;

/**
 * Created by max on 15.06.17.
 */
@Component
@Profile({"local", "all"})
public class LocalSensorHandler extends IFSensorData {

    private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(LocalSensorHandler.class);

    @Autowired
    ItemHandler itemHandler;

    private static final String ROOM_ENV = "ROOM";
    private static final String SENSOR_FOLDER = "/sys/bus/w1/devices/";
    private static final String STARTING_WITH = "28-";
    private static final String SENSOR_DATA_FAILE = "w1_slave";

    String roomName = System.getenv(ROOM_ENV) == null ? "unknown_room" : System.getenv(ROOM_ENV);

    LocalSensorHandler() {
        log.info("Started LocalSensorHandler");
    }

    @Scheduled(fixedRateString = "${rest.localSensor.updateRate}")
    private void updateSensorData(){
        String temperature = getTemperature();

        if (temperature == null){
            return;
        }

        String id = roomName+temperature;
        if (!sensorItems.containsKey(id)) {
            sensorItems.put(id, SensorItem.createTemperatureSensorItem(roomName));
        }

        SensorItem sensorItem = sensorItems.get(id);
        sensorItems.get(id).item.setState(temperature);
        sensorItem.lastUpdateTime = System.currentTimeMillis();
        log.debug("Added temperature " + temperature);
    }

    private String getTemperature(){
        try {
            String temperature = getTemperatureFromDS1820SensorReading(SENSOR_FOLDER, STARTING_WITH, SENSOR_DATA_FAILE);
            if (temperature == null){
                log.error("Error reading temperature!");
                return null;
            }

            Integer temp = Integer.parseInt(temperature);
            Double temp_conv = temp / 1000.0;
            temperature = String.valueOf(temp_conv);
            log.info(temperature + ", int: " + temp + ", double: " + temp_conv);
            return temperature;}
        catch (Exception e){
            log.error("getTemperature(): Exception=" + e.getMessage());
        }
        return null;
    }
}
