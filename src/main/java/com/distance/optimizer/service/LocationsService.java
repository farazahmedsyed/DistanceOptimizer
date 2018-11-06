package com.distance.optimizer.service;

import com.distance.optimizer.dto.DistanceOptimizerConfigurationDto;
import com.distance.optimizer.exception.DistanceOptimizerException;
import com.distance.optimizer.model.entity.LocationString;
import com.distance.optimizer.model.repository.LocationStringRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * @author  FarazAhmed
 */
@Component
public class LocationsService {

    private static final Logger LOGGER = Logger.getLogger(LocationsService.class);

    @Autowired
    private LocationStringRepository locationStringRepository;
    @Autowired
    private DistanceOptimizerConfigurationDto distanceOptimizerConfigurationDto;

    /**
     * @param coordinates valid coordinates from input file.
     * */
    public void saveToDatabase(List<String> coordinates){
        List<String> copied =coordinates.parallelStream().collect(Collectors.toList());
        LOGGER.info("Generating locationStrings.");
        Optional.ofNullable(locationStringRepository.findByLocIn(coordinates)).ifPresent(l -> copied.removeAll(l.parallelStream().map(s -> s.getLoc()).collect(Collectors.toList())));
        for (String location : copied) {
                LocationString locationString = new LocationString();
                locationString.setCompleted(Boolean.FALSE);
                locationString.setLoc(location);
                locationStringRepository.save(locationString);
        }
    }

    /**
     * @param inputFile Address File
     * @param outputFile Location File
     * @throws IOException
     * @throws DistanceOptimizerException
     * */
    public void generate(String inputFile, String outputFile) throws IOException, DistanceOptimizerException {
        List<String> addresses = new ArrayList<>();
        Scanner reader = new Scanner(new FileReader(inputFile));
        while (reader.hasNextLine()) {
            String loc = reader.nextLine();
            addresses.add(loc);
        }
        reader.close();

        Integer size = distanceOptimizerConfigurationDto.getGoogleApiKeys().size();
        Integer index = 0;
        GoogleService googleService;
        StringBuilder stringBuilder = new StringBuilder();
        for (String address : addresses){
            googleService = new GoogleService(distanceOptimizerConfigurationDto.getGoogleApiKeys().get(index));
            index++;
            if (index >= size){
                index = 0;
            }
            stringBuilder.append(googleService.getLocationFromGeocodeResponse(address));
        }

        BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));
        writer.write(stringBuilder.toString());
        writer.close();
    }
}
