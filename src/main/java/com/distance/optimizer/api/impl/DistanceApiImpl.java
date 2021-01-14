package com.distance.optimizer.api.impl;

import com.distance.optimizer.api.DistanceApi;
import com.distance.optimizer.exception.DistanceOptimizerException;
import com.distance.optimizer.model.entity.Distance;
import com.distance.optimizer.service.DistanceService;
import com.distance.optimizer.utils.ValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class DistanceApiImpl implements DistanceApi {

    private DistanceService distanceService;

    @Autowired
    public DistanceApiImpl(DistanceService distanceService) {
        ValidationUtil.requireNonNull(distanceService);
        this.distanceService = distanceService;
    }

    @Override
    public Distance getDistance(String srcLoc, String destLoc, Date departureTime, Double fraction) throws DistanceOptimizerException {
        return distanceService.getDistance(srcLoc, destLoc, departureTime, fraction);
    }

}
