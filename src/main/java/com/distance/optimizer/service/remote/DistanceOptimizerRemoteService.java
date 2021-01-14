package com.distance.optimizer.service.remote;

import com.distance.optimizer.dto.DataCollectionDto;
import com.distance.optimizer.dto.DistanceOptimizerConfigurationDto;
import com.distance.optimizer.dto.LocationPairDto;
import com.distance.optimizer.exception.DistanceOptimizerException;
import com.distance.optimizer.service.LocationProcessorService;
import com.distance.optimizer.utils.EntityHelper;
import com.distance.optimizer.utils.HttpResponseProcessing;
import com.distance.optimizer.utils.Response;
import com.distance.optimizer.utils.WebServiceUtils;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

/**
 * @author FarazAhmed
 */
public class DistanceOptimizerRemoteService {

    private static final Logger LOGGER = Logger.getLogger(DistanceOptimizerRemoteService.class);

    private DistanceOptimizerConfigurationDto distanceOptimizerConfigurationDto;
    private LocationProcessorService locationProcessorService;

    public DistanceOptimizerRemoteService(DistanceOptimizerConfigurationDto distanceOptimizerConfigurationDto,
                                          LocationProcessorService locationProcessorService) {
        Objects.requireNonNull(distanceOptimizerConfigurationDto);
        Objects.requireNonNull(locationProcessorService);
        this.distanceOptimizerConfigurationDto = distanceOptimizerConfigurationDto;
        this.locationProcessorService = locationProcessorService;
    }

    /**
     * It retrieved unprocessed location from the provided get com.distanceoptimizer.api, fetch the distance from google and post it on save com.distanceoptimizer.api.
     *
     * <p>If unable to fetch address for any specific location, then it will log the error and continue its execution.</p>
     */
    public void executeRemote() {
        LOGGER.info("Executing Data Collection remote.");
        for (String googleApiKey : distanceOptimizerConfigurationDto.getGoogleApiKeys()) {
            int i = 100;
            while (i > 0) {
                try {
                    List<LocationPairDto> locationPairDtos = getDataForDataCollectionRemote();
                    List<DataCollectionDto> dataCollectionDtos = locationProcessorService
                            .processLocationPairs(distanceOptimizerConfigurationDto, locationPairDtos, googleApiKey);
                    saveDataForDataCollectionRemote(dataCollectionDtos);
                } catch (Exception e) {
                    LOGGER.error(e);
                }
                i--;
            }
        }
    }

    /**
     * post dataCollectionDtos to url
     *
     * @param dataCollectionDtos distances fetched for un processed locations.
     * @throws IOException
     * @throws DistanceOptimizerException
     */
    public void saveDataForDataCollectionRemote(List<DataCollectionDto> dataCollectionDtos) throws IOException, DistanceOptimizerException {
        LOGGER.info("posting processed location pairs distance to remote.");
        ListMultimap<String, String> queryParams = ArrayListMultimap.create();
        queryParams.put("apikey", distanceOptimizerConfigurationDto.getApiKey());
        if (EntityHelper.isListPopulated(dataCollectionDtos)) {
            ObjectMapper objectMapper = new ObjectMapper();
            String request = objectMapper.writeValueAsString(dataCollectionDtos);
            WebServiceUtils.post(distanceOptimizerConfigurationDto.getDevURLSaveData(), request, queryParams, null, new HttpResponseProcessing() {
                @Override
                public Object process(String responseString) throws DistanceOptimizerException, IOException {
                    ObjectMapper objectMapper = new ObjectMapper();
                    return objectMapper.readValue(responseString, Response.class);
                }
            });
        }
    }

    /**
     * @return unprocessed location pairs fetched from url
     * @throws DistanceOptimizerException
     */
    public List<LocationPairDto> getDataForDataCollectionRemote() throws DistanceOptimizerException {
        LOGGER.info("Fetching Data Collection remote.");
        ListMultimap<String, String> queryParams = ArrayListMultimap.create();
        queryParams.put("apikey", distanceOptimizerConfigurationDto.getApiKey());
        return WebServiceUtils.get(distanceOptimizerConfigurationDto.getDevURLGetLocations(), queryParams, null, new HttpResponseProcessing() {
            @Override
            public Object process(String responseString) throws DistanceOptimizerException, IOException {
                ObjectMapper objectMapper = new ObjectMapper();
                Response response = objectMapper.readValue(responseString, Response.class);
                if (response.getResponseHeader().getIsError() == true) {
                    throw new DistanceOptimizerException();
                }

                String temp = objectMapper.writeValueAsString(response.getResponseBody().get("response"));
                List<LocationPairDto> pairs = objectMapper.readValue(temp, new TypeReference<List<LocationPairDto>>() {
                });
                return pairs;
            }
        });

    }

}
