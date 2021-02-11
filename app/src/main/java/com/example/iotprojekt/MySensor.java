package com.example.iotprojekt;

import android.util.Log;

import org.eclipse.leshan.client.request.ServerIdentity;
import org.eclipse.leshan.client.resource.BaseInstanceEnabler;
import org.eclipse.leshan.core.model.ObjectModel;
import org.eclipse.leshan.core.node.LwM2mResource;
import org.eclipse.leshan.core.response.ReadResponse;
import org.eclipse.leshan.core.response.WriteResponse;

import java.util.Arrays;
import java.util.List;

public class MySensor extends BaseInstanceEnabler {
    private static final List<Integer> supportedResources = Arrays.asList(5700, 5750);

    public MySensor(){

    }

    @Override
    public ReadResponse read(ServerIdentity identity, int resourceid) {
        Log.i("Log:", "Read on Device Resource " + resourceid);
        switch (resourceid){
            case 5700:
                return ReadResponse.success(resourceid, getSensorValue());
            case 5750:
                return ReadResponse.success(resourceid, getApplicationType());
            default:
                return super.read(identity, resourceid);
        }

    }

    @Override
    public WriteResponse write(ServerIdentity identity, int resourceid, LwM2mResource value) {
        Log.i("Log:", "Write on Device Resource " + resourceid + " value " + value);
        switch (resourceid) {
            case 5750:
                setApplicationType((String) value.getValue());
                fireResourcesChange(resourceid);
                return WriteResponse.success();
            default:
                return super.write(identity, resourceid, value);
        }
    }

    private String getSensorValue() {
        return "Leshan Demo Sensor";
    }

    private String applicationType = "Empty application type";

    private String getApplicationType() {
        return applicationType;
    }

    private void setApplicationType(String t) {
        applicationType = t;
    }

    @Override
    public List<Integer> getAvailableResourceIds(ObjectModel model) {
        return supportedResources;
    }
}
