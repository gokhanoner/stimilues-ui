package com.hazelcast.component;

import com.vaadin.data.util.converter.Converter;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Locale;

/**
 * Created by gokhanoner on 05/05/2017.
 */
public class LongToLocalTimeConverter implements Converter<LocalTime, Long> {
    @Override
    public Long convertToModel(LocalTime localTime,
                               Class<? extends Long> aClass,
                               Locale locale) throws ConversionException {
        return null;
    }

    @Override
    public LocalTime convertToPresentation(Long aLong,
                                           Class<? extends LocalTime> aClass,
                                           Locale locale) throws ConversionException {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(aLong), ZoneId.systemDefault()).toLocalTime();
    }

    @Override
    public Class<Long> getModelType() {
        return Long.class;
    }

    @Override
    public Class<LocalTime> getPresentationType() {
        return LocalTime.class;
    }
}
