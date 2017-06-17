package home;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Created by max on 07.06.17.
 */
@Data
@Component
public class ItemHandler {
    private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(ItemHandler.class);

    @Autowired
    private RestHandler restHandler;

    @Autowired
    private List<IFSensorData> ifSensorData;

    @Value("${rest.item.updateRate}")
    private String updateRate;

    private long startTime = System.currentTimeMillis();

    ItemHandler() {
        log.info("Started ItemHandler");
    }

    @Scheduled(fixedRateString = "${rest.item.updateRate}")
    private void updateItems(){
        try {
            if (ifSensorData.size() == 0) {
                log.info("ItemHandler(): No ifSensorData !");
                return;
            }

            for (IFSensorData sensorData : ifSensorData) {
                log.debug(sensorData.getClass());
                updateSensorsOfSensorType(sensorData.getItems());
            }
        }
        catch (Exception e) {
            log.error("Exception occurred: %s" + e.getMessage());
        }
    }

    private void updateSensorsOfSensorType(Map<String,SensorItem> stringSensorItemMap){
        for (SensorItem sensorItem : stringSensorItemMap.values()){

            if (sensorItem == null) {
                log.debug("ItemHandler(): sensorItem is null!");
                continue;
            }

            //log.warn("Demo mode active! " + sensorItem.item.toString());
            updateRestItem(sensorItem);
        }
    }

    private void updateRestItem(SensorItem sensorItem){
        if (sensorItem.isValid()) {
            restHandler.updateItemState(sensorItem.item);
            return;
        }
        log.info(sensorItem.item.getName() + " is not valid!");
    }
}
