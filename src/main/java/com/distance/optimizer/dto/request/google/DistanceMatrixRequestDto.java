package com.distance.optimizer.dto.request.google;

import com.distance.optimizer.utils.ValidationUtil;

import java.util.HashMap;
import java.util.Map;

public class DistanceMatrixRequestDto {
    private String origins;
    private String destinations;
    private Long departureSeconds;
    private String trafficModel;
    private String googleApiKey;

    private DistanceMatrixRequestDto(String origins,
                                     String destinations,
                                     Long departureSeconds,
                                     String trafficModel,
                                     String googleApiKey) {
        ValidationUtil.requireNonNull(origins, destinations, departureSeconds, trafficModel, googleApiKey);
        this.origins = origins;
        this.destinations = destinations;
        this.departureSeconds = departureSeconds;
        this.trafficModel = trafficModel;
        this.googleApiKey = googleApiKey;
    }

    public Map<String, String> getRequestMap() {
        Map<String, String> distanceMatrixQueryParams = new HashMap<>();
        distanceMatrixQueryParams.put("origins", origins);
        distanceMatrixQueryParams.put("destinations", destinations);
        distanceMatrixQueryParams.put("departure_time", Long.toString(departureSeconds));
        distanceMatrixQueryParams.put("traffic_model", trafficModel);
        distanceMatrixQueryParams.put("key", googleApiKey);
        return distanceMatrixQueryParams;
    }

    public static DistanceMatrixRequestDtoBuilder builder(){
        return new DistanceMatrixRequestDtoBuilder();
    }


    public static class DistanceMatrixRequestDtoBuilder {
        private String origins;
        private String destinations;
        private Long departureSeconds;
        private String trafficModel;
        private String googleApiKey;

        public DistanceMatrixRequestDtoBuilder origins(String origins) {
            this.origins = origins;
            return this;
        }

        public DistanceMatrixRequestDtoBuilder destinations(String destinations) {
            this.destinations = destinations;
            return this;
        }

        public DistanceMatrixRequestDtoBuilder departureSeconds(Long departureSeconds) {
            this.departureSeconds = departureSeconds;
            return this;
        }

        public DistanceMatrixRequestDtoBuilder trafficModel(String trafficModel) {
            this.trafficModel = trafficModel;
            return this;
        }

        public DistanceMatrixRequestDtoBuilder googleApiKey(String googleApiKey) {
            this.googleApiKey = googleApiKey;
            return this;
        }

        public DistanceMatrixRequestDto build() {
            ValidationUtil.
                    requireNonNull(origins, destinations, departureSeconds, trafficModel, googleApiKey);
            DistanceMatrixRequestDto requestDto = new DistanceMatrixRequestDto(
                    origins, destinations, departureSeconds, trafficModel, googleApiKey
            );
            return requestDto;
        }
    }
}
