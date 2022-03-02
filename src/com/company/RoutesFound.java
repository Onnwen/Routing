package com.company;

import java.util.Arrays;

public class RoutesFound {
    private EstimatedRoute[] routes;
    private int total;
    private Route[] lastRoutesToAvoid;
    private int totalRoutesToAvoid;

    public RoutesFound(int totalRoutes) {
        this.total = 0;
        this.lastRoutesToAvoid = new Route[totalRoutes];
        this.routes = new EstimatedRoute[totalRoutes];
        this.totalRoutesToAvoid = 0;
    }

    public int getTotalWantedRoutes() {
        return routes.length;
    }

    public void addRouteToAvoid(Route route) {
        lastRoutesToAvoid[totalRoutesToAvoid] = route;
        totalRoutesToAvoid++;
    }

    public Route[] getRoutesToAvoid() {
        return Arrays.copyOf(lastRoutesToAvoid, totalRoutesToAvoid);
    }

    public EstimatedRoute[] getRoutes() {
        return Arrays.copyOf(routes, total);
    }

    public void addFoundRoute(EstimatedRoute route) {
        routes[total] = route;
        total++;
        addRouteToAvoid(route.getLastRoute());
    }

    public EstimatedRoute getLast() {
        return routes[total-1];
    }

    public EstimatedRoute getMinorCostRoute() {
        EstimatedRoute bestRoute = routes[0];
        for(EstimatedRoute foundRoute:getRoutes()) {
            if (foundRoute.validRoute() && foundRoute.getCost() < bestRoute.getCost()) {
                bestRoute = foundRoute;
            }
        }
        return bestRoute;
    }

    public void print() {
        if (getRoutes().length > 0) {
            System.out.println("\n----------------------------------------------------------");
            if (getRoutes().length-1 == 1) {
                System.out.println("\uDBC0\uDD85\t 1 percorso valido trovato.");
            }
            else {
                System.out.println("\uDBC0\uDD85\t" + (getRoutes().length - 1) + " percorsi validi trovati.");
            }
            System.out.println("----------------------------------------------------------");
            if (getMinorCostRoute() == routes[0]) {
                System.out.println("Percorso migliore:");
                routes[0].print();
            }
            else {
                System.out.println("Percorso più veloce:");
                routes[0].print();
                System.out.println("\n----------------------------------------------------------");
                System.out.println("Percorso meno costoso:");
                getMinorCostRoute().print();
            }
            for(EstimatedRoute foundRoute:getRoutes()) {
                if (foundRoute.validRoute() && foundRoute != routes[0] && foundRoute != getMinorCostRoute()) {
                    System.out.println("\n----------------------------------------------------------");
                    System.out.println("Percorso alternativo:");
                    foundRoute.print();
                }
            }
            System.out.println("\n----------------------------------------------------------");
        }
        else {
            try {
                routes[0].print();
            } catch (Exception e) {}
        }
    }
}
