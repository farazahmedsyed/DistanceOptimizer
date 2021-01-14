package com.distance.optimizer.service;

import com.distance.optimizer.dto.*;
import com.distance.optimizer.dto.reponse.google.DistanceMatrixResponse;
import com.distance.optimizer.dto.reponse.google.Row;
import com.distance.optimizer.dto.request.google.DistanceMatrixRequestDto;
import com.distance.optimizer.exception.DistanceOptimizerException;
import com.distance.optimizer.utils.DateUtils;
import com.distance.optimizer.utils.EntityHelper;
import com.distance.optimizer.utils.ValidationUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author FarazAhmed
 */
@Component
public class LocationProcessorService {

    private static final Logger LOGGER = Logger.getLogger(LocationProcessorService.class);

    private GoogleService googleService;

    @Autowired
    public LocationProcessorService(GoogleService googleService) {
        ValidationUtil.requireNonNull(googleService);
        this.googleService = googleService;
    }

    /**
     * @return processed dataCollectionDtos to save remotely or local
     * @throws DistanceOptimizerException
     * @throws ParseException
     * @throws InterruptedException
     */
    public List<DataCollectionDto> processLocationPairs(DisOptimizerDto disOptimizerDto,
                                                        List<LocationPairDto> locationPairDtos,
                                                        String googleApiKey) throws DistanceOptimizerException, ParseException, InterruptedException {
        LOGGER.info("Processing LocationPairs.");
        List<DataCollectionDto> dtos = new ArrayList<>();
        if (EntityHelper.isListNotPopulated(locationPairDtos)) {
            throw new DistanceOptimizerException("No Location Pairs");
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

        Date date = DateUtils.formatDate(disOptimizerDto.getDateTime());

        DistanceMatrixResponse distanceMatrixResponse;
        try {
            Thread.sleep(500);
            DistanceMatrixRequestDto matrixRequestDto = DistanceMatrixRequestDto
                    .builder()
                    .origins(srcParam)
                    .destinations(destParam.toString())
                    .departureSeconds(date.getTime() / 1000)
                    .trafficModel(disOptimizerDto.getFetchStrategy())
                    .googleApiKey(googleApiKey)
                    .build();
            distanceMatrixResponse = googleService.getDistanceMatrixResponse(matrixRequestDto);
        } catch (DistanceOptimizerException e) {
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
