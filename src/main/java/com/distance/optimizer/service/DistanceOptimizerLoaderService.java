package com.distance.optimizer.service;

import com.distance.optimizer.dto.DistanceOptimizerConfigurationDto;
import com.distance.optimizer.scheduler.UpdateDistanceTask;
import com.distance.optimizer.config.DistanceOptimizerBeansConfig;
import com.distance.optimizer.exception.DistanceOptimizerException;
import com.distance.optimizer.utils.EntityHelper;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;

/**
 * @author FarazAhmed.
 */
public abstract class DistanceOptimizerLoaderService {

    private static final Logger LOGGER = Logger.getLogger(DistanceOptimizerLoaderService.class);

    private final static String MONGO_TEMPLATE = "mongoTemplate";
    private final static String DISTANCE_OPTIMIZER_CONFIGURATION = "distanceOptimizerConfigurationDto";
    private static DistanceOptimizerService distanceOptimizerService = null;

    /**
     * @return distanceOptimizerService
     * */
    public static DistanceOptimizerService getInstance(){
        return distanceOptimizerService;
    }
    /**
     * It is the entry point of library.
     * It returns the DistanceOptimizerService object.
     * @return Distance Optimizer object
     * @throws DistanceOptimizerException if distanceOptimizerSerice is already initialized
     * */
    public static DistanceOptimizerService initializeInstance(DistanceOptimizerConfigurationDto distanceOptimizerConfigurationDto) throws DistanceOptimizerException {
        LOGGER.info("Initializing DistanceOptimizerService.");
        if (EntityHelper.isNotNull(distanceOptimizerService)){
            throw new DistanceOptimizerException("object already initialized");
        }
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
        loadMongoDBInstanceIfRequired(distanceOptimizerConfigurationDto, ctx);
        ctx.getBeanFactory().registerSingleton(DISTANCE_OPTIMIZER_CONFIGURATION, distanceOptimizerConfigurationDto);
        ctx.register(DistanceOptimizerBeansConfig.class);
        ctx.refresh();
        setSchedulerIfRequired(distanceOptimizerConfigurationDto, ctx);
        distanceOptimizerService = ctx.getBean(DistanceOptimizerService.class);
        return distanceOptimizerService;
    }

    /**
     * Initialize mongodb instance if server, port and database is set in distanceOptimizerConfigurationDto
     * */
    private static void loadMongoDBInstanceIfRequired(DistanceOptimizerConfigurationDto distanceOptimizerConfigurationDto,
                                               AnnotationConfigApplicationContext ctx){
        if (distanceOptimizerConfigurationDto.getPort() != null
                && distanceOptimizerConfigurationDto.getDatabaseName() != null
                && distanceOptimizerConfigurationDto.getServerAddress() != null) {
            LOGGER.info("Initializing mongoDB instance.");
            ctx.getBeanFactory().registerSingleton(
                    MONGO_TEMPLATE,
                    DistanceOptimizerBeansConfig.mongoTemplate(
                            distanceOptimizerConfigurationDto.getDatabaseName(),
                            distanceOptimizerConfigurationDto.getServerAddress(),
                            distanceOptimizerConfigurationDto.getPort()
                    )
            );
        }
    }

    /**
     * Add cron job if cron expression is set in distanceOptimizerConfigurationDto
     * */
    private static void setSchedulerIfRequired(DistanceOptimizerConfigurationDto distanceOptimizerConfigurationDto,
                                               AnnotationConfigApplicationContext ctx){
        if (EntityHelper.isStringSet(distanceOptimizerConfigurationDto.getSchedulerCronExpression())) {
            LOGGER.info("Adding cron job");
            CronTrigger cronTrigger = new CronTrigger(distanceOptimizerConfigurationDto.getSchedulerCronExpression());
            ThreadPoolTaskScheduler threadPoolTaskScheduler = ctx.getBean(ThreadPoolTaskScheduler.class);
            UpdateDistanceTask updateDistanceTask = new UpdateDistanceTask(ctx.getBean(DistanceService.class));
            threadPoolTaskScheduler.schedule(updateDistanceTask, cronTrigger);
        }
    }
}
