package com.strategyobject.substrateclient.rpc.sections;

import com.strategyobject.substrateclient.common.utils.HexConverter;
import com.strategyobject.substrateclient.crypto.KeyRing;
import com.strategyobject.substrateclient.rpc.core.RpcGeneratedSectionFactory;
import com.strategyobject.substrateclient.rpc.core.RpcInterfaceInitializationException;
import com.strategyobject.substrateclient.rpc.sections.substitutes.BalanceTransfer;
import com.strategyobject.substrateclient.rpc.types.*;
import com.strategyobject.substrateclient.tests.containers.SubstrateVersion;
import com.strategyobject.substrateclient.tests.containers.TestSubstrateContainer;
import com.strategyobject.substrateclient.transport.ws.WsProvider;
import com.strategyobject.substrateclient.types.KeyPair;
import com.strategyobject.substrateclient.types.PublicKey;
import com.strategyobject.substrateclient.types.Signable;
import lombok.val;
import lombok.var;
import org.bouncycastle.crypto.digests.Blake2bDigest;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.Network;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigInteger;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
public class AuthorTests {
    private static final int WAIT_TIMEOUT = 10;
    private static final Network network = Network.newNetwork();
    private static final AtomicInteger NONCE = new AtomicInteger(0);

    @Container
    static final TestSubstrateContainer substrate = new TestSubstrateContainer(SubstrateVersion.V3_0_0)
            .withNetwork(network);

    private static byte[] blake2(byte[] value) {
        val digest = new Blake2bDigest(256);
        digest.update(value, 0, value.length);

        val result = new byte[32];
        digest.doFinal(result, 0);
        return result;
    }

    @Test
    void hasKey() throws ExecutionException, InterruptedException, TimeoutException, RpcInterfaceInitializationException {
        try (WsProvider wsProvider = WsProvider.builder()
                .setEndpoint(substrate.getWsAddress())
                .disableAutoConnect()
                .build()) {
            wsProvider.connect().get(WAIT_TIMEOUT, TimeUnit.SECONDS);

            Author rpcSection = RpcGeneratedSectionFactory.create(Author.class, wsProvider);

            val publicKey = PublicKey.fromBytes(
                    HexConverter.toBytes("0xd43593c715fdd31c61141abd04a99fd6822c8558854ccde39a5684e7a56da27d"));
            val keyType = "aura";
            var result = rpcSection.hasKey(publicKey, keyType).get(WAIT_TIMEOUT, TimeUnit.SECONDS);

            assertFalse(result);

            rpcSection.insertKey(keyType, "alice", publicKey).get(WAIT_TIMEOUT, TimeUnit.SECONDS);
            result = rpcSection.hasKey(publicKey, keyType).get(WAIT_TIMEOUT, TimeUnit.SECONDS);

            assertTrue(result);
        }
    }

    @Test
    void insertKey() throws ExecutionException, InterruptedException, TimeoutException, RpcInterfaceInitializationException {
        try (WsProvider wsProvider = WsProvider.builder()
                .setEndpoint(substrate.getWsAddress())
                .disableAutoConnect()
                .build()) {
            wsProvider.connect().get(WAIT_TIMEOUT, TimeUnit.SECONDS);

            Author rpcSection = RpcGeneratedSectionFactory.create(Author.class, wsProvider);

            assertDoesNotThrow(() -> rpcSection.insertKey("aura",
                            "alice",
                            PublicKey.fromBytes(
                                    HexConverter.toBytes("0xd43593c715fdd31c61141abd04a99fd6822c8558854ccde39a5684e7a56da27d")))
                    .get(WAIT_TIMEOUT, TimeUnit.SECONDS));
        }
    }

    @Test
    void submitExtrinsic() throws ExecutionException, InterruptedException, TimeoutException, RpcInterfaceInitializationException {
        try (WsProvider wsProvider = WsProvider.builder()
                .setEndpoint(substrate.getWsAddress())
                .disableAutoConnect()
                .build()) {
            wsProvider.connect().get(WAIT_TIMEOUT, TimeUnit.SECONDS);

            Chain chainSection = RpcGeneratedSectionFactory.create(Chain.class, wsProvider);

            val genesis = chainSection.getBlockHash(0).get(WAIT_TIMEOUT, TimeUnit.SECONDS);
            Author authorSection = RpcGeneratedSectionFactory.create(Author.class, wsProvider);

            assertDoesNotThrow(() -> authorSection.submitExtrinsic(createBalanceTransferExtrinsic(genesis, NONCE.getAndIncrement()))
                    .get(WAIT_TIMEOUT, TimeUnit.SECONDS));
        }
    }

