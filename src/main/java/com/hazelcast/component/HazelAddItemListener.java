package com.hazelcast.component;

import com.hazelcast.core.ItemEvent;
import com.hazelcast.core.ItemListener;

/**
 * Created by gokhanoner on 05/05/2017.
 */
@FunctionalInterface
public interface HazelAddItemListener<T> extends ItemListener<T> {

    @Override
    default void itemRemoved(ItemEvent<T> item) {
        //NOOP
    }
}
