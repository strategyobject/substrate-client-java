package com.strategyobject.substrateclient.rpc.sections;

import com.strategyobject.substrateclient.common.utils.HexConverter;
import com.strategyobject.substrateclient.crypto.KeyRing;
import com.strategyobject.substrateclient.rpc.codegen.RpcGeneratedSectionFactory;
import com.strategyobject.substrateclient.rpc.codegen.RpcInterfaceInitializationException;
import com.strategyobject.substrateclient.rpc.core.ParameterConverter;
import com.strategyobject.substrateclient.rpc.core.ResultConverter;
import com.strategyobject.substrateclient.rpc.core.RpcEncoder;
import com.strategyobject.substrateclient.rpc.core.RpcEncoderRegistry;
import com.strategyobject.substrateclient.rpc.sections.substitutes.BalanceTransfer;
import com.strategyobject.substrateclient.rpc.types.*;
import com.strategyobject.substrateclient.scale.ScaleWriter;
import com.strategyobject.substrateclient.scale.registry.ScaleWriterNotFoundException;
import com.strategyobject.substrateclient.scale.registry.ScaleWriterRegistry;
import com.strategyobject.substrateclient.scale.writers.CompactIntegerWriter;
import com.strategyobject.substrateclient.scale.writers.U32Writer;
import com.strategyobject.substrateclient.scale.writers.U8Writer;
import com.strategyobject.substrateclient.tests.containers.SubstrateVersion;
import com.strategyobject.substrateclient.tests.containers.TestSubstrateContainer;
import com.strategyobject.substrateclient.transport.ws.WsProvider;
import com.strategyobject.substrateclient.types.KeyPair;
import com.strategyobject.substrateclient.types.PublicKey;
import com.strategyobject.substrateclient.types.Signable;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.val;
import lombok.var;
import org.bouncycastle.crypto.digests.Blake2bDigest;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.Network;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Testcontainers
public class AuthorTests {
    private static final int WAIT_TIMEOUT = 10;
    private static final Network network = Network.newNetwork();

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

            val sectionFactory = new RpcGeneratedSectionFactory();

            // TO DO use real converter
            ParameterConverter parameterConverter = mock(ParameterConverter.class);
            when(parameterConverter.convert(any()))
                    .thenAnswer(invocation -> "0xd43593c715fdd31c61141abd04a99fd6822c8558854ccde39a5684e7a56da27d");
            when(parameterConverter.convert(anyString()))
                    .thenAnswer(invocation -> invocation.getArgument(0));

            // TO DO use real converter
            ResultConverter resultConverter = mock(ResultConverter.class);
            when(resultConverter.convert(any()))
                    .thenAnswer(invocation -> invocation.getArgument(0));

            Author rpcSection = sectionFactory.create(Author.class, wsProvider, parameterConverter, resultConverter);

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

            val sectionFactory = new RpcGeneratedSectionFactory();

            // TO DO use real converter
            ParameterConverter parameterConverter = mock(ParameterConverter.class);
            when(parameterConverter.convert(any()))
                    .thenAnswer(invocation -> "0xd43593c715fdd31c61141abd04a99fd6822c8558854ccde39a5684e7a56da27d");
            when(parameterConverter.convert(anyString()))
                    .thenAnswer(invocation -> invocation.getArgument(0));

            // TO DO use real converter
            ResultConverter resultConverter = mock(ResultConverter.class);
            when(resultConverter.convert(any()))
                    .thenAnswer(invocation -> invocation.getArgument(0));

            Author rpcSection = sectionFactory.create(Author.class, wsProvider, parameterConverter, resultConverter);

