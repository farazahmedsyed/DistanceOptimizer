package com.distance.optimizer.service;

import com.distance.optimizer.dto.LocationPairDto;
import com.distance.optimizer.model.entity.LocationPair;
import com.distance.optimizer.model.entity.LocationString;
import com.distance.optimizer.model.repository.LocationPairRepository;
import com.distance.optimizer.model.repository.LocationStringRepository;
import com.distance.optimizer.utils.EntityHelper;
import com.distance.optimizer.utils.ValidationUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author FarazAhmed
 */
@Component
public class LocationsService {

    private static final Logger LOGGER = Logger.getLogger(LocationsService.class);

    private LocationStringRepository locationStringRepository;
    private LocationPairRepository locationPairRepository;

    @Autowired
    public LocationsService(LocationStringRepository locationStringRepository,
                            LocationPairRepository locationPairRepository) {
        ValidationUtil.requireNonNull(locationStringRepository, locationPairRepository);
        this.locationStringRepository = locationStringRepository;
        this.locationPairRepository = locationPairRepository;
    }

    public List<LocationString> getAll() {
        return locationStringRepository.findAll();
    }

    /**
     * @param coordinates valid coordinates from input file.
     */
    public void saveToDatabase(List<String> coordinates) {
        List<String> copied = coordinates.parallelStream().collect(Collectors.toList());
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
     * @return unprocessed location pairs from database.
     */
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

    public LocationPair findLocationPair(String src, String dest) {
        ValidationUtil.requireNonNull(src, dest);
        return locationPairRepository.findFirst1BySrcLocStrAndDestLocStr(src.trim(), dest.trim());
    }

    public void deleteLocationPair(LocationPair locationPair) {
        if (EntityHelper.isNotNull(locationPair)) {
            locationPairRepository.delete(locationPair);
        }
    }
}
