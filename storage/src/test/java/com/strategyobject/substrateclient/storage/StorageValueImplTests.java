package com.strategyobject.substrateclient.storage;

import com.strategyobject.substrateclient.crypto.ss58.SS58Codec;
import com.strategyobject.substrateclient.rpc.RpcImpl;
import com.strategyobject.substrateclient.rpc.types.AccountId;
import com.strategyobject.substrateclient.scale.registries.ScaleReaderRegistry;
import com.strategyobject.substrateclient.tests.containers.SubstrateVersion;
import com.strategyobject.substrateclient.tests.containers.TestSubstrateContainer;
import com.strategyobject.substrateclient.transport.ws.WsProvider;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Testcontainers
public class StorageValueImplTests {
    @Container
    static final TestSubstrateContainer substrate = new TestSubstrateContainer(SubstrateVersion.V3_0_0);
    private static final int CONNECTION_TIMEOUT = 1000;

    @Test
    public void sudoKey() throws Exception {
        val expected = AccountId.fromBytes(
                SS58Codec.decode(
                                "5GrwvaEF5zXb26Fz9rcQpDWS57CtERHpNehXCPcNoHGKutQY")
                        .getAddress());

        val wsProvider = WsProvider.builder()
                .setEndpoint(substrate.getWsAddress())
                .disableAutoConnect()
                .build();
        wsProvider.connect().get(CONNECTION_TIMEOUT, TimeUnit.MILLISECONDS);
        try (val rpc = RpcImpl.with(wsProvider)) {
            val storage = StorageValueImpl.with(
                    rpc,
                    ScaleReaderRegistry.getInstance().resolve(AccountId.class),
                    StorageKeyProvider.of("Sudo", "Key"));

            val actual = storage.get().get();

            assertEquals(expected, actual);
        }
    }

    @Test
    public void sudoKeyAtGenesis() throws Exception {
        val expected = AccountId.fromBytes(
                SS58Codec.decode(
                                "5GrwvaEF5zXb26Fz9rcQpDWS57CtERHpNehXCPcNoHGKutQY")
                        .getAddress());

        val wsProvider = WsProvider.builder()
                .setEndpoint(substrate.getWsAddress())
                .disableAutoConnect()
                .build();
        wsProvider.connect().get(CONNECTION_TIMEOUT, TimeUnit.MILLISECONDS);
        try (val rpc = RpcImpl.with(wsProvider)) {
            val blockHash = rpc.chain().getBlockHash(0).get();
            val storage = StorageValueImpl.with(
                    rpc,
                    ScaleReaderRegistry.getInstance().resolve(AccountId.class),
                    StorageKeyProvider.of("Sudo", "Key"));

            val actual = storage.at(blockHash).get();

            assertEquals(expected, actual);
        }
    }
}
