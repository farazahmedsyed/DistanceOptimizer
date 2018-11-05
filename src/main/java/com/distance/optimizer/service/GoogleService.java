package com.distance.optimizer.service;

import com.distance.optimizer.dto.reponse.google.DistanceMatrixResponse;
import com.distance.optimizer.exception.DistanceOptimizerException;
import com.distance.optimizer.utils.EntityHelper;
import com.distance.optimizer.utils.StatusCodes;
import com.distance.optimizer.utils.WebServiceUtils;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * @author FarazAhmed
 * Fetch distance from google
 */
public class GoogleService {

    private static final Logger LOGGER = Logger.getLogger(GoogleService.class);
    private String googleApiKey;
    private String googleDistanceMatrixApiUrl = "https://maps.googleapis.com/maps/api/distancematrix/json";

    public GoogleService(String googleApiKey){
        this.googleApiKey = googleApiKey;
    }

    /**
     * @throws DistanceOptimizerException if unable to retreive distance from google.
     * */
    public DistanceMatrixResponse getDistanceMatrixResponse (String origins, String destinations, Long departureSeconds, String trafficModel) throws DistanceOptimizerException {
        Map<String, String> distanceMatrixQueryParams = new HashMap<>();
        distanceMatrixQueryParams.put("origins", origins);
        distanceMatrixQueryParams.put("destinations", destinations);
        distanceMatrixQueryParams.put("departure_time", Long.toString(departureSeconds));
        distanceMatrixQueryParams.put("traffic_model", trafficModel);
        distanceMatrixQueryParams.put("key", googleApiKey);

        LOGGER.info("Hitting googleDistanceMatrixAp.");
        DistanceMatrixResponse distanceMatrixResponse = WebServiceUtils.get(googleDistanceMatrixApiUrl, distanceMatrixQueryParams, null, DistanceMatrixResponse.class);
        if (EntityHelper.isNull(distanceMatrixResponse) || !distanceMatrixResponse.getStatus().equals("OK")) {
            LOGGER.error("GOOGLE_API_RESPONSE = " + distanceMatrixResponse.toString());
            throw new DistanceOptimizerException(StatusCodes.FAILED_TO_GET_API_RESPONSE.getCode(), distanceMatrixResponse.getErrorMessage());
        }
        return distanceMatrixResponse;
    }
}
