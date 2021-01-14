package com.distance.optimizer.service;

import com.distance.optimizer.dto.DataCollectionDto;
import com.distance.optimizer.dto.LocationPairDto;
import com.distance.optimizer.exception.DistanceOptimizerException;
import com.distance.optimizer.model.entity.Distance;
import com.distance.optimizer.model.entity.LocationString;
import com.distance.optimizer.utils.EntityHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @author FarazAhmed
 *
 */
@Component
public class DistanceOptimizerServiceImpl implements DistanceOptimizerService {

    @Autowired
    private LocationsService locationsService;
    @Autowired
    private PairGeneratorService pairGeneratorService;
    @Autowired
    private DistanceService distanceService;

    @Override
    public void createLocPairsWithLocStrings(List<String> addresses){
        Objects.requireNonNull(addresses);
        List<LocationString> locationStrings = locationsService.getAll();
        for (String destAddress : addresses){
            for (LocationString locationString : locationStrings){
                pairGeneratorService.save(locationString.getLoc(), destAddress);
            }
        }
    }

    @Override
    public void generate(String sourceFile) throws FileNotFoundException, DistanceOptimizerException {
        if (EntityHelper.isStringNotSet(sourceFile)) {
            throw new DistanceOptimizerException("Source File is required");
        }
        List<String> coordinates = distanceService.getValidCoordinates(sourceFile);
        locationsService.saveToDatabase(coordinates);
        pairGeneratorService.generate(coordinates);
    }

    @Override
    public void saveLocal() {
        distanceService.executeLocal();
    }

    @Override
    public Distance getDistance(String srcLoc, String destLoc, Date departureTime, Double fraction) throws DistanceOptimizerException {
        return distanceService.getDistance(srcLoc,destLoc,departureTime,fraction);
    }

    @Override
    public List<LocationPairDto> getDataForDataCollectionRemoteApi() throws DistanceOptimizerException {
        return distanceService.getDataForDataCollectionLocal();
    }

    @Override
    public void saveDataForDataCollectionRemoteApi(List<DataCollectionDto> dataCollectionDtos) throws IOException, DistanceOptimizerException {
        distanceService.saveDataForDataCollectionLocal(dataCollectionDtos);
    }

    @Override
    public void generateLocationFile(String inputFilePath, String outputFilePath) throws IOException, DistanceOptimizerException {
        locationsService.generate(inputFilePath,outputFilePath);
    }
}