    @Test
    void submitAndWatchExtrinsic() throws ExecutionException, InterruptedException, TimeoutException, RpcInterfaceInitializationException {
        try (WsProvider wsProvider = WsProvider.builder()
                .setEndpoint(substrate.getWsAddress())
                .disableAutoConnect()
                .build()) {
            wsProvider.connect().get(WAIT_TIMEOUT, TimeUnit.SECONDS);

            Chain chainSection = RpcGeneratedSectionFactory.create(Chain.class, wsProvider);

            val genesis = chainSection.getBlockHash(0).get(WAIT_TIMEOUT, TimeUnit.SECONDS);
            Author authorSection = RpcGeneratedSectionFactory.create(Author.class, wsProvider);

            val updateCount = new AtomicInteger(0);
            val status = new AtomicReference<ExtrinsicStatus>();
            val unsubscribe = authorSection.submitAndWatchExtrinsic(
                            createBalanceTransferExtrinsic(genesis, NONCE.getAndIncrement()),
                            (exception, extrinsicStatus) -> {
                                updateCount.incrementAndGet();
                                status.set(extrinsicStatus);
                            })
                    .get(WAIT_TIMEOUT, TimeUnit.SECONDS);

            await()
                    .atMost(WAIT_TIMEOUT * 2, TimeUnit.SECONDS)
                    .untilAtomic(updateCount, greaterThan(0));

            assertNotNull(status.get());

            val result = unsubscribe.get().get(WAIT_TIMEOUT, TimeUnit.SECONDS);

            assertTrue(result);
        }
    }

    private Extrinsic<?, ?, ?, ?> createBalanceTransferExtrinsic(BlockHash genesis, int nonce) {
        val specVersion = 264;
        val txVersion = 2;
        val moduleIndex = (byte) 6;
        val callIndex = (byte) 0;
        val tip = 0;
        val call = new BalanceTransfer(moduleIndex, callIndex, AddressId.fromBytes(bobKeyPair().asPublicKey().getData()), BigInteger.valueOf(10));

        val extra = new SignedExtra<>(specVersion, txVersion, genesis, genesis, new ImmortalEra(), BigInteger.valueOf(nonce), BigInteger.valueOf(tip));
        val signedPayload = new SignedPayload<>(call, extra);
        val keyRing = KeyRing.fromKeyPair(aliceKeyPair());

        val signature = sign(keyRing, signedPayload);

        return Extrinsic.createSigned(
                new SignaturePayload<>(
                        AddressId.fromBytes(aliceKeyPair().asPublicKey().getData()),
                        signature,
                        extra
                ), call);
    }

    private Signature sign(KeyRing keyRing, Signable payload) {
        var signed = payload.getBytes();
        val signature = signed.length > 256 ? blake2(signed) : signed;

        return Sr25519Signature.from(keyRing.sign(() -> signature));
    }

    private KeyPair aliceKeyPair() {
        val str = "0x98319d4ff8a9508c4bb0cf0b5a78d760a0b2082c02775e6e82370816fedfff48925a225d97aa00682d6a59b95b18780c10d" +
                "7032336e88f3442b42361f4a66011d43593c715fdd31c61141abd04a99fd6822c8558854ccde39a5684e7a56da27d";

        return KeyPair.fromBytes(HexConverter.toBytes(str));
    }

    private KeyPair bobKeyPair() {
        val str = "0x081ff694633e255136bdb456c20a5fc8fed21f8b964c11bb17ff534ce80ebd5941ae88f85d0c1bfc37be41c904e1dfc01de" +
                "8c8067b0d6d5df25dd1ac0894a3258eaf04151687736326c9fea17e25fc5287613693c912909cb226aa4794f26a48";

        return KeyPair.fromBytes(HexConverter.toBytes(str));
    }
}
