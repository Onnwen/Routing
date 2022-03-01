package com.company;

public class EstimatedRoute {
    private Device startingPoint;
    private Device arrivingDevice;
    private double cost;
    private int hops;

    private int maximumHops;
    private int maximumCost;
    private int authorizedMaximumHops;

    public EstimatedRoute(Device sendingDevice, Device arrivingDevice, Network net) {
        this.startingPoint = sendingDevice.copy();
        this.arrivingDevice = arrivingDevice;

        this.maximumHops = 3;
        this.maximumCost = 0;
        this.authorizedMaximumHops = 100;
        this.cost = 0;
        this.hops = 0;

        PathNetwork network = new PathNetwork(net);
        network.setMainDevice(sendingDevice);

        /*
        do {
            maximumHops++;
            checkLink(this, arrivingDevice.copy(), network);
        } while (canProceed() && !startingPoint.equals(arrivingDevice));

        if (getLastDevice() != arrivingDevice) {
            startingPoint = null;
        }
         */
        System.out.println("Ricerca percorso tra " + startingPoint.getId() + " e " + arrivingDevice.getId());
        if(checkLink(this, arrivingDevice.copy(), network) == null) {
            System.out.println("null");
        }
        else {
            System.out.println("Route found");
        }
    }

    private EstimatedRoute checkLink(EstimatedRoute currentRoute, Device arrivingDevice, PathNetwork network) {
        Device networkLastDevice = network.getNetworkDevice(currentRoute.getLastDevice());

        if (currentRoute.getLastDevice().sameAs(arrivingDevice)) {
            return currentRoute;
        }

        // Controllo se dispositivo finale è vicino.
        for(Route deviceRoute:networkLastDevice.getLinkedRoutes()) {
            if (!network.needToAvoid(deviceRoute)) {
                Device nextDevice = deviceRoute.getNextDevice(networkLastDevice);
                if (nextDevice.sameAs(arrivingDevice)) {
                    addHop(deviceRoute, nextDevice);
                    return currentRoute;
                }
            }
        }

        // Se dispositivo finale non è vicino.
        for(Route deviceRoute:networkLastDevice.getLinkedRoutes()) {
            if (!network.needToAvoid(deviceRoute) && deviceRoute.getNextDevice(networkLastDevice) != null) {
                // Se ci sono ulteriori hops a disposizione
                if (addHop(deviceRoute, deviceRoute.getNextDevice(networkLastDevice))) {
                    network.addRouteToAvoid(deviceRoute);
                    // Se si è trovato il percorso
                    if (currentRoute.getLastDevice().sameAs(arrivingDevice)) {
                        return currentRoute;
                    }
                    // Altrimenti si percorre una nuova rotta passando dal dispositivo aggiunto
                    checkLink(currentRoute, arrivingDevice, network);
                }
                else {
                    break;
                }
            }
        }

        if (getLastDevice().sameAs(startingPoint)) {
            return null;
        }
        else {
            removeLastHop();
            return checkLink(currentRoute, arrivingDevice, network);
        }
    }

    public boolean addHop(Route route, Device device) {
        if (canProceed()) {
            getLastDevice().addRoute(route.copy());
            getLastRoute().setOne(getLastDevice());
            getLastRoute().setTwo(device.copy());
            incrementHops();
            increaseCost(route.getCost());
            return true;
        }
        return false;
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
        System.out.println("-----------------------------------------------------------------------------------------------------------");
        // if (!getLastDevice().sameAs(arrivingDevice)) {
        if (false) {
            System.out.println("\uDBC0\uDD84\tNessun percorso trovato.");
            System.out.print("\uDBC0\uDE64\tSalti massimi tentati: " + maximumHops);
        } else {
            Device currentDevice = startingPoint;
            System.out.println("\uDBC0\uDD85\tÈ stato trovato un percorso!");
            System.out.println("\uDBC0\uDE64\tSalti: " + this.hops + " |  \uDBC0\uDF70 Costo: " + this.cost);
            System.out.print("\n\uDBC1\uDE57\tDISPOSITIVO " + currentDevice.getId().toString());
            try {
                while (currentDevice.getLinkedRoutes()[0].linked()) {
                    System.out.print(" \uDBC3\uDC11 ");
                    System.out.println("\uDBC1\uDC98 ROTTA " + currentDevice.getLinkedRoutes()[0].getId().toString() + " | " + currentDevice.getLinkedRoutes()[0].getCost() + " \uDBC0\uDF70");
                    System.out.println("↓");
                    currentDevice = currentDevice.getLinkedRoutes()[0].getTwo();
                    System.out.print("\uDBC1\uDE57\tDISPOSITIVO " + currentDevice.getId());
                }
            } catch (Exception e) {
            }
        }
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
        return ((hops < maximumHops || maximumHops == 0) && (cost < maximumCost || maximumCost == 0) && maximumHops < authorizedMaximumHops);
    }
}
