package com.company;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class EstimatedRouteTest {

    EstimatedRoute testingRoute = new EstimatedRoute(new Device(UUID.fromString("eeee3894-2593-4e62-a404-db5a23a5caf3")), new Device(UUID.fromString("eeee3894-2593-4e62-a404-db5a23a5caf3")), new Network(new Device[]{new Device(UUID.fromString("eeee3894-2593-4e62-a404-db5a23a5caf3"))}));

    // @org.junit.jupiter.api.Test
    void testIncrementHops() {
        int actualHops = testingRoute.getHops();
        testingRoute.incrementHops();
        assertEquals((actualHops + 1), testingRoute.getHops());
    }

    // @org.junit.jupiter.api.Test
    void testDecreaseHops() {
        int actualHops = testingRoute.getHops();
        testingRoute.decreaseHops();
        assertEquals((actualHops - 1), testingRoute.getHops());
    }

    @org.junit.jupiter.api.Test
    void testEstimateRoute() {
        Network net = FileManagment.loadNetwork();
        System.out.println(net.getDevices()[2].getId());
        System.out.println(net.getDevices()[5].getId());
        net.estimateRoute(net.getDevices()[2], net.getDevices()[5]).print();
    }
}