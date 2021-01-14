package com.distance.optimizer.dto.request.google;

import com.distance.optimizer.utils.ValidationUtil;

import java.util.HashMap;
import java.util.Map;

public class GeoCodeRequestDto {
    private String address;
    private String googleApiKey;

    public GeoCodeRequestDto(String address, String googleApiKey) {
        ValidationUtil.requireNonNull(address, googleApiKey);
        this.address = address;
        this.googleApiKey = googleApiKey;
    }

    public Map<String, String> getRequestMap() {
        Map<String, String> getCodeQueryParams = new HashMap<>();
        getCodeQueryParams.put("address", address);
        getCodeQueryParams.put("key", googleApiKey);
        return getCodeQueryParams;
    }
}
