
package home;

import com.fasterxml.jackson.annotation.*;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static home.RestHandler.ITEMS_URL;
import static home.RoomNameMapper.roomNameMap;


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "link",
        "state",
        "type",
        "name",
        "label",
        "category",
        "tags",
        "groupNames",
        "stateDescription",
        "members"
})
public class Item {

    @JsonIgnore
    private static final String TEMP_ITEM_SUFFIX = "_temp_str";

    @JsonIgnore
    private static final String HUM_ITEM_SUFFIX = "_hum_str";

    @JsonProperty("link")
    private String link;
    @JsonProperty("state")
    private String state;
    @JsonProperty("type")
    private String type;
    @JsonProperty("name")
    private String name;
    @JsonProperty("label")
    private String label;
    @JsonProperty("category")
    private String category;
    @JsonProperty("tags")
    private List<String> tags = null;
    @JsonProperty("groupNames")
    private List<String> groupNames = null;
    @JsonProperty("stateDescription")
    private StateDescription stateDescription;
    @JsonProperty("members")
    private List<Object> members = null;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("link")
    public String getLink() {
        return link;
    }

    @JsonProperty("link")
    public void setLink(String link) {
        this.link = link;
    }

    @JsonProperty("state")
    public String getState() {
        return state;
    }

    @JsonProperty("state")
    public void setState(String state) {
        this.state = state;
    }

    @JsonProperty("type")
    public String getType() {
        return type;
    }

    @JsonProperty("type")
    public void setType(String type) {
        this.type = type;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("label")
    public String getLabel() {
        return label;
    }

    @JsonProperty("label")
    public void setLabel(String label) {
        this.label = label;
    }

    @JsonProperty("category")
    public String getCategory() {
        return category;
    }

    @JsonProperty("category")
    public void setCategory(String category) {
        this.category = category;
    }

    @JsonProperty("tags")
    public List<String> getTags() {
        return tags;
    }

    @JsonProperty("tags")
    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    @JsonProperty("groupNames")
    public List<String> getGroupNames() {
        return groupNames;
    }

    @JsonProperty("groupNames")
    public void setGroupNames(List<String> groupNames) {
        this.groupNames = groupNames;
    }

    @JsonProperty("stateDescription")
    public StateDescription getStateDescription() {
        return stateDescription;
    }

    @JsonProperty("stateDescription")
    public void setStateDescription(StateDescription stateDescription) {
        this.stateDescription = stateDescription;
    }

    @JsonProperty("members")
    public List<Object> getMembers() {
        return members;
    }

    @JsonProperty("members")
    public void setMembers(List<Object> members) {
        this.members = members;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    @JsonIgnore
    public static Item createTemperatureItem(String state, String location) {
        List<String> groups = new ArrayList<>();
        groups.add("gTemperature");

        String itemName = location + TEMP_ITEM_SUFFIX;

        Item item = new Item();
        item.setCategory("temperature");
        item.setLabel("Temperatur " + (roomNameMap.containsKey(location) ? roomNameMap.get(location) : location));
        item.setName(itemName);
        item.setType("Number");
        item.setState(state);
        item.setLink(ITEMS_URL + itemName);
        item.setGroupNames(groups);

        StateDescription stateDescription = new StateDescription();
        stateDescription.setPattern("%.1f °C");
        stateDescription.setReadOnly(false);
        item.setStateDescription(stateDescription);

        return item;
    }

    @JsonIgnore
    public static Item createHumidityItem(String state, String location) {
        List<String> groups = new ArrayList<>();
        groups.add("gWeather");

        String itemName = location + HUM_ITEM_SUFFIX;

        Item item = new Item();
        item.setCategory("humidity");
        item.setLabel("Humidity " + (roomNameMap.containsKey(location) ? roomNameMap.get(location) : location));
        item.setName(itemName);
        item.setType("Number");
        item.setState(state);
        item.setLink(ITEMS_URL + itemName);
        item.setGroupNames(groups);

        StateDescription stateDescription = new StateDescription();
        stateDescription.setPattern("%.1f %%");
        stateDescription.setReadOnly(false);
        item.setStateDescription(stateDescription);

        return item;
    }
}