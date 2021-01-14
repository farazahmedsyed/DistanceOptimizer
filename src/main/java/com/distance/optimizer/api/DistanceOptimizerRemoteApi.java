package com.distance.optimizer.api;

import com.distance.optimizer.dto.DataCollectionDto;
import com.distance.optimizer.dto.LocationPairDto;
import com.distance.optimizer.exception.DistanceOptimizerException;
import com.distance.optimizer.model.entity.Distance;

import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * @author FarazAhmed
 * <h1>Main File : It provides access point to functionalities this library is created for.</h1>
 *
 * <p>It requires mongodb connection as created for spring-data.</p>
 * <p>It will save data into three collections of database(create collections if not exist).</p>
 *
 * <p>Collections: location_strings, location_pairs and distances</p>
 * <p>The database size will grow as the address increases.</p>
 */
public interface DistanceOptimizerRemoteApi {

    /**
     * @return unprocessed location pairs fetched for api
     * */
    List<LocationPairDto> getDataForDataCollectionRemoteApi();

    /**
     * save dataCollectionDtos from api
     * @param dataCollectionDtos distances fetched for un processed locations.
     * @throws IOException
     * */
    void saveDataForDataCollectionRemoteApi(List<DataCollectionDto> dataCollectionDtos);

}
