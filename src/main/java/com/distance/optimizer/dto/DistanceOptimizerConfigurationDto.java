package com.distance.optimizer.dto;

import java.io.Serializable;
import java.util.List;

/**
 * @author FarazAhmed
 *         <p>This library requires an instance of this class to save distances.
 *         Required when we need to fetch distances from google.</p>
 */
public class DistanceOptimizerConfigurationDto implements Serializable{
    private String dateTime;
    private String apiKey;
    private String devURLGetLocations;
    private String devURLSaveData;
    private List<String> googleApiKeys;
    private String serverAddress;
    private Integer port;
    private String databaseName;
    private String schedulerCronExpression;

    /**
     * It is for the purpose that if we only need to save location in database.
     * Do not use it if we required to fetch distance from google
     */
    public DistanceOptimizerConfigurationDto() {
    }

    /**
     * If we need to fetch and update location through database and find distance from google.
     * Do not use it if we want to fetch and save through apis.
     *
     * @param dateTime     Date of distance.
     * @param apiKey        get/save com.distanceoptimizer.api apiKey
     * @param googleApiKeys google apikeys.
     */
    public DistanceOptimizerConfigurationDto(String dateTime, String apiKey, List<String> googleApiKeys) {
        this.dateTime = dateTime;
        this.apiKey = apiKey;
        this.googleApiKeys = googleApiKeys;
    }

    /**
     * If we need to fetch and update location through apis and find distance from google.
     *
     * @param dateTime          Date of distance.
     * @param apiKey             get/save com.distanceoptimizer.api apiKey
     * @param devURLGetLocations Api Get location
     * @param devURLSaveData     Api save Location
     * @param googleApiKeys      google apikeys.
     */
    public DistanceOptimizerConfigurationDto(String dateTime, String apiKey, String devURLGetLocations, String devURLSaveData, List<String> googleApiKeys) {
        this.dateTime = dateTime;
        this.apiKey = apiKey;
        this.devURLGetLocations = devURLGetLocations;
        this.devURLSaveData = devURLSaveData;
        this.googleApiKeys = googleApiKeys;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public void setDevURLGetLocations(String devURLGetLocations) {
        this.devURLGetLocations = devURLGetLocations;
    }

    public void setDevURLSaveData(String devURLSaveData) {
        this.devURLSaveData = devURLSaveData;
    }

    public void setGoogleApiKeys(List<String> googleApiKeys) {
        this.googleApiKeys = googleApiKeys;
    }

    public String getDateTime() {
        return dateTime;
    }

    public String getApiKey() {
        return apiKey;
    }

    public String getDevURLGetLocations() {
        return devURLGetLocations;
    }

    public String getDevURLSaveData() {
        return devURLSaveData;
    }

    public List<String> getGoogleApiKeys() {
        return googleApiKeys;
    }

    public String getServerAddress() {
        return serverAddress;
    }

    public void setServerAddress(String serverAddress) {
        this.serverAddress = serverAddress;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public String getSchedulerCronExpression() {
        return schedulerCronExpression;
    }

    public void setSchedulerCronExpression(String schedulerCronExpression) {
        this.schedulerCronExpression = schedulerCronExpression;
    }

}
