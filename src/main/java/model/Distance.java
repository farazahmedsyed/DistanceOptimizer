package model;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

import java.io.Serializable;

/**
 * Created by VenD on 8/24/2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Distance implements Serializable {
    private String text;
    private long value;

    @JsonProperty("text")
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @JsonProperty("value")
    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Distance{" +
                "text='" + text + '\'' +
                ", value=" + value +
                '}';
    }
}

