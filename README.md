# DistanceOptimizer

This project fetches the distances from google and save it into database.

## Getting Started

Consider following locations:

A,B,C and D.

Suppose we are at location A.
We have to go to location B and location C.
We also want to go to location D. 
Possible routes :  A->D, B->D or C->D 
Keeping it simple the best route would be the shortest route.
So we need to find out the distance of all above routes.
It will be a time consuming process we need to call google api each time. 
Hence, response time will increase with increase in number of source or destination locations.

DistanceOptimizer is for such scenarios, when we need to hit google api multiple times which will effect our end-users.

DistanceOptimizer save the distances to your database and then fetch the address from database, it only hits google when it does not found the location in database.
It results in high response time and also avoid rate_limit issues.
### Prerequisites
MongoDB.

Google api key(s).

Log4j file (Optional).

### Input File

[Sample address file](src/main/resources/sample_coordinates.txt)

### Example 1
```
 private void workFlow() throws IOException, DistanceOptimizerException {
        DistanceOptimizerConfigurationDto distanceOptimizerConfiguration = new DistanceOptimizerConfigurationDto();
        distanceOptimizerConfiguration.setDatabaseName("test");
        distanceOptimizerConfiguration.setServerAddress("localhost");
        distanceOptimizerConfiguration.setPort(27017);
        distanceOptimizerConfiguration.setGoogleApiKeys(Arrays.asList("google-api-key-1", "google-api-key-2"));
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        distanceOptimizerConfiguration.setDateTime(df.format(new Date()));
        DistanceOptimizerService distanceOptimizer = DistanceOptimizerLoaderService.initializeInstance(distanceOptimizerConfiguration);


        // fetchAddressesFromInputFileAndSaveInDatabase
        distanceOptimizer.generate("full_path_to_address_file");
        //  fetchDistanceFromGoogleAndSaveInDatabase 
        distanceOptimizer.saveLocal();

         //  getDistanceFromDatabaseElseDatabase. 
         //  Once generate and saveLocal function save distances of all location, you can use this function in your code to fetch the addresses. 
         //  It will find the distance from database and if not found then fetch it from google. 
        distanceOptimizer.getDistance("sourceAddress", "destinationAddress", new Date(), 0.0);
         //   createApiResourceGet 
         //   With this function you can create an api which returns the location pairs whose distance is not yet found. 
        distanceOptimizer.getDataForDataCollectionRemoteApi();
        //  createApiResourcePost. 
        //  With this function you can create an api which save the distances of location pairs. 
        List<DataCollectionDto> dataCollectionDtos = new ArrayList<>();
        distanceOptimizer.saveDataForDataCollectionRemoteApi(dataCollectionDtos);
    }
 

```
### Example 2
```

    // With this function we can create a client application which will fetch the location pairs from your provided api and then 
    // find the distance from google and post it to your api. 
    private void fetchAndSaveDistanceApi(){
        DistanceOptimizerConfigurationDto distanceOptimizerConfigurationDto = new DistanceOptimizerConfigurationDto();
        distanceOptimizerConfigurationDto.setDevURLGetLocations("createApiResourceGet");
        distanceOptimizerConfigurationDto.setDevURLSaveData("createApiResourcePost");
        DistanceOptimizerRemoteService distanceOptimizerRemoteService = new DistanceOptimizerRemoteService(distanceOptimizerConfigurationDto);

        //  This function will fetch the location pairs whose distance is not yet found, fetch the distance from google and post it to the api. 

        distanceOptimizerRemoteService.executeRemote();
    }

```
