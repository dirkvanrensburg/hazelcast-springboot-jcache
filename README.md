## Demo

This repository demonstrates the use of Hazelcast as the cache provider for JCache in a Spring boot REST application.

One hazelcast cache called `testCache` is configured in the `hazelcast.xml` file. This cache is set up to expire entries 
after one minute. 

Hazelcast is added to the Spring project by adding the following two dependencies to the pom file:

                <dependency>
                    <groupId>com.hazelcast</groupId>
                    <artifactId>hazelcast</artifactId>
                </dependency>
        
                <dependency>
                    <groupId>com.hazelcast</groupId>
                    <artifactId>hazelcast-spring</artifactId>
                    <version>${hazelcast.version}</version>
                </dependency>


## Usage

To use this demo:

* Clone the repository
* Run the following commands

        > mvn clean package
        > java -jar taget/hazelcast-springboot-jcache-1.0-SNAPSHOT.jar --server.port=8083

You'll see a Tomcat server start up. Once everything is started you should see something like the following:

        @@@@> Cache miss! Getting new value
        0 seconds since created. 	 Result: test_1486696868113
        7 seconds since created. 	 Result: test_1486696868113


Around 60 seconds later you should see:
        
        56 seconds since created. 	 Result: test_1486696868113
        @@@@> Cache miss! Getting new value
        0 seconds since created. 	 Result: test_1486696931133


> NOTE: The number at the end of the result value will vary. That is the current time in milliseconds


## Issue - Two Hazelcast instances

If you look closely at the console output when starting the application you will notice that two Hazelcast instances are
created and they join in a cluster.

### Hazelcast cache manager
This seems to be the default behaviour. I tried to get only one hazelcast instance to start by providing a `CacheManager`

To start that application with a custom hazelcast cache manager use:

        > java -jar taget/hazelcast-springboot-jcache-1.0-SNAPSHOT.jar --server.port=8083 --custom-hz-cache-manager=true
        
This doesn't work as expected because the cache entry no longer expires after one minute. Inspecting the management centre shows 
that the entry is cached in a Map instead of a Cache.

### JCache cache manager

Try:

        > java -jar taget/hazelcast-springboot-jcache-1.0-SNAPSHOT.jar --server.port=8083 --custom-jx-cache-manager=true
    
Caching works but we still have two Hazelcast instances. So providing a JCache provider has no effect.

### Spring Hazelcast cache manager

Change `pom.xml` to look like this:
(Comment the `hazelcast` and `hazelcast-spring` dependencies and uncomment the `hazelcast-all` dependency)

                <!-- HAZELCAST -->
                <!--<dependency>-->
                    <!--<groupId>com.hazelcast</groupId>-->
                    <!--<artifactId>hazelcast</artifactId>-->
                <!--</dependency>-->
        
                <!--<dependency>-->
                    <!--<groupId>com.hazelcast</groupId>-->
                    <!--<artifactId>hazelcast-spring</artifactId>-->
                    <!--<version>${hazelcast.version}</version>-->
                <!--</dependency>-->
                
                <dependency>
                    <groupId>com.hazelcast</groupId>
                    <artifactId>hazelcast-all</artifactId>
                    <version>${hazelcast.version}</version>
                </dependency>

Then:

        > mvn clean package
        > java -jar taget/hazelcast-springboot-jcache-1.0-SNAPSHOT.jar --server.port=8083 --custom-spring-hz-cache-manager=true
        
Now it works and there is only one cache manager. The entry is placed in a `cache` and is expired appropriately.


## Observations

* Having two hazelcast instances seem to be the default when you enable caching (`@EnableCaching`)

* When only adding `hazelcast-all` as dependency and trying to start the application without providing a custom cache manager, Hazelcast loads 
using the `hazelcast-client-default.xml` and fails because there is no running cluster


## Questions

* Is there a way to get spring auto configuration to only start one Hazelcast instance?


        


        