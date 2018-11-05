package com.distance.optimizer.model.repository;


import com.distance.optimizer.model.entity.LocationString;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author FarazAhmed
 */

@Repository
public interface LocationStringRepository extends MongoRepository<LocationString, String> {
    LocationString findFirst1ByCompleted(Boolean isCompleted);
    LocationString findFirst1ByLoc(String loc);
    List<LocationString> findByLocIn(List<String> locs);
}
