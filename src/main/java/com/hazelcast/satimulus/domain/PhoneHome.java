package com.hazelcast.satimulus.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by gokhanoner on 04/05/2017.
 */
@Data
public class PhoneHome implements Serializable{
    private static final long serialVersionUID = 2L;
    private String ip;
    private String version;
    private long pingTime;
    private String machineId;
    private boolean enterprise;
    private String license;
    private String clusterSize;
    private String country;
    private double lat;
    private double lon;
    private long upTime;
    private String org;
}
