## Demo

This repository demonstrates the use of Hazelcast as the cache provider for JCache in a Spring boot REST application.

One hazelcast cache called `testCache` is configured in the `hazelcast.xml` file. This cache is set up to expire entries 
after one minute. 

Hazelcast is added to the Spring project by adding the following two dependencies to the pom file:

                <dependency>
                    <groupId>com.hazelcast</groupId>
                    <artifactId>hazelcast</artifactId>
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


        


        