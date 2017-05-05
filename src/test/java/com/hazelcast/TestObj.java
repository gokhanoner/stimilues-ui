package com.hazelcast;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * Created by gokhanoner on 04/05/2017.
 */
@Data
public class TestObj {
    private String ip;
    private String version;
    private LocalDateTime pingTime;
    private String machineId;
    private boolean enterprise;
    private String license;
    private String clusterSize;
    private String country;
    private double lat;
    private double lon;
    private long upTime;
}
