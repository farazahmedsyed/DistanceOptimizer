package reponse;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

import java.io.Serializable;
import java.util.List;

/**
 * Created by VenD on 8/24/2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Row implements Serializable {
    private List<Element> elements;

    @JsonProperty("elements")
    public List<Element> getElements() {
        return elements;
    }

    public void setElements(List<Element> elements) {
        this.elements = elements;
    }

    @Override
    public String toString() {
        return "Row{" +
                "elements=" + elements +
                '}';
    }
}

