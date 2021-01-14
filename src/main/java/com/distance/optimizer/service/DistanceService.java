package com.distance.optimizer.service;

import com.distance.optimizer.dto.DataCollectionDto;
import com.distance.optimizer.dto.DisOptimizerDto;
import com.distance.optimizer.dto.reponse.google.DistanceMatrixResponse;
import com.distance.optimizer.dto.reponse.google.Element;
import com.distance.optimizer.dto.request.google.DistanceMatrixRequestDto;
import com.distance.optimizer.exception.DistanceOptimizerException;
import com.distance.optimizer.model.entity.Distance;
import com.distance.optimizer.model.entity.DistanceInfo;
import com.distance.optimizer.model.entity.Location;
import com.distance.optimizer.model.entity.LocationPair;
import com.distance.optimizer.model.repository.DistanceRepository;
import com.distance.optimizer.utils.DateUtils;
import com.distance.optimizer.utils.EntityHelper;
import com.distance.optimizer.utils.ValidationUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author FarazAhmed
 * <p>Provides functionality to retreive ans save procedures of location and distances.</p>
 */
@Component
public class DistanceService {

    private static final Logger LOGGER = Logger.getLogger(DistanceService.class);
    private static final String SPLITTER = ",";
    private static final String COLLECTION_STATUS= "OK";


    @Autowired
    private LocationsService locationsService;
    @Autowired
    private DistanceRepository distanceRepository;
    @Autowired
    private DisOptimizerDto disOptimizerDto;
    @Autowired
    private GoogleService googleService;

    /**
     * @return  Distance from database if not found then from google and update database.
     * @throws DistanceOptimizerException if failed to fetch distance from google.
     * @param srcLoc source Location.
     * @param destLoc Destination Location.
     * @param departureTime DepartureTime
     * @param fraction Fraction
     * */
    public Distance getDistance (String srcLoc, String destLoc, Date departureTime, Double fraction) throws DistanceOptimizerException {
        if(EntityHelper.isListNotPopulated(disOptimizerDto.getGoogleApiKeys())){
            throw new DistanceOptimizerException("Google api keys are required");
        }
        DistanceMatrixRequestDto matrixRequestDto = DistanceMatrixRequestDto
                .builder()
                .origins(srcLoc)
                .destinations(destLoc)
                .departureSeconds((departureTime.getTime() / 1000))
                .trafficModel(disOptimizerDto.getFetchStrategy())
                .googleApiKey(disOptimizerDto.getGoogleApiKeys().get(0))
                .build();
        if (ValidationUtil.isValidLatLngString(srcLoc) && ValidationUtil.isValidLatLngString(destLoc)) {
            Location src = new Location(Double.parseDouble(srcLoc.split(",")[1]), Double.parseDouble(srcLoc.split(",")[0]));
            Location dest = new Location(Double.parseDouble(destLoc.split(",")[1]), Double.parseDouble(destLoc.split(",")[0]));

            Distance distance = distanceRepository.withinSrcAndDestCirclesAndHourIn(src, dest, new ArrayList<>());

            if (EntityHelper.isNotNull(distance)) {
                return distance;
            }

            LOGGER.info("Not found in database. Fetching from google.");

            DistanceMatrixResponse distanceMatrixResponse = googleService.getDistanceMatrixResponse(matrixRequestDto);

            distance = convertToDistance(distanceMatrixResponse, srcLoc, destLoc, departureTime);

            if (EntityHelper.isNotNull(distanceMatrixResponse.getRows().get(0).getElements().get(0).getDurationInTraffic())) {
                distanceRepository.save(distance);
                LOGGER.info("Saved to database.");
            }
            addPercentageOfTravelDurationInGoogleResults(distance, fraction);
            return distance;
        }
        else {
            LOGGER.info("Fetching from google.");
            DistanceMatrixResponse distanceMatrixResponse = googleService.getDistanceMatrixResponse(matrixRequestDto);

            Distance distance = convertToDistance(distanceMatrixResponse, srcLoc, destLoc, departureTime);
            addPercentageOfTravelDurationInGoogleResults(distance, fraction);
            return distance;
        }

    }

