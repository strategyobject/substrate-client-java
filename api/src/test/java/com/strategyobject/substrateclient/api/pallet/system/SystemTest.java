package com.strategyobject.substrateclient.api.pallet.system;

import com.strategyobject.substrateclient.api.Api;
import com.strategyobject.substrateclient.api.pallet.balances.AccountData;
import com.strategyobject.substrateclient.common.convert.HexConverter;
import com.strategyobject.substrateclient.pallet.storage.Arg;
import com.strategyobject.substrateclient.rpc.api.AccountId;
import com.strategyobject.substrateclient.tests.containers.SubstrateVersion;
import com.strategyobject.substrateclient.tests.containers.TestSubstrateContainer;
import com.strategyobject.substrateclient.transport.ws.WsProvider;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigInteger;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
class SystemTest {
    private static final int WAIT_TIMEOUT = 30;

    @Container
    private final TestSubstrateContainer substrate = new TestSubstrateContainer(SubstrateVersion.V3_0_0).waitingFor(Wait.forLogMessage(".*Running JSON-RPC WS server.*", 1));

    @Test
    void account() throws Exception {
        val wsProvider = WsProvider.builder()
                .setEndpoint(substrate.getWsAddress());

        try (val api = Api.with(wsProvider).build().join()) {
            val system = api.pallet(System.class);

            val alicePublicKey = HexConverter.toBytes("0xd43593c715fdd31c61141abd04a99fd6822c8558854ccde39a5684e7a56da27d");
            val actual = system.account().get(AccountId.fromBytes(alicePublicKey)).join();

            assertNotNull(actual);
            assertEquals(
                    new BigInteger("1000000000000000000000"),
                    actual.getData().into(AccountData.class).getFree());
        }
    }

    @Test
    void events() throws Exception {
        val wsProvider = WsProvider.builder()
                .setEndpoint(substrate.getWsAddress());

        try (val api = Api.with(wsProvider).build().join()) {
            val system = api.pallet(System.class);

            AtomicReference<List<EventRecord>> eventRecords = new AtomicReference<>();
            val unsubscribe = system.events()
                    .subscribe((exception, block, value, keys) -> eventRecords.set(value), Arg.EMPTY)
                    .join();

            await()
                    .atMost(WAIT_TIMEOUT, TimeUnit.SECONDS)
                    .untilAtomic(eventRecords, notNullValue());

            assertTrue(unsubscribe.get().join());
            assertEquals(1, eventRecords.get().size());
            assertEquals(0, eventRecords.get().get(0).getPhase().getIndex());

            val eventHolder = eventRecords.get().get(0).getEvent();
            assertEquals(0, eventHolder.getEventIndex());
            assertEquals("System", eventHolder.getPallet().getName());
            assertTrue(eventHolder.getEvent() instanceof System.ExtrinsicSuccess);
        }
    }
}