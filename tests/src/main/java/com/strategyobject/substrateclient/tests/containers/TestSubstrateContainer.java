package com.strategyobject.substrateclient.tests.containers;

import org.testcontainers.containers.GenericContainer;

public class TestSubstrateContainer extends GenericContainer<TestSubstrateContainer> {
    private static final String IMAGE = "parity/substrate:";

    public TestSubstrateContainer(String version) {
        super(IMAGE + version);

        addExposedPorts(30333, 9944, 9933);
        setCommand("--tmp --dev --ws-external --rpc-methods=Unsafe --rpc-cors all --rpc-external");
    }

    public String getWsAddress() {
        return String.format("ws://%s:%s", this.getHost(), this.getMappedPort(9944));
    }
}
