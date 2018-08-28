package api;

import exception.ROException;
import org.apache.log4j.Logger;
import reponse.DistanceMatrixResponse;
import utils.EntityHelper;
import utils.StatusCodes;
import utils.WebServiceUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by VenD on 8/24/2017.
 */
public class GoogleDistanceApi {

    private static final Logger LOGGER = Logger.getLogger(GoogleDistanceApi.class);
    private static final String ERROR_MSG_FORMAT = "Error in method %s Reason: %s";
    public static final Double PERCENT_TIME_ADDED_TO_GOOGLE_RESULTS = 1.5d;

    private String googleApiKey;
    private String googleDistanceMatrixApiUrl = "https://maps.googleapis.com/maps/api/distancematrix/json";

    public GoogleDistanceApi(){
        this.googleApiKey = "AIzaSyAnNx28KUsWsJqdDa45vufcTm-FMfpO6gg";
    }
    public GoogleDistanceApi(String googleApiKey){
        this.googleApiKey = googleApiKey;
    }

    public DistanceMatrixResponse getDistanceMatrixResponse (String origins, String destinations, Long departureSeconds, String trafficModel) throws ROException {
        Map<String, String> distanceMatrixQueryParams = new HashMap<>();
        distanceMatrixQueryParams.put("origins", origins);
        distanceMatrixQueryParams.put("destinations", destinations);
        distanceMatrixQueryParams.put("departure_time", Long.toString(departureSeconds));
        distanceMatrixQueryParams.put("traffic_model", trafficModel);
        distanceMatrixQueryParams.put("key", googleApiKey);

        DistanceMatrixResponse distanceMatrixResponse = WebServiceUtils.get(googleDistanceMatrixApiUrl, distanceMatrixQueryParams, null, DistanceMatrixResponse.class);
        if (EntityHelper.isNull(distanceMatrixResponse) || !distanceMatrixResponse.getStatus().equals("OK")) {
            LOGGER.error("GOOGLE_API_RESPONSE = " + distanceMatrixResponse.toString());
            throw new ROException(StatusCodes.FAILED_TO_GET_API_RESPONSE.getCode(), distanceMatrixResponse.getErrorMessage());
        }
        return distanceMatrixResponse;
    }
}
