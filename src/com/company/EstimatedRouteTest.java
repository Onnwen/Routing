package com.company;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class EstimatedRouteTest {
    @org.junit.jupiter.api.Test
    void testEstimateRoute() {
        Network net = FileManagment.loadNetwork();
        net.estimateRoute(net.getDevices()[3], net.getDevices()[7]).print();
    }
}