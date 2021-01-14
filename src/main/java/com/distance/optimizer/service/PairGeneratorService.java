package com.distance.optimizer.service;

import com.distance.optimizer.model.entity.LocationPair;
import com.distance.optimizer.model.repository.LocationPairRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

/**
 * @author FarazAhmed
 * <p>Read location from file create source destination pair and save it into LocationPair.</p>
 */
@Component
public class PairGeneratorService {

    private static final Logger LOGGER = Logger.getLogger(PairGeneratorService.class);
    @Autowired
    private LocationPairRepository locationPairRepository;
    /**
     * @param coordinates list of valid coordinates.
     * */
    public void generate(List<String> coordinates){
        LOGGER.info("Generating locationPairs.");
        List<LocationPair> locationPairs = locationPairRepository.findBySrcLocStrInAndDestLocStrIn(coordinates, coordinates);
        int size = coordinates.size();
        for (int i=0; i<size;i++){
            String source = coordinates.get(i);
            for (int j=i+1;j<size;j++){
                String destination = coordinates.get(j);
                if (!source.trim().equalsIgnoreCase(destination.trim())) {
                    if (!locationPairs.parallelStream().filter(l -> l.getSrcLocStr().equals(source) && l.getDestLocStr().equals(destination)).findFirst().isPresent()) {
                        save(source, destination);
                    }
                }
            }
        }

    }

    public void save(String srcLoc, String destLoc){
        Objects.requireNonNull(srcLoc);
        Objects.requireNonNull(destLoc);
        LocationPair locationPair = new LocationPair();
        locationPair.setSrcLocStr(srcLoc);
        locationPair.setDestLocStr(destLoc);
        locationPair.setSent(Boolean.FALSE);
        locationPairRepository.save(locationPair);
    }
}
