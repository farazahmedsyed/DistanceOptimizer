package v1;

import utils.EntityHelper;

import java.util.List;

/**
 * Created by Venturdive on 28/08/2018.
 */
public class Location {

    protected String sourceFile;
    protected String destinationFile;

    protected String DATE_TIME;
    protected String apiKey;
    protected String devURLGetLocations;
    protected String devURLSaveData;
    protected List<String> googleApiKeys;

    public Location(String sourceFile, String destinationFile) {
        this.sourceFile = sourceFile;
        this.destinationFile = destinationFile;
    }

    public Location(List<String> googleApiKeys, String devURLSaveData, String devURLGetLocations, String apiKey, String DATE_TIME) {
        this.googleApiKeys = googleApiKeys;
        this.devURLSaveData = devURLSaveData;
        this.devURLGetLocations = devURLGetLocations;
        this.apiKey = apiKey;
        this.DATE_TIME = DATE_TIME;
    }

    public String getSourceFile() {
        return sourceFile;
    }

    public void setSourceFile(String sourceFile) {
        this.sourceFile = sourceFile;
    }

    public String getDestinationFile() {
        return destinationFile;
    }

    public void setDestinationFile(String destinationFile) {
        this.destinationFile = destinationFile;
    }

    public String getDATE_TIME() {
        return DATE_TIME;
    }

    public void setDATE_TIME(String DATE_TIME) {
        this.DATE_TIME = DATE_TIME;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getDevURLGetLocations() {
        return devURLGetLocations;
    }

    public void setDevURLGetLocations(String devURLGetLocations) {
        this.devURLGetLocations = devURLGetLocations;
    }

    public String getDevURLSaveData() {
        return devURLSaveData;
    }

    public void setDevURLSaveData(String devURLSaveData) {
        this.devURLSaveData = devURLSaveData;
    }

    public List<String> getGoogleApiKeys() {
        return googleApiKeys;
    }

    public void setGoogleApiKeys(List<String> googleApiKeys) {
        this.googleApiKeys = googleApiKeys;
    }

    public void generate() throws Exception{
        if (EntityHelper.isStringNotSet(sourceFile) || EntityHelper.isStringNotSet(destinationFile)){
            throw new Exception("Source and destination File required");
        }
        LocationsGenerator.generate(sourceFile, destinationFile);
        PairGenerator.generate(sourceFile, "pair-" + destinationFile);
    }

    public void save() throws Exception{
        if (EntityHelper.isStringNotSet(devURLGetLocations)
                || EntityHelper.isListNotPopulated(googleApiKeys)
                || EntityHelper.isStringNotSet(devURLSaveData)
                || EntityHelper.isStringNotSet(apiKey)
                || EntityHelper.isStringNotSet(DATE_TIME) ){
            DistanceOptimizer distanceOptimizer = new DistanceOptimizer(DATE_TIME,apiKey,devURLGetLocations,devURLSaveData,googleApiKeys);
            distanceOptimizer.execute();
        }
    }

}
