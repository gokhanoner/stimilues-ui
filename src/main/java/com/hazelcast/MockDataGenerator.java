package com.hazelcast;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.model.Client;
import com.hazelcast.satimulus.domain.Alert;
import com.hazelcast.model.MockObj;
import com.hazelcast.satimulus.domain.PhoneHome;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Map;
import java.util.Random;

/**
 * Created by gokhanoner on 04/05/2017.
 */
@RequiredArgsConstructor
public class MockDataGenerator {

    private final HazelcastInstance hi;
    private final JetProperties jp;
    private final Random rnd = new Random(System.currentTimeMillis());


    @Scheduled(fixedDelay = 10000, initialDelay = 15000)
    protected void addMockData() {
        Alert alert = new Alert();
        alert.setDesc("dfsdfsf");
        alert.setTimestamp(System.currentTimeMillis());
        PhoneHome ph = new PhoneHome();
        int size = hi.getMap(jp.getQueues().get(JetProperties.QueueType.CLIENTS)).size();
        int elem = Math.abs(rnd.nextInt(size));

        Client client = (Client) hi.getMap(jp.getQueues().get(JetProperties.QueueType.CLIENTS))
                .entrySet().stream()
                .skip(elem)
                .findFirst()
                .get()
                .getValue();

        ph.setOrg(client.getName());

        alert.setPhoneHome(ph);

        hi.getList(jp.getQueues().get(JetProperties.QueueType.ALERTS)).add(alert);
    }

    @Scheduled(fixedDelay = 10000, initialDelay = 30000)
    protected void addMockData2() {
        Alert alertObj = new Alert();
        alertObj.setTimestamp(System.currentTimeMillis() - 32562L);
        alertObj.setDesc("test desc");
        PhoneHome ph = new PhoneHome();
        int size = hi.getMap(jp.getQueues().get(JetProperties.QueueType.CLIENTS)).size();
        int elem = Math.abs(rnd.nextInt(size));

        Client client = (Client) hi.getMap(jp.getQueues().get(JetProperties.QueueType.CLIENTS))
                .entrySet().stream()
                .skip(elem)
                .findFirst()
                .get()
                .getValue();

        ph.setOrg(client.getName());

        alertObj.setPhoneHome(ph);

        hi.getList(jp.getQueues().get(JetProperties.QueueType.WINDOWING)).add(alertObj);
    }
}
