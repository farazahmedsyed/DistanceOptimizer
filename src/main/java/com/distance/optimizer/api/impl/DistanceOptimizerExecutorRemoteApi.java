package com.distance.optimizer.api.impl;

import com.distance.optimizer.api.DistanceOptimizerExecutorApi;
import com.distance.optimizer.dto.DataCollectionDto;
import com.distance.optimizer.dto.DisOptimizerRemoteDto;
import com.distance.optimizer.dto.LocationPairDto;
import com.distance.optimizer.exception.DistanceOptimizerException;
import com.distance.optimizer.service.LocationProcessorService;
import com.distance.optimizer.utils.EntityHelper;
import com.distance.optimizer.utils.Response;
import com.distance.optimizer.utils.ValidationUtil;
import com.distance.optimizer.utils.WebServiceUtils;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

public class DistanceOptimizerExecutorRemoteApi implements DistanceOptimizerExecutorApi {
    public static final Logger LOGGER = Logger.getLogger(DistanceOptimizerExecutorRemoteApi.class);

    private DisOptimizerRemoteDto disOptimizerRemoteDto;
    private LocationProcessorService locationProcessorService;

    @Autowired
    public DistanceOptimizerExecutorRemoteApi(DisOptimizerRemoteDto disOptimizerRemoteDto,
                                              LocationProcessorService locationProcessorService) {
        ValidationUtil.requireNonNull(disOptimizerRemoteDto, locationProcessorService);
        this.disOptimizerRemoteDto = disOptimizerRemoteDto;
        this.locationProcessorService = locationProcessorService;
    }

    @Override
    public void execute() {
        LOGGER.info("Executing Data Collection remote.");
        for (String googleApiKey : disOptimizerRemoteDto.getGoogleApiKeys()) {
            int i = 100;
            while (i > 0) {
                try {
                    List<LocationPairDto> locationPairDtos = getDataForDataCollectionRemote();
                    List<DataCollectionDto> dataCollectionDtos = locationProcessorService
                            .processLocationPairs(disOptimizerRemoteDto, locationPairDtos, googleApiKey);
                    saveDataForDataCollectionRemote(dataCollectionDtos);
                } catch (Exception e) {
                    LOGGER.error(e);
                }
                i--;
            }
        }
    }

    private List<LocationPairDto> getDataForDataCollectionRemote() throws DistanceOptimizerException {
        LOGGER.info("Fetching Data Collection remote.");
        ListMultimap<String, String> queryParams = ArrayListMultimap.create();
        queryParams.put("apikey", disOptimizerRemoteDto.getApiKey());
        return WebServiceUtils.get(disOptimizerRemoteDto.getUrlGet(), queryParams, null, responseString -> {
            ObjectMapper objectMapper = new ObjectMapper();
            Response response = objectMapper.readValue(responseString, Response.class);
            if (Boolean.TRUE.equals(response.getResponseHeader().getIsError())) {
                throw new DistanceOptimizerException();
            }

            String temp = objectMapper.writeValueAsString(response.getResponseBody().get("response"));
            return objectMapper.readValue(temp, new TypeReference<List<LocationPairDto>>() {
            });

        });

    }

    private void saveDataForDataCollectionRemote(List<DataCollectionDto> dataCollectionDtos) throws IOException, DistanceOptimizerException {
        LOGGER.info("posting processed location pairs distance to remote.");
        ListMultimap<String, String> queryParams = ArrayListMultimap.create();
        queryParams.put("apikey", disOptimizerRemoteDto.getApiKey());
        if (Boolean.TRUE.equals(EntityHelper.isListPopulated(dataCollectionDtos))) {
            ObjectMapper objectMapper = new ObjectMapper();
            String request = objectMapper.writeValueAsString(dataCollectionDtos);
            WebServiceUtils.post(disOptimizerRemoteDto.getUrlSave(), request, queryParams, null, responseString -> {
                ObjectMapper objectMapper1 = new ObjectMapper();
                return objectMapper1.readValue(responseString, Response.class);
            });
        }
    }


}