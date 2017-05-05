package com.hazelcast.satimulus.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by gokhanoner on 04/05/2017.
 */
@Data
public class Alert implements Serializable{
    private static final long serialVersionUID = 1L;
    private long timestamp;
    private String desc;
    private PhoneHome phoneHome;

}
