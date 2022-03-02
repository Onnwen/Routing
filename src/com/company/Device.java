package com.company;

import java.util.Arrays;
import java.util.UUID;

public class Device {
    private UUID id;
    private Route[] linkedRoutes;
    private int totalLinkedRoutes;

    public Device(UUID id) {
        this.id = id;
        this.linkedRoutes = new Route[100];
        this.totalLinkedRoutes = 0;
    }

    public Device copy() {
        return new Device(id);
    }

    public boolean sameAs(Device device) {
        return id.toString().equals(device.getId().toString());
    }

    public Route[] getLinkedRoutes() {
        return Arrays.copyOf(linkedRoutes, totalLinkedRoutes);
    }

    public UUID getId() {
        return id;
    }

    public void addRoute(Route route) {
        if (!routeAlreadyLinked(route)) {
            linkedRoutes[totalLinkedRoutes] = route;
            totalLinkedRoutes++;
        }
    }

    public void stampaCiao() {
        System.out.println("ciao");
    }

    public void resetRoutes() {
        this.linkedRoutes = new Route[10];
        this.totalLinkedRoutes = 0;
    }

    public boolean routeAlreadyLinked(Route route) {
        for(Route linkedRoute:getLinkedRoutes()) {
            if (linkedRoute == route) {
                return true;
            }
        }
        return false;
    }
}
