package com.distance.optimizer.model.entity;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

/**
 * @author FarazAhmed
 */
@Document(collection = "distances")
public class Distance {
    private Location src;
    private Location dest;
    private DistanceInfo distance;
    private DistanceInfo duration;
    private DistanceInfo durationInTraffic;
    private String trafficModel;
    private String date;
    private Integer hour;
    private Integer minute;
    private String dayOfWeek;
    private String status;
    private String errorMessage;
    private Date updatedAt;

    @Field("s")
    public Location getSrc() {
        return src;
    }

    public void setSrc(Location src) {
        this.src = src;
    }

    @Field("d")
    public Location getDest() {
        return dest;
    }

    public void setDest(Location dest) {
        this.dest = dest;
    }

    @Field("dis")
    public DistanceInfo getDistance() {
        return distance;
    }

    public void setDistance(DistanceInfo distance) {
        this.distance = distance;
    }

    @Field("dur")
    public DistanceInfo getDuration() {
        return duration;
    }

    public void setDuration(DistanceInfo duration) {
        this.duration = duration;
    }

    @Field("durT")
    public DistanceInfo getDurationInTraffic() {
        return durationInTraffic;
    }

    public void setDurationInTraffic(DistanceInfo durationInTraffic) {
        this.durationInTraffic = durationInTraffic;
    }

    @Field("tm")
    public String getTrafficModel() {
        return trafficModel;
    }

    public void setTrafficModel(String trafficModel) {
        this.trafficModel = trafficModel;
    }

    @Field("dt")
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Field("h")
    public Integer getHour() {
        return hour;
    }

    public void setHour(Integer hour) {
        this.hour = hour;
    }

    @Field("m")
    public Integer getMinute() {
        return minute;
    }

    public void setMinute(Integer minute) {
        this.minute = minute;
    }

    @Field("dow")
    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    @Field("st")
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Field("err")
    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Field("update")
    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Distance distance1 = (Distance) o;

        if (src != null ? !src.equals(distance1.src) : distance1.src != null) return false;
        if (dest != null ? !dest.equals(distance1.dest) : distance1.dest != null) return false;
        if (distance != null ? !distance.equals(distance1.distance) : distance1.distance != null) return false;
        if (duration != null ? !duration.equals(distance1.duration) : distance1.duration != null) return false;
        if (durationInTraffic != null ? !durationInTraffic.equals(distance1.durationInTraffic) : distance1.durationInTraffic != null)
            return false;
        if (trafficModel != null ? !trafficModel.equals(distance1.trafficModel) : distance1.trafficModel != null)
            return false;
        if (date != null ? !date.equals(distance1.date) : distance1.date != null) return false;
        if (hour != null ? !hour.equals(distance1.hour) : distance1.hour != null) return false;
        if (minute != null ? !minute.equals(distance1.minute) : distance1.minute != null) return false;
        if (dayOfWeek != null ? !dayOfWeek.equals(distance1.dayOfWeek) : distance1.dayOfWeek != null) return false;
        if (status != null ? !status.equals(distance1.status) : distance1.status != null) return false;
        if (errorMessage != null ? !errorMessage.equals(distance1.errorMessage) : distance1.errorMessage != null)
            return false;
        return !(updatedAt != null ? !updatedAt.equals(distance1.updatedAt) : distance1.updatedAt != null);

    }

    @Override
    public int hashCode() {
        int result = src != null ? src.hashCode() : 0;
        result = 31 * result + (dest != null ? dest.hashCode() : 0);
        result = 31 * result + (distance != null ? distance.hashCode() : 0);
        result = 31 * result + (duration != null ? duration.hashCode() : 0);
        result = 31 * result + (durationInTraffic != null ? durationInTraffic.hashCode() : 0);
        result = 31 * result + (trafficModel != null ? trafficModel.hashCode() : 0);
        result = 31 * result + (date != null ? date.hashCode() : 0);
        result = 31 * result + (hour != null ? hour.hashCode() : 0);
        result = 31 * result + (minute != null ? minute.hashCode() : 0);
        result = 31 * result + (dayOfWeek != null ? dayOfWeek.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (errorMessage != null ? errorMessage.hashCode() : 0);
        result = 31 * result + (updatedAt != null ? updatedAt.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Distance{" +
                "src=" + src +
                ", dest=" + dest +
                ", distance=" + distance +
                ", duration=" + duration +
                ", durationInTraffic=" + durationInTraffic +
                ", trafficModel='" + trafficModel + '\'' +
                ", date='" + date + '\'' +
                ", hour=" + hour +
                ", minute=" + minute +
                ", dayOfWeek='" + dayOfWeek + '\'' +
                ", status='" + status + '\'' +
                ", errorMessage='" + errorMessage + '\'' +
                ", updatedAt=" + updatedAt +
                '}';
    }
}