/*******************************************************************************
 * Copyright (c) 2013-2015 Sierra Wireless and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Eclipse Distribution License v1.0 which accompany this distribution.
 * 
 * The Eclipse Public License is available at
 *    http://www.eclipse.org/legal/epl-v10.html
 * and the Eclipse Distribution License is available at
 *    http://www.eclipse.org/org/documents/edl-v10.html.
 * 
 * Contributors:
 *     Zebra Technologies - initial API and implementation
 *     Sierra Wireless, - initial API and implementation
 *     Bosch Software Innovations GmbH, - initial API and implementation
 *******************************************************************************/

package com.example.iotprojekt;

import android.os.Build;

import org.eclipse.leshan.LwM2m;
import org.eclipse.leshan.client.californium.LeshanClient;
import org.eclipse.leshan.client.californium.LeshanClientBuilder;
import org.eclipse.leshan.client.object.Server;
import org.eclipse.leshan.client.resource.LwM2mObjectEnabler;
import org.eclipse.leshan.client.resource.ObjectsInitializer;
import org.eclipse.leshan.core.request.BindingMode;


import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.util.List;

import static org.eclipse.leshan.LwM2mId.DEVICE;
import static org.eclipse.leshan.LwM2mId.LOCATION;
import static org.eclipse.leshan.LwM2mId.SECURITY;
import static org.eclipse.leshan.LwM2mId.SERVER;
import static org.eclipse.leshan.client.object.Security.noSec;

public class LeshanClientDemo {

    private static MyLocation locationInstance;



    public static void init(final String[] args) {
        // Get endpoint name
        String endpoint;
        endpoint = Build.DEVICE + "-" + Build.ID;

        // Get server URI
        String serverURI;
        serverURI = "coap://192.168.2.154:" + LwM2m.DEFAULT_COAP_PORT;
        System.err.println(serverURI);
        // get PSK info
        byte[] pskIdentity = null;
        byte[] pskKey = null;

        // get RPK info
        PublicKey clientPublicKey = null;
        PrivateKey clientPrivateKey = null;
        PublicKey serverPublicKey = null;

        // get X509 info
        X509Certificate clientCertificate = null;
        X509Certificate serverCertificate = null;

        // get local address
        String localAddress = null;
        int localPort = 0;


        Float latitude = null;
        Float longitude = null;
        Float scaleFactor = 1.0f;

        try {
            createAndStartClient(endpoint, localAddress, localPort, false, serverURI, pskIdentity, pskKey,
                    clientPrivateKey, clientPublicKey, serverPublicKey, clientCertificate, serverCertificate, latitude,
                    longitude, scaleFactor);
        } catch (Exception e) {
            System.err.println("Unable to create and start client ...");
            e.printStackTrace();
            return;
        }
    }
    public static void moveLocation(double lat, double lon ) {
        locationInstance.moveLatitude(lat);
        locationInstance.moveLongitude(lon);
    }

    public static void createAndStartClient(String endpoint, String localAddress, int localPort, boolean needBootstrap,
                                            String serverURI, byte[] pskIdentity, byte[] pskKey, PrivateKey clientPrivateKey, PublicKey clientPublicKey,
                                            PublicKey serverPublicKey, X509Certificate clientCertificate, X509Certificate serverCertificate,
                                            Float latitude, Float longitude, float scaleFactor) throws CertificateEncodingException {

        locationInstance = new MyLocation(latitude, longitude, scaleFactor);

        // Initialize model
        /*
        List<ObjectModel> models = ObjectLoader.loadDefault();
        models.addAll(ObjectLoader.loadDdfResources("/models", modelPaths));
        */
        // Initialize object list
        //ObjectsInitializer initializer = new ObjectsInitializer(new LwM2mModel(models));
        ObjectsInitializer initializer = new ObjectsInitializer();
        initializer.setInstancesForObject(SECURITY, noSec(serverURI, 123));
        initializer.setInstancesForObject(SERVER, new Server(123, 30, BindingMode.U, false));

        initializer.setInstancesForObject(DEVICE, new MyDevice());
        initializer.setInstancesForObject(LOCATION, locationInstance);
        //initializer.setInstancesForObject(OBJECT_ID_TEMPERATURE_SENSOR, new RandomTemperatureSensor());
        List<LwM2mObjectEnabler> enablers = initializer.createAll();

        // Create CoAP Config
        /*
        NetworkConfig coapConfig;
        File configFile = new File(NetworkConfig.DEFAULT_FILE_NAME);
        if (configFile.isFile()) {
            coapConfig = new NetworkConfig();
            coapConfig.load(configFile);
        } else {
            coapConfig = LeshanClientBuilder.createDefaultNetworkConfig();
            coapConfig.store(configFile);
        }
        */
        // Create client
        LeshanClientBuilder builder = new LeshanClientBuilder(endpoint);
        builder.setLocalAddress(localAddress, localPort);
        builder.setObjects(enablers);
        //builder.setCoapConfig(coapConfig);
        final LeshanClient client = builder.build();

        // Start the client
        client.start();


    }
}
