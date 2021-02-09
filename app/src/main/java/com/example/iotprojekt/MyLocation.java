package com.example.iotprojekt;

import android.util.Log;

import org.eclipse.leshan.client.request.ServerIdentity;
import org.eclipse.leshan.client.resource.BaseInstanceEnabler;
import org.eclipse.leshan.core.model.ObjectModel;
import org.eclipse.leshan.core.response.ReadResponse;


import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class MyLocation extends BaseInstanceEnabler {



    private static final List<Integer> supportedResources = Arrays.asList(0, 1, 5);

    private double latitude;
    private double longitude;
    private Date timestamp;

    public MyLocation() {
        this(null, null, 1.0f);
    }

    public MyLocation(Float latitude, Float longitude, float scaleFactor) {
        timestamp = new Date();
    }

    @Override
    public ReadResponse read(ServerIdentity identity, int resourceid) {
        Log.i("Log:","Read on Location Resource " + resourceid);
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