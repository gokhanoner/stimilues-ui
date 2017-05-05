package com.hazelcast.model;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by gokhanoner on 04/05/2017.
 */
@Data
public class MockObj implements Serializable{
    private final double lat;
    private final double lon;
}
