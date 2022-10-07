package com.strategyobject.substrateclient.api.pallet.msa;

import com.strategyobject.substrateclient.api.Api;
import com.strategyobject.substrateclient.api.CreateMsa;
import com.strategyobject.substrateclient.api.TestsHelper;
import com.strategyobject.substrateclient.api.pallet.system.EventRecord;
import com.strategyobject.substrateclient.api.pallet.system.System;
import com.strategyobject.substrateclient.common.convert.HexConverter;
import com.strategyobject.substrateclient.common.types.Size;
import com.strategyobject.substrateclient.crypto.Hasher;
import com.strategyobject.substrateclient.crypto.KeyPair;
import com.strategyobject.substrateclient.crypto.KeyRing;
import com.strategyobject.substrateclient.pallet.storage.Arg;
import com.strategyobject.substrateclient.rpc.api.*;
import com.strategyobject.substrateclient.rpc.api.primitives.BlockHash;
import com.strategyobject.substrateclient.rpc.api.primitives.BlockNumber;
import com.strategyobject.substrateclient.rpc.api.primitives.Index;
import com.strategyobject.substrateclient.rpc.api.section.Author;
import com.strategyobject.substrateclient.rpc.api.section.Chain;
import com.strategyobject.substrateclient.scale.ScaleUtils;
import com.strategyobject.substrateclient.tests.containers.SubstrateVersion;
import com.strategyobject.substrateclient.tests.containers.TestSubstrateContainer;
import com.strategyobject.substrateclient.transport.ws.WsProvider;
import lombok.SneakyThrows;
import lombok.val;
import org.assertj.core.api.Assertions;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.iterableWithSize;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Testcontainers
public class CreateMsaTest {
    private static final int WAIT_TIMEOUT = 30;

    @Container
    private final TestSubstrateContainer substrate = new TestSubstrateContainer(SubstrateVersion.V3_0_0).waitingFor(Wait.forLogMessage(".*Running JSON-RPC WS server.*", 1));

    @Test
    @SneakyThrows
    public void createMsa() {
        val wsProvider = WsProvider.builder().setEndpoint(substrate.getWsAddress());

        try (val api = Api.with(wsProvider).build().join()) {
            AtomicReference<List<EventRecord>> eventRecords = new AtomicReference<>(new ArrayList<>());
            val unsubscribe = api.pallet(System.class).events()
                    .subscribe((exception, block, value, keys) -> {
                        if (exception != null) {
                            throw new RuntimeException(exception);
                        }

                        eventRecords.set(value);
                    }, Arg.EMPTY)
                    .join();

            doCreateMsa(api);
            await()
                    .atMost(WAIT_TIMEOUT, TimeUnit.SECONDS)
                    .untilAtomic(eventRecords, iterableWithSize(greaterThan(1)));
            assertTrue(unsubscribe.get().join());
            Supplier<Stream<Object>> events = () -> eventRecords.get().stream().map(x -> x.getEvent().getEvent());

            assertTrue(events.get().anyMatch(x -> x instanceof Msa.MsaCreated));
            List<Object> _events = eventRecords.get().stream().map(er -> er.getEvent().getEvent()).collect(Collectors.toList());
            Msa.MsaCreated msaCreated = null;
            for(Object o : _events){
                if(o instanceof Msa.MsaCreated){
                    msaCreated = (Msa.MsaCreated) o;
                    break;
                }
            }
            assertTrue(events.get().anyMatch(x -> x instanceof System.ExtrinsicSuccess));

            Msa msaPallet = api.pallet(Msa.class);
            AccountId aliceAccountId = AccountId.fromBytes(aliceKeyPair().asPublicKey().getBytes());
            MessageSourceId msaId = msaPallet.messageSourceIdOf().get(aliceAccountId).join();
            Assertions.assertThat(msaId).isNotNull();
            Assertions.assertThat(msaId.getValue()).isEqualTo(msaCreated.getMsaId().getValue());
        }


    }

    private void doCreateMsa(Api api) {
        val genesis = api.rpc(Chain.class).getBlockHash(BlockNumber.GENESIS).join();
        AtomicReference<ExtrinsicStatus.Status> statusAtomicReference = new AtomicReference<>();
        assertDoesNotThrow(() ->
                api.rpc(Author.class).submitAndWatchExtrinsic(createCreateMsaExtrinsic(genesis), (x, status) -> {
                    if(status.getStatus().equals(ExtrinsicStatus.Status.IN_BLOCK)){
                        statusAtomicReference.set(status.getStatus());
                    }
                }).join());
        await()
                .atMost(WAIT_TIMEOUT, TimeUnit.SECONDS)
                .untilAtomic(statusAtomicReference, Matchers.equalTo(ExtrinsicStatus.Status.IN_BLOCK));
    }

    private Extrinsic<?, ?, ?, ?> createCreateMsaExtrinsic(BlockHash genesis) {
        val specVersion = 1;
        val txVersion = 1;
        val moduleIndex = (byte) 60;
        val callIndex = (byte) 0;
        val tip = 0;
        val call = new CreateMsa(moduleIndex, callIndex);

        val extra = new SignedExtra<>(specVersion, txVersion, genesis, genesis, new ImmortalEra(), Index.of(0), BigInteger.valueOf(tip));
        val signedPayload = ScaleUtils.toBytes(
                new SignedPayload<>(call, extra),
                TestsHelper.SCALE_WRITER_REGISTRY,
                SignedPayload.class);
        val keyRing = KeyRing.fromKeyPair(aliceKeyPair());

        val signature = sign(keyRing, signedPayload);

        return Extrinsic.createSigned(
                new SignaturePayload<>(
                        AddressId.fromBytes(aliceKeyPair().asPublicKey().getBytes()),
                        signature,
                        extra
                ), call);
    }

    private Signature sign(KeyRing keyRing, byte[] payload) {
        val signature = payload.length > 256 ? Hasher.blake2(Size.of256, payload) : payload;

        return Sr25519Signature.from(keyRing.sign(() -> signature));
    }

    private KeyPair aliceKeyPair() {
        val str = "0x98319d4ff8a9508c4bb0cf0b5a78d760a0b2082c02775e6e82370816fedfff48925a225d97aa00682d6a59b95b18780c10d" +
                "7032336e88f3442b42361f4a66011d43593c715fdd31c61141abd04a99fd6822c8558854ccde39a5684e7a56da27d";

        return KeyPair.fromBytes(HexConverter.toBytes(str));
    }
}
