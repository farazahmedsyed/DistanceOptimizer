package com.distance.optimizer.model.repository;


import com.distance.optimizer.model.entity.Distance;
import com.distance.optimizer.model.entity.Location;

import java.util.List;

/**
 * @author FarazAhmed
 */
public interface DistanceCustomRepository {
    Distance withinSrcAndDestCirclesAndHourIn(Location src, Location dest, List<Integer> hours);
}