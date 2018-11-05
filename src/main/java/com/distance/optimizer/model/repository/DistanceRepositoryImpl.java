package com.distance.optimizer.model.repository;

import com.distance.optimizer.model.entity.Location;
import com.distance.optimizer.model.entity.Distance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.geo.Circle;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

/**
 * @author FarazAhmed
 */

public class DistanceRepositoryImpl implements DistanceCustomRepository {

    private static final Double RADIUS = 0.005;
    @Autowired
    @Qualifier("mongoTemplate")
    private MongoOperations mongoOperations;

    @Override
    public Distance withinSrcAndDestCirclesAndHourIn(Location src, Location dest, List<Integer> hours) {
        Circle srcCircle = new Circle(src.getLng(), src.getLat(), RADIUS);
        Circle destCircle = new Circle(dest.getLng(), dest.getLat(), RADIUS);
        Query query = new Query(Criteria.where("s").within(srcCircle).and("d").within(destCircle));
        List<Distance> distances = mongoOperations.find(query, Distance.class);
        if (distances != null && distances.size() > 0) {
            return distances.get(0);
        }
        return null;
    }
}
