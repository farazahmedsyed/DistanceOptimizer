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
    private String fetchStrategy = "best_guess";

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

    /**
     * @param dateTime time at which distance is required
     * */
    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    /**
     * @param apiKey Api key, created by developer for get and post api.
     * */
    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    /**
     * @param devURLGetLocations url created by developer for get api.
     * */
    public void setDevURLGetLocations(String devURLGetLocations) {
        this.devURLGetLocations = devURLGetLocations;
    }

    /**
     * @param devURLSaveData url created by developer for post api.
     * */
    public void setDevURLSaveData(String devURLSaveData) {
        this.devURLSaveData = devURLSaveData;
    }

    /**
     * @param googleApiKeys with permissions to distance matrix and geo coding api.
     * */
    public void setGoogleApiKeys(List<String> googleApiKeys) {
        this.googleApiKeys = googleApiKeys;
    }

    /**
     * @param port Required only if developer need to create spring data mongodb object.
     * */
    public void setPort(Integer port) {
        this.port = port;
    }

    /**
     * @param databaseName Required only if developer need to create spring data mongodb object.
     * */
    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    /**
     * @param schedulerCronExpression if set then a cron job will execute to update the distances saved in database.
     * */
    public void setSchedulerCronExpression(String schedulerCronExpression) {
        this.schedulerCronExpression = schedulerCronExpression;
    }

    /**
     * @param fetchStrategy default is best_guess though developer can update it.
     * */
    public void setFetchStrategy(String fetchStrategy) {
        this.fetchStrategy = fetchStrategy;
    }

    /**
     * Getter
     * */
    public String getDateTime() {
        return dateTime;
    }

    /**
     * Getter
     * */
    public String getApiKey() {
        return apiKey;
    }

    /**
     * Getter
     * */
    public String getDevURLGetLocations() {
        return devURLGetLocations;
    }

    /**
     * Getter
     * */
    public String getDevURLSaveData() {
        return devURLSaveData;
    }

    /**
     * Getter
     * */
    public List<String> getGoogleApiKeys() {
        return googleApiKeys;
    }

    /**
     * Getter
     * */
    public String getServerAddress() {
        return serverAddress;
    }

    /**
     * Getter
     * */
    public void setServerAddress(String serverAddress) {
        this.serverAddress = serverAddress;
    }

    /**
     * Getter
     * */
    public Integer getPort() {
        return port;
    }

    /**
     * Getter
     * */
    public String getDatabaseName() {
        return databaseName;
    }

    /**
     * Getter
     * */
    public String getSchedulerCronExpression() {
        return schedulerCronExpression;
    }

    /**
     * Getter
     * */
    public String getFetchStrategy() {
        return fetchStrategy;
    }

}
