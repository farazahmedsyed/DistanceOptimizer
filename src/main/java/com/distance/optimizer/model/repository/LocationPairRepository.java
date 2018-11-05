package com.distance.optimizer.model.repository;


import com.distance.optimizer.model.entity.LocationPair;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * @author FarazAhmed
 */
public interface LocationPairRepository extends MongoRepository<LocationPair, String> {
    List<LocationPair> findFirst25BySrcLocStrAndSent(String srcLocStr, Boolean sent);
    LocationPair findFirst1BySrcLocStrAndDestLocStr(String srcLocStr, String destLocStr);
    List<LocationPair> findBySrcLocStrInAndDestLocStrIn(List<String> srcLocStr, List<String> destLocStr);
}
