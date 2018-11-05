package com.distance.optimizer.dto;

import java.io.Serializable;

/**
 * @author FarazAhmed
 */
public class DataCollectionDto implements Serializable {
    private String srcLocString;
    private String destLocString;
    private DistanceInfoDto distance;
    private DistanceInfoDto duration;
    private DistanceInfoDto durationInTraffic;
    private String trafficModel;
    private Integer hour;
    private Integer minute;
    private String dayOfWeek;
    private String status;
    private String errorMessage;

    public String getSrcLocString() {
        return srcLocString;
    }

    public void setSrcLocString(String srcLocString) {
        this.srcLocString = srcLocString;
    }

    public String getDestLocString() {
        return destLocString;
    }

    public void setDestLocString(String destLocString) {
        this.destLocString = destLocString;
    }

    public DistanceInfoDto getDistance() {
        return distance;
    }

    public void setDistance(DistanceInfoDto distance) {
        this.distance = distance;
    }

    public DistanceInfoDto getDuration() {
        return duration;
    }

    public void setDuration(DistanceInfoDto duration) {
        this.duration = duration;
    }

    public DistanceInfoDto getDurationInTraffic() {
        return durationInTraffic;
    }

    public void setDurationInTraffic(DistanceInfoDto durationInTraffic) {
        this.durationInTraffic = durationInTraffic;
    }

    public String getTrafficModel() {
        return trafficModel;
    }

    public void setTrafficModel(String trafficModel) {
        this.trafficModel = trafficModel;
    }

    public Integer getHour() {
        return hour;
    }

    public void setHour(Integer hour) {
        this.hour = hour;
    }

    public Integer getMinute() {
        return minute;
    }

    public void setMinute(Integer minute) {
        this.minute = minute;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DataCollectionDto that = (DataCollectionDto) o;

        if (srcLocString != null ? !srcLocString.equals(that.srcLocString) : that.srcLocString != null) return false;
        if (destLocString != null ? !destLocString.equals(that.destLocString) : that.destLocString != null)
            return false;
        if (distance != null ? !distance.equals(that.distance) : that.distance != null) return false;
        if (duration != null ? !duration.equals(that.duration) : that.duration != null) return false;
        if (durationInTraffic != null ? !durationInTraffic.equals(that.durationInTraffic) : that.durationInTraffic != null)
            return false;
        if (trafficModel != null ? !trafficModel.equals(that.trafficModel) : that.trafficModel != null) return false;
        if (hour != null ? !hour.equals(that.hour) : that.hour != null) return false;
        if (minute != null ? !minute.equals(that.minute) : that.minute != null) return false;
        if (dayOfWeek != null ? !dayOfWeek.equals(that.dayOfWeek) : that.dayOfWeek != null) return false;
        if (status != null ? !status.equals(that.status) : that.status != null) return false;
        return !(errorMessage != null ? !errorMessage.equals(that.errorMessage) : that.errorMessage != null);

    }

    @Override
    public int hashCode() {
        int result = srcLocString != null ? srcLocString.hashCode() : 0;
        result = 31 * result + (destLocString != null ? destLocString.hashCode() : 0);
        result = 31 * result + (distance != null ? distance.hashCode() : 0);
        result = 31 * result + (duration != null ? duration.hashCode() : 0);
        result = 31 * result + (durationInTraffic != null ? durationInTraffic.hashCode() : 0);
        result = 31 * result + (trafficModel != null ? trafficModel.hashCode() : 0);
        result = 31 * result + (hour != null ? hour.hashCode() : 0);
        result = 31 * result + (minute != null ? minute.hashCode() : 0);
        result = 31 * result + (dayOfWeek != null ? dayOfWeek.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (errorMessage != null ? errorMessage.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "DataCollectionDto{" +
                "srcLocString='" + srcLocString + '\'' +
                ", destLocString='" + destLocString + '\'' +
                ", distance=" + distance +
                ", duration=" + duration +
                ", durationInTraffic=" + durationInTraffic +
                ", trafficModel='" + trafficModel + '\'' +
                ", hour=" + hour +
                ", minute=" + minute +
                ", dayOfWeek='" + dayOfWeek + '\'' +
                ", status='" + status + '\'' +
                ", errorMessage='" + errorMessage + '\'' +
                '}';
    }
}
