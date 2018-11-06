package com.distance.optimizer.service;

import com.distance.optimizer.dto.DataCollectionDto;
import com.distance.optimizer.dto.DistanceInfoDto;
import com.distance.optimizer.dto.DistanceOptimizerConfigurationDto;
import com.distance.optimizer.dto.LocationPairDto;
import com.distance.optimizer.dto.reponse.google.DistanceMatrixResponse;
import com.distance.optimizer.dto.reponse.google.Row;
import com.distance.optimizer.exception.DistanceOptimizerException;
import com.distance.optimizer.utils.DateUtils;
import com.distance.optimizer.utils.EntityHelper;
import org.apache.log4j.Logger;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author FarazAhmed
 */
public class LocationProcessorService {

    private static final Logger LOGGER = Logger.getLogger(LocationsService.class);

    /**
     * @return processed dataCollectionDtos to save remotely or local
     * @throws DistanceOptimizerException
     * @throws ParseException
     * @throws InterruptedException
     * */
    public static List<DataCollectionDto> processLocationPairs(DistanceOptimizerConfigurationDto distanceOptimizerConfigurationDto, List<LocationPairDto> locationPairDtos, String googleApiKey) throws DistanceOptimizerException, ParseException, InterruptedException {
        LOGGER.info("Processing LocationPairs.");
        List<DataCollectionDto> dtos = new ArrayList<>();
        if (EntityHelper.isListNotPopulated(locationPairDtos)) {
            throw new DistanceOptimizerException();
        }
        String srcParam = locationPairDtos.get(0).getSrcLocStr();
        StringBuffer destParam = new StringBuffer();
        List<String> destList = new ArrayList<>();
        for (int j = 0; j < locationPairDtos.size(); j++) {
            destList.add(locationPairDtos.get(j).getDestLocStr());
            if (j == 0) {
                destParam.append(locationPairDtos.get(j).getDestLocStr());
            } else {
                destParam.append("|" + locationPairDtos.get(j).getDestLocStr());
            }
        }

        Date date = DateUtils.formatDate(distanceOptimizerConfigurationDto.getDateTime());

        DistanceMatrixResponse distanceMatrixResponse = null;
        try {
            Thread.sleep(500);
            GoogleService googleService = new GoogleService(googleApiKey);
            distanceMatrixResponse = googleService.getDistanceMatrixResponse(srcParam, destParam.toString(), date.getTime() / 1000, distanceOptimizerConfigurationDto.getFetchStrategy());
        }
        catch (DistanceOptimizerException e) {
            LOGGER.error(e);
            return dtos;
        }

        Row row = distanceMatrixResponse.getRows().get(0);

        for (int j = 0; j < destList.size(); j++) {
            try {
                DataCollectionDto dto = new DataCollectionDto();

                dto.setSrcLocString(srcParam);
                dto.setDestLocString(destList.get(j));

                DistanceInfoDto distanceInfo = new DistanceInfoDto();
                distanceInfo.setText(row.getElements().get(j).getDistance().getText());
                distanceInfo.setValue((int) row.getElements().get(j).getDistance().getValue());

                DistanceInfoDto duration = new DistanceInfoDto();
                duration.setText(row.getElements().get(j).getDuration().getText());
                duration.setValue(row.getElements().get(j).getDuration().getValue());

                DistanceInfoDto durationInTraffic = new DistanceInfoDto();
                durationInTraffic.setText(row.getElements().get(j).getDurationInTraffic().getText());
                durationInTraffic.setValue(row.getElements().get(j).getDurationInTraffic().getValue());

                dto.setDistance(distanceInfo);
                dto.setDuration(duration);
                dto.setDurationInTraffic(durationInTraffic);
                dto.setTrafficModel("B");
                dto.setHour(date.getHours());
                dto.setMinute(date.getMinutes());
                dto.setDayOfWeek(DateUtils.getDayOfWeek(date));
                dto.setStatus(row.getElements().get(j).getStatus());
                dto.setErrorMessage(distanceMatrixResponse.getErrorMessage());

                dtos.add(dto);
            } catch (Exception e) {
                LOGGER.error(e);
            }
        }

        return dtos;

    }

}
