package com.hazelcast.component;

import com.hazelcast.satimulus.domain.Alert;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.Window;
import org.vaadin.viritin.fields.LabelField;
import org.vaadin.viritin.form.AbstractForm;
import org.vaadin.viritin.layouts.MVerticalLayout;

import java.time.LocalTime;

/**
 * Created by gokhanoner on 04/05/2017.
 */
public class AlertViewer extends AbstractForm<AlertTable.AlertDao> {

    private LabelField<LocalTime> ts = new LabelField<LocalTime>(LocalTime.class);
    private LabelField<String> org = new LabelField<String>(String.class);
    private LabelField<String> desc = new LabelField<String>(String.class);
    private Label additional = new Label();

    public AlertViewer() {
        setModalWindowTitle("Alert Viewer");
        ts.setCaption("Time");
        org.setCaption("Organization");
        desc.setCaption("Message");
        //ts.setConverter(new LongToLocalTimeConverter());
        additional.setValue("Additional data...");
    }

    @Override
    protected Component createContent() {
        return new MVerticalLayout(ts, org, desc);
    }

    @Override
    public Window openInModalPopup() {
        Window w = super.openInModalPopup();
        w.setHeight("40%");
        w.setWidth("40%");
        return w;
    }



}
