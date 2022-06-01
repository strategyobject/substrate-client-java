package com.strategyobject.substrateclient.pallet.storage;

import com.strategyobject.substrateclient.common.utils.HexConverter;
import com.strategyobject.substrateclient.crypto.ss58.SS58Codec;
import com.strategyobject.substrateclient.rpc.api.AccountId;
import com.strategyobject.substrateclient.rpc.api.StorageKey;
import com.strategyobject.substrateclient.scale.ScaleReader;
import com.strategyobject.substrateclient.scale.ScaleWriter;
import com.strategyobject.substrateclient.scale.registries.ScaleReaderRegistry;
import com.strategyobject.substrateclient.scale.registries.ScaleWriterRegistry;
import lombok.NonNull;
import lombok.val;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class StorageKeyProviderTests {
    @SuppressWarnings("unchecked")
    private static Stream<Arguments> getTestCasesForGetBySingleKey() {
        return Stream.of(
                Arguments.of("Balances",
                        "FreeBalance",
                        KeyHasher.with(
                                resolveWriter(AccountId.class),
                                resolveReader(AccountId.class),
                                Blake2B128Concat.getInstance()
                        ),
                        newAccountId("5GrwvaEF5zXb26Fz9rcQpDWS57CtERHpNehXCPcNoHGKutQY"),
                        "0x" +
                                "c2261276cc9d1f8598ea4b6a74b15c2f" +
                                "6482b9ade7bc6657aaca787ba1add3b4" +
                                "de1e86a9a8c739864cf3cc5ec2bea59fd43593c715fdd31c61141abd04a99fd6822c8558854ccde39a5684e7a56da27d"),

                Arguments.of("System",
                        "BlockHash",
                        KeyHasher.with(
                                (ScaleWriter<Integer>) ScaleWriterRegistry.getInstance().resolve(Integer.class),
                                (ScaleReader<Integer>) ScaleReaderRegistry.getInstance().resolve(Integer.class),
                                TwoX64Concat.getInstance()
                        ),
                        2,
                        "0x" +
                                "26aa394eea5630e07c48ae0c9558cef7" +
                                "a44704b568d21667356a5a050c118746" +
                                "9eb2dcce60f37a2702000000")
        );
    }

    @SuppressWarnings({"unchecked", "SameParameterValue"})
    private static <T> ScaleReader<T> resolveReader(Class<T> clazz) {
        return (ScaleReader<T>) ScaleReaderRegistry.getInstance().resolve(clazz);
    }

    @SuppressWarnings({"unchecked", "SameParameterValue"})
    private static <T> ScaleWriter<T> resolveWriter(Class<T> clazz) {
        return (ScaleWriter<T>) ScaleWriterRegistry.getInstance().resolve(clazz);
    }

    private static Stream<Arguments> getTestCasesForGetByDoubleKey() {
        return Stream.of(
                Arguments.of("Society",
                        "Votes",
                        KeyHasher.with(
                                resolveWriter(AccountId.class),
                                resolveReader(AccountId.class),
                                TwoX64Concat.getInstance()
                        ),
                        newAccountId("5GrwvaEF5zXb26Fz9rcQpDWS57CtERHpNehXCPcNoHGKutQY"),
                        KeyHasher.with(
                                resolveWriter(AccountId.class),
                                resolveReader(AccountId.class),
                                TwoX64Concat.getInstance()
                        ),
                        newAccountId("5FHneW46xGXgs5mUiveU4sbTyGBzmstUspZC92UhjJM694ty"),
                        "0x" +
                                "426e15054d267946093858132eb537f1" +
                                "b4adc6a1ce4f7cc2e696ed0fd06bd01c" +
                                "518366b5b1bc7c99d43593c715fdd31c61141abd04a99fd6822c8558854ccde39a5684e7a56da27d" +
                                "a647e755c30521d38eaf04151687736326c9fea17e25fc5287613693c912909cb226aa4794f26a48")
        );
    }

    private static Stream<Arguments> getTestCasesForExtractKeys() {
        val providers = Arrays.asList(
                newFreeBalanceKeyProvider(),
                newVotesKeyProvider()
        );

        val keys = Arrays.asList(
                Collections.singletonList(newAccountId("5GrwvaEF5zXb26Fz9rcQpDWS57CtERHpNehXCPcNoHGKutQY")),
                Arrays.asList(
                        newAccountId("5GrwvaEF5zXb26Fz9rcQpDWS57CtERHpNehXCPcNoHGKutQY"),
                        newAccountId("5FHneW46xGXgs5mUiveU4sbTyGBzmstUspZC92UhjJM694ty")
                )
        );

        return IntStream
                .range(0, providers.size())
                .mapToObj(i -> Arguments.of(
                        providers.get(i),
                        providers.get(i).get(keys.get(i).toArray()),
                        keys.get(i)
                ));
    }

    @NonNull
    private static AccountId newAccountId(String encoded) {
        return AccountId.fromBytes(
                SS58Codec.decode(
                                encoded)
                        .getAddress());
    }

    private static Stream<Arguments> getTestCasesForExtractKeysUsingQueryKey() {
        val providers = Arrays.asList(
                newFreeBalanceKeyProvider(),
                newVotesKeyProvider(),
                newVotesKeyProvider()
        );

        val allKeys = Arrays.asList(
                Collections.singletonList(newAccountId("5GrwvaEF5zXb26Fz9rcQpDWS57CtERHpNehXCPcNoHGKutQY")),
                Arrays.asList(
                        newAccountId("5GrwvaEF5zXb26Fz9rcQpDWS57CtERHpNehXCPcNoHGKutQY"),
                        newAccountId("5FHneW46xGXgs5mUiveU4sbTyGBzmstUspZC92UhjJM694ty")
                ),
                Arrays.asList(
                        newAccountId("5GrwvaEF5zXb26Fz9rcQpDWS57CtERHpNehXCPcNoHGKutQY"),
                        newAccountId("5FHneW46xGXgs5mUiveU4sbTyGBzmstUspZC92UhjJM694ty")
                )
        );

        val queryKeys = Arrays.asList(
                Collections.emptyList(),
                Collections.emptyList(),
                Collections.singletonList(
                        newAccountId("5GrwvaEF5zXb26Fz9rcQpDWS57CtERHpNehXCPcNoHGKutQY")
                )
        );

        return IntStream
                .range(0, providers.size())
                .mapToObj(i -> Arguments.of(
                        providers.get(i),
                        providers.get(i).get(allKeys.get(i).toArray()),
                        providers.get(i).get(queryKeys.get(i).toArray()),
                        queryKeys.get(i).size(),
                        allKeys.get(i).stream().skip(queryKeys.get(i).size()).collect(Collectors.toList())
                ));
    }

    private static StorageKeyProvider newVotesKeyProvider() {
        return StorageKeyProvider.of("Society", "Votes")
                .use(KeyHasher.with(
                                resolveWriter(AccountId.class),
                                resolveReader(AccountId.class),
                                TwoX64Concat.getInstance()
                        ),
                        KeyHasher.with(
                                resolveWriter(AccountId.class),
                                resolveReader(AccountId.class),
                                TwoX64Concat.getInstance()
                        )
                );
    }

    private static StorageKeyProvider newFreeBalanceKeyProvider() {
        return StorageKeyProvider.of("Balances", "FreeBalance")
                .use(KeyHasher.with(
                        resolveWriter(AccountId.class),
                        resolveReader(AccountId.class),
                        Blake2B128Concat.getInstance()
                ));
    }

    @ParameterizedTest
    @CsvSource({
            "SomePallet,SomeStorage,0x832718a9c64cbad10dc0772e2c2d3d9c2e186e85ed8948269c15e1c78ccd4305",
            "Sudo,Key,0x5c0d1176a568c1f92944340dbfed9e9c530ebca703c85910e7164cb7d1c9e47b"})
    void getWithoutKey(String pallet, String storage, String expectedInHex) {
        val provider = StorageKeyProvider.of(pallet, storage);

        val storageKey = provider.get();
        val actualInHex = HexConverter.toHex(storageKey.getData());

        assertEquals(expectedInHex, actualInHex);
    }

    @ParameterizedTest
    @MethodSource("getTestCasesForGetBySingleKey")
    <T> void getBySingleKey(String pallet,
                            String storage,
                            KeyHasher<T> keyHasher,
                            T key,
                            String expectedInHex) {
        val provider = StorageKeyProvider.of(pallet, storage)
                .use(keyHasher);

        val storageKey = provider.get(key);
        val actualInHex = HexConverter.toHex(storageKey.getData());

        assertEquals(expectedInHex, actualInHex);
    }

    @ParameterizedTest
    @MethodSource("getTestCasesForGetByDoubleKey")
    <F, S> void getByDoubleKey(String pallet,
                               String storage,
                               KeyHasher<F> firstHasher,
                               F firstKey,
                               KeyHasher<S> secondHasher,
                               S secondKey,
                               String expectedInHex) {
        val provider = StorageKeyProvider.of(pallet, storage)
                .use(firstHasher, secondHasher);

        val storageKey = provider.get(firstKey, secondKey);
        val actualInHex = HexConverter.toHex(storageKey.getData());

        assertEquals(expectedInHex, actualInHex);
    }

    @ParameterizedTest
    @MethodSource("getTestCasesForExtractKeys")
    void extractKeys(StorageKeyProvider provider, StorageKey fullKey, List<Object> expected) {
        val actual = provider.extractKeys(fullKey);

        assertArrayEquals(expected.toArray(), actual.toArray());
    }

    @ParameterizedTest
    @MethodSource("getTestCasesForExtractKeysUsingQueryKey")
    void extractKeysUsingQueryKey(StorageKeyProvider provider,
                                  StorageKey fullKey,
                                  StorageKey queryKey,
                                  int countOfKeysInQuery,
                                  List<Object> expected) {
        val actual = provider.extractKeys(fullKey, queryKey, countOfKeysInQuery);

        assertArrayEquals(expected.toArray(), actual.toArray());
    }
}
