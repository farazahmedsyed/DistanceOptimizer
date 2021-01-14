package com.distance.optimizer.dto;

import com.distance.optimizer.utils.ValidationUtil;

public class DisOptimizerDatabaseDto {
    private String serverAddress;
    private Integer port;
    private String databaseName;

    private DisOptimizerDatabaseDto(String serverAddress, Integer port, String databaseName) {
        ValidationUtil.requireNonNull(serverAddress, port, databaseName);
        this.serverAddress = serverAddress;
        this.port = port;
        this.databaseName = databaseName;
    }

    public String getServerAddress() {
        return serverAddress;
    }

    public Integer getPort() {
        return port;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public static DisOptimizerDatabaseDtoBuilder builder() {
        return new DisOptimizerDatabaseDtoBuilder();
    }

    public static class DisOptimizerDatabaseDtoBuilder {
        private String serverAddress;
        private Integer port;
        private String databaseName;

        private DisOptimizerDatabaseDtoBuilder() {

        }

        public DisOptimizerDatabaseDtoBuilder serverAddress(String serverAddress) {
            this.serverAddress = serverAddress;
            return this;
        }

        public DisOptimizerDatabaseDtoBuilder port(Integer port) {
            this.port = port;
            return this;
        }

        public DisOptimizerDatabaseDtoBuilder databaseName(String databaseName) {
            this.databaseName = databaseName;
            return this;
        }

        public DisOptimizerDatabaseDto build() {
            ValidationUtil.requireNonNull(serverAddress, port, databaseName);
            return new DisOptimizerDatabaseDto(serverAddress, port, databaseName);
        }
    }
}
