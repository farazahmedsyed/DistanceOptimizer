package com.distance.optimizer.service;

import com.distance.optimizer.dto.DistanceOptimizerConfigurationDto;
import com.distance.optimizer.dto.DataCollectionDto;
import com.distance.optimizer.dto.DistanceInfoDto;
import com.distance.optimizer.dto.LocationPairDto;
import com.distance.optimizer.dto.reponse.google.DistanceMatrixResponse;
import com.distance.optimizer.model.entity.*;
import com.distance.optimizer.model.repository.DistanceRepository;
import com.distance.optimizer.model.repository.LocationPairRepository;
import com.distance.optimizer.model.repository.LocationStringRepository;
import com.distance.optimizer.utils.*;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.distance.optimizer.exception.DistanceOptimizerException;
import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.distance.optimizer.dto.reponse.google.Element;
import com.distance.optimizer.dto.reponse.google.Row;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
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
    private String fetchStrategy = "best_guess";

    @Autowired
    private LocationStringRepository locationStringRepository;
    @Autowired
    private LocationPairRepository locationPairRepository;
    @Autowired
    private DistanceRepository distanceRepository;
    @Autowired
    private DistanceOptimizerConfigurationDto distanceOptimizerConfigurationDto;

    /**
     * @return google fetch strategy
     * */
    public String getFetchStrategy() {
        return fetchStrategy;
    }

    /**
     * @param fetchStrategy set google fetch strategy
     * */
    public void setFetchStrategy(String fetchStrategy) {
        this.fetchStrategy = fetchStrategy;
    }

    /**
     * Fetch location from com.distanceoptimizer.api and post distance to com.distanceoptimizer.api.
     * */
    public void executeRemote(){
        LOGGER.info("Executing Data Collection remote.");
        for(String googleApiKey : distanceOptimizerConfigurationDto.getGoogleApiKeys()) {
            int i = 100;
            while (i > 0) {
                try {
                    saveDataForDataCollectionRemote(processLocationPairs(getDataForDataCollectionRemote(), googleApiKey));
                }
                catch (Exception e) {
                   LOGGER.error(e);
                }
                i--;
            }
        }
    }

    /**
     * Fetch location from database and save distance in database.
     * */
    public void executeLocal(){
        LOGGER.info("Executing Data Collection local.");
        for(String googleApiKey : distanceOptimizerConfigurationDto.getGoogleApiKeys()) {
            int i = 1;
            while (i > 0) {
                try {
                    saveDataForDataCollectionLocal(processLocationPairs(getDataForDataCollectionLocal(), googleApiKey));
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
     * @param trafficModel Traffic Model
     * @param fraction Fraction
     * */
    public Distance getDistance (String srcLoc, String destLoc, Date departureTime, String trafficModel, Double fraction) throws DistanceOptimizerException {
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
            DistanceMatrixResponse distanceMatrixResponse = googleService.getDistanceMatrixResponse(srcLoc, destLoc, (departureTime.getTime()/1000), trafficModel);

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
            DistanceMatrixResponse distanceMatrixResponse = googleService.getDistanceMatrixResponse(srcLoc, destLoc, (departureTime.getTime()/1000), trafficModel);

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
     * @return unprocessed location pairs fetched from url
     * @throws DistanceOptimizerException
     * */
    public List<LocationPairDto> getDataForDataCollectionRemote() throws DistanceOptimizerException {
        LOGGER.info("Fetching Data Collection remote.");
        ListMultimap<String, String> queryParams = ArrayListMultimap.create();
        queryParams.put("apikey", distanceOptimizerConfigurationDto.getApiKey());
        return WebServiceUtils.get(distanceOptimizerConfigurationDto.getDevURLGetLocations(), queryParams, null, new HttpResponseProcessing() {
            @Override
            public Object process(String responseString) throws DistanceOptimizerException, IOException {
                ObjectMapper objectMapper = new ObjectMapper();
                Response response = objectMapper.readValue(responseString, Response.class);
                if (response.getResponseHeader().getIsError() == true) {
                    throw new DistanceOptimizerException();
                }

                String temp = objectMapper.writeValueAsString(response.getResponseBody().get("response"));
                List<LocationPairDto> pairs = objectMapper.readValue(temp, new TypeReference<List<LocationPairDto>>() {
                });
                return pairs;
            }
        });

    }

    /**
     * post dataCollectionDtos to url
     * @param dataCollectionDtos distances fetched for un processed locations.
     * @throws IOException
     * @throws DistanceOptimizerException
     * */
    public void saveDataForDataCollectionRemote(List<DataCollectionDto> dataCollectionDtos) throws IOException, DistanceOptimizerException {
        LOGGER.info("posting processed location pairs distance to remote.");
        ListMultimap<String, String> queryParams = ArrayListMultimap.create();
        queryParams.put("apikey", distanceOptimizerConfigurationDto.getApiKey());
        if (EntityHelper.isListPopulated(dataCollectionDtos)) {
            ObjectMapper objectMapper = new ObjectMapper();
            String request = objectMapper.writeValueAsString(dataCollectionDtos);
            WebServiceUtils.post(distanceOptimizerConfigurationDto.getDevURLSaveData(), request, queryParams, null, new HttpResponseProcessing() {
                @Override
                public Object process(String responseString) throws DistanceOptimizerException, IOException {
                    ObjectMapper objectMapper = new ObjectMapper();
                    return objectMapper.readValue(responseString, Response.class);
                }
            });
        }
    }

    /**
     * @return processed dataCollectionDtos to save remotely or local
     * @throws DistanceOptimizerException
     * @throws ParseException
     * @throws InterruptedException
     * */
    private List<DataCollectionDto> processLocationPairs(List<LocationPairDto> locationPairDtos, String googleApiKey) throws DistanceOptimizerException, ParseException, InterruptedException {
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
            distanceMatrixResponse = googleService.getDistanceMatrixResponse(srcParam, destParam.toString(), date.getTime() / 1000, fetchStrategy);
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
                dto.setDayOfWeek(getDayOfWeek(date));
                dto.setStatus(row.getElements().get(j).getStatus());
                dto.setErrorMessage(distanceMatrixResponse.getErrorMessage());

                dtos.add(dto);
            } catch (Exception e) {
               LOGGER.error(e);
            }
        }

        return dtos;

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
                            distance.getDest().getLocation(), DateUtils.addDay(date,15).getTime() /1000, fetchStrategy);
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

    private static String getDayOfWeek (Date date) {
        if (EntityHelper.isNull(date))
            return null;

        switch (date.getDay()) {
            case 1:
                return "M";
            case 2:
                return "T";
            case 3:
                return "W";
            case 4:
                return "TH";
            case 5:
                return "F";
            case 6:
                return "S";
            default:
                return "SU";
        }
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
            if (parseDouble > 108 || parseDouble < 180){
                return false;
            }
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
        distance.setDayOfWeek(getDayOfWeek(departureTime));
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
        distance.setDayOfWeek(getDayOfWeek(departureTime));
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
