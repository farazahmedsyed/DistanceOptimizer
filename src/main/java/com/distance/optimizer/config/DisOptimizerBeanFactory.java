package com.distance.optimizer.config;

import com.distance.optimizer.api.DistanceApi;
import com.distance.optimizer.api.DistanceOptimizerExecutorApi;
import com.distance.optimizer.api.DistanceOptimizerRemoteApi;
import com.distance.optimizer.api.LocationApi;
import com.distance.optimizer.utils.ValidationUtil;
import org.springframework.context.ApplicationContext;

public class DisOptimizerBeanFactory {

    private ApplicationContext applicationContext;

    public DisOptimizerBeanFactory(ApplicationContext ctx) {
        ValidationUtil.requireNonNull(ctx);
        applicationContext = ctx;
    }

    public <T> T getBean(Class<T> tClass) {
        return applicationContext.getBean(tClass);
    }

    public DistanceApi getDistanceApi() {
        return applicationContext.getBean(DistanceApi.class);
    }

    public LocationApi getLocationApi() {
        return applicationContext.getBean(LocationApi.class);
    }

    public DistanceOptimizerExecutorApi getDistanceOptimizerExecutorApi() {
        return applicationContext.getBean(DistanceOptimizerExecutorApi.class);
    }

    public DistanceOptimizerRemoteApi getDistanceOptimizerRemoteApi() {
        return applicationContext.getBean(DistanceOptimizerRemoteApi.class);
    }
}
