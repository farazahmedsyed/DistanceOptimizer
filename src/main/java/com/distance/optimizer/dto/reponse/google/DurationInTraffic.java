package com.distance.optimizer.dto.reponse.google;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

import java.io.Serializable;

/**
 * @author FarazAhmed
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class DurationInTraffic implements Serializable {
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
        return "DurationInTraffic{" +
                "text='" + text + '\'' +
                ", value=" + value +
                '}';
    }
}

