package home;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by max on 15.06.17.
 */
public class RoomNameMapper {

    public static final Map<String, String> roomNameMap;
    static
    {
        roomNameMap = new HashMap<String, String>();
        roomNameMap.put("bedroom", "Schlafzimmer");
        roomNameMap.put("weather_station", "Wetterstation");
    }
}
