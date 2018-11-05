package com.distance.optimizer.service;

import com.distance.optimizer.dto.DataCollectionDto;
import com.distance.optimizer.dto.LocationPairDto;
import com.distance.optimizer.model.entity.Distance;
import com.distance.optimizer.exception.DistanceOptimizerException;

import java.io.FileNotFoundException;
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
public interface DistanceOptimizerService {

    /**
     * Read the sourcefile for location in each line and save it to database.
     * <p>The data will grow as the number of addresses increases.</p>
     * @param sourceFile The full path to address file.
     *                   The Address file should contain location in each line
     *                   <b>Format : latitude,longitude</b>
     *                   1.0,0.1
     *                   1.9,2.1
     * @throws DistanceOptimizerException           if parameter is null or empty.
     * @throws FileNotFoundException if source file not found.
     */
    void generate(String sourceFile) throws FileNotFoundException, DistanceOptimizerException;

    /**
     * It retrieved unprocessed location from database, fetch the distance from google and save it in database.
     *
     * <p>If unable to fetch address for any specific location, then it will log the error and continue its execution.</p>
     */
    void saveLocal();

    /**
     * @return Distance from database if not found then fetch it from google.
     * @throws DistanceOptimizerException if unable to retreive distance from google.
     *
     * @param srcLoc source Location.
     * @param destLoc Destination Location.
     * @param departureTime DepartureTime
     * @param trafficModel Traffic Model
     * @param fraction Fraction
     * */
    Distance getDistance (String srcLoc, String destLoc, Date departureTime, String trafficModel, Double fraction) throws DistanceOptimizerException;

    /**
     * @return unprocessed location pairs fetched for api
     * @throws DistanceOptimizerException
     * */
    List<LocationPairDto> getDataForDataCollectionRemoteApi() throws DistanceOptimizerException;

    /**
     * save dataCollectionDtos from api
     * @param dataCollectionDtos distances fetched for un processed locations.
     * @throws IOException
     * @throws DistanceOptimizerException
     * */
    void saveDataForDataCollectionRemoteApi(List<DataCollectionDto> dataCollectionDtos) throws IOException, DistanceOptimizerException;
}
