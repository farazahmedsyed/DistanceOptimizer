package com.distance.optimizer.service;

import com.distance.optimizer.dto.reponse.google.DistanceMatrixResponse;
import com.distance.optimizer.exception.DistanceOptimizerException;
import com.distance.optimizer.utils.EntityHelper;
import com.distance.optimizer.utils.StatusCodes;
import com.distance.optimizer.utils.WebServiceUtils;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author FarazAhmed
 * Fetch distance from google
 */
public class GoogleService {

    private static final Logger LOGGER = Logger.getLogger(GoogleService.class);
    private String googleApiKey;
    private String googleDistanceMatrixApiUrl = "https://maps.googleapis.com/maps/api/distancematrix/json";
    private String googleGeocodeApiUrl = "https://maps.googleapis.com/maps/api/geocode/json";

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

    /**
     * @throws DistanceOptimizerException
     * @param address
     * @return location
     * */
    public String getLocationFromGeocodeResponse(String address) throws DistanceOptimizerException {

        Map<String, String> getCodeQueryParams = new HashMap<>();
        getCodeQueryParams.put("address", address);
        getCodeQueryParams.put("key", googleApiKey);

        Map<String, Object> distanceMatrixResponse = WebServiceUtils.get(googleGeocodeApiUrl, getCodeQueryParams, null, Map.class);
        if (EntityHelper.isNull(distanceMatrixResponse) || !distanceMatrixResponse.get("status").equals("OK")) {
            LOGGER.error("GOOGLE_API_RESPONSE = " + distanceMatrixResponse.toString());
            Object status = distanceMatrixResponse.get("status");
            if ("ZERO_RESULTS".equals(status)) {
                return "";
            }
            throw new DistanceOptimizerException(StatusCodes.FAILED_TO_GET_API_RESPONSE.getCode());
        }

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(((Map) ((Map) ((Map) ((List) distanceMatrixResponse.get("results")).get(0)).get("geometry")).get("location")).get("lat"));
        stringBuilder.append(",");
        stringBuilder.append(((Map) ((Map) ((Map) ((List) distanceMatrixResponse.get("results")).get(0)).get("geometry")).get("location")).get("lng"));
        stringBuilder.append("\n");
        return stringBuilder.toString();
    }
}
