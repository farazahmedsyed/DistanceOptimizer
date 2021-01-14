package com.distance.optimizer.dto;

import com.distance.optimizer.utils.ValidationUtil;

import java.util.List;

public class DisOptimizerRemoteDto extends DisOptimizerDto {
    private String apiKey;
    private String urlGet;
    private String urlSave;

    private DisOptimizerRemoteDto(String dateTime,
                                  List<String> googleApiKeys,
                                  String apiKey,
                                  String urlGet,
                                  String urlSave) {
        super(dateTime, googleApiKeys);
        ValidationUtil.requireNonNull(apiKey, urlGet, urlSave);
        this.apiKey = apiKey;
        this.urlGet = urlGet;
        this.urlSave = urlSave;
    }

    public String getApiKey() {
        return apiKey;
    }

    public String getUrlGet() {
        return urlGet;
    }

    public String getUrlSave() {
        return urlSave;
    }

    public static DisOptimizerRemoteDtoBuilder builder() {
        return new DisOptimizerRemoteDtoBuilder();
    }

    public static class DisOptimizerRemoteDtoBuilder {
        private String dateTime;
        private List<String> googleApiKeys;
        private String apiKey;
        private String urlGet;
        private String urlSave;

        private DisOptimizerRemoteDtoBuilder() {

        }

        public DisOptimizerRemoteDtoBuilder dateTime(String dateTime) {
            this.dateTime = dateTime;
            return this;
        }

        public DisOptimizerRemoteDtoBuilder googleApiKeys(List<String> googleApiKeys) {
            this.googleApiKeys = googleApiKeys;
            return this;
        }

        public DisOptimizerRemoteDtoBuilder apiKey(String apiKey) {
            this.apiKey = apiKey;
            return this;
        }

        public DisOptimizerRemoteDtoBuilder urlGet(String urlGet) {
            this.urlGet = urlGet;
            return this;
        }

        public DisOptimizerRemoteDtoBuilder urlSave(String urlSave) {
            this.urlSave = urlSave;
            return this;
        }

        public DisOptimizerRemoteDto build() {
            ValidationUtil.requireNonNull(dateTime, googleApiKeys, apiKey, urlGet, urlSave);
            return new DisOptimizerRemoteDto(dateTime, googleApiKeys, apiKey, urlGet, urlSave);
        }
    }
}
