package com.distance.optimizer.api.impl;

import com.distance.optimizer.dto.DataCollectionDto;
import com.distance.optimizer.dto.LocationPairDto;
import com.distance.optimizer.api.DistanceOptimizerRemoteApi;
import com.distance.optimizer.service.DistanceService;
import com.distance.optimizer.service.LocationsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author FarazAhmed
 *
 */
@Component
public class DistanceOptimizerRemoteApiImpl implements DistanceOptimizerRemoteApi {

    @Autowired
    private DistanceService distanceService;
    @Autowired
    private LocationsService locationsService;

    @Override
    public List<LocationPairDto> getDataForDataCollectionRemoteApi() {
        return locationsService.getDataForDataCollectionLocal();
    }

    @Override
    public void saveDataForDataCollectionRemoteApi(List<DataCollectionDto> dataCollectionDtos){
        distanceService.saveDataForDataCollectionLocal(dataCollectionDtos);
    }

}
