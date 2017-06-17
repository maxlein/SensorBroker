package home;

import com.fasterxml.jackson.annotation.*;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "pattern",
        "readOnly",
        "options",
        "minimum",
        "maximum",
        "step"
})
public class StateDescription {

    @JsonProperty("pattern")
    private String pattern;
    @JsonProperty("readOnly")
    private Boolean readOnly;
    @JsonProperty("options")
    private List<Object> options = null;
    @JsonProperty("minimum")
    private Integer minimum;
    @JsonProperty("maximum")
    private Integer maximum;
    @JsonProperty("step")
    private Integer step;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("pattern")
    public String getPattern() {
        return pattern;
    }

    @JsonProperty("pattern")
    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    @JsonProperty("readOnly")
    public Boolean getReadOnly() {
        return readOnly;
    }

    @JsonProperty("readOnly")
    public void setReadOnly(Boolean readOnly) {
        this.readOnly = readOnly;
    }

    @JsonProperty("options")
    public List<Object> getOptions() {
        return options;
    }

    @JsonProperty("options")
    public void setOptions(List<Object> options) {
        this.options = options;
    }

    @JsonProperty("minimum")
    public Integer getMinimum() {
        return minimum;
    }

    @JsonProperty("minimum")
    public void setMinimum(Integer minimum) {
        this.minimum = minimum;
    }

    @JsonProperty("maximum")
    public Integer getMaximum() {
        return maximum;
    }

    @JsonProperty("maximum")
    public void setMaximum(Integer maximum) {
        this.maximum = maximum;
    }

    @JsonProperty("step")
    public Integer getStep() {
        return step;
    }

    @JsonProperty("step")
    public void setStep(Integer step) {
        this.step = step;
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

}