            assertDoesNotThrow(() -> rpcSection.insertKey("aura",
                            "alice",
                            PublicKey.fromBytes(
                                    HexConverter.toBytes("0xd43593c715fdd31c61141abd04a99fd6822c8558854ccde39a5684e7a56da27d")))
                    .get(WAIT_TIMEOUT, TimeUnit.SECONDS));
        }
    }

    @Test
    void submitExtrinsic() throws ExecutionException, InterruptedException, TimeoutException, RpcInterfaceInitializationException {
        mockRegistries();

        try (WsProvider wsProvider = WsProvider.builder()
                .setEndpoint(substrate.getWsAddress())
                .disableAutoConnect()
                .build()) {
            wsProvider.connect().get(WAIT_TIMEOUT, TimeUnit.SECONDS);

            val sectionFactory = new RpcGeneratedSectionFactory();

            // TO DO use real converter
            ParameterConverter parameterConverter = mock(ParameterConverter.class);
            when(parameterConverter.convert(any()))
                    .thenAnswer(invocation -> invocation.getArgument(0));

            // TO DO use real converter
            ResultConverter resultConverter = mock(ResultConverter.class);
            when(resultConverter.<String, BlockHash>convert(anyString()))
                    .thenAnswer(invocation -> BlockHash.fromBytes(HexConverter.toBytes(invocation.getArgument(0))));

            Chain chainSection = sectionFactory.create(Chain.class, wsProvider, parameterConverter, resultConverter);

            val genesis = chainSection.getBlockHash(0).get(WAIT_TIMEOUT, TimeUnit.SECONDS);

            // TO DO use real converter
            parameterConverter = mock(ParameterConverter.class);
            when(parameterConverter.convert(any()))
                    .thenAnswer(invocation -> ((Extrinsic<?, ?, ?, ?>) (invocation.getArgument(0))).encode());

            // TO DO use real converter
            resultConverter = mock(ResultConverter.class);
            when(resultConverter.convert(any()))
                    .thenAnswer(invocation -> new Hash());

            Author authorSection = sectionFactory.create(Author.class, wsProvider, parameterConverter, resultConverter);

            assertDoesNotThrow(() -> authorSection.submitExtrinsic(createBalanceTransferExtrinsic(genesis))
                    .get(WAIT_TIMEOUT, TimeUnit.SECONDS));
        }
    }

    @SneakyThrows
    private void mockRegistries() {
        ScaleWriterRegistry scaleWriterRegistry = mockScaleWriterRegistry();
        mockStatic(ScaleWriterRegistry.class)
                .when(ScaleWriterRegistry::getInstance)
                .thenReturn(scaleWriterRegistry);

        RpcEncoderRegistry rpcEncoderRegistry = mockRpcEncoderRegistry();
        mockStatic(RpcEncoderRegistry.class)
                .when(RpcEncoderRegistry::getInstance)
                .thenReturn(rpcEncoderRegistry);
    }

    @SneakyThrows
    @SuppressWarnings("rawtypes")
    private RpcEncoderRegistry mockRpcEncoderRegistry() {
        RpcEncoderRegistry rpcEncoderRegistry = mock(RpcEncoderRegistry.class);
        when(rpcEncoderRegistry.resolve(Extrinsic.class))
                .thenReturn(new RpcEncoder<Extrinsic>() {
                    @SneakyThrows
                    @Override
                    public Object encode(Extrinsic obj) {
                        val encodedExtrinsic = new ByteArrayOutputStream();
                        obj.write(encodedExtrinsic);

                        val targetBuf = new ByteArrayOutputStream();
                        new CompactIntegerWriter().write(encodedExtrinsic.size(), targetBuf);
                        targetBuf.write(encodedExtrinsic.toByteArray());

                        return HexConverter.toHex(targetBuf.toByteArray());
                    }
                });

        return rpcEncoderRegistry;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private ScaleWriterRegistry mockScaleWriterRegistry() throws ScaleWriterNotFoundException {
        ScaleWriterRegistry registry = mock(ScaleWriterRegistry.class);

        when(registry.resolve(AddressId.class)).thenReturn((ScaleWriter) getAddressIdScaleWriter());

        when(registry.resolve(BalanceTransfer.class)).thenReturn((ScaleWriter) getBalanceTransferScaleWriter());

        when(registry.resolve(ImmortalEra.class)).thenReturn((ScaleWriter) getImmortalEraScaleWriter());

        when(registry.resolve(SignedExtra.class)).thenReturn((ScaleWriter) getSignedExtraScaleWriter());

        when(registry.resolve(SignedAdditionalExtra.class)).thenReturn((ScaleWriter) getSignedAdditionalExtraScaleWriter());

        when(registry.resolve(BlockHash.class)).thenReturn((ScaleWriter) getBlockHashScaleWriter());

        when(registry.resolve(Extrinsic.class)).thenReturn((ScaleWriter) getExtrinsicScaleWriter());

        when(registry.resolve(Sr25519Signature.class)).thenReturn((ScaleWriter) getSr25519SignatureScaleWriter());

        when(registry.resolve(SignaturePayload.class)).thenReturn((ScaleWriter) getSignaturePayloadScaleWriter());

        return registry;
    }

    private ScaleWriter<Sr25519Signature> getSr25519SignatureScaleWriter() {
        return (value, stream, writers) -> {
            new U8Writer().write((int) value.getKind().getValue(), stream);
            newByteArrayWriter().write(value.getData().getData(), stream);
        };
    }

    @SuppressWarnings("rawtypes")
    private ScaleWriter<Extrinsic> getExtrinsicScaleWriter() {
        return new ScaleWriter<Extrinsic>() {
            @SneakyThrows
            @Override
            public void write(@NonNull Extrinsic value, @NonNull OutputStream stream, ScaleWriter<?>... writers) {
                var version = 4;
                version = value.getSignature().isPresent() ? version | 0b1000_0000 : version & 0b0111_1111;
                new U8Writer().write(version, stream);

                if (value.getSignature().isPresent()) {
                    val signature = (SignaturePayload<?, ?, ?>) value.getSignature().get();
                    signature.write(stream);
                }

                value.getCall().write(stream);
            }
        };
    }

    private ScaleWriter<BlockHash> getBlockHashScaleWriter() {
        return (value, stream, writers) -> newByteArrayWriter().write(value.getData(), stream);
    }

    private ScaleWriter<SignedAdditionalExtra> getSignedAdditionalExtraScaleWriter() {
        return new ScaleWriter<SignedAdditionalExtra>() {
            @SneakyThrows
            @Override
            public void write(@NonNull SignedAdditionalExtra value, @NonNull OutputStream stream, ScaleWriter<?>... writers) {
                val writer = new U32Writer();

                writer.write(value.getSpecVersion(), stream);
                writer.write(value.getTxVersion(), stream);
                value.getGenesis().write(stream);
                value.getEraBlock().write(stream);
            }
        };
    }

    @SuppressWarnings("rawtypes")
    private ScaleWriter<SignedExtra> getSignedExtraScaleWriter() {
        return new ScaleWriter<SignedExtra>() {
            @SneakyThrows
            @Override
            public void write(@NonNull SignedExtra value, @NonNull OutputStream stream, ScaleWriter<?>... writers) {
                val writer = new CompactIntegerWriter();
                value.getEra().write(stream);
                writer.write((int) value.getNonce(), stream);
                writer.write((int) value.getTip(), stream);
            }
        };
    }

    private ScaleWriter<ImmortalEra> getImmortalEraScaleWriter() {
        return (value, stream, writers) -> new U8Writer().write(value.getEncoded(), stream);
    }

    private ScaleWriter<BalanceTransfer> getBalanceTransferScaleWriter() {
        return new ScaleWriter<BalanceTransfer>() {
            @SneakyThrows
            @Override
            public void write(@NonNull BalanceTransfer value, @NonNull OutputStream stream, ScaleWriter<?>... writers) {
                val u8writer = new U8Writer();
                val compactWriter = new CompactIntegerWriter();

                u8writer.write(value.getModuleIndex(), stream);
                u8writer.write(value.getCallIndex(), stream);
                value.getDestination().write(stream);
                compactWriter.write((int) value.getAmount(), stream);
            }
        };
    }

    private ScaleWriter<AddressId> getAddressIdScaleWriter() {
        return new ScaleWriter<AddressId>() {
            @Override
            public void write(@NonNull AddressId value, @NonNull OutputStream stream, ScaleWriter<?>... writers) throws IOException {
                val u8writer = new U8Writer();

                u8writer.write((int) value.getKind().getValue(), stream);
                newByteArrayWriter().write(value.getAddress().getData(), stream);
            }
        };
    }

    @SuppressWarnings("rawtypes")
    private ScaleWriter<SignaturePayload> getSignaturePayloadScaleWriter() {
        return new ScaleWriter<SignaturePayload>() {
            @SneakyThrows
            @Override
            public void write(@NonNull SignaturePayload value, @NonNull OutputStream stream, ScaleWriter<?>... writers) {
                value.getAddress().write(stream);
                value.getSignature().write(stream);
                value.getExtra().write(stream);
            }
        };
    }

    private ScaleWriter<byte[]> newByteArrayWriter() {
        return (value, stream, writers) -> stream.write(value);
    }

    private Extrinsic<?, ?, ?, ?> createBalanceTransferExtrinsic(BlockHash genesis) {
        val specVersion = 264;
        val txVersion = 2;
        val moduleIndex = 6;
        val callIndex = 0;
        val nonce = 0;
        val tip = 0;
        val call = new BalanceTransfer(moduleIndex, callIndex, AddressId.fromBytes(bobKeyPair().asPublicKey().getData()), 10);

        val extra = new SignedExtra<>(specVersion, txVersion, genesis, genesis, new ImmortalEra(), nonce, tip);
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
