package com.distance.optimizer.dto;

import com.distance.optimizer.utils.ValidationUtil;

import java.util.List;

public abstract class DisOptimizerDto {
    private String dateTime;
    private List<String> googleApiKeys;
    private String fetchStrategy = "best_guess";

    public DisOptimizerDto(String dateTime, List<String> googleApiKeys) {
        ValidationUtil.requireNonNull(dateTime, googleApiKeys);
        this.dateTime = dateTime;
        this.googleApiKeys = googleApiKeys;
    }

    public String getDateTime() {
        return dateTime;
    }

    public List<String> getGoogleApiKeys() {
        return googleApiKeys;
    }

    public String getFetchStrategy() {
        return fetchStrategy;
    }
}
