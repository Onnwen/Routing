package com.company;

import java.util.UUID;

public class Route {
    private UUID id;
    private int cost;

    private Device one;
    private Device two;

    public Route(UUID id, int cost, Device one, Device two) {
        this.id = id;
        this.cost = cost;
        this.one = one;
        this.two = two;
    }

    public Route(UUID id, int cost) {
        this.id = id;
        this.cost = cost;
        this.one = null;
        this.two = null;
    }

    public boolean sameAs(Route route) {
        return this.id.toString().equals(route.getId().toString());
    }

    public String getStringCost() {
        if (cost == 0) {
            return "0 \uDBC0\uDF6F";
        }
        else {
            return cost + " \uDBC0\uDF70";
        }
    }

    public Route copy() {
        return new Route(this.id, this.cost);
    }

    public UUID getId() {
        return id;
    }

    public int getCost() {
        return cost;
    }

    public Device getOne() {
        return one;
    }

    public Device getTwo() {
        return two;
    }

    public boolean linkedTo(Device device) {
        return (one == device || two == device);
    }

    public Device getNextDevice(Device currentDevice) {
        if (currentDevice.sameAs(one)) {
            return two;
        }
        else {
            return one;
        }
    }

    public boolean linked() {
        return (one != null && two != null);
    }

    public void setOne(Device one) {
        this.one = one;
    }

    public void setTwo(Device two) {
        this.two = two;
    }

    public void removeTwo() {
        this.two = null;
    }

    public void setLink(Device device) {
        if (one == null) {
            one = device;
        }
        else if (two == null) {
            two = device;
        }
    }
}
