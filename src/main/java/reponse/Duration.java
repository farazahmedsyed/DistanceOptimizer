package reponse;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

import java.io.Serializable;

/**
 * Created by VenD on 8/24/2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Duration implements Serializable {
    private String text;
    private Integer value;

    @JsonProperty("text")
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @JsonProperty("value")
    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Duration{" +
                "text='" + text + '\'' +
                ", value=" + value +
                '}';
    }
}

