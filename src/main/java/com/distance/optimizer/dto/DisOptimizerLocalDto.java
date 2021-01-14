package com.distance.optimizer.dto;

import com.distance.optimizer.utils.ValidationUtil;

import java.util.List;

public class DisOptimizerLocalDto extends DisOptimizerDto {
    private String schedulerCronExpression;
    private DisOptimizerDatabaseDto disOptimizerDatabaseDto;

    private DisOptimizerLocalDto(String dateTime,
                                 List<String> googleApiKeys,
                                 DisOptimizerDatabaseDto disOptimizerDatabaseDto,
                                 String schedulerCronExpression) {
        super(dateTime, googleApiKeys);
        ValidationUtil.requireNonNull(disOptimizerDatabaseDto);
        this.disOptimizerDatabaseDto = disOptimizerDatabaseDto;
        this.schedulerCronExpression = schedulerCronExpression;
    }

    public String getSchedulerCronExpression() {
        return schedulerCronExpression;
    }

    public DisOptimizerDatabaseDto getDisOptimizerDatabaseDto() {
        return disOptimizerDatabaseDto;
    }

    public static DisOptimizerLocalDtoBuilder builder() {
        return new DisOptimizerLocalDtoBuilder();
    }

    public static class DisOptimizerLocalDtoBuilder {
        private String dateTime;
        private List<String> googleApiKeys;
        private String schedulerCronExpression;
        private DisOptimizerDatabaseDto disOptimizerDatabaseDto;

        private DisOptimizerLocalDtoBuilder() {

        }

        public DisOptimizerLocalDtoBuilder dateTime(String dateTime) {
            this.dateTime = dateTime;
            return this;
        }

        public DisOptimizerLocalDtoBuilder googleApiKeys(List<String> googleApiKeys) {
            this.googleApiKeys = googleApiKeys;
            return this;
        }

        public DisOptimizerLocalDtoBuilder schedulerCronExpression(String schedulerCronExpression) {
            this.schedulerCronExpression = schedulerCronExpression;
            return this;
        }

        public DisOptimizerLocalDtoBuilder disOptimizerDatabaseDto(DisOptimizerDatabaseDto disOptimizerDatabaseDto) {
            this.disOptimizerDatabaseDto = disOptimizerDatabaseDto;
            return this;
        }

        public DisOptimizerLocalDto build() {
            ValidationUtil.requireNonNull(dateTime, googleApiKeys, disOptimizerDatabaseDto);
            return new DisOptimizerLocalDto(dateTime, googleApiKeys, disOptimizerDatabaseDto, schedulerCronExpression);
        }
    }
}
