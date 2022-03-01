package com.company;

import java.io.File;

public class EstimatedRoute {
    private Device startingPoint;
    private double cost;
    private int hops;

    private int maximumHops;
    private int maximumCost;
    private int authorizedMaximumHops;

    public EstimatedRoute(Device sendingDevice, Device arrivingDevice, Network net) {
        this.startingPoint = sendingDevice.copy();

        this.maximumHops = 10;
        this.maximumCost = 0;
        this.authorizedMaximumHops = 100;
        this.cost = 0;
        this.hops = 0;

        PathNetwork network = new PathNetwork(net);
        network.setMainDevice(sendingDevice);

        do {
            maximumHops++;
            checkLink(this, arrivingDevice.copy(), network);
        } while (getLastDevice() == startingPoint && canProceed());
    }

    private EstimatedRoute checkLink(EstimatedRoute currentRoute, Device arrivingDevice, PathNetwork network) {
        Device networkLastDevice = network.getNetworkDevice(currentRoute.getLastDevice());

        if (currentRoute.getLastDevice().sameAs(arrivingDevice)) {
            return currentRoute;
        }

        if (!canProceed()) {
            return null;
        }

        network.addDeviceToAvoid(currentRoute.getLastDevice());

        for(Route deviceRoute: networkLastDevice.getLinkedRoutes()) {
            Device nextDevice = deviceRoute.getNextDevice(networkLastDevice);

            if (currentRoute.getLastDevice().sameAs(arrivingDevice)) {
                return currentRoute;
            } else if (nextDevice != null && !network.needToAvoid(nextDevice)) {
                addHop(deviceRoute, deviceRoute.getNextDevice(networkLastDevice));
                if (currentRoute.getLastDevice().sameAs(arrivingDevice)) {
                    return currentRoute;
                }
                if (checkLink(currentRoute, arrivingDevice, network) == null) {
                    reset();
                    return null;
                }
            }
        }

        if (getLastDevice().sameAs(startingPoint)) {
            removeLastHop();
            if (checkLink(currentRoute, arrivingDevice, network) == null) {
                reset();
                return null;
            }
        }

        reset();
        return null;
    }

    public void addHop(Route route, Device device) {
        getLastDevice().addRoute(route.copy());
        getLastRoute().setOne(getLastDevice());
        getLastRoute().setTwo(device.copy());
        incrementHops();
        increaseCost(route.getCost());
    }

    public void reset() {
        resetStartingPoint();
        this.cost = 0;
        this.hops = 0;
    }

    public void resetStartingPoint() {
        while (getLastDevice() != startingPoint) {
            removeLastRoute();
        }
    }

    public void removeLastHop() {
        decreaseCost(getLastRoute().getCost());
        decreaseHops();
        removeLastRoute();
    }

    public void removeLastDevice() {
        if (getLastRoute() != null) {
            getLastRoute().removeTwo();
        }
    }

    public void removeLastRoute() {
        if (getLastRoute() != null) {
            getLastRoute().getOne().resetRoutes();
        }
    }

    public Device getLastDevice() {
        Device currentDevice = startingPoint;
        while (currentDevice.getLinkedRoutes().length > 0 && currentDevice.getLinkedRoutes()[0] != null && currentDevice.getLinkedRoutes()[0].linked()) {
            currentDevice = currentDevice.getLinkedRoutes()[0].getNextDevice(currentDevice);
        }
        return currentDevice;
    }

    public void print() {
        if (startingPoint == null) {
            System.out.println("Nessuna rotta è stata trovata rispettando i requisiti prestabili.");
            System.out.println("Salti massimi: " + maximumHops);
            System.out.println("Costo massimo: " + maximumCost);
            return;
        }

        Device currentDevice = startingPoint;
        System.out.println("-----------------------------------------------------------------------------------------------------------");
        System.out.println("\uDBC0\uDD85 Path find!");
        System.out.println("HOPS: " + this.hops + " \uDBC0\uDE64 | COST: " + this.cost + " \uDBC0\uDF70");
        System.out.print("\n\uDBC1\uDE57 DEVICE " + currentDevice.getId().toString());
        try {
            while (currentDevice.getLinkedRoutes()[0].linked()) {
                System.out.print(" \uDBC3\uDC11 ");
                System.out.println("\uDBC1\uDC98 ROUTE  " + currentDevice.getLinkedRoutes()[0].getId().toString() + " | " + currentDevice.getLinkedRoutes()[0].getCost() + " \uDBC0\uDF70");
                System.out.println("↓");
                currentDevice = currentDevice.getLinkedRoutes()[0].getTwo();
                System.out.print("\uDBC1\uDE57 DEVICE " + currentDevice.getId());
            }
        } catch (Exception e) {}
        System.out.println("\n-----------------------------------------------------------------------------------------------------------");
    }

    public Route getLastRoute() {
        if (startingPoint.getLinkedRoutes().length != 0) {
            Route currentCheckingRoute = startingPoint.getLinkedRoutes()[0];
            while (currentCheckingRoute != null && currentCheckingRoute.linked()) {
                if (currentCheckingRoute.getNextDevice(currentCheckingRoute.getOne()).getLinkedRoutes().length == 0) {
                    break;
                }
                currentCheckingRoute = currentCheckingRoute.getNextDevice(currentCheckingRoute.getOne()).getLinkedRoutes()[0];
            }
            return currentCheckingRoute;
        }
        return null;
    }

    public Device getStartingPoint() {
        return startingPoint;
    }

    public double getCost() {
        return cost;
    }

    public void increaseCost(double cost) {
        this.cost += cost;
    }

    public void decreaseCost(double cost) {
        this.cost -= cost;
    }

    public int getHops() {
        return hops;
    }

    public void decreaseHops() { this.hops--; }

    public void incrementHops() {
        this.hops++;
    }

    public void paste(EstimatedRoute newRoute) {
        this.startingPoint = newRoute.startingPoint;
        this.cost = newRoute.cost;
        this.hops = newRoute.hops;
    }

    private boolean canProceed() {
        return ((hops <= maximumHops || maximumHops == 0) && (cost <= maximumCost || maximumCost == 0) && maximumHops <= authorizedMaximumHops);
    }
}
