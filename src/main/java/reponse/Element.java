package reponse;

import model.Distance;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

import java.io.Serializable;

/**
 * Created by VenD on 8/24/2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Element implements Serializable {
    private Distance distance;
    private Duration duration;
    private DurationInTraffic durationInTraffic;
    private String status;

    @JsonProperty("distance")
    public Distance getDistance() {
        return distance;
    }

    public void setDistance(Distance distance) {
        this.distance = distance;
    }

    @JsonProperty("duration")
    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    @JsonProperty("duration_in_traffic")
    public DurationInTraffic getDurationInTraffic() {
        return durationInTraffic;
    }

    public void setDurationInTraffic(DurationInTraffic durationInTraffic) {
        this.durationInTraffic = durationInTraffic;
    }

    @JsonProperty("status")
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Element{" +
                "distance=" + distance +
                ", duration=" + duration +
                ", durationInTraffic=" + durationInTraffic +
                ", status='" + status + '\'' +
                '}';
    }
}
