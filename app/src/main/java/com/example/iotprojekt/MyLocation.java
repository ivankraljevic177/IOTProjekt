package com.example.iotprojekt;

import org.eclipse.leshan.client.request.ServerIdentity;
import org.eclipse.leshan.client.resource.BaseInstanceEnabler;
import org.eclipse.leshan.core.model.ObjectModel;
import org.eclipse.leshan.core.response.ReadResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class MyLocation extends BaseInstanceEnabler {

    private static final Logger LOG = LoggerFactory.getLogger(MyLocation.class);

    private static final List<Integer> supportedResources = Arrays.asList(0, 1, 5);
    private static final Random RANDOM = new Random();

    private double latitude;
    private double longitude;
    private float scaleFactor;
    private Date timestamp;

    public MyLocation() {
        this(null, null, 1.0f);
    }

    public MyLocation(Float latitude, Float longitude, float scaleFactor) {
        timestamp = new Date();
    }

    @Override
    public ReadResponse read(ServerIdentity identity, int resourceid) {
        LOG.info("Read on Location Resource " + resourceid);
        switch (resourceid) {
        case 0:
            return ReadResponse.success(resourceid, getLatitude());
        case 1:
            return ReadResponse.success(resourceid, getLongitude());
        case 5:
            return ReadResponse.success(resourceid, getTimestamp());
        default:
            return super.read(identity, resourceid);
        }
    }

    public void moveLocation(String nextMove) {
        switch (nextMove.charAt(0)) {
        case 'w':
            moveLatitude(1.0f);
            break;
        case 'a':
            moveLongitude(-1.0f);
            break;
        case 's':
            moveLatitude(-1.0f);
            break;
        case 'd':
            moveLongitude(1.0f);
            break;
        }
    }

    public void moveLatitude(double delta) {
        latitude = delta;
        timestamp = new Date();
        fireResourcesChange(0, 5);
    }

    public void moveLongitude(double delta) {
        longitude = delta;
        timestamp = new Date();
        fireResourcesChange(1, 5);
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    @Override
    public List<Integer> getAvailableResourceIds(ObjectModel model) {
        return supportedResources;
    }
}