package com.strategyobject.substrateclient.tests.containers;

import org.testcontainers.containers.GenericContainer;

public class TestSubstrateContainer extends GenericContainer<TestSubstrateContainer> {
    private static final String IMAGE = "frequencychain/instant-seal-node:";

    public TestSubstrateContainer(String version) {
        super(IMAGE + version);

        addExposedPorts(30333, 9944, 9933);
        //setCommand("");
    }

    public String getWsAddress() {
        System.out.println("####################### " + this.getMappedPort(9933));
        return String.format("ws://%s:%s", this.getHost(), this.getMappedPort(9944));
    }
}