    /**
     * Save unprocessed location pairs to database.
     * @param dataCollectionDtos distances fetched for un processed locations.
     * */
    public void saveDataForDataCollectionLocal(List<DataCollectionDto> dataCollectionDtos) {
        LOGGER.info("saving processed location pairs distance to local.");
        if (EntityHelper.isListNotPopulated(dataCollectionDtos)){
            return;
        }
        for (DataCollectionDto dto : dataCollectionDtos) {
            if (dto.getStatus().equals(COLLECTION_STATUS)) {
                LocationPair locationPair = locationsService
                        .findLocationPair(dto.getSrcLocString(), dto.getDestLocString());
                if (locationPair != null) {
                    Distance distance = convertDataCollectionDtoToDistance(dto);
                    distanceRepository.save(distance);
                    locationsService.deleteLocationPair(locationPair);
                }
            }
        }
    }

    /**
     * Cron job to execute at fix delay
     * */
    public void updateDistanceCronJob(){
        try {
            LOGGER.info("Update Distance Cron");
            Date date = new Date();
            List<Distance> distances = distanceRepository.findFirst100DistanceByUpdatedAtLessThan(DateUtils.addDay(date, -30));
            for (Distance distance : distances) {
                DistanceMatrixResponse distanceMatrixResponse = null;
                try {
                    Thread.sleep(500);
                    DistanceMatrixRequestDto matrixRequestDto = DistanceMatrixRequestDto
                            .builder()
                            .origins(distance.getSrc().getLocation())
                            .destinations(distance.getDest().getLocation())
                            .departureSeconds(DateUtils.addDay(date,15).getTime() /1000)
                            .trafficModel(disOptimizerDto.getFetchStrategy())
                            .googleApiKey(disOptimizerDto.getGoogleApiKeys().get(0))
                            .build();
                    distanceMatrixResponse = googleService.getDistanceMatrixResponse(matrixRequestDto);
                    updateDistance(distance, distanceMatrixResponse, DateUtils.addDay(date,15));
                } catch (DistanceOptimizerException e) {
                   LOGGER.error(e);
                    break;
                }

            }
        }
        catch (Exception e){
           LOGGER.error(e);
        }
    }

    private Distance convertDataCollectionDtoToDistance(DataCollectionDto dataCollectionDto) {
        Distance distance = new Distance();
        Location srcLoc = new Location();
        srcLoc.setLng(Double.parseDouble(dataCollectionDto.getSrcLocString().split(SPLITTER)[1]));
        srcLoc.setLat(Double.parseDouble(dataCollectionDto.getSrcLocString().split(SPLITTER)[0]));
        Location destLoc = new Location();
        destLoc.setLng(Double.parseDouble(dataCollectionDto.getDestLocString().split(SPLITTER)[1]));
        destLoc.setLat(Double.parseDouble(dataCollectionDto.getDestLocString().split(SPLITTER)[0]));
        DistanceInfo distanceInfo = new DistanceInfo();
        BeanUtils.copyProperties(dataCollectionDto.getDistance(), distanceInfo);
        BeanUtils.copyProperties(dataCollectionDto.getDistance(), distanceInfo);
        DistanceInfo duration = new DistanceInfo();
        BeanUtils.copyProperties(dataCollectionDto.getDuration(), duration);
        DistanceInfo durationInTraffic = new DistanceInfo();
        BeanUtils.copyProperties(dataCollectionDto.getDurationInTraffic(), durationInTraffic);
        BeanUtils.copyProperties(dataCollectionDto, distance);
        distance.setSrc(srcLoc);
        distance.setDest(destLoc);
        distance.setDistance(distanceInfo);
        distance.setDuration(duration);
        distance.setDurationInTraffic(durationInTraffic);
        distance.setUpdatedAt(new Date());
        return distance;
    }

