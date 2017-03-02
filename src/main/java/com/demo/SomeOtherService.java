package com.demo;

import com.hazelcast.core.HazelcastInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by dirk on 3/03/17.
 */
@Service
public class SomeOtherService {

    @Autowired
    private HazelcastInstance instance;


    public void test() {
        System.out.println(instance.getConfig());
    }

}