package com.hazelcast;

import com.hazelcast.component.AlertTable;
import com.hazelcast.component.AlertViewer;
import com.hazelcast.component.HazelAddItemListener;
import com.hazelcast.core.*;
import com.hazelcast.satimulus.domain.Alert;
import com.vaadin.annotations.Push;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.data.util.BeanItem;
import com.vaadin.server.ClassResource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.ui.ui.Transport;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.*;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.vaadin.addon.leaflet.AbstractLeafletLayer;
import org.vaadin.addon.leaflet.LMap;
import org.vaadin.addon.leaflet.LOpenStreetMapLayer;
import org.vaadin.addon.leaflet.LTileLayer;
import org.vaadin.addon.leaflet.util.JTSUtil;
import org.vaadin.spring.events.Event;
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.events.EventBusListener;
import org.vaadin.spring.events.EventScope;

import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by gokhanoner on 04/05/2017.
 */
@SpringUI
@Push(transport = Transport.WEBSOCKET)
@Theme("valo")
@Title("HazelcastJet Stimilus")
@RequiredArgsConstructor
@Slf4j
public class StimilusUI extends UI implements EventBusListener<Alert>{

    private final HazelcastInstance hi;
    private final EventBus.UIEventBus eventBus;
    private final JetProperties jp;

    private LMap map = new LMap();
    private LTileLayer osmTiles = new LOpenStreetMapLayer();
    private LTileLayer mapBoxTiles;
    private final GeometryFactory factory = new GeometryFactory();
    private final Random rn = new Random();
    private VerticalLayout alertView;
    private AlertViewer viewer = new AlertViewer();
    private AlertTable alertTable = new AlertTable();

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        eventBus.subscribe(this);
        viewer.setSavedHandler(event -> viewer.closePopup());

        VerticalLayout mainLayout = new VerticalLayout();

        ClassResource resource = new ClassResource("/static/img/stimulus.png");

        // Show the image in the application
        Image image = new Image(null, resource);

        VerticalLayout im = new VerticalLayout();
        im.setWidth(70, Unit.PERCENTAGE);
        im.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        im.addComponent(image);
        mainLayout.addComponent(im);
        mainLayout.setSizeFull();
        osmTiles.setAttributionString("© HazelcastJet Stimilus");

        mapBoxTiles = new LTileLayer(
                "http://{s}.tiles.mapbox.com/v4/vaadin.i1pikm9o/{z}/{x}/{y}.png?access_token=pk.eyJ1IjoiZ29raGFub25lciIsImEiOiJjajJhdXBkcXYwMDFlMzNwNGZmNnhud3RqIn0.7gW9iantUzC2hB2DJpIWmw");
        mapBoxTiles.setAttributionString("© HazelcastJet Stimilus");
        clearMap();
        map.setWidth(70, Unit.PERCENTAGE);

        alertView = new VerticalLayout();
        alertView.setWidth(10, Unit.PERCENTAGE);

        alertTable.setWidth(70, Unit.PERCENTAGE);
        alertTable.addItemClickListener(event -> viewInPopup(((BeanItem<AlertTable.AlertDao>) event.getItem()).getBean()));

        mainLayout.setSpacing(true);
        mainLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        mainLayout.addComponent(map);
        mainLayout.addComponent(alertTable);
        mainLayout.setExpandRatio(map, 3);
        mainLayout.setExpandRatio(alertTable, 1);

        HazelAddItemListener<Alert> windowListener = (item) -> eventBus.publish(EventScope.UI, "window", item.getItem());
        HazelAddItemListener<Alert> alertListener = (item) -> eventBus.publish(EventScope.UI, "alert", item.getItem());

        IList<Alert> fuList = hi.getList(jp.getQueues().get(JetProperties.QueueType.WINDOWING));
        fuList.addItemListener(windowListener, true);

        IList<Alert> l1List = hi.getList(jp.getQueues().get(JetProperties.QueueType.ALERTS));
        l1List.addItemListener(alertListener, true);

        setSizeFull();
        setContent(mainLayout);
        setPollInterval(1000);
    }
    private void listenAlertEvent(Alert data) {
        getUI().access(() -> {
            try {
                alertTable.addItemAt(0, data);
            } catch ( Exception e ) {}
        });
    }

    private void listenWindowEvent(Alert data) {
        IMap<String, com.hazelcast.model.Client> clients = hi.getMap(jp.getQueues().get(JetProperties.QueueType.CLIENTS));
        final com.hazelcast.model.Client cli = clients.get(data.getPhoneHome().getOrg());
        log.info("{}", cli);
        getUI().access(() -> {
            try{
                Notification not = new Notification(null, "Event received for " + cli.getName());
                not.setStyleName("warning closable");
                not.setDelayMsec(1000);
                not.show(getUI().getPage());
                Point point = factory.createPoint(new Coordinate(cli.getLon(), cli.getLat()));
                AbstractLeafletLayer layer = (AbstractLeafletLayer) JTSUtil.toLayer(point);
                clearMap();
                map.addLayer(layer);
                map.setCenter(point);
            }catch (Exception e) {}
        });
    }

    private void viewInPopup(AlertTable.AlertDao alertdao) {
        viewer.setEntity(alertdao);
        viewer.openInModalPopup();
    }

    private void clearMap() {
        map.removeAllComponents();
        //map.addBaseLayer(osmTiles, "OSM");
        map.addBaseLayer(mapBoxTiles, "OSM");
        map.setZoomLevel(1.6);
    }

    @Override
    public void onEvent(org.vaadin.spring.events.Event<Alert> event) {
        if(event.getSource().equals("alert")) {
            listenAlertEvent(event.getPayload());
        } else {
            listenWindowEvent(event.getPayload());
        }
    }
}