    private void updateDistance(Distance distance, DistanceMatrixResponse distanceMatrixResponse, Date departureTime){
        Element element = distanceMatrixResponse.getRows().get(0).getElements().get(0);
        DistanceInfo distanceInfo = new DistanceInfo();
        distanceInfo.setText(element.getDistance().getText());
        distanceInfo.setValue((int) element.getDistance().getValue());

        DistanceInfo duration = new DistanceInfo();
        BeanUtils.copyProperties(element.getDuration(), duration);
        DistanceInfo durationInTraffic = new DistanceInfo();
        if (EntityHelper.isNotNull(element.getDurationInTraffic())) {
            BeanUtils.copyProperties(element.getDurationInTraffic(), durationInTraffic);
        }
        else {
            BeanUtils.copyProperties(element.getDuration(), durationInTraffic);
        }

        distance.setDistance(distanceInfo);
        distance.setDuration(duration);
        distance.setDurationInTraffic(durationInTraffic);
        distance.setTrafficModel("B");
        distance.setDate(DateUtils.format(departureTime, DateUtils.DATE_FORMAT));
        distance.setHour(departureTime.getHours());
        distance.setMinute(departureTime.getMinutes());
        distance.setDayOfWeek(DateUtils.getDayOfWeek(departureTime));
        distance.setStatus(distanceMatrixResponse.getRows().get(0).getElements().get(0).getStatus());
        distance.setErrorMessage(distanceMatrixResponse.getErrorMessage());
        distance.setUpdatedAt(new Date());
        distanceRepository.save(distance);

    }

    private Distance convertToDistance (DistanceMatrixResponse distanceMatrixResponse, String src, String dest, Date departureTime) {
        Distance distance = new Distance();
        Element element = distanceMatrixResponse.getRows().get(0).getElements().get(0);

        Location srcLoc = null;
        if (ValidationUtil.isValidLatLngString(src)) {
            srcLoc = new Location();
            srcLoc.setLng(Double.parseDouble(src.split(",")[1]));
            srcLoc.setLat(Double.parseDouble(src.split(",")[0]));
        }

        Location destLoc = null;
        if (ValidationUtil.isValidLatLngString(dest)) {
            destLoc = new Location();
            destLoc.setLng(Double.parseDouble(dest.split(",")[1]));
            destLoc.setLat(Double.parseDouble(dest.split(",")[0]));
        }

        DistanceInfo distanceInfo = new DistanceInfo();
        distanceInfo.setText(element.getDistance().getText());
        distanceInfo.setValue((int) element.getDistance().getValue());

        DistanceInfo duration = new DistanceInfo();
        BeanUtils.copyProperties(element.getDuration(), duration);
        DistanceInfo durationInTraffic = new DistanceInfo();
        if (EntityHelper.isNotNull(element.getDurationInTraffic())) {
            BeanUtils.copyProperties(element.getDurationInTraffic(), durationInTraffic);
        }
        else {
            BeanUtils.copyProperties(element.getDuration(), durationInTraffic);
        }

        distance.setSrc(srcLoc);
        distance.setDest(destLoc);
        distance.setDistance(distanceInfo);
        distance.setDuration(duration);
        distance.setDurationInTraffic(durationInTraffic);
        distance.setTrafficModel("B");
        distance.setDate(DateUtils.format(departureTime, DateUtils.DATE_FORMAT));
        distance.setHour(departureTime.getHours());
        distance.setMinute(departureTime.getMinutes());
        distance.setDayOfWeek(DateUtils.getDayOfWeek(departureTime));
        distance.setStatus(distanceMatrixResponse.getRows().get(0).getElements().get(0).getStatus());
        distance.setErrorMessage(distanceMatrixResponse.getErrorMessage());

        return distance;
    }

    private void addPercentageOfTravelDurationInGoogleResults (Distance distance, Double percentageFraction) {
        if (EntityHelper.isNotNull(distance.getDuration())) {
            Double d = new Double(distance.getDuration().getValue());
            d *= percentageFraction;
            distance.getDuration().setValue(d.intValue());
        }
        if (EntityHelper.isNotNull(distance.getDurationInTraffic())) {
            Double d = new Double(distance.getDurationInTraffic().getValue());
            d *= percentageFraction;
            distance.getDurationInTraffic().setValue(d.intValue());
        }
    }

}
