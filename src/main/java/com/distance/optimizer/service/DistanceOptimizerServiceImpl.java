package com.distance.optimizer.service;

import com.distance.optimizer.dto.DataCollectionDto;
import com.distance.optimizer.dto.LocationPairDto;
import com.distance.optimizer.model.entity.Distance;
import com.distance.optimizer.exception.DistanceOptimizerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.distance.optimizer.utils.EntityHelper;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * @author FarazAhmed
 *
 */
@Component
public class DistanceOptimizerServiceImpl implements DistanceOptimizerService {

    @Autowired
    private LocationsGeneratorService locationsGeneratorService;
    @Autowired
    private PairGeneratorService pairGeneratorService;
    @Autowired
    private DistanceService distanceService;

    @Override
    public void generate(String sourceFile) throws FileNotFoundException, DistanceOptimizerException {
        if (EntityHelper.isStringNotSet(sourceFile)) {
            throw new DistanceOptimizerException("Source File is required");
        }
        List<String> coordinates = distanceService.getValidCoordinates(sourceFile);
        locationsGeneratorService.generate(coordinates);
        pairGeneratorService.generate(coordinates);
    }

    @Override
    public void saveLocal() {
        distanceService.executeLocal();
    }

    @Override
    public void saveRemote() {
        distanceService.executeRemote();
    }

    @Override
    public Distance getDistance(String srcLoc, String destLoc, Date departureTime, String trafficModel, Double fraction) throws DistanceOptimizerException {
        return distanceService.getDistance(srcLoc,destLoc,departureTime,trafficModel,fraction);
    }

    @Override
    public String getFetchStrategy() {
        return distanceService.getFetchStrategy();
    }

    @Override
    public void setFetchStrategy(String fetchStrategy) {
        distanceService.setFetchStrategy(fetchStrategy);
    }

    @Override
    public List<LocationPairDto> getDataForDataCollectionRemoteApi() throws DistanceOptimizerException {
        return distanceService.getDataForDataCollectionLocal();
    }

    @Override
    public void saveDataForDataCollectionRemoteApi(List<DataCollectionDto> dataCollectionDtos) throws IOException, DistanceOptimizerException {
        distanceService.saveDataForDataCollectionLocal(dataCollectionDtos);
    }
}
