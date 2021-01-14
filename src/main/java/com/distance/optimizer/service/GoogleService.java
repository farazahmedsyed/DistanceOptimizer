package com.distance.optimizer.service;

import com.distance.optimizer.dto.reponse.google.DistanceMatrixResponse;
import com.distance.optimizer.dto.request.google.DistanceMatrixRequestDto;
import com.distance.optimizer.dto.request.google.GeoCodeRequestDto;
import com.distance.optimizer.exception.DistanceOptimizerException;
import com.distance.optimizer.utils.EntityHelper;
import com.distance.optimizer.utils.StatusCodes;
import com.distance.optimizer.utils.ValidationUtil;
import com.distance.optimizer.utils.WebServiceUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author FarazAhmed
 * Fetch distance from google
 */
@Service
public class GoogleService {

    private static final Logger LOGGER = Logger.getLogger(GoogleService.class);
    private static final String GOOGLE_DISTANCE_MATRIX_API_URL = "https://maps.googleapis.com/maps/api/distancematrix/json";
    private static final String GOOGLE_GEOCODE_API_URL = "https://maps.googleapis.com/maps/api/geocode/json";

    /**
     * @throws DistanceOptimizerException if unable to retreive distance from google.
     * */
    public DistanceMatrixResponse getDistanceMatrixResponse (DistanceMatrixRequestDto requestDto) throws DistanceOptimizerException {
        ValidationUtil.requireNonNull(requestDto);
        Map<String, String> distanceMatrixQueryParams = requestDto.getRequestMap();
        LOGGER.info("Hitting googleDistanceMatrixAp.");
        DistanceMatrixResponse distanceMatrixResponse = WebServiceUtils.get(GOOGLE_DISTANCE_MATRIX_API_URL, distanceMatrixQueryParams, null, DistanceMatrixResponse.class);
        if (EntityHelper.isNull(distanceMatrixResponse) || !distanceMatrixResponse.getStatus().equals("OK")) {
            LOGGER.error("GOOGLE_API_RESPONSE = " + distanceMatrixResponse.toString());
            throw new DistanceOptimizerException(StatusCodes.FAILED_TO_GET_API_RESPONSE.getCode(), distanceMatrixResponse.getErrorMessage());
        }
        return distanceMatrixResponse;
    }

    /**
     * @param geoCodeRequestDto
     * @return location
     * */
    public String getLocationFromGeoCodeResponse(GeoCodeRequestDto geoCodeRequestDto){
        ValidationUtil.requireNonNull(geoCodeRequestDto);
        Map<String, String> getCodeQueryParams = geoCodeRequestDto.getRequestMap();

        Map<String, Object> geoCodeResponse = WebServiceUtils.get(GOOGLE_GEOCODE_API_URL, getCodeQueryParams, null, Map.class);
        if (EntityHelper.isNull(geoCodeResponse) || !geoCodeResponse.get("status").equals("OK")) {
            LOGGER.error("GOOGLE_API_RESPONSE = " + geoCodeResponse.toString());
            Object status = geoCodeResponse.get("status");
            if ("ZERO_RESULTS".equals(status)) {
                return "";
            }
            throw new RuntimeException("Google Geocode Api Error. Status : " + status);
        }

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(((Map) ((Map) ((Map) ((List) geoCodeResponse.get("results")).get(0)).get("geometry")).get("location")).get("lat"));
        stringBuilder.append(",");
        stringBuilder.append(((Map) ((Map) ((Map) ((List) geoCodeResponse.get("results")).get(0)).get("geometry")).get("location")).get("lng"));
        stringBuilder.append("\n");
        return stringBuilder.toString();
    }
}
