package com.distance.optimizer.model.repository;

import com.distance.optimizer.model.entity.Distance;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Date;
import java.util.List;

/**
 * @author FarazAhmed
 */
public interface DistanceRepository extends MongoRepository<Distance, String>, DistanceCustomRepository{
    List<Distance> findFirst100DistanceByUpdatedAtLessThan(Date date);
}
