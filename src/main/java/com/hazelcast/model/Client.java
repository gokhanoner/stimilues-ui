package com.hazelcast.model;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by gokhanoner on 05/05/2017.
 */
@Data
public class Client implements Serializable {
    private final String name;
    private final double lat;
    private final double lon;
}
