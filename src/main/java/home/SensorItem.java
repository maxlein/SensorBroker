package home;

import lombok.AllArgsConstructor;

/**
 * Created by max on 15.06.17.
 */
@AllArgsConstructor
public class SensorItem {
    private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(SensorItem.class);

    private int sensorValidTimeout = 60;

    public long lastUpdateTime;
    public Item item;
    String location;

    SensorItem(long lastUpdateTime, Item item, String location){
        this.lastUpdateTime = lastUpdateTime;
        this.item = item;
        this.location = location;
    }

    public static SensorItem createTemperatureSensorItem(String location){
        return new SensorItem(System.currentTimeMillis(), Item.createTemperatureItem("", location), location);
    }

    public static SensorItem createHumiditySensorItem(String location){
        return new SensorItem(System.currentTimeMillis(), Item.createHumidityItem("", location), location);
    }

    public boolean isValid() {
        long age = (System.currentTimeMillis() - lastUpdateTime)/1000;
        log.info("age of sensor is " + age + ", timeout: " + sensorValidTimeout);
        return age < sensorValidTimeout;
    }
}
