package com.demo;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.spring.cache.HazelcastCacheManager;
import com.hazelcast.spring.cache.SpringHazelcastCachingProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import javax.cache.CacheManager;
import javax.cache.Caching;
import java.util.Properties;

@SpringBootApplication
@ComponentScan("com.demo")
@EnableCaching
public class Application {

    @Bean
    @ConditionalOnProperty("custom-hz-cache-manager")
    public HazelcastCacheManager hzCacheManager(HazelcastInstance instance) {
        return new com.hazelcast.spring.cache.HazelcastCacheManager(instance);
    }

    @Bean
    @ConditionalOnProperty("custom-jx-cache-manager")
    public CacheManager jxCacheManager() {
        return Caching.getCachingProvider().getCacheManager();
    }

    @Bean
    @ConditionalOnProperty("custom-spring-hz-cache-manager")
    public CacheManager springHzProvider(HazelcastInstance instance) {
        return SpringHazelcastCachingProvider.getCacheManager(instance, null, new Properties());
    }


    public static void main(String[] args) {

        ConfigurableApplicationContext ctx = SpringApplication.run(Application.class, args);
        TheService service = ctx.getBean(TheService.class);

        new Thread(() -> {
            String prevResult = "";
            int count = 0;
            int step = 7;

            while(true) {
                String result = service.getData("test");
                if (!result.equals(prevResult)) {
                    prevResult = result;
                    count = 0;
                }

                System.out.println(count + " seconds since created. \t Result: " + result);

                try {Thread.sleep(step * 1000);} catch(Exception e){}
                count += step;
            }

        }).run();

    }
}