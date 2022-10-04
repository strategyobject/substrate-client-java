package com.strategyobject.substrateclient.api;

import com.google.inject.CreationException;
import com.google.inject.util.Modules;
import com.strategyobject.substrateclient.common.convert.HexConverter;
import com.strategyobject.substrateclient.crypto.ss58.SS58AddressFormat;
import com.strategyobject.substrateclient.pallet.PalletFactory;
import com.strategyobject.substrateclient.rpc.api.AccountId;
import com.strategyobject.substrateclient.rpc.api.primitives.BlockNumber;
import com.strategyobject.substrateclient.rpc.api.primitives.Index;
import com.strategyobject.substrateclient.rpc.api.section.System;
import com.strategyobject.substrateclient.tests.containers.SubstrateVersion;
import com.strategyobject.substrateclient.tests.containers.TestSubstrateContainer;
import com.strategyobject.substrateclient.transport.ws.WsProvider;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigInteger;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@Testcontainers
class ApiTest {
    private static final int WAIT_TIMEOUT = 1000;

    @Container
    private final TestSubstrateContainer substrate = new TestSubstrateContainer(SubstrateVersion.V3_0_0).waitingFor(Wait.forLogMessage(".*Running JSON-RPC WS server.*", 1));

    @Test
    void getSystemPalletAndCall() throws Exception { // TODO move the test out of the project
        val wsProvider = WsProvider.builder()
                .setEndpoint(substrate.getWsAddress());

        try (val api = Api.with(wsProvider).build().join()) {
            val systemPallet = api.pallet(SystemPallet.class);
            val blockHash = systemPallet
                    .blockHash()
                    .get(BlockNumber.GENESIS)
                    .get(WAIT_TIMEOUT, TimeUnit.SECONDS);

            assertNotNull(blockHash);
            assertNotEquals(BigInteger.ZERO, new BigInteger(blockHash.getBytes()));
        }
    }

    @Test
    void getSystemSectionAndCall() throws Exception {
        val wsProvider = WsProvider.builder()
                .setEndpoint(substrate.getWsAddress());

        try (val api = Api.with(wsProvider).build().join()) {
            val system = api.rpc(System.class);
            val alicePublicKey = HexConverter.toBytes("0xd43593c715fdd31c61141abd04a99fd6822c8558854ccde39a5684e7a56da27d");
            val actual = system.accountNextIndex(AccountId.fromBytes(alicePublicKey)).join();

            assertEquals(Index.ZERO, actual);
        }
    }

    @Test
    void getSS58AddressFormat() throws Exception {
        val wsProvider = WsProvider.builder()
                .setEndpoint(substrate.getWsAddress());

        try (val api = Api.with(wsProvider).build().join()) {
            val ss58AddressFormat = api.metadata().getSS58AddressFormat();

            assertNotNull(ss58AddressFormat);
            assertEquals(SS58AddressFormat.SUBSTRATE_ACCOUNT, ss58AddressFormat);
        }
    }

    @Test
    void configureApi() throws Exception {
        val wsProvider = WsProvider.builder()
                .setEndpoint(substrate.getWsAddress());

        val expected = mock(Index.class);
        try (val api = Api.with(wsProvider)
                .configure(defaultModule ->
                        defaultModule.configureRpcDecoderRegistry((registry, _factory) ->
                                registry.register((value, decoders) -> expected, Index.class)))
                .build()
                .join()) {
            val system = api.rpc(System.class);
            val alicePublicKey = HexConverter.toBytes("0xd43593c715fdd31c61141abd04a99fd6822c8558854ccde39a5684e7a56da27d");
            val actual = system.accountNextIndex(AccountId.fromBytes(alicePublicKey)).join();

            assertEquals(expected, actual);
        }
    }

    @Test
    void reconfigureApi() throws Exception {
        val wsProvider = WsProvider.builder()
                .setEndpoint(substrate.getWsAddress());

        val mockFactory = mock(PalletFactory.class);
        try (val api = Api.with(wsProvider)
                .reconfigure(defaultModule ->
                        Modules.override(defaultModule).with(binder ->
                                binder.bind(PalletFactory.class).toInstance(mockFactory)))
                .build()
                .join()) {

            api.pallet(SystemPallet.class);
            verify(mockFactory).create(SystemPallet.class);
        }
    }

    @Test
    void validateModule() {
        val api = Api.with(binder -> {
        });

        assertThrows(CreationException.class, api::build);
    }
}
