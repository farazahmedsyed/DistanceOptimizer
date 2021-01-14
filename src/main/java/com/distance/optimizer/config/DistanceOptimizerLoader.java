package com.distance.optimizer.config;

import com.distance.optimizer.api.DistanceOptimizerExecutorApi;
import com.distance.optimizer.api.impl.DistanceOptimizerExecutorLocalApi;
import com.distance.optimizer.api.impl.DistanceOptimizerExecutorRemoteApi;
import com.distance.optimizer.dto.DisOptimizerDatabaseDto;
import com.distance.optimizer.dto.DisOptimizerDto;
import com.distance.optimizer.dto.DisOptimizerLocalDto;
import com.distance.optimizer.dto.DisOptimizerRemoteDto;
import com.distance.optimizer.exception.DistanceOptimizerException;
import com.distance.optimizer.scheduler.UpdateDistanceTask;
import com.distance.optimizer.service.DistanceService;
import com.distance.optimizer.service.LocationProcessorService;
import com.distance.optimizer.service.LocationsService;
import com.distance.optimizer.utils.EntityHelper;
import com.distance.optimizer.utils.ValidationUtil;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;

/**
 * @author FarazAhmed.
 */
public class DistanceOptimizerLoader {

    private static final Logger LOGGER = Logger.getLogger(DistanceOptimizerLoader.class);
    private final static String MONGO_TEMPLATE = "mongoTemplate";
    private final static String DISTANCE_OPTIMIZER_DTO = "disOptimizerDto";

    public static DisOptimizerBeanFactory beanFactoryApi;

    private DistanceOptimizerLoader() {
        throw new AssertionError("constructor not allowed");
    }

    /**
     * @return distanceOptimizerService
     */
    public static DisOptimizerBeanFactory getInstance() {
        return beanFactoryApi;
    }

    /**
     * It is the entry point of library.
     * It returns the DistanceOptimizerService object.
     *
     * @return Distance Optimizer object
     * @throws DistanceOptimizerException if distanceOptimizerSerice is already initialized
     */
    public static synchronized DisOptimizerBeanFactory build(DisOptimizerDto disOptimizerDto) {
        LOGGER.info("Initializing Distance Optimizer.");
        ValidationUtil.requireNonNull(disOptimizerDto);
        ValidationUtil.requireNull(beanFactoryApi);
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
        DistanceOptimizerLoader.loadMongoDBInstanceIfRequired(disOptimizerDto, ctx);
        ctx.getBeanFactory().registerSingleton(DISTANCE_OPTIMIZER_DTO, disOptimizerDto);
        ctx.register(DistanceOptimizerBeansConfig.class);
        ctx.refresh();
        DistanceOptimizerLoader.setSchedulerIfRequired(disOptimizerDto, ctx);
        registerExecutorApiBean(ctx, disOptimizerDto);
        beanFactoryApi = new DisOptimizerBeanFactory(ctx);
        return beanFactoryApi;
    }

    private static void registerExecutorApiBean(AnnotationConfigApplicationContext ctx,
                                                                   DisOptimizerDto disOptimizerDto){
        ValidationUtil.requireNonNull(ctx, disOptimizerDto);
        DistanceOptimizerExecutorApi executorApi;
        LocationProcessorService lps = ctx.getBean(LocationProcessorService.class);
        if (disOptimizerDto instanceof DisOptimizerLocalDto){
            LocationsService lc =ctx.getBean(LocationsService.class);
            DistanceService ds =ctx.getBean(DistanceService.class);
            executorApi = new DistanceOptimizerExecutorLocalApi(
                    (DisOptimizerLocalDto) disOptimizerDto, lc,lps,ds);
        }
        else if (disOptimizerDto instanceof DisOptimizerRemoteDto){
            executorApi = new DistanceOptimizerExecutorRemoteApi(
                    (DisOptimizerRemoteDto) disOptimizerDto, lps);
        }
        else {
            return ;
        }
        ctx.getBeanFactory()
                .registerSingleton("DistanceOptimizerExecutorApi", executorApi);
    }
    /**
     * Initialize mongodb instance if server, port and database is set in distanceOptimizerConfigurationDto
     */
    public static void loadMongoDBInstanceIfRequired(DisOptimizerDto disOptimizerDto,
                                                     AnnotationConfigApplicationContext ctx) {
        DisOptimizerDatabaseDto disOptimizerDatabaseDto = getDbDto(disOptimizerDto);
        if (disOptimizerDatabaseDto == null) {
            return;
        }
        LOGGER.info("Initializing mongoDB instance.");
        ctx.getBeanFactory().registerSingleton(
                MONGO_TEMPLATE,
                DistanceOptimizerBeansConfig.mongoTemplate(
                        disOptimizerDatabaseDto.getDatabaseName(),
                        disOptimizerDatabaseDto.getServerAddress(),
                        disOptimizerDatabaseDto.getPort()
                )
        );
    }

    private static DisOptimizerDatabaseDto getDbDto(DisOptimizerDto disOptimizerDto) {
        ValidationUtil.requireNonNull(disOptimizerDto);
        DisOptimizerLocalDto disOptimizerLocalDto = null;
        if (disOptimizerDto instanceof DisOptimizerLocalDto) {
            disOptimizerLocalDto = (DisOptimizerLocalDto) disOptimizerDto;
        }
        return disOptimizerLocalDto.getDisOptimizerDatabaseDto();
    }

    /**
     * Add cron job if cron expression is set in distanceOptimizerConfigurationDto
     */
    public static void setSchedulerIfRequired(DisOptimizerDto disOptimizerDto,
                                              AnnotationConfigApplicationContext ctx) {
        if (Boolean.FALSE.equals(disOptimizerDto instanceof DisOptimizerLocalDto)) {
            return;
        }
        DisOptimizerLocalDto disOptimizerLocalDto = (DisOptimizerLocalDto) disOptimizerDto;
        if (EntityHelper.isStringSet(disOptimizerLocalDto.getSchedulerCronExpression())) {
            LOGGER.info("Adding cron job");
            CronTrigger cronTrigger = new CronTrigger(disOptimizerLocalDto.getSchedulerCronExpression());
            ThreadPoolTaskScheduler threadPoolTaskScheduler = ctx.getBean(ThreadPoolTaskScheduler.class);
            UpdateDistanceTask updateDistanceTask = new UpdateDistanceTask(ctx.getBean(DistanceService.class));
            threadPoolTaskScheduler.schedule(updateDistanceTask, cronTrigger);
        }
    }
}
