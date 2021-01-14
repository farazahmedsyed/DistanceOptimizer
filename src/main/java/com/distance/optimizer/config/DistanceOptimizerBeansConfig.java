package com.distance.optimizer.config;

import com.mongodb.MongoClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

/**
 * @author Faraz Ahmed
 * <p>Creates required configuration beans.</p>
 * */
@Configuration
@ComponentScan({"com.distance.optimizer"})
@EnableMongoRepositories(basePackages = "com.distance.optimizer.model.repository")
@EnableScheduling
public class DistanceOptimizerBeansConfig {


    /**
     * @return Instance of mongo client.
     * */
    public static MongoClient mongo(String serverAddress, Integer port){
        return new MongoClient(serverAddress, port);
    }

    /**
     * @return Instance of mongo template
     * */
    public static MongoTemplate mongoTemplate(String databaseName, String serverAddress, Integer port) {
        return new MongoTemplate(mongo(serverAddress, port),databaseName);
    }

    /**
     * Creates Task scheduler bean, task will be added when first instance is created from Distance optimizer configuration.
     * */
    @Bean
    public ThreadPoolTaskScheduler threadPoolTaskScheduler(){
        ThreadPoolTaskScheduler threadPoolTaskScheduler
                = new ThreadPoolTaskScheduler();
        threadPoolTaskScheduler.setPoolSize(5);
        threadPoolTaskScheduler.setThreadNamePrefix("ThreadPoolTaskScheduler");
        return threadPoolTaskScheduler;
    }
}
