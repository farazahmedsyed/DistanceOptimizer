package dto;

import java.io.Serializable;

/**
 * Created by Venturedive on 9/13/2017.
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
    private Integer serviceAreaId;

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

    public Integer getServiceAreaId() {
        return serviceAreaId;
    }

    public void setServiceAreaId(Integer serviceAreaId) {
        this.serviceAreaId = serviceAreaId;
    }
}
