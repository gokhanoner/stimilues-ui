package com.hazelcast.component;

import com.hazelcast.satimulus.domain.Alert;
import com.vaadin.data.Property;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.Table;
import com.vaadin.ui.themes.ValoTheme;
import lombok.Data;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * Created by gokhanoner on 04/05/2017.
 */
@SuppressWarnings("serial")
public final class AlertTable extends Table {

    private BeanItemContainer<AlertDao> bic;


    public AlertTable() {
        setCaption("Alerts");
        addStyleName(ValoTheme.TABLE_BORDERLESS);
        addStyleName(ValoTheme.TABLE_NO_STRIPES);
        addStyleName(ValoTheme.TABLE_NO_VERTICAL_LINES);
        addStyleName(ValoTheme.TABLE_NO_HORIZONTAL_LINES);
        setSortEnabled(false);
        setColumnAlignment("desc", Align.RIGHT);
        setRowHeaderMode(RowHeaderMode.HIDDEN);
        setColumnHeaderMode(ColumnHeaderMode.HIDDEN);
        setSizeFull();

        bic = new BeanItemContainer<AlertDao>(AlertDao.class, null);

        setContainerDataSource(bic);

        setVisibleColumns("ts", "org", "desc");
        setColumnHeaders("Timestamp", "Organization", "Description");
        setColumnExpandRatio("ts", 1);
        setColumnExpandRatio("org", 2);
        setColumnExpandRatio("desc", 2);

        setSortContainerPropertyId("ts");
        setSortAscending(false);
    }

    public void addItemAt(int index, Alert item) {
        bic.addItemAt(0, new AlertDao(item));
    }

    @Data
    public class AlertDao {
        private final LocalTime ts;
        private final String org;
        private final String desc;

        public AlertDao(Alert alert) {
            this.ts = LocalDateTime.ofInstant(Instant.ofEpochMilli(alert.getTimestamp()), ZoneId.systemDefault()).toLocalTime();
            this.org = alert.getPhoneHome().getOrg();
            this.desc = alert.getDesc();
        }
    }
}
