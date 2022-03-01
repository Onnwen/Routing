package com.company;

import java.util.Arrays;

public class PathNetwork {
    Network network;
    Route[] routesToAvoid;
    int totalRoutesToAvoid;

    public PathNetwork(Network network) {
        this.network = network;
        this.routesToAvoid = new Route[100];
        this.totalRoutesToAvoid = 0;
    }

    public Route[] getRoutesToAvoid() {
        return Arrays.copyOf(routesToAvoid, totalRoutesToAvoid);
    }

    public void setMainDevice(Device mainDevice) {
        this.network.setMainDevice(mainDevice);
    }

    public boolean needToAvoid(Route route) {
        for(Route currentRoute: getRoutesToAvoid()) {
            if (currentRoute.sameAs(route)) {
                return true;
            }
        }
        return false;
    }

    public void resetRoutesToAvoid() {
        this.routesToAvoid = new Route[100];
        this.totalRoutesToAvoid = 0;
    }

    public boolean addRouteToAvoid(Route route) {
        if (needToAvoid(route)) {
            return false;
        }
        else {
            routesToAvoid[totalRoutesToAvoid] = route;
            totalRoutesToAvoid++;
            return true;
        }
    }

    public Device getNetworkDevice(Device device) {
        for(Device currentDevice: network.getDevices()) {
            if (currentDevice.sameAs(device)) {
                return currentDevice;
            }
        }
        return null;
    }
}
