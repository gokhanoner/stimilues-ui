package com.hazelcast.component;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import lombok.Data;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

/**
 * Created by gokhanoner on 04/05/2017.
 */
public class AlertComponent extends HorizontalLayout {

    private Label tsLabel = new Label();
    private Label dataLabel = new Label();


    public AlertComponent(long ts, final String data) {
        LocalDateTime now = LocalDateTime.now();
        //long millis = TimeUnit.MILLISECONDS.convert(ts, TimeUnit.MICROSECONDS);
        LocalDateTime eventTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(ts), ZoneId.systemDefault());

        long seconds = ChronoUnit.SECONDS.between(eventTime, now);

        tsLabel.setValue(eventTime.toLocalTime()  + "");
        dataLabel.setValue(data);

        addComponent(tsLabel);
        addComponent(dataLabel);
        setComponentAlignment(tsLabel, Alignment.MIDDLE_LEFT);
        setComponentAlignment(dataLabel, Alignment.MIDDLE_RIGHT);
        setWidth(30, Unit.PERCENTAGE);
        setHeight(40, Unit.PERCENTAGE);
    }

}
