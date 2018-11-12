# DistanceOptimizer

This project fetches the distances from google and save it into database.

## Getting Started

Consider destinations A, B, C and D — in which B, C and D are connected to A directly while B and C can also lead to D and B is not directly connected to C. Thus, forming a rhombus structure. Let A be the Source and D being the destination. While standing at the source (A) if the destination is D, the possible routes would be AD, BD and CD. Looking at the situation, the simplest route would be the shortest route — AD. At it’s simplest, a traditional application would call Google API to compute the distances of each route and then to determine the better one (in the above case — AD), thereby, increasing the latency. Furthermore, the response time of the application will drastically increase with the increase in number of source and destinations and different paths between source and destination.


“Distance Optimizer” is thus an improved solution for the above scenario. When an application’s user (end-user) is going to be affected by the processing of multiple Google API request hits (to compute paths), the Distance Optimizer is a perfect solution to the problem.
What Distance Optimizer does, is that, it saves the distances between a source and a destination to a connected database thus fetching the address from the database when required. But when does it hit the Google API? Well, it only hits Google API when a specific location is then not found in the database. Therefore, resulting in low response time (high performance) and thereby avoids “rate_limit” issues.

### Prerequisites
MongoDB.

Google api key(s) with permissions to distance matrix and geocoding apis.

Log4j file (Optional).

### Input File

[Sample address file](src/main/resources/sample_address.txt)

[Sample location file](src/main/resources/sample_coordinates.txt)  System can also generate this file from above address file

#### [DistanceOptimizerConfigurationDto](src/main/java/com/distance/optimizer/dto/DistanceOptimizerConfigurationDto.java)

#### [DistanceOptimizerService](src/main/java/com/distance/optimizer/service/DistanceOptimizerService.java)

#### [DistanceOptimizerRemoteService](src/main/java/com/distance/optimizer/service/remote/DistanceOptimizerRemoteService.java)

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

        //if you have location file then below is not required.
        distanceOptimizer.generateLocationFile("inputAddressFilePath", "outpuLocationFile");

        // fetchAddressesFromInputFileAndSaveInDatabase
        distanceOptimizer.generate("locationFile");
        //  fetchDistanceFromGoogleAndSaveInDatabase
        distanceOptimizer.saveLocal();
        //  getDistanceFromDatabaseElseDatabase.
        //  Once generate and saveLocal function save distances of all location, you can use this function in your code to fetch the             //  addresses.
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
