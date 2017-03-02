package com.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
@EnableCaching
public class Application {

    public static void main(String[] args) {

        ConfigurableApplicationContext ctx = SpringApplication.run(Application.class, args);
        SomeOtherService other = ctx.getBean(SomeOtherService.class);
        other.test();

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