package com.distance.optimizer.service;

import com.distance.optimizer.dto.DataCollectionDto;
import com.distance.optimizer.dto.DistanceOptimizerConfigurationDto;
import com.distance.optimizer.dto.LocationPairDto;
import com.distance.optimizer.dto.reponse.google.DistanceMatrixResponse;
import com.distance.optimizer.dto.reponse.google.Element;
import com.distance.optimizer.exception.DistanceOptimizerException;
import com.distance.optimizer.model.entity.*;
import com.distance.optimizer.model.repository.DistanceRepository;
import com.distance.optimizer.model.repository.LocationPairRepository;
import com.distance.optimizer.model.repository.LocationStringRepository;
import com.distance.optimizer.utils.DateUtils;
import com.distance.optimizer.utils.EntityHelper;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

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
    private LocationStringRepository locationStringRepository;
    @Autowired
    private LocationPairRepository locationPairRepository;
    @Autowired
    private DistanceRepository distanceRepository;
    @Autowired
    private DistanceOptimizerConfigurationDto distanceOptimizerConfigurationDto;
    @Autowired
    private LocationProcessorService locationProcessorService;

    /**
     * Fetch location from database and save distance in database.
     * */
    public void executeLocal(){
        LOGGER.info("Executing Data Collection local.");
        for(String googleApiKey : distanceOptimizerConfigurationDto.getGoogleApiKeys()) {
            int i = 1;
            while (i > 0) {
                try {
                    List<LocationPairDto> locationPairDtos = getDataForDataCollectionLocal();
                    List<DataCollectionDto> dataCollectionDtos = locationProcessorService
                            .processLocationPairs(distanceOptimizerConfigurationDto, locationPairDtos, googleApiKey);
                    saveDataForDataCollectionLocal(dataCollectionDtos);
                } catch (Exception e) {
                   LOGGER.error(e);
                }
                i--;
            }
        }
    }

    /**
     * @return  Distance from database if not found then from google and update database.
     * @throws DistanceOptimizerException if failed to fetch distance from google.
     * @param srcLoc source Location.
     * @param destLoc Destination Location.
     * @param departureTime DepartureTime
     * @param fraction Fraction
     * */
    public Distance getDistance (String srcLoc, String destLoc, Date departureTime, Double fraction) throws DistanceOptimizerException {
        if(EntityHelper.isListNotPopulated(distanceOptimizerConfigurationDto.getGoogleApiKeys())){
            throw new DistanceOptimizerException("Google api keys are required");
        }
        GoogleService googleService = new GoogleService(distanceOptimizerConfigurationDto.getGoogleApiKeys().get(0));
        if (isValidLatLngString(srcLoc) && isValidLatLngString(destLoc)) {
            Location src = new Location(Double.parseDouble(srcLoc.split(",")[1]), Double.parseDouble(srcLoc.split(",")[0]));
            Location dest = new Location(Double.parseDouble(destLoc.split(",")[1]), Double.parseDouble(destLoc.split(",")[0]));

            Distance distance = distanceRepository.withinSrcAndDestCirclesAndHourIn(src, dest, new ArrayList<>());

            if (EntityHelper.isNotNull(distance)) {
                return distance;
            }

            LOGGER.info("Not found in database. Fetching from google.");
            DistanceMatrixResponse distanceMatrixResponse = googleService.getDistanceMatrixResponse(srcLoc, destLoc, (departureTime.getTime()/1000), distanceOptimizerConfigurationDto.getFetchStrategy());

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
            DistanceMatrixResponse distanceMatrixResponse = googleService.getDistanceMatrixResponse(srcLoc, destLoc, (departureTime.getTime()/1000), distanceOptimizerConfigurationDto.getFetchStrategy());

            Distance distance = convertToDistance(distanceMatrixResponse, srcLoc, destLoc, departureTime);
            addPercentageOfTravelDurationInGoogleResults(distance, fraction);
            return distance;
        }

    }

    /**
     * @return unprocessed location pairs from database.
     * */
    public List<LocationPairDto> getDataForDataCollectionLocal() {
        LOGGER.info("Fetching location pairs from local.");
        List<LocationPair> locationPairs = new ArrayList<>();
        LocationString locationString = locationStringRepository.findFirst1ByCompleted(Boolean.FALSE);
        if (EntityHelper.isNotNull(locationString)) {
            locationPairs = locationPairRepository.findFirst25BySrcLocStrAndSent(locationString.getLoc(), Boolean.FALSE);
            if (EntityHelper.isListNotPopulated(locationPairs)) {
                locationString.setCompleted(Boolean.TRUE);
                locationStringRepository.save(locationString);
            }
            for (LocationPair locationPair : locationPairs) {
                locationPair.setSent(Boolean.TRUE);
                locationPairRepository.save(locationPair);
            }
        }
        return LocationPair.convertToDto(locationPairs);
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
                LocationPair locationPair = locationPairRepository.findFirst1BySrcLocStrAndDestLocStr(dto.getSrcLocString().trim(), dto.getDestLocString().trim());
                if (EntityHelper.isNotNull(locationPair)) {
                    Distance distance = convertDataCollectionDtoToDistance(dto);
                    distanceRepository.save(distance);
                    locationPairRepository.delete(locationPair);
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
                    GoogleService googleService = new GoogleService(distanceOptimizerConfigurationDto.getGoogleApiKeys().get(0));
                    distanceMatrixResponse = googleService.getDistanceMatrixResponse(distance.getSrc().getLocation(),
                            distance.getDest().getLocation(), DateUtils.addDay(date,15).getTime() /1000, distanceOptimizerConfigurationDto.getFetchStrategy());
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

    /**
     * @return List of valid cooordinates.
     * @throws FileNotFoundException if file not found.
     * */
    public List<String> getValidCoordinates(String sourceFile) throws FileNotFoundException {
        List<String> coordinates = new ArrayList<>();
        Scanner reader = new Scanner(new FileReader(sourceFile));
        while (reader.hasNextLine()) {
            String loc = reader.nextLine();
            if (isValidLatLngString(loc)) {
                coordinates.add(loc);
            }
        }
        reader.close();
        return coordinates;
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

    private Boolean isValidLatLngString (String loc) {
        String[] arr = loc.split(",");
        if (arr.length == 2) {
            if (isDoubleAndValidLocation(arr[0]) && isDoubleAndValidLocation(arr[1])) {
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }

    private boolean isDoubleAndValidLocation(String str) {
        try {
            Double parseDouble = Double.parseDouble(str);
            /*if (parseDouble > 108 || parseDouble < 180){
                return false;
            }*/
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
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
        if (isValidLatLngString(src)) {
            srcLoc = new Location();
            srcLoc.setLng(Double.parseDouble(src.split(",")[1]));
            srcLoc.setLat(Double.parseDouble(src.split(",")[0]));
        }

        Location destLoc = null;
        if (isValidLatLngString(dest)) {
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
