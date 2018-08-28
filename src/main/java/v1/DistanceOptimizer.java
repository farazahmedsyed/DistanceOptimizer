package v1;

import api.GoogleDistanceApi;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import dto.DataCollectionDto;
import dto.DistanceInfoDto;
import exception.ROException;
import model.LocationPair;
import reponse.DistanceMatrixResponse;
import reponse.Row;
import utils.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by VenD on 9/13/2017.
 */
public class DistanceOptimizer {

    private String DATE_TIME;
    private String apiKey;
    private String devURLGetLocations;
    private String devURLSaveData;
    private List<String> googleApiKeys;

    public DistanceOptimizer(String DATE_TIME,
                             String apiKey,
                             String devURLGetLocations,
                             String devURLSaveData, List<String> googleApiKeys) {
        this.DATE_TIME = DATE_TIME;
        this.apiKey = apiKey;
        this.devURLGetLocations = devURLGetLocations;
        this.devURLSaveData = devURLSaveData;
        this.googleApiKeys = googleApiKeys;
    }

    public void execute(){

        ListMultimap<String, String> queryParams = ArrayListMultimap.create();
        queryParams.put("apikey", apiKey);
        for(String googleApiKey : googleApiKeys) {
            System.out.println(googleApiKey);
            int i = 100;
            while (i > 0) {
                try {
                    List<LocationPair> locationPairs = WebServiceUtils.get(devURLGetLocations, queryParams, null, new HttpResponseProcessing() {
                        @Override
                        public Object process(String responseString) throws ROException, IOException {
                            ObjectMapper objectMapper = new ObjectMapper();
                            Response response = objectMapper.readValue(responseString, Response.class);
                            if (response.getResponseHeader().getIsError() == true) {
                                throw new ROException();
                            }

                            String temp = objectMapper.writeValueAsString(response.getResponseBody().get("response"));
                            List<LocationPair> pairs = objectMapper.readValue(temp, new TypeReference<List<LocationPair>>() {
                            });
                            return pairs;
                        }
                    });


                    if (EntityHelper.isListNotPopulated(locationPairs)) {
                        throw new ROException();
                    }

                    String srcParam = locationPairs.get(0).getSrcLocStr();
                    StringBuffer destParam = new StringBuffer();
                    List<String> destList = new ArrayList<>();
                    for (int j = 0; j < locationPairs.size(); j++) {
                        destList.add(locationPairs.get(j).getDestLocStr());
                        if (j == 0) {
                            destParam.append(locationPairs.get(j).getDestLocStr());
                        } else {
                            destParam.append("|" + locationPairs.get(j).getDestLocStr());
                        }
                    }

                    Date date = DateUtils.formatDate(DATE_TIME);

                    DistanceMatrixResponse distanceMatrixResponse = null;
                    try {
                        Thread.sleep(500);
                        GoogleDistanceApi googleDistanceApi = new GoogleDistanceApi(googleApiKey);
                        distanceMatrixResponse = googleDistanceApi.getDistanceMatrixResponse(srcParam, destParam.toString(), date.getTime() / 1000, "best_guess");
                        //System.out.println(distanceMatrixResponse.toString());
                    } catch (ROException e) {
                        break;
                    }

                    Row row = distanceMatrixResponse.getRows().get(0);

                    List<DataCollectionDto> dtos = new ArrayList<>();
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
                            dto.setServiceAreaId(2);

                            dtos.add(dto);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    if (EntityHelper.isListPopulated(dtos)) {
                        ObjectMapper objectMapper = new ObjectMapper();
                        String request = objectMapper.writeValueAsString(dtos);

                        Response response = WebServiceUtils.post(devURLSaveData, request, queryParams, null, new HttpResponseProcessing() {
                            @Override
                            public Object process(String responseString) throws ROException, IOException {
                                ObjectMapper objectMapper = new ObjectMapper();
                                return objectMapper.readValue(responseString, Response.class);
                            }
                        });
                    }

                    //System.out.println();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                i--;
            }
        }
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
}
