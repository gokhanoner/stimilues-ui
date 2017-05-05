package com.hazelcast;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;
import java.util.Map;

/**
 * Created by gokhanoner on 04/05/2017.
 */
@ConfigurationProperties(prefix = "hazelcast.jet")
@Data
public class JetProperties {

    private List<String> nodes;
    private Map<QueueType, String> queues;


    public enum QueueType {
        ALERTS,
        WINDOWING,
        CLIENTS;
    }
}
