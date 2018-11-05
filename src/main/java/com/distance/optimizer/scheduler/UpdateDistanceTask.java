package com.distance.optimizer.scheduler;

import com.distance.optimizer.service.DistanceService;

/**
 * @author FarazAhmed
 * <p>Run by the cron expression provided by user in distance optimizer configuration</p>
 */
public class UpdateDistanceTask  implements  Runnable{

    DistanceService distanceService;

    public UpdateDistanceTask(DistanceService distanceService) {
        this.distanceService = distanceService;
    }

    @Override
    public void run() {
        distanceService.updateDistanceCronJob();
    }
}
