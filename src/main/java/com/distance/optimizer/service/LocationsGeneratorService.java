package com.distance.optimizer.service;

import com.distance.optimizer.model.entity.LocationString;
import com.distance.optimizer.model.repository.LocationStringRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author  FarazAhmed
 * <p>Reads location from file and save it into LocationString.</p>
 */
@Component
public class LocationsGeneratorService {

    private static final Logger LOGGER = Logger.getLogger(LocationsGeneratorService.class);

    @Autowired
    private LocationStringRepository locationStringRepository;

    /**
     * @param coordinates valid coordinates from input file.
     * */
    public void generate(List<String> coordinates){
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
}
