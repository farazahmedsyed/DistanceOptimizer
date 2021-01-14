package com.distance.optimizer.api;

import com.distance.optimizer.exception.DistanceOptimizerException;
import com.distance.optimizer.model.entity.Distance;

import java.util.Date;

public interface DistanceApi {
    /**
     * @param srcLoc        source Location.
     * @param destLoc       Destination Location.
     * @param departureTime DepartureTime
     * @param fraction      Fraction
     * @return Distance from database if not found then fetch it from google.
     * @throws DistanceOptimizerException if unable to retreive distance from google.
     */
    Distance getDistance(String srcLoc, String destLoc, Date departureTime, Double fraction) throws DistanceOptimizerException;
}
