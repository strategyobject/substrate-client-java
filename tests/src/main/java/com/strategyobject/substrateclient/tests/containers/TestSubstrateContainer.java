package com.strategyobject.substrateclient.tests.containers;

import org.testcontainers.containers.GenericContainer;

/**
 * We are using our Frequency node for the Substrate container as Substrate is no longer versioned well
 */
public class TestSubstrateContainer extends GenericContainer<TestSubstrateContainer> {
    private static final String IMAGE = "frequencychain/instant-seal-node:";

    public TestSubstrateContainer(String version) {
        super(IMAGE + version);

        addExposedPorts(30333, 9944, 9933);
        //setCommand("");
    }

    public String getWsAddress() {
        System.out.println("####################### RPC: " + this.getMappedPort(9933) + " WS: " + this.getMappedPort(9944));
        return String.format("ws://%s:%s", this.getHost(), this.getMappedPort(9944));
    }
}
