package com.distance.optimizer.api.impl;

import com.distance.optimizer.api.DistanceOptimizerExecutorApi;
import com.distance.optimizer.dto.DataCollectionDto;
import com.distance.optimizer.dto.DisOptimizerLocalDto;
import com.distance.optimizer.dto.LocationPairDto;
import com.distance.optimizer.service.DistanceService;
import com.distance.optimizer.service.LocationProcessorService;
import com.distance.optimizer.service.LocationsService;
import com.distance.optimizer.utils.ValidationUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

public class DistanceOptimizerExecutorLocalApi implements DistanceOptimizerExecutorApi {
    public static final Logger LOGGER = Logger.getLogger(DistanceOptimizerExecutorLocalApi.class);

    private DisOptimizerLocalDto disOptimizerLocalDto;
    private LocationsService locationsService;
    private LocationProcessorService locationProcessorService;
    private DistanceService distanceService;

    @Autowired
    public DistanceOptimizerExecutorLocalApi(DisOptimizerLocalDto disOptimizerLocalDto,
                                             LocationsService locationsService,
                                             LocationProcessorService locationProcessorService,
                                             DistanceService distanceService) {
        ValidationUtil.requireNonNull(
                disOptimizerLocalDto,
                locationProcessorService,
                locationsService,
                distanceService);
        this.disOptimizerLocalDto = disOptimizerLocalDto;
        this.locationsService = locationsService;
        this.locationProcessorService = locationProcessorService;
        this.distanceService = distanceService;
    }

    @Override
    public void execute() {
        LOGGER.info("Executing Data Collection local.");
        for (String googleApiKey : disOptimizerLocalDto.getGoogleApiKeys()) {
            int i = 1;
            while (i > 0) {
                try {
                    List<LocationPairDto> locationPairDtos = locationsService.getDataForDataCollectionLocal();
                    List<DataCollectionDto> dataCollectionDtos = locationProcessorService
                            .processLocationPairs(disOptimizerLocalDto, locationPairDtos, googleApiKey);
                    distanceService.saveDataForDataCollectionLocal(dataCollectionDtos);
                } catch (Exception e) {
                    LOGGER.error(e);
                }
                i--;
            }
        }
    }
}